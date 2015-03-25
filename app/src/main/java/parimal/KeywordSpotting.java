package parimal;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.LENGTH_SHORT;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Parimal on 3/10/2015.
 */
public class KeywordSpotting implements edu.cmu.pocketsphinx.RecognitionListener{
    public static Context context = null;
    protected static KeywordSpotting instance = null;
    private SpeechRecognizer recognizer;
    public static String key = "key";
    public static String value = "scalpel";
    public static File assetDirectory;
    static Toast toast;
    public static SpeechListener andSpeRec;
    //HashMap<String,String> keywords;
    //Implementation of singleton pattern--------------------
    private KeywordSpotting(){}
    public static KeywordSpotting getInstance(Context context_in){
        if(instance == null){
            instance = new KeywordSpotting();
        }
        context = context_in;
        andSpeRec = new SpeechListener(context);
        return instance;
    }
    //--------------------------------------------------------
    //Sphinx implementation-----------------------------------
    public void startUpSphinx(){
        new AsyncTask<Void,Void,Exception>(){
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(context);
                    File assetDir = assets.syncAssets();
                    assetDirectory = assetDir;
                    setupRecognizer(assetDir);
                }catch (IOException e){
                    return e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Log.e("Scalpel","Failed to init sphinx recognizer" + result);
                } else {
                    restart_KeywordSpotting(key);
                }
            }
        }.execute();
    }
    public void restart_KeywordSpotting(String key) {
        stopRecognizer();
        startRecognizer(key);
    }
    public void stopRecognizer(){
        recognizer.stop();
        Log.e("Scalpel","stop Recognizer Listener");
    }
    public void startRecognizer(String key){
        recognizer.startListening(key);
        Log.e("Scalpel","start Recognizer Listener: ");
    }
    public void setupRecognizer(File assetDir) throws IOException {
        if(recognizer != null)
            recognizer.shutdown();
        File modelsDir = new File(assetDir, "models");
        Log.e("File",modelsDir.toString());
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetDir).setKeywordThreshold(1e-20f)
                .getRecognizer();
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(key,value);
        //-----------------------------------------

        //-----------------------------------------
    }
    //--------------------------------------------------------
    @Override
    public void onBeginningOfSpeech() {
        Log.e("Scalpel","BeginningOfSpeech");
    }
    @Override
    public void onEndOfSpeech() {
        Log.e("Scalpel","EndOfSpeech");
    }
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        String result = "";
        if(hypothesis != null) {
            result = hypothesis.getHypstr();
            if(result.equals(value)){
                Log.e("Scalpel","PartialResult is keyword: " + result);
                if(toast != null)
                    toast.cancel();
                toast = Toast.makeText(context, result, LENGTH_SHORT);
                toast.show();
                Log.e("Scalpel","Recognizer Shutdown");
                stopAll();
                Log.e("Scalpel","Starting Recognizer Intent");
                andSpeRec.startSpeechR();
            }
        }
    }
    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.e("Scalpel","on Result");
        if(hypothesis != null){
            Log.e("Scalpel","Result " + hypothesis.getHypstr());
        }
    }
    @Override
    public void onError(Exception e) {
        Log.e("File",e.toString());
    }

    @Override
    public void onTimeout() {

    }
    public void stopAll(){
        recognizer.stop();
        recognizer.cancel();
        recognizer.shutdown();
    }
}
