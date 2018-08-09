package com.example.tallesperozzo.filmesfamososparte2.classes;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final String AUTHORITY = "com.example.tallesperozzo.filmesfamososparte2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final class FavoriteMoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoritemovies";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_MOVIE = "id_movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
