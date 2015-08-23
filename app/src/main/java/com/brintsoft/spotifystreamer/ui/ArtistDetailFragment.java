package com.brintsoft.spotifystreamer.ui;

import android.support.v4.app.FragmentManager ;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistItem;
import com.brintsoft.spotifystreamer.model.ArtistTrack;
import com.brintsoft.spotifystreamer.spotify.SpotifyArtistTracksTask;

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
    private static final String KEY_ARTIST_ID    = "artist_id";
    private static final String KEY_ARTIST_NAME  = "artist_name";
    private static final String KEY_SAVED_TRACKS = "saved_tracks" ;

    public static final String ARG_DETAIL_ARTIST  = "artist_detail" ;

    /** Spotify ID of the artist we are showing tracks for. */
    private String mArtistId ;
    private String mArtistName ;

    /** Keep a list of tracks.  Same list that goes into the track adapter. */
    private ArrayList<ArtistTrack> mTracks = new ArrayList<ArtistTrack>() ;

    private ArtistTrackArrayAdapter mArtistTrackAdapter ;

    public ArtistDetailFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState) ;

        Bundle arguments = getArguments();
        if (arguments != null) {
            ArtistItem artist = arguments.getParcelable(ArtistDetailFragment.ARG_DETAIL_ARTIST) ;
            mArtistId   = artist.getArtistId() ;
            mArtistName = artist.getArtistName() ;
        }

        Log.d(LOG_TAG,"onCreateView()") ;
        View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        List<ArtistTrack> emptyData = new ArrayList<ArtistTrack>();
        mArtistTrackAdapter = new ArtistTrackArrayAdapter(getActivity().getBaseContext(), emptyData);

        ListView artistTrackListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        artistTrackListView.setAdapter(mArtistTrackAdapter);

        // Set action when an artist is selected from the list
        artistTrackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistTrack track = mArtistTrackAdapter.getItem(position);
                Log.d(LOG_TAG, "You picked track #"+position+": " + track);

                showPlayerDialog(position) ;
            }
        });

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

    /** Bring up the media player dialog, via an intent */
    private void xxxshowPlayerDialog(int index) {
        Log.d(LOG_TAG, "showPlayerDialog, track index = " + index) ;

        FragmentManager fm = getActivity().getSupportFragmentManager() ;
        PlayerDialogFragment playerDialog = PlayerDialogFragment.newInstance(mTracks,index) ;

        playerDialog.show(fm, "fragment_player_dialog");
    }

    /** Bring up the media player dialog.  Get the parent activity to decide how to do that, */
    private void showPlayerDialog(int index) {
        Log.d(LOG_TAG, "showPlayerDialog, track index = " + index) ;

        ArtistTrackSelectionCallback trackCallback = (ArtistTrackSelectionCallback)getActivity() ;
        trackCallback.onArtistTrackSelected(mTracks,index);
    }

    /** Restore list of tracks from the bundle. */
    private void restoreTracks(Bundle savedInstanceState) {
        ArrayList<ArtistTrack> trackList = savedInstanceState.getParcelableArrayList(KEY_SAVED_TRACKS) ;
        if( trackList!=null ) {
            Log.d(LOG_TAG, "restoreTracks() restored "+trackList.size()+" tracks") ;
            mArtistTrackAdapter.addAll(trackList);
            mTracks.addAll(trackList) ;
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

        int numTracks = mTracks.size() ;
        Log.d(LOG_TAG, "onSaveInstanceState() saving " + numTracks + " tracks");

        saveInstanceState.putParcelableArrayList(KEY_SAVED_TRACKS, mTracks);
        saveInstanceState.putString(KEY_ARTIST_ID, mArtistId);
        saveInstanceState.putString(KEY_ARTIST_NAME, mArtistName);
    }

    private void findTracks() {
        SpotifyArtistTracksTask tracksTask = new SpotifyArtistTracksTask(getActivity(),mArtistTrackAdapter,mTracks) ;
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
