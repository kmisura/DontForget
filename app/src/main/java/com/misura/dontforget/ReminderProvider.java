package com.misura.dontforget;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kmisura on 02/04/16.
 */
public class ReminderProvider extends ContentProvider {

    static final int REMINDER = 100;
    static final int REMINDER_BY_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ReminderDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = RemindersContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, RemindersContract.PATH_REMINDERS, REMINDER);
        matcher.addURI(authority, RemindersContract.PATH_REMINDERS + "/#", REMINDER_BY_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ReminderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case REMINDER:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RemindersContract.ReminderEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REMINDER_BY_ID:
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(RemindersContract.ReminderEntry.TABLE_NAME);

                retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        RemindersContract.ReminderEntry._ID + " = ? ",
                        new String[]{"" + ContentUris.parseId(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException();
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return RemindersContract.ReminderEntry.CONTENT_TYPE;
            case REMINDER_BY_ID:
                return RemindersContract.ReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri retUri;
        switch (match) {
            case REMINDER:
                long _id = db.insert(RemindersContract.ReminderEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = RemindersContract.ReminderEntry.buildReminderUriById(_id);
                else
                    throw new SQLException("Failed to insert row into: " + uri);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                int count = 0;
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long _id = db.insert(RemindersContract.ReminderEntry.TABLE_NAME, null, cv);
                        if (_id != -1) {
                            count++;
                        }
                    }
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (selection == null)
            selection = "1";
        switch (match) {
            case REMINDER:
                rowsDeleted = db.delete(RemindersContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case REMINDER:
                rowsUpdated = db.update(RemindersContract.ReminderEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }
}
