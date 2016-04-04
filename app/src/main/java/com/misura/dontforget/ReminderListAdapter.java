package com.misura.dontforget;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kmisura on 04/04/16.
 */
public class ReminderListAdapter extends CursorRecyclerViewAdapter<ReminderListAdapter.ViewHolder> {
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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;

        public ViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R.id.reminder_list_item_text_title);
            this.descriptionView = (TextView) view.findViewById(R.id.reminder_list_item_text_description);
        }
    }
}
