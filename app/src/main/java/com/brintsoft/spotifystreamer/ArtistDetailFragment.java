package com.brintsoft.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private static final String LOG_TAG = ArtistDetailFragment.class.getSimpleName();
    private static final String KEY_ARTIST_ID   = "artist_id";
    private static final String KEY_ARTIST_NAME = "artist_name";
    private static final String KEY_SAVED_TRACKS = "saved_tracks" ;

    private String mArtistId ;
    private String mArtistName ;

    private ArtistTrackArrayAdapter mArtistTrackAdapter ;

    public ArtistDetailFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState) ;

        Log.d(LOG_TAG,"onCreateView()") ;
        View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        List<ArtistTrack> emptyData = new ArrayList<ArtistTrack>();
        mArtistTrackAdapter = new ArtistTrackArrayAdapter(getActivity().getBaseContext(), emptyData);

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        artistListView.setAdapter(mArtistTrackAdapter);

        Intent intent = getActivity().getIntent() ;

        // Artist ID & name are passed when the Intent is called.
        // Execute a task to get the top tracks for the artist.
        if( intent!=null && intent.hasExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID) ) {
            mArtistId   = intent.getExtras().getString(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID) ;
            mArtistName = intent.getExtras().getString(ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME) ;
            Log.d(LOG_TAG, "ArtistDetailFragment: extra text, artist ID = " + mArtistId + ", name = " + mArtistName) ;
        }

        // If we don't have a saved list of tracks then we use Spotify API to get it.
        if( savedInstanceState==null || !savedInstanceState.containsKey(KEY_SAVED_TRACKS) ) {
            findTracks() ;
        }
        else {
            restoreTracks(savedInstanceState);
        }

        setTracksTitle();

        return rootView ;
    }

    // Restore list of tracks from the bundle.
    private void restoreTracks(Bundle savedInstanceState) {
        ArrayList<ArtistTrack> trackList = savedInstanceState.getParcelableArrayList(KEY_SAVED_TRACKS) ;
        if( trackList!=null ) {
            Log.d(LOG_TAG, "restoreTracks() restored "+trackList.size()+" tracks") ;
            mArtistTrackAdapter.addAll(trackList);
        }
        else {
            Log.d(LOG_TAG, "restoreTracks() no tracks to restore") ;
        }

        mArtistId   = savedInstanceState.getString(KEY_ARTIST_ID) ;
        mArtistName = savedInstanceState.getString(KEY_ARTIST_NAME) ;
        Log.d(LOG_TAG, "restoreTracks() restored artistId: '"+mArtistId+"', artistName: '"+mArtistName+"'") ;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        ArrayList<ArtistTrack> tracks = new ArrayList<ArtistTrack>() ;
        int numTracks = mArtistTrackAdapter.getCount() ;

        Log.d(LOG_TAG, "onSaveInstanceState() saving " + numTracks + " tracks");

        for(int i=0; i<numTracks; i++ ) {
            tracks.add(mArtistTrackAdapter.getItem(i)) ;
        }

        saveInstanceState.putParcelableArrayList(KEY_SAVED_TRACKS, tracks);
        saveInstanceState.putString(KEY_ARTIST_ID, mArtistId);
        saveInstanceState.putString(KEY_ARTIST_NAME, mArtistName);
    }

    private void findTracks() {
        SpotifyArtistTracksTask tracksTask = new SpotifyArtistTracksTask(getActivity(),mArtistTrackAdapter) ;
        tracksTask.execute(mArtistId) ;

    }

    // Put the artist name in the activity title
    private void setTracksTitle() {
        // Include artist name in the title
        String title = getString(R.string.title_activity_artist_detail).toString() ;
        getActivity().setTitle(title + ": " + mArtistName);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart()") ;
    }
}
