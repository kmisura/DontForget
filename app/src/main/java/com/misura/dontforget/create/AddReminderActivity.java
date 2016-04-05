package com.misura.dontforget.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.misura.dontforget.R;
import com.misura.dontforget.RemindersContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String LOG_TAG = AddReminderActivity.class.getSimpleName();
    private static final int PLACE_PICKER_REQUEST = 1;

    private EditText mTitle;
    private EditText mDescription;

    private View mLocationTriggerGroup;
    private TextView mLocationTriggerText;
    private Button mLocationTriggerRemove;
    private Button mAddLocationTrigger;

    private View mTimeTriggerGroup;
    private TextView mTimeTriggerText;
    private Button mTimeTriggerRemove;
    private Button mAddTimeTrigger;

    private Button mFinish;
    private Calendar tmpCalendar;
    private Calendar mFinalCalendar;
    private Place mSelectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_reminder_activity_title);

        mTitle = (EditText) findViewById(R.id.add_reminder_et_title);
        mDescription = (EditText) findViewById(R.id.add_reminder_et_description);

        mAddLocationTrigger = (Button) findViewById(R.id.add_reminder_button_add_location_trigger);
        mAddLocationTrigger.setOnClickListener(this);
        mLocationTriggerGroup = findViewById(R.id.add_reminder_group_location_trigger);
        mLocationTriggerText = (TextView) findViewById(R.id.add_reminder_text_location_trigger);
        mLocationTriggerRemove = (Button) findViewById(R.id.add_reminder_button_location_trigger_remove);
        mLocationTriggerRemove.setOnClickListener(this);

        mAddTimeTrigger = (Button) findViewById(R.id.add_reminder_button_add_time_trigger);
        mAddTimeTrigger.setOnClickListener(this);
        mTimeTriggerGroup = findViewById(R.id.add_reminder_group_time_trigger);
        mTimeTriggerText = (TextView) findViewById(R.id.add_reminder_text_time_trigger);
        mTimeTriggerRemove = (Button) findViewById(R.id.add_reminder_button_time_trigger_remove);
        mTimeTriggerRemove.setOnClickListener(this);

        mFinish = (Button) findViewById(R.id.add_reminder_button_add_reminder);
        mFinish.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mFinish) {
            saveReminder();
            finish();
        } else if (v == mAddLocationTrigger) {
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(LOG_TAG, "Problem when starting the place picker", e);
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(LOG_TAG, "Problem when starting the place picker", e);
            }
        } else if (v == mAddTimeTrigger) {
            tmpCalendar = null;
            DatePickerFragment dateFragment = new DatePickerFragment();
            dateFragment.setListener(this);
            dateFragment.show(getFragmentManager(), "datePicker");
        } else if (v == mTimeTriggerRemove) {
            tmpCalendar = null;
            mFinalCalendar = null;
            mTimeTriggerGroup.setVisibility(View.GONE);
            mAddTimeTrigger.setVisibility(View.VISIBLE);
        } else if (v == mLocationTriggerRemove) {
            mSelectedPlace = null;
            mLocationTriggerGroup.setVisibility(View.GONE);
            mAddLocationTrigger.setVisibility(View.VISIBLE);
        }
    }

    private void saveReminder() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RemindersContract.ReminderEntry.COLUMN_TITLE, title);
        contentValues.put(RemindersContract.ReminderEntry.COLUMN_DESCRIPTION, description);
        if (mFinalCalendar != null) {
            int time = (int) (mFinalCalendar.getTimeInMillis() / 1000);
            contentValues.put(RemindersContract.ReminderEntry.COLUMN_TIME, time);
        }

        if (mSelectedPlace != null) {
            contentValues.put(RemindersContract.ReminderEntry.COLUMN_LOCATION_LAT, mSelectedPlace.getLatLng().latitude);
            contentValues.put(RemindersContract.ReminderEntry.COLUMN_LOCATION_LON, mSelectedPlace.getLatLng().longitude);
            contentValues.put(RemindersContract.ReminderEntry.COLUMN_LOCATION_NAME, "" + mSelectedPlace.getName());
        }

        getContentResolver().insert(RemindersContract.ReminderEntry.CONTENT_URI, contentValues);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        tmpCalendar = Calendar.getInstance();
        tmpCalendar.set(Calendar.YEAR, year);
        tmpCalendar.set(Calendar.MONTH, monthOfYear);
        tmpCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        TimePickerFragment timeFragment = new TimePickerFragment();
        timeFragment.setListener(this);
        timeFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        tmpCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        tmpCalendar.set(Calendar.MINUTE, minute);
        mAddTimeTrigger.setVisibility(View.GONE);
        mTimeTriggerGroup.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
        String formattedTime = format.format(tmpCalendar.getTime());
        mTimeTriggerText.setText(formattedTime);
        mFinalCalendar = tmpCalendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                mSelectedPlace = PlacePicker.getPlace(this, data);
                mAddLocationTrigger.setVisibility(View.GONE);
                mLocationTriggerGroup.setVisibility(View.VISIBLE);
                mLocationTriggerText.setText(mSelectedPlace.getName());
            }
        }
    }
}
