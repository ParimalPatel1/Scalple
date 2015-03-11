package com.example.main.scalpel;

import com.example.main.scalpel.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import parimal.SpeechRecognition;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FindImageActivity extends Activity {
    //TODO: Parimal
    SpeechRecognition speak;
    ImageView sImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_image);
        //New code-----------------------------------------
        sImage = (ImageView)findViewById(R.id.imageView2);
        //-------------------------------------------------
    }
    //New code-----------------------------------------
    public void startSpeechIntent(View v){
        Toast.makeText(this,"Start Recognition",Toast.LENGTH_SHORT).show();
        speak = new SpeechRecognition(this);
        speak.startSpeechR();
    }
    //-------------------------------------------------
    public void homePageHandler(View v) {
        finish();
    }

    public void findImageHandler(View v) {
        Intent rateIntent = new Intent(this, ImageFoundActivity.class);
        startActivity(rateIntent);
    }

}
