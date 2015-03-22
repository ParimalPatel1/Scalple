package parimal;

import android.content.Context;

import com.example.main.scalpel.CameraActivity;
import com.example.main.scalpel.FindImageActivity;
import com.example.main.scalpel.FullscreenActivity;
import com.example.main.scalpel.ImageFoundActivity;
import com.example.main.scalpel.ImageFoundTextActivity;
import com.example.main.scalpel.ImageMatchActivity;
import com.example.main.scalpel.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parimal on 3/7/2015.
 */
public class Utility {
    private KeywordSpotting kws_instance;
    private static Utility instance = null;
    public static Context context;
    FullscreenActivity fullscreen = null;
    FindImageActivity findImage = null;
    public CameraActivity cameraActivity = null;
    public ImageFoundActivity foundImageActivity = null;
    public ImageFoundTextActivity foundImageText;
    public ImageMatchActivity imageMatch;

    public Utility(){

    }
    public void setFullscreenActivity(FullscreenActivity ac){
        this.fullscreen = ac;
    }
    public void setFindImageActivity(FindImageActivity ac){
        this.findImage = ac;
    }
    public static Utility getInstance(Context context_in){
        if(instance == null){
            instance = new Utility();
        }
        context = context_in;
        return instance;
    }
    public void init_SpeechRecognition(){
        kws_instance = KeywordSpotting.getInstance(context);
        kws_instance.startUpSphinx();
    }

    public void setCameraActivity(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
    }
    public CameraActivity getCameraActivity() {
        return cameraActivity;
    }

    public void setFoundImageActivity(ImageFoundActivity foundImageActivity) {
        this.foundImageActivity = foundImageActivity;
    }

    public ImageFoundActivity getFoundImageActivity() {
        return foundImageActivity;
    }
}
