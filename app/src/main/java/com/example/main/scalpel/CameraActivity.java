package com.example.main.scalpel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import parimal.Utility;

//Based in part on an example from java2s.com

public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {
    public byte[] lastImage; // store results of photo here
    public Camera mCamera;
    SurfaceView mPreview;

    public static final int downSamplingRate = 16;
    private static final int HALFQUALITY = 50;

    //These are the smallest sizes on my camera.
    //They are here as an example, and the program will overwrite them
    public int pictureWidth = 640;
    public int pictureHeight = 480;
    public int landScape = 90; // in degrees

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //===============================================================================
        //Required for speech recognition to work
        Utility instance = Utility.getInstance(this);
        instance.init_SpeechRecognition();
        instance.setCameraActivity(this);
        //===============================================================================
        mPreview = (SurfaceView)findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if(mCamera!=null){mCamera.release();}
        mCamera = Camera.open();

        lastImage=null; // avoid any memory leaks

        Camera.Parameters params = mCamera.getParameters();

        //Loop trhough the camera sizes and display them on console
        List<Camera.Size> picSizes = params.getSupportedPictureSizes();
        for (int ii=0; ii<picSizes.size();ii++)
            Log.v("CameraSizes", "Width: " + picSizes.get(ii).width + " Height:" + picSizes.get(ii).height);

        //The smallest size option comes last, pick this to save data
        //smallestWidth = picSizes.get(picSizes.size()-1).width;
        //smallestHeight = picSizes.get(picSizes.size()-1).height;

        //Pick the largest to demonstrate data-saving efficacy
        pictureWidth = picSizes.get(0).width;
        pictureHeight = picSizes.get(0).height;
        params.setPictureSize(pictureWidth,pictureHeight);

        //Set it to JPEG format if it contains that
        List<Integer> picFormats = params.getSupportedPictureFormats();
        for (int ii=0; ii<picFormats.size();ii++)
        {
            if(picFormats.get(ii)== ImageFormat.JPEG)
            {params.setPictureFormat(ImageFormat.JPEG);
                params.setJpegQuality(HALFQUALITY);}
        }
        params.setRotation(landScape);//Change this if images are showing up sideways
        mCamera.setParameters(params);
        ((Button)(findViewById(R.id.processImageID))).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        Log.d("CAMERA","Destroy");
    }

    public void captureImageHandler(View v)
    {
        mCamera.takePicture(this, null, null, this);
    }
    public void homePageHandler(View v)
    {
     FullscreenActivity.justFinishedTakingPicture=false; //no picture was taken
     lastImage = null;
     finish();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);

        //Camera is naturally sidewise, fix it with a rotation
        mCamera.setDisplayOrientation(landScape);
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW","surfaceDestroyed");
    }

    //Exiting too early can cause garbage collection errors
    //Display warning message to avoid this
    @Override
    public void onShutter() {
        Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera)
    {
        lastImage = data;
        //picture has been taken, so now there is something to process
        ((Button)(findViewById(R.id.processImageID))).setVisibility(View.VISIBLE);

        camera.startPreview();
    }

    //Take PNG byte array, convert to JPEG  byte array
    public void pngToJpeg()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap b= BitmapFactory.decodeByteArray(lastImage, 0, lastImage.length);
        b.compress(Bitmap.CompressFormat.JPEG,0,out);
        /*use only one byte array variable throughout activity
        to avoid excessive memory usage*/
        this.lastImage = out.toByteArray();

        Log.v("Me","Finished Conversion");
    }

    public void processImageHandler(View v)
    {
        boolean aPictureWasTaken = (lastImage!=null);
        if(aPictureWasTaken)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = this.downSamplingRate;
            //pngToJpeg(); //uncomment if your phone camera doesn't support JPEG
            FullscreenActivity.resultImage =BitmapFactory.decodeByteArray(lastImage,0,lastImage.length,options);

            Intent resultIntent = this.getIntent();
            setResult(Activity.RESULT_OK);

            FullscreenActivity.justFinishedTakingPicture = true;
        }

        this.lastImage=null; //conserve memory
        finish();
    }

}
