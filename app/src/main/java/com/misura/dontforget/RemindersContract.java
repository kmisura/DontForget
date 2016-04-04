package com.misura.dontforget;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kmisura on 02/04/16.
 */
public class RemindersContract {
    public static final String CONTENT_AUTHORITY = "com.misura.dontforget";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_REMINDERS = "reminders";

    public static final class ReminderEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMINDERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMINDERS;

        public static final String TABLE_NAME = "reminder";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_LOCATION_NAME = "location_name";
        public static final String COLUMN_LOCATION_LAT = "location_lat";
        public static final String COLUMN_LOCATION_LON = "location_lon";

        public static Uri buildReminderUriById(long id) {
            return CONTENT_URI.buildUpon().appendPath("" + id).build();
        }
    }

}
