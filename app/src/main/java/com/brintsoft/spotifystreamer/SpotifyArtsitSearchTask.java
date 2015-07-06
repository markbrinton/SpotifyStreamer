package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by mark on 17/06/15.
 */
public class SpotifyArtsitSearchTask extends AsyncTask<String, Void, ArtistItem[]> {
    private final String LOG_TAG = SpotifyArtsitSearchTask.class.getSimpleName();
    private SpotifyApi mApi ;

    private ArrayAdapter<ArtistItem> mArtistAdapter;
    private final Context mContext;

    public SpotifyArtsitSearchTask(Context context, ArrayAdapter<ArtistItem> artistAdapter) {
        mContext = context;
        mArtistAdapter = artistAdapter;
    }

    @Override
    protected ArtistItem[] doInBackground(String... params) {
        ArtistItem result[] = null ;
        String artistName = params[0] ;

        Log.d(LOG_TAG,"SpotifyArtsitSearchTask.doInBackground, searching for artist '"+artistName+"'") ;

        mApi = new SpotifyApi() ;

        SpotifyService spotify = mApi.getService() ;
        Log.d("play", "spotify service = " + spotify) ;

        ArtistsPager results = spotify.searchArtists(artistName);
        Log.d("play","results = "+results) ;

        int numArtists = results.artists.items.size() ;
        result = new ArtistItem[numArtists] ;

        for( int i=0 ; i<numArtists; i++ ) {
            Artist artist = results.artists.items.get(i) ;

            Log.d("play", "artist = " + artist.name) ;
            for( Image pic : artist.images ) {
                Log.d("play", ".. image: " + pic.url) ;
//                Picasso.with( context ).load( uri ).ito( view ) ;
            }

            String imageURL = null ;
            if( artist.images !=null && !artist.images.isEmpty() ) {
                imageURL = artist.images.get(0).url ;
            }
            result[i] = new ArtistItem(artist.name, imageURL) ;
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArtistItem[] artists) {
        super.onPostExecute(artists);
        mArtistAdapter.clear();
        if( artists!=null ) {
            for( ArtistItem artist : artists ) {
                mArtistAdapter.add(artist);
            }
        }
        mArtistAdapter.notifyDataSetChanged();
    }
}
