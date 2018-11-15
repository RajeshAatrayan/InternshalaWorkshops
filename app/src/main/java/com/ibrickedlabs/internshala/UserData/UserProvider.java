package com.ibrickedlabs.internshala.UserData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class UserProvider extends ContentProvider {
    //ContentProvider to share the data with other apps & to abstract the database working
    private static final String TAG = "UserProvider";
    //UserDbHelper
    private UserDbHelper mUserDbHelper;
    //SQLiteDatabase
    SQLiteDatabase mSqLiteDatabase;


    @Override
    public boolean onCreate() {
        mUserDbHelper = new UserDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        mSqLiteDatabase = mUserDbHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null);

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
        mSqLiteDatabase = mUserDbHelper.getWritableDatabase();
        long id = mSqLiteDatabase.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        if (id == -1) {
//            Log.d(TAG,"failed to insert the data for" + uri);
            System.out.println("failed to insert");
        } else {
            Log.d(TAG, "HERE IS YOUR ID-" + id);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
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
