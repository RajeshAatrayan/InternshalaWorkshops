package com.ibrickedlabs.internshala.WorkshopsData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.ibrickedlabs.internshala.WorkshopsData.WorkshopContract.WorkshopEntry;

public class WorkshopDbHelper extends SQLiteOpenHelper {
    //Database name for the workshops we have
    private static final String DATABASE_NAME = "workshoporg.db";
    //Database version
    private static final int DATABASE_VERSION = 1;

    public WorkshopDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + WorkshopEntry.TABLE_NAME + " (" +
                WorkshopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkshopEntry.WORKSHOP_NAME + " NOT NULL," +
                WorkshopEntry.WORKSHOP_DETAILS + " NOT NULL );";
        db.execSQL(SQL_CREATE_TABLE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
