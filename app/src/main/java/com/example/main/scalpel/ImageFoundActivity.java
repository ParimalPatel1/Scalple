package com.example.main.scalpel;

import com.example.main.scalpel.util.SystemUiHider;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import parimal.Utility;

/**
 * @see SystemUiHider
 */
public class ImageFoundActivity extends Activity {
    public static ImageFoundActivity instance;
    public static SQLiteSearch[] matches;
    public static int searchIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_found);

        instance = this;

        ImageView iv = (ImageView) (findViewById(R.id.imageView));
        iv.setImageBitmap(FullscreenActivity.resultImage);
        ImageView iv2 = (ImageView) (findViewById(R.id.imageViewFound));
        iv2.setVisibility(View.VISIBLE);

        RelativeLayout mainScreen = (RelativeLayout)(findViewById(R.id.foundDisplay));
       int baseWidth = mainScreen.getWidth()/2;
        int baseHeight = mainScreen.getHeight()/2;
        searchIndex =0;

        BitmapAsyncSQLite initial = new BitmapAsyncSQLite(PCHelper.myDataBase,matches[searchIndex]._id
                            , baseWidth,baseHeight, BitmapAsyncSQLite.imageSearch);
        initial.execute();

        //===============================================================================
        //Required for speech recognition to work
        Utility instance = Utility.getInstance(this);
        instance.init_SpeechRecognition();
        instance.setFoundImageActivity(this);
        //===============================================================================
    }

    public void homePageHandler(View v) {
        finish();
    }

    public static void asyncHandler(Bitmap b)
    {
     ImageView iv2 = (ImageView) (instance.findViewById(R.id.imageViewFound));
     iv2.setImageBitmap(b);
    }


    public void findBetterHandler(View v)
    {
        searchIndex++;
        if (searchIndex >=matches.length)
        {searchIndex=0;}

        RelativeLayout mainScreen = (RelativeLayout)(findViewById(R.id.foundDisplay));
        int baseWidth = mainScreen.getWidth()/2;
        int baseHeight = mainScreen.getHeight()/2;

        BitmapAsyncSQLite attempt = new BitmapAsyncSQLite(PCHelper.myDataBase,matches[searchIndex]._id
                                        , baseWidth,baseHeight,BitmapAsyncSQLite.imageSearch);
        attempt.execute();
    }

    public void tryAgainHandler(View v)
    {
        FullscreenActivity.needToTakeNewPicture=true;
        finish();
    }

}
