package com.github.sohe1l.inspiremealarmclock.ui.alarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sohe1l.inspiremealarmclock.DashboardActivity;
import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.github.sohe1l.inspiremealarmclock.network.GetQuotesService;
import com.github.sohe1l.inspiremealarmclock.network.RetrofitClientInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends AppCompatActivity implements RecognitionListener {

    private static final String TAG = AlarmActivity.class.toString();

    @BindView(R.id.tvQuote) TextView tvQuote;

    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer = "";
    Quote quote;
    String lowerCaseQuoteText;
    Spannable spannable;

    private SpeechRecognizer speech = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        // Keeps the screen on while alarm is ringing...
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



//        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
//
//

//        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
//            updateResults("\nNo voice recognition support on your device!");
//        } else {
//            LanguageDetailsReceiver ldr = new  LanguageDetailsReceiver(this);
//            sendOrderedBroadcast(RecognizerIntent
//                            .getVoiceDetailsIntent(this), null, ldr, null,
//                    Activity.RESULT_OK, null, null);
//        }


        loadRandomQuote();

        spannable = new SpannableString(quote.getQuote());
        tvQuote.setText(spannable);

        startListening();
    }

    private void loadRandomQuote(){

        AppDatabase mDB = AppDatabase.getInstance(this);

        int count = mDB.quoteDao().getNumberOfQuotes();
        Log.v (TAG, "Quotes count:" + String.valueOf(count));

        if(count == 0){
            // the database is empty, load new quotes
            loadQuotesFromAPI();

            // use the default quote
            quote = new Quote(getString(R.string.default_quote),
                              getString(R.string.default_quote_author),
                              getString(R.string.default_quote_category));

        }else{
            quote = mDB.quoteDao().getRandomQuote();
            lowerCaseQuoteText = quote.getQuote().toLowerCase();
            Log.v (TAG, "Random quotes: " + quote);
        }
    }

    private void loadQuotesFromAPI(){

        GetQuotesService service = RetrofitClientInstance.getRetrofitInstance().create(GetQuotesService.class);
        Call<List<Quote>> call = service.getQuotes();

        call.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful()) {

                    AppDatabase mDB = AppDatabase.getInstance(getApplicationContext());

                    List<Quote> quotesList = response.body();
                    for (Quote q: quotesList) {
                        Quote quote = new Quote(q.getQuote(), q.getAuthor(), q.getCategory());
                        mDB.quoteDao().insert(quote);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.d(TAG, "Error on loading quotes from the API: " + t.getMessage());
            }
        });
    }


    private void startListening(){
        if (speech != null) {
            cleanUpSpeech();
        }

        // After testing on several decides this settings works the best
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        speech.startListening(intent);
    }

    // Lazy instantiation method for getting the speech recognizer
    private SpeechRecognizer getSpeechRevognizer(){
        if (speech == null) {
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            speech.setRecognitionListener(this);
        }

        return speech;
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
//            if (resultCode == RESULT_OK) {
//                List<String> results = data.getStringArrayListExtra (RecognizerIntent.EXTRA_RESULTS);
//                mAnswer = results.get(0);
//
//                if (mAnswer.toUpperCase().indexOf("AMAZON") > -1)
//                    mTextView.setText("\n\nQuestion: " + mQuestion +
//                            "\n\nYour answer is '" + mAnswer +
//                            "' and it is correct!");
//                else
//                    mTextView.setText("\n\nQuestion: " + mQuestion +
//                            "\n\nYour answer is '" + mAnswer +
//                            "' and it is incorrect!");
//            }
//        }
//    }




























    /**
     * Called when the endpointer is ready for the user to start speaking.
     *
     * @param params parameters set by the recognition service. Reserved for future use.
     */
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG,"onReadyForSpeech");

    }

    /**
     * The user has started to speak.
     */
    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG,"onBeginningOfSpeech");

    }

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    @Override
    public void onRmsChanged(float rmsdB) {
        //Log.d(TAG,"onRmsChanged");

    }

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     *               single channel audio stream. The sample rate is implementation dependent.
     */
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG,"onBufferReceived");

    }

    /**
     * Called after the user stops speaking.
     */
    @Override
    public void onEndOfSpeech() {
        Log.d(TAG,"onEndOfSpeech");

    }

    @Override
    protected void onPause() {
        cleanUpSpeech();
        super.onPause();
    }

    // Reference https://github.com/fcrisciani/android-speech-recognition/
    @Override
    public void onError(int error) {
        Log.d(TAG,"onError");

        String message;
        boolean restart = true;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                restart = false;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                restart = false;
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Not recognised";
                break;
        }

        Log.d(TAG,"onError code:" + error + " message: " + message);
        if (speech != null) {
            speech.cancel();
            startListening();
        }
    }

    /**
     * Called when recognition results are ready.
     *
     * @param results the recognition results. To retrieve the results in {@code
     *                ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
     *                {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter. A float array of
     *                confidence values might also be given in {@link SpeechRecognizer#CONFIDENCE_SCORES}.
     */
    @Override
    public void onResults(Bundle results) {
        Log.d(TAG,"onResults");
        processResultBundle(results);
        startListening();
    }


    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG,"onPartialResults");
        processResultBundle(partialResults);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG,"onEvent");
    }



    private void cleanUpSpeech(){
        if (speech != null) {
            //speech.stopListening();
            //speech.cancel();
            speech.destroy();
            //speech = null;
        }
    }

    private void processResultBundle(Bundle results) {
        try {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                processResult(matches);
            }
        } catch (Exception e) {
            Log.d(TAG, "Process result exception!");
        }
    }

    private void processResult(List<String> stringArr){
        int start, index;
        for(String s : stringArr){
            s = s.toLowerCase();
            Log.d(TAG, "Checking :" + s);

            // check if string has multiple words, split them!
            if(s.matches(".*\\s.*")){ //it has whitespace
                processResult(Arrays.asList(s.split("\\s")));
                continue; // skip the current iteration
            }

            start = 0;
            index = lowerCaseQuoteText.indexOf(s);
            while(index != -1){
                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), index, index+s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvQuote.setText(spannable);
                start = index+s.length() ;
                index = lowerCaseQuoteText.indexOf(s, start);
            }
        }
    }

}
