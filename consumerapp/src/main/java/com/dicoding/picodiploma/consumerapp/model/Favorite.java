package com.dicoding.picodiploma.consumerapp.model;

public class Favorite {

    public int id;

    private String title;

    private String overview;

    private String release_date;

    private String poster_path;

    private int source;

    public Favorite(int id, String title, String overview, String release_date, String poster_path, int source) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.source = source;
    }

    public Favorite() {
    }

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

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getSource() {
        return source;
    }
}
