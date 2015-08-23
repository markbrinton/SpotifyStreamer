package com.brintsoft.spotifystreamer.spotify;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistTrack;
import com.brintsoft.spotifystreamer.ui.ArtistTrackArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * An AsyncTask class that will use the Spotify Web API to bring back a list of tracks for an artist.
 *
 * Spotify Web API:
 *   https://github.com/kaaes/spotify-web-api-android
 *
 * That provides an API on top of the official Spotify API
 *   https://developer.spotify.com/web-api/endpoint-reference/
 *
 * Created by mark on 17/06/15.
 */

public class SpotifyArtistTracksTask extends AsyncTask<String, Void, ArtistTrack[]> {
    private final String LOG_TAG = SpotifyArtistTracksTask.class.getSimpleName();
    private final String COUNTRY_CODE = "GB" ;

    private SpotifyApi mApi ;

    private ArtistTrackArrayAdapter mArtistTrackAdapter;
    private List<ArtistTrack> mTracks ;
    private final Context mContext;
    private Toast mToast = null;

    public SpotifyArtistTracksTask(Context context, ArtistTrackArrayAdapter artistTrackAdapter, List<ArtistTrack> tracks) {
        mContext = context;
        mArtistTrackAdapter = artistTrackAdapter;
        mTracks = tracks ;
    }

    @Override
    protected ArtistTrack[] doInBackground(String... params) {
        ArtistTrack result[] = null ;

        // Search for the artist name supplied as a parameter.
        String artistId = params[0] ;

        Log.d(LOG_TAG,"SpotifyArtistTracksTask.doInBackground, searching for artist id = '"+artistId+"'") ;

        if( artistId==null ) {
            return result ;
        }

        mApi = new SpotifyApi() ;

        SpotifyService spotify = mApi.getService() ;

        // Country code has to be included in the options
        // TODO: Make it a user preference.
        Map<String,Object> options = new HashMap<String,Object>() ;
        options.put("country",COUNTRY_CODE) ;

        // Guard against potential RetrofitError error
        try {
            Tracks topTracks = spotify.getArtistTopTrack(artistId,options) ;
            Log.d(LOG_TAG, "getArtistTopTrack success, got " + topTracks) ;

            int numTracks = topTracks.tracks.size() ;
            result = new ArtistTrack[numTracks] ;

            for( int i=0; i<numTracks; i++ ) {
                Track track = topTracks.tracks.get(i) ;

                // TODO: this gets the 1st image - but we should search through to find smallest size.
                List<Image> albumImages = track.album.images ;
                String imageURL = (albumImages!=null && !albumImages.isEmpty())? albumImages.get(0).url : null ;
                String artistName = track.artists.get(0).name ;

                ArtistTrack artistTrack = new ArtistTrack(track.id, artistName, track.album.name, track.name, imageURL, track.preview_url) ;
                result[i] = artistTrack ;
                Log.d(LOG_TAG,"Track "+i+", "+artistTrack) ;
            }
        }
        // Only expect to see a RetrofitError exception, Handle any exception just in case.
        catch( Exception e ) {
            Log.e(LOG_TAG, "getArtistTopTrack failed, caught: " + e.getMessage(), e) ;
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArtistTrack[] artistTracks) {
        super.onPostExecute(artistTracks);

        // Re-populate the list adapter with the new search results.
        mArtistTrackAdapter.clear();
        if( artistTracks!=null && artistTracks.length>0 ) {
            for( ArtistTrack track : artistTracks ) {
                mArtistTrackAdapter.add(track);
                mTracks.add(track) ;
            }
        } else {
            String noTracks = mContext.getString(R.string.toast_no_tracks_found) ;
            showToast(noTracks) ;
        }
    }

    private void showToast(String text) {
        // Cancel any existing Toast so they don't stack up.
        if( mToast!=null ) {
            mToast.cancel() ;
        }

        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
