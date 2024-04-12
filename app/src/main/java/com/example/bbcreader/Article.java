package com.example.bbcreader;

import android.os.Parcel;
import android.os.Parcelable;
import android.security.identity.IdentityCredentialStore;

public class Article implements Parcelable {
    public String title;
    public String description;
    public String link;
    public String guid;
    public String pubDate;
    public String thumbnailUrl;

    public Article(String title, String description, String link, String guid, String pubDate, String thumbnailUrl){
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.pubDate = pubDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Article(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    protected Article(Parcel in) {
    }
}
