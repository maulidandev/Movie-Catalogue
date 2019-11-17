package com.dicoding.picodiploma.moviecatalogueapi.api;

import com.dicoding.picodiploma.moviecatalogueapi.model.APIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("{category}/top_rated")
    Call<APIResponse> listMovie(@Path("category") String category, @Query("api_key") String apiKey);

    @GET("search/{category}")
    Call<APIResponse> searchMovie(@Path("category") String category, @Query("api_key") String apiKey, @Query("query") String query);

    @GET("discover/movie")
    Call<APIResponse> releaseMovie(@Query("api_key") String apiKey, @Query("primary_release_date.gte") String primaryReleaseDateGte, @Query("primary_release_date.lte") String primaryReleaseDateLte);
}
