package com.brintsoft.spotifystreamer;

import android.content.Intent;
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

    private String mArtistId ;
    private String mArtistName ;
    private ArtistTrackArrayAdapter mArtistTrackAdapter ;

    public ArtistDetailFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

            SpotifyArtistTracksTask tracksTask = new SpotifyArtistTracksTask(getActivity(),mArtistTrackAdapter) ;
            tracksTask.execute(mArtistId) ;

            // Include artist name in the title
            String title = getString(R.string.title_activity_artist_detail).toString() ;
            getActivity().setTitle(title+": "+mArtistName);
        }

        return rootView ;
    }

}
