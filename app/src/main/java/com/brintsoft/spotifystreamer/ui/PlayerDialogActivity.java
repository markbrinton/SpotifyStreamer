package com.brintsoft.spotifystreamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.brintsoft.spotifystreamer.R;

/**
 * Artist detail shows additional information for a selected artist.
 * At the moment that info is the top 10 tracks for the artist.
 */

public class PlayerDialogActivity extends ActionBarActivity implements PlayerDialogFragment.OnFragmentInteractionListener {
    private static final String LOG_TAG = PlayerDialogActivity.class.getSimpleName() ;

    public static final String EXTRA_TEXT_ARTIST_ID   = "ARTIST_DETAIL.ARTIST_ID" ;
    public static final String EXTRA_TEXT_ARTIST_NAME = "ARTIST_DETAIL.ARTIST_NAME" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate()") ;
        setContentView(R.layout.activity_player_dialog);

        if (savedInstanceState == null  || false) {
            Intent intent = getIntent() ;
            Bundle args = intent.getExtras() ;

            Fragment fragment = new PlayerDialogFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_player_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onFragmentInteraction() {

    }
}
