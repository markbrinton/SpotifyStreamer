package com.brintsoft.spotifystreamer.spotify;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.brintsoft.spotifystreamer.ui.ArtistItemArrayAdapter;
import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistItem;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

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

public class SpotifyArtistSearchTask extends AsyncTask<String, Void, ArtistItem[]> {
    private final String LOG_TAG = SpotifyArtistSearchTask.class.getSimpleName();
    private SpotifyApi mApi ;

    private ArrayAdapter<ArtistItem> mArtistAdapter;
    private final Context mContext;
    private Toast mToast = null;

    public SpotifyArtistSearchTask(Context context, ArtistItemArrayAdapter artistAdapter) {
        mContext = context;
        mArtistAdapter = artistAdapter;
    }

    @Override
    protected ArtistItem[] doInBackground(String... params) {
        ArtistItem result[] = null ;

        // Search for the artist name supplied as a parameter.
        String artistName = params[0] ;

        Log.d(LOG_TAG,"SpotifyArtistSearchTask.doInBackground, searching for artist '"+artistName+"'") ;

        mApi = new SpotifyApi() ;

        SpotifyService spotify = mApi.getService() ;

        // Guard against potential problems (Spotify, n/w, API, etc)
        try {
            ArtistsPager results = spotify.searchArtists(artistName);
            Log.d(LOG_TAG,"artist search results = "+results) ;

            int numArtists = results.artists.items.size() ;
            result = new ArtistItem[numArtists] ;

            // Build up an array of ArtistItem objects representing all the matches.
            for( int i=0 ; i<numArtists; i++ ) {
                Artist artist = results.artists.items.get(i) ;

                // There can be 0 or more image URLs for an artist
                String imageURL = null ;
                if( artist.images !=null && !artist.images.isEmpty() ) {
                    imageURL = artist.images.get(0).url ;
                }

                result[i] = new ArtistItem(artist.id, artist.name, imageURL) ;
            }
        }
        catch (Exception e) {
            Log.e(LOG_TAG,"Problem getting artists from Spotify, caught: "+e.getMessage(), e) ;
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArtistItem[] artists) {
        super.onPostExecute(artists);

        // Re-populate the list adapter with the new search results.
        mArtistAdapter.clear();
        if( artists!=null && artists.length>0 ) {
            for( ArtistItem artist : artists ) {
                mArtistAdapter.add(artist);
            }
        } else {
            String notFound = mContext.getText(R.string.toast_no_artist_found).toString() ;
            showToast(notFound);
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
