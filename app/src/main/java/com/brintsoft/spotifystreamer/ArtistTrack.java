package com.brintsoft.spotifystreamer;

/**
 * Value class for an Artist's Track.
 *
 * A subset of the data we can get from the API class: kaaes.spotify.webapi.android.models.Track
 * We only need the track id, name,album name and a URL for an associated image.
 *
 * Created by mark on 06/07/15.
 */

public class ArtistTrack {
    private String mTrackId ;
    private String mImageURI ;
    private String mAlbumName ;
    private String mTrackName ;

    public ArtistTrack(String id, String albumName, String trackName, String imageURI) {
        mTrackId    = id ;
        mAlbumName  = albumName ;
        mTrackName  = trackName ;
        mImageURI   = imageURI ;
    }

    public String getImageURL() {
        return mImageURI ;
    }

    public String getTrackId() {
        return mTrackId ;
    }

    public String getAlbumName() {
        return mAlbumName ;
    }

    public String getTrackName() {
        return mTrackName ;
    }

    public String toString() {
        return "[ArtistTrack: track='"+mTrackName+"', album='"+mAlbumName+"']" ;
    }
}
