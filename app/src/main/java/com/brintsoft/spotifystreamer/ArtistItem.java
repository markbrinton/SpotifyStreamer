package com.brintsoft.spotifystreamer;

/**
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
}
