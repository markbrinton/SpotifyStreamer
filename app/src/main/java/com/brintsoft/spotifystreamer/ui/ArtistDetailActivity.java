package com.brintsoft.spotifystreamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistItem;
import com.brintsoft.spotifystreamer.model.ArtistTrack;

import java.util.ArrayList;

/**
 * Artist detail shows additional information for a selected artist.
 * At the moment that info is the top 10 tracks for the artist.
 */

public class ArtistDetailActivity extends ActionBarActivity implements ArtistTrackSelectionCallback {
    private static final String LOG_TAG = ArtistDetailActivity.class.getSimpleName() ;

    public static final String EXTRA_TEXT_ARTIST_ID   = "ARTIST_DETAIL.ARTIST_ID" ;
    public static final String EXTRA_TEXT_ARTIST_NAME = "ARTIST_DETAIL.ARTIST_NAME" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate()") ;
        setContentView(R.layout.activity_artist_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent() ;
            String artistName = intent.getStringExtra(EXTRA_TEXT_ARTIST_NAME) ;
            String artistId   = intent.getStringExtra(EXTRA_TEXT_ARTIST_ID) ;

            ArtistItem artist = new ArtistItem(artistName,artistId,null) ;

            Bundle arguments = new Bundle();
            arguments.putParcelable(ArtistDetailFragment.ARG_DETAIL_ARTIST, artist);

            ArtistDetailFragment fragment = new ArtistDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    // http://developer.android.com/training/implementing-navigation/ancestral.html
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG,"onOptionsItemSelected: item="+item) ;

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
    public void onArtistTrackSelected(ArrayList<ArtistTrack> tracks, int index) {
        Log.d(LOG_TAG, "onArtistTrackSelected - launching player for track index: "+index) ;

        // Launch as an intent
        Bundle args = new Bundle() ;
        args.putParcelableArrayList(PlayerDialogFragment.ARG_TRACK_LIST, tracks);
        args.putInt(PlayerDialogFragment.ARG_TRACK_INDEX, index);

        Intent intent = new Intent(this,PlayerDialogActivity.class) ;
        intent.putExtras(args) ;
        startActivity(intent);
    }
}
