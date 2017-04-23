package com.iamdilipkumar.movies.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created on 21/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int FAVOURITES = 100;
    public static final int FAVOURITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Method to match the URI to check which part of the database
     * operation that it should perform
     *
     * @return - interger to match the operation to be perforrmed
     */
    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_TASKS, FAVOURITES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_TASKS + "/#", FAVOURITES_WITH_ID);

        return uriMatcher;
    }

    private MoviesDbHelper mMoviesHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesHelper = new MoviesDbHelper(context);
        return true;
    }

    /**
     * Method to query database table based on the URI
     *
     * @param uri - used to fetching the integer to determine the operation
     * @param projection - The columns that has to be displayed
     * @param selection - The selection condition
     * @param selectionArgs - selection arguments
     * @param sortOrder - The order in which the query has to return the results
     *
     * @return - returns the cursor containing the results
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMoviesHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVOURITES:
                retCursor =  db.query(MoviesContract.MoviesTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVOURITES_WITH_ID:
                String movieSelection = "movieid="+uri.getPathSegments().get(1);
                retCursor =  db.query(MoviesContract.MoviesTable.TABLE_NAME,
                        projection,
                        movieSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    /**
     * Method to insert data within the database table
     *
     * @param uri - Used for matching the operation
     * @param values - The content values that has to be entered within the database table
     *
     * @return - Uri based on the result of the operation
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITES:
                long id = db.insert(MoviesContract.MoviesTable.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesTable.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    /**
     * Method to delete favourites from the database table
     *
     * @param uri - To determine the operation
     * @param selection - The selection criteria for deletion
     * @param selectionArgs - Selection arguments
     * @return - Integer determining the result of the oepration
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(MoviesContract.MoviesTable.TABLE_NAME, "movieid=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
