package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Artist Detail shows top 10 tracks for a selected artist.
 * When called as an intent the artist ID & name should be passed as Extra text values.
 * The keys are:
 *   ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID
 *   ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME
 *
 */

public class ArtistDetailFragment extends Fragment {
    private final String LOG_TAG = ArtistDetailFragment.class.getSimpleName();
    private final String PREFS_KEY_ARTIST_ID   = "artist_id";
    private final String PREFS_KEY_ARTIST_NAME = "artist_name";

    private String mArtistId ;
    private String mArtistName ;
    private ArtistTrackArrayAdapter mArtistTrackAdapter ;

    public ArtistDetailFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState) ;

        Log.d(LOG_TAG,"onCreateView()") ;

        View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        Intent intent = getActivity().getIntent() ;

        List<ArtistTrack> emptyData = new ArrayList<ArtistTrack>();
        mArtistTrackAdapter = new ArtistTrackArrayAdapter(getActivity().getBaseContext(), emptyData);

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        artistListView.setAdapter(mArtistTrackAdapter);

        // Artist ID & name are passed when the Intent is called.
        // Execute a task to get the top tracks for the artist.

        if( intent!=null && intent.hasExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID) ) {
            mArtistId   = intent.getExtras().getString(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID) ;
            mArtistName = intent.getExtras().getString(ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME) ;
            Log.d(LOG_TAG, "ArtistDetailFragment: extra text, artist ID = " + mArtistId+", name = "+mArtistName) ;

            findTracks() ;
        }

        return rootView ;
    }

    private void findTracks() {
        SpotifyArtistTracksTask tracksTask = new SpotifyArtistTracksTask(getActivity(),mArtistTrackAdapter) ;
        tracksTask.execute(mArtistId) ;

        // Include artist name in the title
        String title = getString(R.string.title_activity_artist_detail).toString() ;
        getActivity().setTitle(title+": "+mArtistName);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"onPause()") ;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mArtistId   = prefs.getString(PREFS_KEY_ARTIST_ID, "") ;
        mArtistName = prefs.getString(PREFS_KEY_ARTIST_NAME, "") ;

        Log.d(LOG_TAG, "onResume() restoring artist id '" + mArtistId + "', nane '" + mArtistName + "'") ;

        if(mArtistId!=null && mArtistId.length()>0 ) {
            findTracks();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(LOG_TAG, "onStop() saving artistId '"+mArtistId+"', name '"+mArtistName+"'") ;

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREFS_KEY_ARTIST_ID,   mArtistId) ;
        editor.putString(PREFS_KEY_ARTIST_NAME, mArtistName) ;
        editor.commit() ;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart()") ;
    }
}
