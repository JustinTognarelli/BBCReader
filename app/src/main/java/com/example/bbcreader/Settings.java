package com.example.bbcreader;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static final String PREFS_NAME = "Settings";

    /**
     * Saves data to the shared preferences using a key value pair
     */
    public static void saveData(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Gets the data associated with a key in the shared preferences
     */
    public static String loadData(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String preferences = prefs.getString(key, "");
        return preferences;
    }


}

