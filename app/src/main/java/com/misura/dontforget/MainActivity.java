package com.misura.dontforget;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.misura.dontforget.create.AddReminderActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    static final int COL_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_DESCRIPTION = 2;
    static final int COL_REMAINING_TIME = 3;
    static final int COL_LOCATION_NAME = 4;
    private static final int REMINDERS_LOADER = 0;
    private static final String[] REMINDERS_LIST_COLUMNS = new String[]{
            RemindersContract.ReminderEntry._ID,
            RemindersContract.ReminderEntry.COLUMN_TITLE,
            RemindersContract.ReminderEntry.COLUMN_DESCRIPTION,
            RemindersContract.ReminderEntry.COLUMN_TIME,
            RemindersContract.ReminderEntry.COLUMN_LOCATION_NAME
    };
    private FloatingActionButton mAddReminderButton;
    private ReminderListAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_activity_title);
        setSupportActionBar(toolbar);

        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);
        assert mAddReminderButton != null;
        mAddReminderButton.setOnClickListener(this);

        mListAdapter = new ReminderListAdapter(this, null);
        mRecyclerView = (RecyclerView) findViewById(R.id.content_main_rv_remider_list);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(REMINDERS_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent addReminderIntent = new Intent(this, AddReminderActivity.class);
        startActivity(addReminderIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                RemindersContract.ReminderEntry.CONTENT_URI,
                REMINDERS_LIST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
    }
}
