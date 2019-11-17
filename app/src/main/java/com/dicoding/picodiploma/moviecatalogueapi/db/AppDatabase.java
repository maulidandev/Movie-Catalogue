package com.dicoding.picodiploma.moviecatalogueapi.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
}
