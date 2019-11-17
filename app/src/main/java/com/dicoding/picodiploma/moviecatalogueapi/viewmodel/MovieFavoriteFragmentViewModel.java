package com.dicoding.picodiploma.moviecatalogueapi.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.dicoding.picodiploma.moviecatalogueapi.db.AppDatabase;
import com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract;
import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;

import java.util.ArrayList;

public class MovieFavoriteFragmentViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Favorite>> listFavorite = new MutableLiveData<>();

    public void setFavorite(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DatabaseContract.FAVORITE_TABLE_NAME).allowMainThreadQueries().build();
        listFavorite.postValue((ArrayList<Favorite>) db.favoriteDao().getAllMovie());
    }

    public LiveData<ArrayList<Favorite>> getFavorite() {
        return listFavorite;
    }
}
