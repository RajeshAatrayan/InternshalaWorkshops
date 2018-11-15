package com.ibrickedlabs.internshala.WorkshopsData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WorkshopProvider extends ContentProvider {
    //Constants
    public static final int WORKSHOPS = 100;
    public static final int WORKSHOPS_ID = 101;

    //WorkshopDbHelper ref var
    WorkshopDbHelper mWorkshopDbHelper;


    // We are using UriMatcher to check wether the query is for a single entity or          for the entire table
    //It's common to use NO_MATCH as the input for this case.

    private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //Static block to add the items into the UriMatcher before any of the method executes

    static {
        //here it will return 100 if the queried uri matches with this uri
        sURI_MATCHER.addURI(WorkshopContract.CONTENT_AUTHORITY, WorkshopContract.PATH_WORKSHOPS, WORKSHOPS);
        //here it will return 101 if the queried uri matches with this uri
        sURI_MATCHER.addURI(WorkshopContract.CONTENT_AUTHORITY, WorkshopContract.PATH_WORKSHOPS + "/#", WORKSHOPS_ID);
    }

    @Override
    public boolean onCreate() {
        mWorkshopDbHelper = new WorkshopDbHelper(getContext());
        return true;
    }

    /**
     * @param uri                 -the uri we sent
     * @param projection-req      cols
     * @param selection-where     attributes
     * @param selectionArgs-their values
     * @param sortOrder
     * @return
     */

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase mSqLiteDatabase = mWorkshopDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (sURI_MATCHER.match(uri)) {
            case WORKSHOPS:
                cursor = mSqLiteDatabase.query(WorkshopContract.WorkshopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case WORKSHOPS_ID:
                selection = WorkshopContract.WorkshopEntry._ID + "=?";
                //ContentUris.parseId(uri) returns the id of the row we are referring
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = mSqLiteDatabase.query(WorkshopContract.WorkshopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null, null);
                break;

        }
/**
 * setNotificationUri
 * So when ever the data of a particular Uri changes we need to update it
 */

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
