package com.misura.dontforget;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mTitle;
    private EditText mDescription;
    private Button mFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        mTitle = (EditText) findViewById(R.id.add_reminder_et_title);
        mDescription = (EditText) findViewById(R.id.add_reminder_et_description);
        mFinish = (Button) findViewById(R.id.add_reminder_button_add_reminder);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mFinish) {
            saveReminder();
            finish();
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
}
