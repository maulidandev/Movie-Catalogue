package com.dicoding.picodiploma.moviecatalogueapi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.moviecatalogueapi.BuildConfig;
import com.dicoding.picodiploma.moviecatalogueapi.api.APIService;
import com.dicoding.picodiploma.moviecatalogueapi.api.Constants;
import com.dicoding.picodiploma.moviecatalogueapi.model.APIResponse;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieFragmentViewModel extends ViewModel {
    private static final String CATEGORY = "movie";
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private Retrofit retrofit;
    private Call<APIResponse> call;

    public void setMovie(String query) {
        if (call != null)
            call.cancel();

        if (retrofit == null)
            retrofit = Constants.getRetrofit();

        APIService service = retrofit.create(APIService.class);

        if (query.equals(""))
            call = service.listMovie(CATEGORY, BuildConfig.API_KEY);
        else
            call = service.searchMovie(CATEGORY, BuildConfig.API_KEY, query);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    listMovies.postValue(apiResponse.getResults());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }
}
