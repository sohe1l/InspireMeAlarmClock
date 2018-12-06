package com.github.sohe1l.inspiremealarmclock.ui.alarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.github.sohe1l.inspiremealarmclock.utilities.Speech;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity
        implements Speech.SpeechCallback, TextHighlighter.TextHighlighterCallback {

    private static final String TAG = AlarmActivity.class.toString();

    private Quote quote;
    private Alarm alarm;
    private boolean isChallengeDone = false;

    private AdView mAdView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private final String ANALYTICS_EVENT_KEY = "CHALLENGE_DONE";
    private final String ANALYTICS_NUM_TRY_KEY = "NUM_TRIES";
    private int tryCounter = 0;

    private boolean isTesting = false;


    private Speech speech;
    @BindView(R.id.tvQuote)
    TextView tvQuote;

    @BindView(R.id.btn_speak)
    ImageButton btnSpeak;

    @BindView(R.id.btn_share)
    ImageButton btnShare;

    @BindView(R.id.alarm_view)
    LinearLayout alarmView;

    private Ringtone ringtone;
    private Vibrator vibrator;
    private Animation speakBtnAnim;
    private TextHighlighter textHighlighter;
    private AsyncTask asyncTaskHighlighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf("AlarmActivity", "On create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ButterKnife.bind(this);

        // Keeps the screen on while alarm is ringing...
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        speakBtnAnim = AnimationUtils.loadAnimation(this, R.anim.bouncing);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        speakBtnAnim.setInterpolator(interpolator);

        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(Alarm.INTENT_KEY)){
            alarm = creatingIntent.getParcelableExtra(Alarm.INTENT_KEY);
            startAlarm();
        }

        if(creatingIntent.hasExtra(Alarm.INTENT_KEY_TESTING)){
            isTesting = creatingIntent.getBooleanExtra(Alarm.INTENT_KEY_TESTING, false);
        }

        quote = Quote.getRandomQuote(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        tvQuote.setText(quote.getQuote());

        textHighlighter = new TextHighlighter(tvQuote, quote.getQuote(), getResources().getColor(R.color.highlightedQuote), this);
        asyncTaskHighlighter = textHighlighter.execute();

        speech = new Speech(this, this);
    }

    private void challengeDone(){
        isChallengeDone = true;

        if(!isTesting){
            // update the challenge done on db
            AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
            alarm.setChallengeDone(true);
            mDb.alarmDao().update(alarm);

            //delete the quote
            mDb.quoteDao().delete(quote); // delete so next time new quote will appear
        }

        stopAlarm();
        btnSpeak.clearAnimation();
        btnSpeak.setVisibility(View.GONE);
        btnShare.setVisibility(View.VISIBLE);

        tvQuote.setText(quote.getQuote());
        tvQuote.setTextColor(getResources().getColor(R.color.colorWhite));
        alarmView.setBackgroundColor(getResources().getColor(R.color.highlightedQuote));

        // Log event
        Bundle bundle = new Bundle();
        bundle.putInt(ANALYTICS_NUM_TRY_KEY, tryCounter);
        mFirebaseAnalytics.logEvent(ANALYTICS_EVENT_KEY, bundle);
    }


    private void check_permission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d(TAG, "PERMISSION - SHOW EXPLANATION");

            } else {
                Log.d(TAG, "PERMISSION - REQUESTING");

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        123);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d(TAG, "PERMISSION ALREADY GRANTED");

        }
    }

    public void startListening(View view) {
        if(isChallengeDone) return;

        stopAlarm();

        check_permission();

        tryCounter++;

        btnSpeak.startAnimation(speakBtnAnim);
        btnSpeak.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_bg_round_orange));

        speech.startListening();
    }


    private void startAlarm(){

        if(isChallengeDone) return;
        Log.wtf("GGGG", "Cleared animation");

        btnSpeak.clearAnimation();
        btnSpeak.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_bg_round));

        if(ringtone == null || !ringtone.isPlaying()){
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarm.getRingtone());
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
            ringtone.play();
        }

        if(alarm.isVibrate()){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] mVibratePattern = new long[]{300, 500};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, 1);
                vibrator.vibrate(effect);
            }else{
                //deprecated in API 26
                vibrator.vibrate(mVibratePattern, 0);
            }
        }
    }

    private void stopAlarm(){
        if(ringtone != null){
            ringtone.stop();
        }

        if(vibrator != null){
            vibrator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        Log.wtf("AlarmActivity", "On Destroy");
        stopAlarm();
        asyncTaskHighlighter.cancel(true);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //Log.wtf("AlarmActivity", "On pause");
        speech.cleanUpSpeech();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.wtf("AlarmActivity", "On resume");
        stopAlarm();
        startAlarm();
    }


    public void processResultBundle(Bundle results) {
        try {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                for(String s : matches){
                    textHighlighter.checkWords(s);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Process result exception!");
        }
    }

    @Override
    public void endOfSpeech() {
        startAlarm();
    }

    public void shareQuote(View view) {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_title))
                .setText(getString(R.string.share_text, quote.getQuote()))
                .startChooser();
    }

    @Override
    public void onHighlightingDone() {
        challengeDone();
    }
}
