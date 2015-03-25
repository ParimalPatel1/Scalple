package com.example.main.scalpel;

import com.example.main.scalpel.util.SystemUiHider;
//
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import parimal.SpeechListener;
import parimal.Utility;


public class FullscreenActivity extends Activity  {

    /*
    //singleton pattern ---------------------------
    public static FullscreenActivity instance;
    protected FullscreenActivity(){}
    public static FullscreenActivity getInstance(){
        if(instance != null){
            instance = new FullscreenActivity();
        }
        return instance;
    }
    //singleton pattern ---------------------------
    */
    public static int maxMatches = 5;

    public static Bitmap resultImage = null; // initialize to avoid errors

    public static final int TEXT_SEARCH = 85;
    public static final int REQUEST_CODE = 87; // chosen arbitrarily
    public static final int TAKE_NEW_PICTURE = 89;

    public static boolean justFinishedTakingPicture = false;
    public static boolean needToTakeNewPicture = false;

    /*To allow calling non-static methods in a static circumstance
       *create a universal (static) object of this class so the calls are
       * counted as dynamic
       */
    public static FullscreenActivity mainActivityInstance = new FullscreenActivity();

    public void takePictureHandler(View v)
    {
        Intent myIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(myIntent, REQUEST_CODE);
    }

    public void findImageHandler(View v) {
        Intent rateIntent = new Intent(this, FindImageActivity.class);
        startActivityForResult(rateIntent,TEXT_SEARCH);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        this.mainActivityInstance = this;

        PCHelper myDbHelper = new PCHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        //===============================================================================
        Utility instance = Utility.getInstance(this);
        instance.init_SpeechRecognition();
        instance.setFullscreenActivity(this);
        //===============================================================================
    }
    @Override
    public void onActivityResult(int requestCode, int RESULT_CODE, Intent data) {
        /*
        *Due to an error in Ice Cream Sandwich, data will usually be null.
        * so use a boolean set in the other activity to make sure
        * it's actually done
        * */
        if (justFinishedTakingPicture == true) {
            justFinishedTakingPicture = false;

           /* Due to an error in Ice Cream Sandwich, the other activity
            *cannot reliably return an extra,
            *so it just sets resultImage to the picture taken.
            * We will then process this
            */

            // For debugging purposes, keep track of memory allocation
            int dataSizeOfBitmap = resultImage.getRowBytes() * resultImage.getHeight();
            Log.v("Data used", "" + dataSizeOfBitmap);
            Log.v("Max Data:,", "" + Runtime.getRuntime().maxMemory());

            SQLiteSearch(87); //Test until further image processing;

            //Now go display the image
            Intent rateIntent = new Intent(this, ImageFoundActivity.class);
            startActivityForResult(rateIntent, TAKE_NEW_PICTURE);

        }
         // told to take a new picture, start CameraActivity
        else if (needToTakeNewPicture == true) {
            needToTakeNewPicture = false;
            Intent rateIntent = new Intent(this, CameraActivity.class);
            startActivityForResult(rateIntent, REQUEST_CODE);
        }
    }

    //Believed to be from mathcs.com
    public static SQLiteSearch[] bubbleSort( SQLiteSearch [ ] results )
    {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        SQLiteSearch temp;   //holding variable

        while ( flag )
        {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < results.length-1;  j++ )
            {
                if ( results[ j ].nonSimiliarity > results[j+1].nonSimiliarity )   // change to > for ascending sort
                {
                    temp = results[ j ];                //swap elements
                    results[ j ] = results[ j+1 ];
                    results[ j+1 ] = temp;
                    flag = true;              //shows a swap occurred
                }
            }
        }
        return results;
    }

    public static void SQLiteSearch(int param) // change this one we have image processing working
    {
        String cols[] = {"SCORE","NAME","_id"};
        Cursor cc = PCHelper.myDataBase.query("patientPictures",cols,null,null,null,null,null,""+maxMatches);
       SQLiteSearch[] results = new SQLiteSearch[cc.getCount()];

        cc.moveToFirst();
        String id =cc.getString(cc.getColumnIndex("_id"));
        int score = cc.getInt(cc.getColumnIndex("SCORE"));
        int offBy = Math.abs(score-param);
        results[0]=new SQLiteSearch(id, offBy);

        int z =1;
        while(z<cc.getCount())
        {
            cc.moveToNext();
             id =cc.getString(cc.getColumnIndex("_id"));
             score = cc.getInt(cc.getColumnIndex("SCORE"));

             offBy = Math.abs(score-param);

            results[z]=new SQLiteSearch(id, offBy);
           z++;
        }
         results = bubbleSort(results);

        int max =Math.min(maxMatches, results.length);
        SQLiteSearch[] resultsCapped = new SQLiteSearch[max];
        for (int i=0; i<max; i++)
        {resultsCapped[i]=results[i];}
        ImageFoundActivity.matches = resultsCapped;
    }
    @Override
    public void onResume() {
        super.onResume();
        Utility.context = this;
    }



}