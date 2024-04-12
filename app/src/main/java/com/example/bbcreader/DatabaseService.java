package com.example.bbcreader;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * A service for getting and updating the articles database for this app
 */
public class DatabaseService {

    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "articles";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_GUID = "guid";
    private static final String COLUMN_PUB_DATE = "pubDate";
    private static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseService(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Opens the app for editing and reading
     */
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the app
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Adds an article to the artices table in the db
     */
    public void addArticle(Article article) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, article.title);
        values.put(COLUMN_DESCRIPTION, article.description);
        values.put(COLUMN_LINK, article.link);
        values.put(COLUMN_GUID, article.guid);
        values.put(COLUMN_PUB_DATE, article.pubDate);
        values.put(COLUMN_THUMBNAIL_URL, article.thumbnailUrl);
        database.insert(TABLE_NAME, null, values);
        close();
    }

    /**
     * Gets all the articles in the articles table
     */

    public ArrayList<Article> getAllArticles() {
        open();
        ArrayList<Article> articles = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article article = cursorToArticle(cursor);
            articles.add(article);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return articles;
    }

    /**
     * Deletes an article from the table based off the guid of the article
     */

    public void removeArticle(Article article) {
        open();
        database.delete(TABLE_NAME, COLUMN_GUID + " = ?", new String[]{article.guid});
        close();
    }

    /**
     * Checks whether an article is already in the db
     */

    public boolean isInDB(String guid){
        open();
        String selection = COLUMN_GUID + "=?";
        String[] selectionArgs = { guid };

        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isInDatabase = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        close();

        return isInDatabase;
    }

    /**
     * Converts the sql results to an Article object
     */

    @SuppressLint("Range")
    private Article cursorToArticle(Cursor cursor) {
        Article article = new Article();
        article.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        article.description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        article.link = cursor.getString(cursor.getColumnIndex(COLUMN_LINK));
        article.guid = cursor.getString(cursor.getColumnIndex(COLUMN_GUID));
        article.pubDate = cursor.getString(cursor.getColumnIndex(COLUMN_PUB_DATE));
        article.thumbnailUrl = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL_URL));
        return article;
    }


    /**
     * Creating the DBHelper class
     */
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_LINK + " TEXT, " +
                    COLUMN_GUID + " TEXT, " +
                    COLUMN_PUB_DATE + " TEXT, " +
                    COLUMN_THUMBNAIL_URL + " TEXT);";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
