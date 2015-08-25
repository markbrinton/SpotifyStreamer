package com.brintsoft.spotifystreamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

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
    // http://developer.android.com/training/implementing-navigation/ancestral.html
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected: item=" + item) ;

        // Up navigation, Keeping bundle on parent activity.
        // Solution from:
        // http://stackoverflow.com/questions/14462456/returning-from-an-activity-using-navigateupfromsametask/16147110#16147110

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction() {

    }
}
