package parimal;

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
import android.widget.Toast;

import com.example.main.scalpel.CameraActivity;
import com.example.main.scalpel.FindImageActivity;
import com.example.main.scalpel.FullscreenActivity;
import com.example.main.scalpel.ImageFoundTextActivity;
import com.example.main.scalpel.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Parimal on 3/21/2015.
 */
public class SpeechListener implements RecognitionListener {
    Utility instance;
    private Toast text = KeywordSpotting.toast;
    private String TAKE_PICTURE;
    private String FIND_IMAGE;
    private String MAIN_MENU;
    private String NEW_SEARCH;
    private String NEXT_MATCH;
    private String SEARCH = "search";
    private String CAPTURE_IMAGE;
    private String PROCESS_IMAGE;
    private String NEW_PICTURE;

    private SpeechRecognizer sr;
    private Intent speechIntent;
    private Context context = Utility.context;
    public SpeechListener(Context context){
        instance = Utility.getInstance(context);
        sr = SpeechRecognizer.createSpeechRecognizer(context);
        this.context = context;
        TAKE_PICTURE = context.getString(R.string.takePicture).toLowerCase();
        FIND_IMAGE = context.getString(R.string.findImage).toLowerCase();
        MAIN_MENU = context.getString(R.string.HomePage).toLowerCase();
        NEW_SEARCH = context.getString(R.string.NEW_SEARCH).toLowerCase();
        NEXT_MATCH = context.getString(R.string.NEXT_MATCH).toLowerCase();
        CAPTURE_IMAGE = context.getString(R.string.captureImage).toLowerCase();
        PROCESS_IMAGE = context.getString(R.string.PROCESS_IMAGE).toLowerCase();
        NEW_PICTURE = context.getString(R.string.NEW_PICTURE).toLowerCase();
    }
    public void showCmd(String str){
        Log.e("SpeechCmd", str);
        if(text != null)
            text.cancel();
        text = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        text.show();
    }
    public void FIND_IMAGE_ACTION(){
        if(instance.fullscreen != null){
            Intent rateIntent = new Intent(instance.fullscreen, FindImageActivity.class);
            instance.fullscreen.startActivityForResult(rateIntent,instance.fullscreen.TEXT_SEARCH);
        }
    }
    public void TAKE_PICTURE_ACTION(){
        if(instance.fullscreen != null){
            Button picture = (Button)(instance.fullscreen.findViewById(R.id.takePictureID));
            picture.performClick();
        }

    }
    public void NEW_SEARCH_ACTION(){
        if(instance.foundImageText != null){
            Button new_search = (Button)(instance.foundImageText.findViewById(R.id.tryAgainID));
            new_search.performClick();
        }

    }
    public void SEARCH_ACTION(){
        if(instance.findImage != null){
            Button search = (Button)(instance.findImage.findViewById(R.id.findImageID));
            search.performClick();
        }

    }
    public void CAPTURE_IMAGE_ACTION(){
        if(instance.cameraActivity != null){
            Button capture_image = (Button)(instance.cameraActivity.findViewById(R.id.captureImageID));
            capture_image.performClick();
        }

        /*
        if(instance.cameraActivity != null){
            instance.cameraActivity
                    .mCamera.takePicture(instance.cameraActivity, null,
                                   null, instance.cameraActivity);
        }
        */
    }
    public void PROCESS_IMAGE_ACTION(){
        if(instance.cameraActivity != null){
            Button proc_img = (Button)(instance.cameraActivity.findViewById(R.id.processImageID));
            proc_img.performClick();
        }

    }
    public void NEW_PICTURE_ACTION(){
        if(instance.foundImageActivity != null){
            Button new_picture = (Button)(instance.foundImageActivity.findViewById(R.id.tryAgainID));
            new_picture.performClick();
        }
    }
    public void NEXT_MATCH_ACTION(){
        Button next_match;
        if(instance.foundImageActivity != null){
            next_match = (Button)(instance.foundImageActivity.findViewById(R.id.findBetterID));
            next_match.performClick();
        }
        //
        if(instance.foundImageText != null){
            next_match = (Button)(instance.foundImageText.findViewById(R.id.findBetterID));
            next_match.performClick();
        }
    }
    public void MAIN_MENU_ACTION(){
        Button main_menu;
        if(instance.findImage != null){
            main_menu = (Button)(instance.findImage.findViewById(R.id.homePageFromCamID));
            main_menu.performClick();
        }
        //
        if(instance.foundImageText != null){
            main_menu = (Button)(instance.foundImageText.findViewById(R.id.homePageFromCamID));
            main_menu.performClick();
        }
    }
    public void checkCommands(String cmd) {
        if (cmd != null) {
            if (cmd.equals(TAKE_PICTURE)) {
                showCmd(cmd);
                TAKE_PICTURE_ACTION();
            } else {
            if (cmd.equals(FIND_IMAGE)) {
                showCmd(cmd);
                FIND_IMAGE_ACTION();
            } else {
            if (cmd.equals(MAIN_MENU)) {
                showCmd(cmd);
                MAIN_MENU_ACTION();
            } else {
            if (cmd.equals(NEW_SEARCH)) {
                showCmd(cmd);
                NEW_SEARCH_ACTION();
            } else {
            if (cmd.equals(NEXT_MATCH)) {
                showCmd(cmd);
                NEXT_MATCH_ACTION();
            } else {
            if (cmd.equals(SEARCH)) {
                showCmd(cmd);
                SEARCH_ACTION();
            } else {
            if (cmd.equals(CAPTURE_IMAGE)) {
                showCmd(cmd);
                CAPTURE_IMAGE_ACTION();
            } else {
            if (cmd.equals(PROCESS_IMAGE)) {
                showCmd(cmd);
                PROCESS_IMAGE_ACTION();
            } else {
            if (cmd.equals(NEW_PICTURE)) {
                showCmd(cmd);
                NEW_PICTURE_ACTION();
            } else {
                showCmd("Not a command: "+ cmd);
            }}}}}}}}}
        }
    }
    public void startSpeechR(){
        if(sr != null)
            sr.destroy();
        sr = SpeechRecognizer.createSpeechRecognizer(context);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "parimal");
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech Recognition");
        sr.startListening(speechIntent);
        sr.setRecognitionListener(this);
        Log.e("SpeechTest",sr.toString());
    }
    public void restartKeywordSpotting(){
        Log.e("Speech","Restarting Sphinx");
        sr.destroy();
        try {
            KeywordSpotting.getInstance(KeywordSpotting.context).setupRecognizer(KeywordSpotting.assetDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeywordSpotting.getInstance(KeywordSpotting.context).restart_KeywordSpotting(KeywordSpotting.key);
    }
    public void restartRecognition(){
        Log.e("SpeechTest",sr.toString());
        sr.destroy();
        sr = null;
        startSpeechR();
        Log.e("Speech","restart recognizer");
    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.e("Speech", "onReadyForSpeech");
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
                Log.e("Speech","Network error");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Log.e("Speech","Network Time Out error");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Log.e("Speech","No Match Error");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.e("Speech","ERROR_RECOGNIZER_BUSY");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.e("Speech","Server Error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Log.e("Speech","No speech input");
                break;
        }
        sr.destroy();
        restartKeywordSpotting();
    }
    @Override
    public void onResults(Bundle results) {
        Log.e("Speech","onResults");
        ArrayList<String> result = results.getStringArrayList(sr.RESULTS_RECOGNITION);
        if(result != null){
            for(int i=0;i<result.size();i++){
                Log.e("Speech",result.get(i));
            }
            checkCommands(result.get(0));
            restartKeywordSpotting();
        }else{
            Log.e("Speech","There is nothing in string array");
        }
    }
    @Override
    public void onPartialResults(Bundle partialResults){
        Log.e("Speech","onPartialResults");
    }
    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.e("Speech","onEvent");
    }
}
