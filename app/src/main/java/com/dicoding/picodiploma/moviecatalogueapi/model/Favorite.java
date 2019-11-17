package com.dicoding.picodiploma.moviecatalogueapi.model;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"id", "source"})
public class Favorite {

    public int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "release_date")
    private String release_date;

    @ColumnInfo(name = "poster_path")
    private String poster_path;

    // 1 untuk movie, 2 untuk tv show
    @ColumnInfo(name = "source")
    private int source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public static Favorite fromContentValues(ContentValues values) {
        final Favorite favorite = new Favorite();
        if (values.containsKey("title")) favorite.setTitle(values.getAsString("title"));
        if (values.containsKey("overview")) favorite.setOverview(values.getAsString("overview"));
        if (values.containsKey("release_date")) favorite.setRelease_date(values.getAsString("release_date"));
        if (values.containsKey("poster_path")) favorite.setPoster_path(values.getAsString("poster_path"));
        return favorite;
    }
}
