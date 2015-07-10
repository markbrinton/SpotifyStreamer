package com.brintsoft.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Artist detail shows additional information for a selected artist.
 * At the moment that info is the top 10 tracks for the artist.
 */

public class ArtistDetailActivity extends ActionBarActivity {
    private static final String LOG_TAG = ArtistDetailActivity.class.getSimpleName() ;

    public static final String EXTRA_TEXT_ARTIST_ID   = "ARTIST_DETAIL.ARTIST_ID" ;
    public static final String EXTRA_TEXT_ARTIST_NAME = "ARTIST_DETAIL.ARTIST_NAME" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate()") ;
        setContentView(R.layout.activity_artist_detail);
    }
}
