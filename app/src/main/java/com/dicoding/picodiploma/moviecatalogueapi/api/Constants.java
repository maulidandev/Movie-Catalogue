package com.dicoding.picodiploma.moviecatalogueapi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_SOURCE = "https://image.tmdb.org/t/p/w500/";
    public static final String MOVIE = "MOVIE";

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
