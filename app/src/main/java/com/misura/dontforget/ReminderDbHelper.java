package com.misura.dontforget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kmisura on 02/04/16.
 */
public class ReminderDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "reminder.db";
    private static final int DATABASE_VERSION = 2;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FRIENDS_TABLE = "CREATE TABLE " + RemindersContract.ReminderEntry.TABLE_NAME + " (" +
                RemindersContract.ReminderEntry._ID + " INTEGER PRIMARY KEY, " +
                RemindersContract.ReminderEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                RemindersContract.ReminderEntry.COLUMN_TIME + " INTEGER, " +
                RemindersContract.ReminderEntry.COLUMN_DESCRIPTION + " TEXT, " +
                RemindersContract.ReminderEntry.COLUMN_LOCATION_NAME + " TEXT, " +
                RemindersContract.ReminderEntry.COLUMN_LOCATION_LAT + " REAL, " +
                RemindersContract.ReminderEntry.COLUMN_LOCATION_LON + " REAL);";

        db.execSQL(SQL_CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RemindersContract.ReminderEntry.TABLE_NAME);
        onCreate(db);
    }

}
