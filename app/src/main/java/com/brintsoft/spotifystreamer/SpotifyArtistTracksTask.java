package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * An AsyncTask class that will use the Spotify Web API to bring back a list of artists based on
 * a search string.
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
    private final Context mContext;
    private Toast mToast = null;

    public SpotifyArtistTracksTask(Context context, ArtistTrackArrayAdapter artistTrackAdapter) {
        mContext = context;
        mArtistTrackAdapter = artistTrackAdapter;
    }

    @Override
    protected ArtistTrack[] doInBackground(String... params) {
        ArtistTrack result[] = null ;

        // Search for the artist name supplied as a parameter.
        String artistId = params[0] ;

        Log.d(LOG_TAG,"SpotifyArtistTracksTask.doInBackground, searching for artist id = '"+artistId+"'") ;

        mApi = new SpotifyApi() ;

        SpotifyService spotify = mApi.getService() ;

        Map<String,Object> options = new HashMap<String,Object>() ;
        options.put("country",COUNTRY_CODE) ;

        Tracks topTracks = spotify.getArtistTopTrack(artistId,options) ;
        Log.d(LOG_TAG, "getArtistTopTrack success, got " + topTracks) ;

        int numTracks = topTracks.tracks.size() ;
        result = new ArtistTrack[numTracks] ;

        for( int i=0; i<numTracks; i++ ) {
            Track track = topTracks.tracks.get(i) ;
            List<Image> albumImages = track.album.images ;
            String imageURL = (albumImages!=null && !albumImages.isEmpty())? albumImages.get(0).url : null ;
            ArtistTrack artistTrack = new ArtistTrack(track.id,track.album.name,track.name, imageURL) ;
            result[i] = artistTrack ;
            Log.d(LOG_TAG,"Track "+i+", "+artistTrack) ;
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
            }
        } else {
            showToast("No tracks found") ;
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