package com.example.bbcreader;

import android.util.Xml;

import com.example.bbcreader.Article;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * A class for parsing the xml data from the bbc rrs feed
 */
public class BBCNewsFeed {

    /**
     * Gets the xml data from the bbc rrs feed and returns the articles as an Article objects list
     */
    public static ArrayList<Article> ParseNewsXML() throws IOException, XmlPullParserException {
        URL url = new URL("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        InputStream inputStream = conn.getInputStream();

        int responseCode = conn.getResponseCode();

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, "UTF-8");

        ArrayList<Article> articles = parseXML(parser);

        inputStream.close();

        return articles;
    }

    /**
     * Converts the xml data into a list of Article objects
     */

    private static ArrayList<Article> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Article> articles = new ArrayList<>();
        int eventType = parser.getEventType();
        Article article = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName;

            tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();

                    if (tagName.equalsIgnoreCase("item")) {
                        article = new Article();
                    } else if (article != null) {
                        if (tagName.equalsIgnoreCase("title")) {
                            article.title = parser.nextText();
                        } else if (tagName.equalsIgnoreCase("description")) {
                            article.description = parser.nextText();
                        } else if (tagName.equalsIgnoreCase("link")) {
                            article.link = parser.nextText();
                        } else if (tagName.equalsIgnoreCase("guid")) {
                            article.guid = parser.nextText();
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            article.pubDate = parser.nextText();
                        } else if (tagName.equalsIgnoreCase("thumbnail")) {
                            article.thumbnailUrl = parser.getAttributeValue(null, "url");
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();

                    if (tagName.equalsIgnoreCase("item") && article != null) {
                        articles.add(article);
                    }
                    break;
            }

            eventType = parser.next();
        }

        return articles;
    }
}
