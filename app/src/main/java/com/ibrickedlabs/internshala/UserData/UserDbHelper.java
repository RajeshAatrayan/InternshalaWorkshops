package com.ibrickedlabs.internshala.UserData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.ibrickedlabs.internshala.UserData.UserContract.UserEntry;

public class UserDbHelper extends SQLiteOpenHelper {
    //Database name
    public static final String DATABASE_NAME = "activeuser.db";
    //Database version
    public static final int DATBASE_VERSION = 1;

    public UserDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_SQL_QUERY = "CREATE TABLE " + UserEntry.TABLE_NAME +
                "( " + UserEntry.id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.USER_EMAIL + " TEXT NOT NULL, " +
                UserEntry.USER_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.USER_FIRST_NAME + " TEXT NOT NULL, " +
                UserEntry.USER_LAST_NAME + " TEXT NOT NULL, " +
                UserEntry.WORKSHOP_ANDROID + " INTEGER DEFAULT 0 ," +
                UserEntry.WORKSHOP_Artificial_Intelligence + " INTEGER DEFAULT 0 ," +
                UserEntry.WORKSHOP_BigData_Bootcamp + " INTEGER DEFAULT 0 ," +
                UserEntry.WORKSHOP_BlockChain_Technology + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_CloudComputing + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_Cyber_Security + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_Datascience_Bootcamp + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_IoT + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_Machine_Learning + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_SAP_Bootcamp + " INTEGER DEFAULT 0," +
                UserEntry.WORKSHOP_Web_Development + " INTEGER DEFAULT 0 );";
        //Execute the query

        db.execSQL(CREATE_TABLE_SQL_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
