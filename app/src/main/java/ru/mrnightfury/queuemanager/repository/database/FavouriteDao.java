package ru.mrnightfury.queuemanager.repository.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavouriteDao {
    @Insert
    void addToFavourite(FavouriteEntity entity);

    @Query("SELECT * from favourite")
    FavouriteEntity[] getFavourite();

    @Delete
    void removeFromFavourite(FavouriteEntity entity);
}
