package com.brintsoft.spotifystreamer;

/**
 * Value class for an Artist.
 * A subset of the data we can get from the API class: kaaes.spotify.webapi.android.models.Artist
 * We only care about the artist name and a URL for an associated image.
 * Created by mark on 06/07/15.
 */

public class ArtistItem {
    private String mImageURI ;
    private String mArtistName ;

    public ArtistItem(String name, String imageURI ) {
        mArtistName = name ;
        mImageURI = imageURI ;
    }

    public String getImageURL() {
        return mImageURI ;
    }

    public String getArtistName() {
        return mArtistName ;
    }

    public String toString() {
        return "[ArtistItem: artist='"+mArtistName+"']" ;
    }
}
