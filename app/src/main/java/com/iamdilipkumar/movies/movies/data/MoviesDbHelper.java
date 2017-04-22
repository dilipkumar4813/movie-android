package com.iamdilipkumar.movies.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created on 21/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "udacitymovies.db";

    private static final int VERSION = 1;

    public MoviesDbHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MoviesContract.MoviesTable.TABLE_NAME + " (" +
                MoviesContract.MoviesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.MoviesTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_PLOT + " TEXT NULL, " + //Check this
                MoviesContract.MoviesTable.COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_AVERAGE_VOTE + " DOUBLE NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_BACKDROP + " TEXT NULL, " +
                MoviesContract.MoviesTable.COLUMN_RELEASE_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesTable.TABLE_NAME);
        onCreate(db);
    }
}
