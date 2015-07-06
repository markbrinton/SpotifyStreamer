package com.brintsoft.spotifystreamer;

import android.util.Log;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by mark on 17/06/15.
 */
public class ApiPlay {
    private static SpotifyApi mApi = null;

    public static void trySomething() {
        mApi = new SpotifyApi() ;

        SpotifyService spotify = mApi.getService() ;
        Log.d("play","spotify service = "+spotify) ;

        //NewReleases releases = spotify.getNewReleases() ;
        //Log.d("play","releases = "+releases) ;

        //Pager<AlbumSimple> albums = releases.albums ;
        //for( AlbumSimple album : albums.items ) {
        //    Log.d("play","Album name: "+album.name) ;
        //}
    }
}
