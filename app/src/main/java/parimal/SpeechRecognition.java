package parimal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.main.scalpel.R;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Parimal on 3/7/2015.
 */
public class SpeechRecognition {

    //global variables and references
    private static final int REQUEST_OK = 1;
    SpeechRecognizer sr;
    Context context = null;
    //constructor
    public SpeechRecognition(Context context){
        this.context = context;
        sr = SpeechRecognizer.createSpeechRecognizer(context);
    }

    //public methods/functions
    public void startSpeechR(){
        final Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech Recognition");
        sr.startListening(speechIntent);
        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.e("Speech","onReadyForSpeech");
            }
            @Override
            public void onBeginningOfSpeech() {
                Log.e("Speech","onBeginningOfSpeech");
            }
            @Override
            public void onRmsChanged(float rmsdB) {
                //Log.e("Speech","Sound Level: "+rmsdB+"db");
            }
            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.e("Speech","onBufferReceived");
            }
            @Override
            public void onEndOfSpeech() {
                Log.e("Speech","onEndOfSpeech");
            }
            @Override
            public void onError(int error) {
                switch(error){
                    case SpeechRecognizer.ERROR_AUDIO:
                        Log.e("Speech","Audio recording error");
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        Log.e("Speech","Other client side errors");
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        Log.e("Speech","Insufficient permissions");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        Log.e("Speech","Audio recording error");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        Log.e("Speech","Network Time Out error");
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        Log.e("Speech","Audio recording error");
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        Log.e("Speech","No recognition result matched");
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        Log.e("Speech","Server Error");
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        Log.e("Speech","No speech input");
                        break;
                }
            }
            @Override
            public void onResults(Bundle results) {
                Log.e("Speech","onResults");
                ArrayList<String> result = results.getStringArrayList(sr.RESULTS_RECOGNITION);
                if(result != null){
                    //TODO: set search text
                    Toast.makeText(context,result.get(0),Toast.LENGTH_LONG).show();
                    for(int i=0;i<result.size();i++){
                        Log.e("Speech",result.get(i));
                    }
                }else{
                    Log.e("Speech","There is nothing in string array");
                }
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.e("Speech","onPartialResults");
                Toast.makeText(context,"PartialResults is available",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.e("Speech","onEvent");
            }
        });
    }
}
