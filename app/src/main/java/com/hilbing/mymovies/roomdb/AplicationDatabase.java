package com.hilbing.mymovies.roomdb;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class AplicationDatabase extends RoomDatabase {

    private static final String TAG = AplicationDatabase.class.getSimpleName();
    private static final Object OBJECT = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static AplicationDatabase sInstance;


    public static AplicationDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (OBJECT){
                Log.d(TAG, "Creating new database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AplicationDatabase.class, AplicationDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(TAG, "Getting database instance");
        return sInstance;
    }

    public abstract FavoriteDAO favoriteDAO();

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
