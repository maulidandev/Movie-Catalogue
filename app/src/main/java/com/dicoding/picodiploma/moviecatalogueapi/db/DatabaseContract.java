package com.dicoding.picodiploma.moviecatalogueapi.db;

import android.net.Uri;

public class DatabaseContract {

    public static String FAVORITE_TABLE_NAME = "favorite";
    public static final String AUTHORITY = "com.dicoding.picodiploma.moviecatalogueapi";
    private static final String SCHEME = "content";

    // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(FAVORITE_TABLE_NAME)
            .build();
}
