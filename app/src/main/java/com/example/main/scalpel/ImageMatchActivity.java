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
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ImageMatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_match);
        TextView Search = (TextView) findViewById(R.id.searchID);
        Search.startAnimation(AnimationUtils.loadAnimation(ImageMatchActivity.this, android.R.anim.slide_in_left));
    }

    public void homePageHandler(View v) {
        Intent rateIntent = new Intent(this, FullscreenActivity.class);
        startActivity(rateIntent);
    }
}
