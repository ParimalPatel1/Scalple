package com.example.main.scalpel;

import com.example.main.scalpel.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import parimal.Utility;

public class ImageMatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_match);

        TextView Search = (TextView) findViewById(R.id.searchID);
        Search.startAnimation(AnimationUtils.loadAnimation(ImageMatchActivity.this, android.R.anim.slide_in_left));
        //===============================================================================
        //Required for speech recognition to work
        Utility instance = Utility.getInstance(this);
        instance.init_SpeechRecognition();
        instance.imageMatch= this;
        //===============================================================================
    }
    @Override
    public void onResume() {
        super.onResume();
        Utility.context = this;
    }
    public void homePageHandler(View v)
    {
       finish();
    }
}
