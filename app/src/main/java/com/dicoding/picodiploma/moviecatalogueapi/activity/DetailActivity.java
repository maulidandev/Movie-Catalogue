package com.dicoding.picodiploma.moviecatalogueapi.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.api.Constants;
import com.dicoding.picodiploma.moviecatalogueapi.db.AppDatabase;
import com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract;
import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;
import com.dicoding.picodiploma.moviecatalogueapi.widget.MovieFavoriteWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.dicoding.picodiploma.moviecatalogueapi.api.Constants.MOVIE;

public class DetailActivity extends AppCompatActivity {

    private Favorite favorite;
    private AppDatabase db;
    private TextView title;
    private TextView description;
    private TextView date;
    private ImageView image;
    private FloatingActionButton fab;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        showData();

        // check jika sudah ada
        favorite = db.favoriteDao().findById(movie.getId());
        if (favorite != null) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_white_24dp));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorite == null) {
                    insertData();

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_white_24dp));
                    Toast.makeText(DetailActivity.this, getResources().getString(R.string.added_success), Toast.LENGTH_SHORT).show();
                } else {
                    deleteData();

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                    Toast.makeText(DetailActivity.this, getResources().getString(R.string.removed_success), Toast.LENGTH_SHORT).show();
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(DetailActivity.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(DetailActivity.this, MovieFavoriteWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            }
        });
    }

    private void deleteData() {
        db.favoriteDao().delete(favorite);
        favorite = null;
    }

    private void insertData() {
        favorite = new Favorite();
        favorite.setId(movie.getId());
        favorite.setTitle(movie.getTitle() != null ? movie.getTitle() : movie.getName());
        favorite.setRelease_date(movie.getRelease_date() != null ? movie.getRelease_date() : movie.getFirst_air_date());
        favorite.setOverview(movie.getOverview());
        favorite.setPoster_path(movie.getPoster_path());
        favorite.setSource(movie.getSource());

        db.favoriteDao().insertAll(favorite);
    }

    private void showData() {
        movie = getIntent().getParcelableExtra(MOVIE);

        if (movie.getTitle() != null)
            title.setText(movie.getTitle());
        else
            title.setText(movie.getName());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        try {
            if (movie.getRelease_date() != null)
                date.setText(output.format(input.parse(movie.getRelease_date())));
            else
                date.setText(output.format(input.parse(movie.getFirst_air_date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        description.setText(movie.getOverview());

        Glide.with(DetailActivity.this)
                .load(Constants.IMAGE_SOURCE + movie.getPoster_path())
                .fitCenter()
                .into(image);
        image.setContentDescription(getResources().getString(R.string.image_movie) + movie.getTitle());
    }

    private void init() {
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DatabaseContract.FAVORITE_TABLE_NAME).allowMainThreadQueries().build();
        title = findViewById(R.id.title);
        description = findViewById(R.id.overview);
        date = findViewById(R.id.release_date);
        image = findViewById(R.id.poster_path);
        fab = findViewById(R.id.fab);
    }

}
