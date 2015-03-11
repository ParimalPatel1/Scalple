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


public class FullscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fullscreen);
    }

    public void takePictureHandler(View v) {
        Intent rateIntent = new Intent(this, CameraActivity.class);
        startActivity(rateIntent);
    }

    public void findImageHandler(View v) {
        Intent rateIntent = new Intent(this, FindImageActivity.class);
        startActivity(rateIntent);
    }
}
