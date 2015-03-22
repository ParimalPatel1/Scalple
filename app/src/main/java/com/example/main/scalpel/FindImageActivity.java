package com.example.main.scalpel;

import com.example.main.scalpel.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import java.util.ArrayList;

import parimal.KeywordSpotting;
import parimal.SpeechListener;
import parimal.Utility;


/**
 *
 * @see SystemUiHider
 */
public class FindImageActivity extends Activity {

 public static boolean goHome;
 public static final int FIND_IMAGE_FROM_TEXT = 81;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_image);
        //===============================================================================
        Utility instance = Utility.getInstance(this);
        instance.init_SpeechRecognition();
        instance.setFindImageActivity(this);
        //===============================================================================
    }
    public void homePageHandler(View v) {
        finish();
    }
    public void findImageHandler(View v) {
        EditText entered = (EditText)(findViewById(R.id.editText));
        String terms = entered.getText().toString();
        SQLiteSearchText(terms);
        ImageFoundTextActivity.latestSearch = terms;
        Intent rateIntent = new Intent(this, ImageFoundTextActivity.class);
        startActivityForResult(rateIntent, FIND_IMAGE_FROM_TEXT);
    }
    @Override
    public void onActivityResult(int requestCode, int RESULT_CODE, Intent data) {
        /*
        *Due to an error in Ice Cream Sandwich, data will usually be null.
        * so use a boolean set in the other activity to make sure
        * it's actually done
        * */
        if (goHome == true) {
            goHome = false;
            finish();
        }
    }

    public static void SQLiteSearchText(String param) // change this one we have image processing working
    {
        String cols[] = {"DESCRIPTION","NAME","_id"};
        Cursor cc = PCHelper.myDataBase.query("patientPictures",cols,null,null,null,null,null,""+FullscreenActivity.maxMatches);
        SQLiteSearch[] results = new SQLiteSearch[cc.getCount()];

        cc.moveToFirst();
        String id =cc.getString(cc.getColumnIndex("_id"));
        String describe = cc.getString(cc.getColumnIndex("DESCRIPTION"));

        int offBy = Math.abs(LevenshteinDistance.stringDistance(param,describe));
        results[0]=new SQLiteSearch(id, offBy);
        int z =1;
        while(z<cc.getCount())
        {   cc.moveToNext();
            id =cc.getString(cc.getColumnIndex("_id"));
            describe = cc.getString(cc.getColumnIndex("DESCRIPTION"));
            offBy = Math.abs(LevenshteinDistance.stringDistance(param,describe));
            results[z]=new SQLiteSearch(id, offBy);
            z++;}
        results = FullscreenActivity.bubbleSort(results);
        int max =Math.min(FullscreenActivity.maxMatches, results.length);
        SQLiteSearch[] resultsCapped = new SQLiteSearch[max];
        for (int i=0; i<max; i++)
        {resultsCapped[i]=results[i];}
        ImageFoundTextActivity.matches = resultsCapped;
    }

}

