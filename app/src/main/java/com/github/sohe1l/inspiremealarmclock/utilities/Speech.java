package com.github.sohe1l.inspiremealarmclock.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class Speech implements RecognitionListener {

    private static final String TAG = "Speech Class";
    private SpeechRecognizer speech = null;
    private Context context;
    private SpeechCallback speechCallback;

    public Speech(Context context, SpeechCallback speechCallback){
        this.context = context;
        this.speechCallback = speechCallback;
    }

    public void startListening(){
        if (speech != null) {
            cleanUpSpeech();
        }

        // After testing on several decides this settings works the best
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        speech.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) { }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsDB) { }

    @Override
    public void onBufferReceived(byte[] buffer) { }

    /**
     * Called after the user stops speaking.
     */
    @Override
    public void onEndOfSpeech() {
        speechCallback.endOfSpeech();
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
        }
    }

    @Override
    public void onResults(Bundle results) {
        speechCallback.processResultBundle(results);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        speechCallback.processResultBundle(partialResults);
    }

    @Override
    public void onEvent(int eventType, Bundle params) { }

    public void cleanUpSpeech(){
        if (speech != null) {
            speech.destroy();
        }
    }

    public interface SpeechCallback{
        void processResultBundle(Bundle partialResults);
        void endOfSpeech();
    }
}
