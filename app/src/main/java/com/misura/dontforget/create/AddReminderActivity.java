package com.misura.dontforget.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.misura.dontforget.R;
import com.misura.dontforget.RemindersContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText mTitle;
    private EditText mDescription;
    private View mTimeTriggerGroup;
    private TextView mTimeTriggerText;
    private Button mTimeTriggerRemove;
    private Button mAddTimeTrigger;
    private Button mFinish;
    private Calendar tmpCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = (EditText) findViewById(R.id.add_reminder_et_title);
        mDescription = (EditText) findViewById(R.id.add_reminder_et_description);
        mAddTimeTrigger = (Button) findViewById(R.id.add_reminder_button_add_time_trigger);
        mAddTimeTrigger.setOnClickListener(this);
        mFinish = (Button) findViewById(R.id.add_reminder_button_add_reminder);
        mFinish.setOnClickListener(this);
        mTimeTriggerGroup = findViewById(R.id.add_reminder_group_time_trigger);
        mTimeTriggerText = (TextView) findViewById(R.id.add_reminder_text_time_trigger);
        mTimeTriggerRemove = (Button) findViewById(R.id.add_reminder_button_time_trigger_remove);
        mTimeTriggerRemove.setOnClickListener(this);
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
        } else if (v == mAddTimeTrigger) {
            tmpCalendar = null;
            DatePickerFragment dateFragment = new DatePickerFragment();
            dateFragment.setListener(this);
            dateFragment.show(getFragmentManager(), "datePicker");
        } else if (v == mTimeTriggerRemove) {
            tmpCalendar = null;
            mTimeTriggerGroup.setVisibility(View.GONE);
            mAddTimeTrigger.setVisibility(View.VISIBLE);
        }
    }

    private void saveReminder() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RemindersContract.ReminderEntry.COLUMN_TITLE, title);
        contentValues.put(RemindersContract.ReminderEntry.COLUMN_DESCRIPTION, description);
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
    }
}
