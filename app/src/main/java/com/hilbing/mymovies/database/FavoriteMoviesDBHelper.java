package com.hilbing.mymovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.hilbing.mymovies.MyApplication;
import com.hilbing.mymovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TAG = FavoriteMoviesDBHelper.class.getSimpleName();

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        database = dbHandler.getWritableDatabase();
    }

    public void close(){
        dbHandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + "(" +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING + " REAL NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addFavorite(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING, movie.getVoteAverage());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS, movie.getOverview());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        Log.d(TAG, movie.getPosterPath() + " " + movie.getBackdropPath());

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM favorites where movie_id = " + movie.getId(), null);
            if (cursor.getCount() > 0){
                Toast.makeText(MyApplication.getContext(), "Movie Already Exists", Toast.LENGTH_LONG).show();
            } else {
                db.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, values);
                db.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void removeFavorite(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=" + id, null);
        db.close();
    }

    public List<Movie> getAllFavorites(){
        String[] columns = {
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_PATH,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_PATH

        };

        String sortOrder = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE + " ASC";
        List<Movie> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING))));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_SYNOPSIS)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_PATH)));

                favoriteList.add(movie);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        return favoriteList;

    }
}
