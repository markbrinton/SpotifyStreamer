package com.brintsoft.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()") ;
        setContentView(R.layout.activity_main);
        // tryStuff() ;
    }

    private void tryStuff() {
        SpotifyArtsitSearchTask task = new SpotifyArtsitSearchTask(this,null);
        Log.d(LOG_TAG, "tryStuff") ;
        task.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"onPause()") ;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onStop()") ;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"onResume()") ;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart()") ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy()") ;
    }
}
