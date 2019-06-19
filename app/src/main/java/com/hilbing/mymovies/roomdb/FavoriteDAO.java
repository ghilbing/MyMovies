package com.hilbing.mymovies.roomdb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM favorite_table")
    LiveData<List<Favorite>> loadAllFavorites();

    @Query("SELECT * FROM favorite_table WHERE title = :title")
    List<Favorite> loadAll(String title);

    @Insert
    void insertFavorite(Favorite favorite);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorite_table WHERE movie_id = :movie_id")
    void deleteFavoriteById(int movie_id);

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    LiveData<Favorite> loadFavoriteById(int id);

}
