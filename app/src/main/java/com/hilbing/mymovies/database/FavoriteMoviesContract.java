package com.hilbing.mymovies.database;

import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final class FavoriteMoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_USER_RATING = "movie_user_rating";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";

    }

}
