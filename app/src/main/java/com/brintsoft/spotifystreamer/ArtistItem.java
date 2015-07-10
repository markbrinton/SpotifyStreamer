package com.brintsoft.spotifystreamer;

/**
 * Value class for an Artist.
 *
 * A subset of the data we can get from the API class: kaaes.spotify.webapi.android.models.Artist
 * We only need the artist id, name and a URL for an associated image.
 *
 * Created by mark on 06/07/15.
 */

public class ArtistItem {
    private String mArtistId ;
    private String mImageURI ;
    private String mArtistName ;

    public ArtistItem(String id, String name, String imageURI ) {
        mArtistId   = id ;
        mArtistName = name ;
        mImageURI   = imageURI ;
    }

    public String getImageURL() {
        return mImageURI ;
    }

    public String getArtistId() {
        return mArtistId ;
    }

    public String getArtistName() {
        return mArtistName ;
    }

    public String toString() {
        return "[ArtistItem: artist='"+mArtistName+"']" ;
    }
}
