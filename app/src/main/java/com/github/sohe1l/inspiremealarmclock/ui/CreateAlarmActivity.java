package com.github.sohe1l.inspiremealarmclock.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarmActivity extends AppCompatActivity {

    @BindView(R.id.tp_time)
    TimePicker timePicker;

    @BindView(R.id.txt_title)
    EditText tvTitle;

    @BindView(R.id.sp_challenge)
    Spinner spChallenge;

    @BindView(R.id.btn_sunday)
    Button btnSunday;

    @BindView(R.id.btn_monday)
    Button btnMonday;

    @BindView(R.id.btn_tuesday)
    Button btnTuesday;

    @BindView(R.id.btn_wednesday)
    Button btnWednesday;

    @BindView(R.id.btn_thursday)
    Button btnThursday;

    @BindView(R.id.btn_friday)
    Button btnFriday;

    @BindView(R.id.btn_saturday)
    Button btnSaturday;

    @BindView(R.id.txt_sound)
    EditText tvSound;

    @BindView(R.id.sw_vibrate)
    Switch swVibrate;


    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDb = AppDatabase.getInstance(getApplicationContext());




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_add_alarm) {
            int[] repeat = new int[1];
            repeat[0] = 1;

            Alarm alarm = new Alarm(tvTitle.getText().toString() ,timePicker.toString(),
                    tvSound.getText().toString(), swVibrate.isChecked(), repeat, spChallenge.getSelectedItem().toString());

            mDb.alarmDao().insert(alarm);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
