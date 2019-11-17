package com.dicoding.picodiploma.consumerapp.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.consumerapp.R;
import com.dicoding.picodiploma.consumerapp.adapter.FavoriteAdapter;
import com.dicoding.picodiploma.consumerapp.model.Favorite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String FAVORITE_TABLE_NAME = "favorite";
    public static final String AUTHORITY = "com.dicoding.picodiploma.moviecatalogueapi";
    private static final String SCHEME = "content";

    // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(FAVORITE_TABLE_NAME)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor dataCursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        ArrayList<Favorite> favorites = mapCursorToArrayList(dataCursor);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, favorites);

        RecyclerView listMovies = findViewById(R.id.list_movies);
        listMovies.setLayoutManager(new LinearLayoutManager(this));
        listMovies.setAdapter(favoriteAdapter);
        listMovies.setHasFixedSize(true);
    }

    public static ArrayList<Favorite> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Favorite> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow("id"));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow("title"));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow("overview"));
            String release_date = notesCursor.getString(notesCursor.getColumnIndexOrThrow("release_date"));
            String poster_path = notesCursor.getString(notesCursor.getColumnIndexOrThrow("poster_path"));
            int source = notesCursor.getInt(notesCursor.getColumnIndexOrThrow("source"));

            notesList.add(new Favorite(id, title, overview, release_date, poster_path, source));
        }

        return notesList;
    }
}
