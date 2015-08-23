package com.brintsoft.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Value class for an Artist.
 *
 * A subset of the data we can get from the API class: kaaes.spotify.webapi.android.models.Artist
 * We only need the artist id, name and a URL for an associated image.
 *
 * Created by mark on 06/07/15.
 */

public class ArtistItem implements Parcelable {
    private String mArtistId ;
    private String mImageURI ;
    private String mArtistName ;

    public ArtistItem(String id, String name, String imageURI ) {
        mArtistId   = id ;
        mArtistName = name ;
        mImageURI   = imageURI ;
    }

    protected ArtistItem(Parcel in) {
        mArtistId = in.readString();
        mImageURI = in.readString();
        mArtistName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mArtistId);
        dest.writeString(mImageURI);
        dest.writeString(mArtistName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArtistItem> CREATOR = new Creator<ArtistItem>() {
        @Override
        public ArtistItem createFromParcel(Parcel in) {
            return new ArtistItem(in);
        }

        @Override
        public ArtistItem[] newArray(int size) {
            return new ArtistItem[size];
        }
    };

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
