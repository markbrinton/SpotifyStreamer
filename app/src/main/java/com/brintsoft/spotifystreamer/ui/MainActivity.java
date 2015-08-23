package com.brintsoft.spotifystreamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistItem;
import com.brintsoft.spotifystreamer.model.ArtistTrack;
import com.brintsoft.spotifystreamer.spotify.SpotifyArtistSearchTask;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ArtistSelectionCallback, ArtistTrackSelectionCallback, PlayerDialogFragment.OnFragmentInteractionListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()") ;
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_detail_container) != null) {
            Log.d(LOG_TAG, "onCreate() - two pane mode") ;
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a fragment transaction.
            if (savedInstanceState == null) {
                Log.d(LOG_TAG, "onCreate() - two pane mode - replacing fragment") ;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, new ArtistDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            Log.d(LOG_TAG, "onCreate() - NOT two pane mode") ;
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    /** Callback invoked when an artist is selected and we should bring up a list of top tracks */
    public void onArtistSelected(ArtistItem artist) {
        Log.d(LOG_TAG, "onArtistSelected(" + artist + ")") ;
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by adding or replacing
            // the detail fragment using a fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(ArtistDetailFragment.ARG_DETAIL_ARTIST, artist);

            ArtistDetailFragment fragment = new ArtistDetailFragment();
            fragment.setArguments(args);

            Log.d(LOG_TAG, "onArtistSelected - two pane mode, replacing fragment") ;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        }
        else {
            Intent intent = new Intent(this, ArtistDetailActivity.class) ;
            intent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID,   artist.getArtistId() );
            intent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME, artist.getArtistName());

            Log.d(LOG_TAG, "onArtistSelected - one pane mode, starting activity") ;
            startActivity(intent);
        }
    }

    /** Callback invoked when a track is selected and the media player should be launched. */
    public void onArtistTrackSelected(ArrayList<ArtistTrack> tracks, int index) {
        Log.d(LOG_TAG, "onArtistTrackSelected - launching player for track index: "+index) ;

        if( mTwoPane ) {
            // Launch as a fragment dialog
            FragmentManager fm = getSupportFragmentManager() ;
            PlayerDialogFragment playerDialog = PlayerDialogFragment.newInstance(tracks,index) ;

            playerDialog.show(fm, "fragment_player_dialog");
        }
        else {
            // Launch as an intent
            Bundle args = new Bundle() ;
            args.putParcelableArrayList(PlayerDialogFragment.ARG_TRACK_LIST, tracks);
            args.putInt(PlayerDialogFragment.ARG_TRACK_INDEX, index);

            Intent intent = new Intent(this,PlayerDialogActivity.class) ;
            intent.putExtras(args) ;
            startActivity(intent);
        }
    }

    private void tryStuff() {
        SpotifyArtistSearchTask task = new SpotifyArtistSearchTask(this,null);
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

    @Override
    public void onFragmentInteraction() {
        Log.d(LOG_TAG,"onFragmentInteraction:") ;
    }

}
