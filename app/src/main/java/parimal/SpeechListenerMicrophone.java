package parimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.main.scalpel.R;

import java.util.ArrayList;

/**
 * Created by Parimal on 3/24/2015.
 */
public class SpeechListenerMicrophone implements RecognitionListener {
    static SpeechRecognizer sr;
    static Intent speechIntent;
    Activity ac;
    final String LOG = "Microphone";
    public SpeechListenerMicrophone(Activity ac){
        this.ac = ac;
    }
    public void startRecognition(){
        Utility.getInstance().stopAllSpeech();
        if(sr != null){
            sr.stopListening();
            sr.cancel();
            sr.destroy();
        }
        sr = SpeechRecognizer.createSpeechRecognizer(ac.getApplicationContext());
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "parimal");
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech Recognition");
        sr.startListening(speechIntent);
        sr.setRecognitionListener(this);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(LOG,"onBeginningOfSpeech");
    }
    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.e(LOG,"onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.e(LOG,"onError");
        Utility.getInstance().init_SpeechRecognition();
    }

    @Override
    public void onResults(Bundle results) {
        if(results != null){
            EditText text = (EditText) ac.findViewById(R.id.editText);
            ArrayList<String> list = results.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            if(list != null){
                String str = list.get(0);
                Log.e(LOG,"Result"+str);
                text.setText(str);
                if(ac != null){
                    Button search = (Button)(ac.findViewById(R.id.findImageID));
                    search.performClick();
                }
            }
        }
        stopAll();
        Utility.getInstance().init_SpeechRecognition();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
    public void stopAll(){
        if(sr != null){
            sr.stopListening();
            sr.cancel();
            sr.destroy();
        }
    }
}
