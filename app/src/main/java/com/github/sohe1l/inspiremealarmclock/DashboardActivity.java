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

    // https://developer.android.com/training/scheduling/alarms#java
    // https://github.com/googlesamples/android-RepeatingAlarm/

    //used for register alarm manager
    PendingIntent alarmIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;



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
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        Context context = getApplicationContext();








//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//
//        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
//
//
//        alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);
//
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                SystemClock.elapsedRealtime() + 15000,
//                15000, alarmIntent);



//
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        10 * 1000, alarmIntent);




        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000,
        //AlarmManager.INTERVAL_DAY, alarmIntent);




        //RegisterAlarmBroadcast();
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

    public void onOpenQuotes(View view) {
        Intent i = new Intent(this, QuotesActivity.class);
        startActivity(i);
    }



    AppDatabase mDb;

    public void onLoadQuotes(View view) {

        mDb = AppDatabase.getInstance(this);

        int countQuotes = mDb.quoteDao().getNumberOfQuotes();

        Log.d("TAGGGG", String.valueOf(countQuotes));
        if(countQuotes < 5){


//            /*Create handle for the RetrofitInstance interface*/
//            GetQuotesService service = RetrofitClientInstance.getRetrofitInstance().create(GetQuotesService.class);
//            Call<List<Quote>> call = service.getQuotes();
//
//            Log.wtf("URL Called", call.request().url() + "");


//            call.enqueue(new Callback<List<Quote>>() {
//                @Override
//                public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
//
//                    Log.d("TAGGGG", "Got Response");
//
//                    if (response.isSuccessful()) {
//                        Log.d("TAGGGG", "Got Response --- ok");
//
//                        saveToDatabase(response.body());
//                    } else {
//
//                        Log.d("TAGGGG", "Got Response --- error");
//
//                        String errorBody = response.errorBody().toString();
//                        Log.d("TAGGGG", "ERROR " + errorBody);
//
//                    }
//
//
//
//                }
//
//                @Override
//                public void onFailure(Call<List<Quote>> call, Throwable t) {
//
//                    Log.d("TAGGGG", "Got Response -- BIG ERROR");
//
//                    Toast.makeText(DashboardActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_LONG).show();
//
//
//                    if (t instanceof IOException) {
//                        Log.d("TAGGGG", "Got Response -- NETWORK ERROR");
//
//
//                        // logging probably not necessary
//                    }
//                    else {
//                        Log.d("TAGGGG", "Got Response -- CONVERSION ERROR");
//
//                    }
//
//                    // error
//                }
//            });


        }


    }

    private void saveToDatabase(List<Quote> quotesList){
        Log.d("TAGGGG", "Saving Response ... Count: " +  quotesList.size());


        for (Quote q: quotesList) {
            Quote quote = new Quote(q.getQuote(), q.getAuthor(), q.getCategory());
            mDb.quoteDao().insert(quote);
        }

        Log.d("TAGGGG", "Done Saving Response!");

    }

    public void onOpenAlarm(View view) {
        Intent i = new Intent(this, AlarmActivity.class);
        startActivity(i);
    }


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





























