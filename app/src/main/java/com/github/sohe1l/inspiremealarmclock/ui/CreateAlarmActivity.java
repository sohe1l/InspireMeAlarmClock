package com.github.sohe1l.inspiremealarmclock.ui;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarmActivity extends AppCompatActivity {

    private static final String TAG = CreateAlarmActivity.class.toString();
    private static final int REQUEST_RINGTONE = 100;

    Alarm alarm; // if its null means we are creating a new alarm otherwise we are editing

    @BindView(R.id.tp_time)
    TimePicker timePicker;

    @BindView(R.id.txt_title)
    EditText tvTitle;

    @BindView(R.id.sw_repeat)
    Switch swRepeat;

    @BindView(R.id.layDays)
    LinearLayout layDays;

    @BindView(R.id.btn_sunday)
    ToggleButton btnSunday;

    @BindView(R.id.btn_monday)
    ToggleButton btnMonday;

    @BindView(R.id.btn_tuesday)
    ToggleButton btnTuesday;

    @BindView(R.id.btn_wednesday)
    ToggleButton btnWednesday;

    @BindView(R.id.btn_thursday)
    ToggleButton btnThursday;

    @BindView(R.id.btn_friday)
    ToggleButton btnFriday;

    @BindView(R.id.btn_saturday)
    ToggleButton btnSaturday;

    @BindView(R.id.tv_ringtone)
    TextView tvRingtone;

    @BindView(R.id.btn_ringtone)
    ImageButton btnRingtone;

    @BindView(R.id.sw_vibrate)
    Switch swVibrate;

    @BindView(R.id.sw_active)
    Switch swActive;

    Uri ringtoneUri;
    Ringtone ringtone;

    private AppDatabase mDb;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_delete_alarm)
    Button btnDeleteAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Load the default ringtone
        selectRingtone(RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM));

        swRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layDays.setVisibility(View.VISIBLE);
                }else{
                    layDays.setVisibility(View.GONE);
                }
            }
        });


        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(Alarm.INTENT_KEY)){ // editing alarm
            alarm = creatingIntent.getParcelableExtra(Alarm.INTENT_KEY);
            populateForm();
        }else{ // creating alarm
            swActive.setVisibility(View.GONE);
            btnDeleteAlarm.setVisibility(View.GONE);

        }

        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    private void populateForm(){
        btnDeleteAlarm.setVisibility(View.VISIBLE);
        swActive.setChecked(alarm.isActive());
        swVibrate.setChecked(alarm.isVibrate());
        tvTitle.setText(alarm.getLabel());
        timePicker.setCurrentHour(alarm.getHour());
        timePicker.setCurrentMinute(alarm.getMinute());

        ArrayList<Integer> repeat = alarm.getRepeat();
        if(repeat.size() == 0){
            swRepeat.setChecked(false);
        }else{
            swRepeat.setChecked(true);
            if(repeat.contains(1)) btnMonday.setChecked(true);
            if(repeat.contains(2)) btnTuesday.setChecked(true);
            if(repeat.contains(3)) btnWednesday.setChecked(true);
            if(repeat.contains(4)) btnThursday.setChecked(true);
            if(repeat.contains(5)) btnFriday.setChecked(true);
            if(repeat.contains(6)) btnSaturday.setChecked(true);
            if(repeat.contains(7)) btnSunday.setChecked(true);
        }

        selectRingtone(alarm.getRingtone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "SELECTED MENU");

        int id = item.getItemId();

        if(id == R.id.action_add_alarm) {
            if(alarm == null)
                saveAlarm();
            else
                updateAlarm();
        }else if(id == R.id.action_try_alarm){
            Intent alarmIntent = new Intent(this, AlarmActivity.class);
            alarmIntent.putExtra(Alarm.INTENT_KEY, alarm);
            startActivity(alarmIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveAlarm(){
        Alarm alarm = new Alarm(
                true, // always start as active swActive.isChecked()
                tvTitle.getText().toString(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute(),
                ringtoneUri,
                swVibrate.isChecked(),
                getRepeat(),
                "");
        mDb.alarmDao().insert(alarm);
        finish();
    }

    private void updateAlarm(){
        alarm.setActive(swActive.isChecked());
        alarm.setLabel(tvTitle.getText().toString());
        alarm.setHour(timePicker.getCurrentHour());
        alarm.setMinute(timePicker.getCurrentMinute());
        alarm.setRingtone(ringtoneUri);
        alarm.setVibrate(swVibrate.isChecked());
        alarm.setRepeat(getRepeat());
        mDb.alarmDao().update(alarm);
        finish();
    }

    private ArrayList<Integer> getRepeat(){
        ArrayList<Integer> repeatList = new ArrayList<Integer>();

        if(btnMonday.isChecked()){
            repeatList.add(1);
        }
        if(btnTuesday.isChecked()){
            repeatList.add(2);
        }
        if(btnWednesday.isChecked()){
            repeatList.add(3);
        }
        if(btnThursday.isChecked()){
            repeatList.add(4);
        }
        if(btnFriday.isChecked()){
            repeatList.add(5);
        }
        if(btnSaturday.isChecked()){
            repeatList.add(6);
        }
        if(btnSunday.isChecked()){
            repeatList.add(7);
        }

        return repeatList;
    }

    public void selectRingtone(View view) {
        final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose a tone:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_ALARM_ALERT_URI);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        startActivityForResult(intent, REQUEST_RINGTONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Load the selected ringtone
        if(requestCode == REQUEST_RINGTONE && resultCode == RESULT_OK){
            selectRingtone((Uri) (data != null ? data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) : null));
        }
    }

    // Set the selected ringtone and update in UI
    private void selectRingtone(Uri uri){
        if(uri != null){
            ringtoneUri = uri;
            ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            tvRingtone.setText(ringtone.getTitle(this));
        } else {
            ringtoneUri = null;
            tvRingtone.setText(R.string.alarm_silent);

        }
    }

    public void deleteAlarm(View view) {
        mDb.alarmDao().delete(alarm);
        finish();
    }
}
