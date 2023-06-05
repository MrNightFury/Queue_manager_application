package ru.mrnightfury.queuemanager.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavouriteEntity.class}, version = 1)
public abstract class FavouriteDatabase extends RoomDatabase {
    private static final String dbName = "favouriteDB";
    private static FavouriteDatabase db;

    public static synchronized FavouriteDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, FavouriteDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }

    public abstract FavouriteDao favouriteDao();
}
