package com.misura.dontforget;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kmisura on 04/04/16.
 */
public class ReminderListAdapter extends CursorRecyclerViewAdapter<ReminderListAdapter.ViewHolder> {
    private static final int SECONDS_IN_DAY = 3600 * 24;

    ReminderListAdapter(Context context, Cursor cursor) {
        super(context, cursor, RemindersContract.ReminderEntry._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.titleView.setText(cursor.getString(MainActivity.COL_TITLE));
        viewHolder.descriptionView.setText(cursor.getString(MainActivity.COL_DESCRIPTION));
        if (cursor.getString(MainActivity.COL_LOCATION_NAME) != null) {
            viewHolder.remainingView.setText(cursor.getString(MainActivity.COL_LOCATION_NAME));
        } else {
            viewHolder.remainingView.setText(getFormattedRemainingTime(cursor.getInt(MainActivity.COL_REMAINING_TIME)));
        }
    }

    private String getFormattedRemainingTime(int secondsSinceEpoch) {
        int currentSeconds = (int) (System.currentTimeMillis() / 1000);
        int diffSeconds = secondsSinceEpoch - currentSeconds;
        return formatTime(diffSeconds);
    }

    private String formatTime(int seconds) {
        if (seconds < 60) {
            String unformatted = getContext().getResources().getString(R.string.main_activity_remaining_time_seconds);
            return String.format(unformatted, seconds);
        } else if (seconds < 3600) {
            String unformatted = getContext().getResources().getString(R.string.main_activity_remaining_time_minutes_seconds);
            int minutes = seconds / 60;
            int sec = seconds % 60;
            return String.format(unformatted, minutes, sec);
        } else if (seconds < 3600 * 24) {
            String unformatted = getContext().getResources().getString(R.string.main_activity_remaining_time_hours_minutes);
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            return String.format(unformatted, hours, minutes);
        } else {
            String unformatted = getContext().getResources().getString(R.string.main_activity_remaining_time_days_hours);
            int days = seconds / SECONDS_IN_DAY;
            int hours = (seconds % SECONDS_IN_DAY) / 3600;
            return String.format(unformatted, days, hours);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;
        public final TextView remainingView;

        public ViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R.id.reminder_list_item_text_title);
            this.descriptionView = (TextView) view.findViewById(R.id.reminder_list_item_text_description);
            this.remainingView = (TextView) view.findViewById(R.id.reminder_list_item_text_remaining);
        }
    }
}
