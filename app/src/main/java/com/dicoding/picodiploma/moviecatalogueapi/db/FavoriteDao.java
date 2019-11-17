package com.dicoding.picodiploma.moviecatalogueapi.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite where source=1")
    List<Favorite> getAllMovie();

    @Query("SELECT * FROM favorite where source=1")
    Cursor getAllMovieCursor();

    @Query("SELECT * FROM favorite where source=2")
    List<Favorite> getAllTvShow();

    @Query("SELECT * FROM favorite WHERE id LIKE :id ")
    Favorite findById(int id);

    @Query("SELECT * FROM favorite WHERE id LIKE :id ")
    Cursor findByIdCursor(int id);

    @Update
    int update(Favorite favorite);

    @Insert
    void insertAll(Favorite... favorites);

    @Insert
    long insert(Favorite favorites);

    @Delete
    int delete(Favorite favorites);

}
