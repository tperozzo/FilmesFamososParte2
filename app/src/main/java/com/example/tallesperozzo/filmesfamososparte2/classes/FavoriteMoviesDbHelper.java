package com.example.tallesperozzo.filmesfamososparte2.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favoritemovies.db";

    public static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME
                + " (" + FavoriteMoviesContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID_MOVIE + " TEXT NOT NULL,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_DATE + " TEXT," 
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW + " TEXT,"
                + FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
