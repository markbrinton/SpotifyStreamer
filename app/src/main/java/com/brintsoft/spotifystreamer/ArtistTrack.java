package com.brintsoft.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Value class for an Artist's Track.
 *
 * A subset of the data we can get from the API class: kaaes.spotify.webapi.android.models.Track
 * We only need the track id, name,album name and a URL for an associated image.
 *
 * Created by mark on 06/07/15.
 */

public class ArtistTrack implements Parcelable {
    private String mTrackId ;
    private String mImageURI ;
    private String mAlbumName ;
    private String mTrackName ;

    public static final Parcelable.Creator<ArtistTrack> CREATOR = new Parcelable.Creator<ArtistTrack>() {
        public ArtistTrack createFromParcel(Parcel in) {
            return new ArtistTrack(in);
        }

        public ArtistTrack[] newArray(int size) {
            return new ArtistTrack[size];
        }
    } ;

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

    public ArtistTrack(Parcel in) {
        mTrackId    = in.readString();
        mAlbumName  = in.readString() ;
        mTrackName  = in.readString() ;
        mImageURI   = in.readString() ;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTrackId);
        out.writeString(mAlbumName);
        out.writeString(mTrackName);
        out.writeString(mImageURI);
    }
}
