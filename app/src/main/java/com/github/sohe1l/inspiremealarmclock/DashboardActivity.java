package com.github.sohe1l.inspiremealarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.github.sohe1l.inspiremealarmclock.network.GetQuotesService;
import com.github.sohe1l.inspiremealarmclock.network.RetrofitClientInstance;
import com.github.sohe1l.inspiremealarmclock.receiver.AlarmReceiver;
import com.github.sohe1l.inspiremealarmclock.ui.CreateAlarmActivity;
import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;
import com.github.sohe1l.inspiremealarmclock.ui.debug.QuotesActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAlarmIntent = new Intent(getApplicationContext(), CreateAlarmActivity.class);
                startActivity(createAlarmIntent);
            }
        });

        Context context = getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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



//    public void onOpenQuotes(View view) {
//        Intent i = new Intent(this, QuotesActivity.class);
//        startActivity(i);
//    }
////    private void saveToDatabase(List<Quote> quotesList){
//        for (Quote q: quotesList) {
//            Quote quote = new Quote(q.getQuote(), q.getAuthor(), q.getCategory());
//            mDb.quoteDao().insert(quote);
//        }
//    }
//
//    public void onOpenAlarm(View view) {
//        Intent i = new Intent(this, AlarmActivity.class);
//        startActivity(i);
//    }
//
//    public void onClickSetAlarm(View v)
//    {
//
//        alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000 , pendingIntent );
//    }
//
//
//
//
////    public void onClickSetAlarm(View v)
////    {
////        //Get the current time and set alarm after 10 seconds from current time
////        // so here we get
////        alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000 , pendingIntent );
////    }
//
//
//    private void RegisterAlarmBroadcast()
//    {
//        Log.i("RegisterAlarmB", "Going to register Intent.RegisterAlramBroadcast");
//
//
//        //This is the call back function(BroadcastReceiver) which will be call when your
//        //alarm time will reached.
//        mReceiver = new BroadcastReceiver()
//        {
//            private static final String TAG = "Alarm Example Receiver";
//            @Override
//            public void onReceive(Context context, Intent intent)
//            {
//                Log.i(TAG,"BroadcastReceiver::OnReceive() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                Toast.makeText(context, "Congrats!. Your Alarm time has been reached", Toast.LENGTH_LONG).show();
//            }
//        };
//
//    // register the alarm broadcast here
//        registerReceiver(mReceiver, new IntentFilter("com.github.sohe1l.inspiremealarmclock") );
//        pendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.github.sohe1l.inspiremealarmclock"),0 );
//        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
//    }
//
//
//
//    private void UnregisterAlarmBroadcast()
//    {
//        alarmManager.cancel(pendingIntent);
//        getBaseContext().unregisterReceiver(mReceiver);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(mReceiver);
//        super.onDestroy();
//    }
//
//
//


}
