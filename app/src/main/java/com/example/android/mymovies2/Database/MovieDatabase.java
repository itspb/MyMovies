package com.example.android.mymovies2.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.mymovies2.pojo.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static String DB_NAME = "movies.db";
    private static MovieDatabase database;

    public synchronized static MovieDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract MovieDao movieDao();
}
