package com.brintsoft.spotifystreamer.model;

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
    private String mArtistName ;
    private String mAlbumName ;
    private String mTrackName ;
    private String mPreviewURI ;

    public static final Parcelable.Creator<ArtistTrack> CREATOR = new Parcelable.Creator<ArtistTrack>() {
        public ArtistTrack createFromParcel(Parcel in) {
            return new ArtistTrack(in);
        }

        public ArtistTrack[] newArray(int size) {
            return new ArtistTrack[size];
        }
    } ;

    public ArtistTrack(String trackId, String artistName, String albumName, String trackName, String imageURI, String previewURI) {
        mTrackId    = trackId ;
        mArtistName = artistName ;
        mAlbumName  = albumName ;
        mTrackName  = trackName ;
        mImageURI   = imageURI ;
        mPreviewURI = previewURI ;
    }

    public String getImageURL() {
        return mImageURI ;
    }

    public String getTrackId() {
        return mTrackId ;
    }

    public String getArtistName() {
        return mArtistName ;
    }

    public String getAlbumName() {
        return mAlbumName ;
    }

    public String getTrackName() {
        return mTrackName ;
    }

    public String getPreviewURL() {
        return mPreviewURI ;
    }

    public String toString() {
        return "[ArtistTrack: track='"+mTrackName+"', artist='"+mArtistName+"', album='"+mAlbumName+"', image="+mImageURI+", preview="+mPreviewURI+"]" ;
    }

    public ArtistTrack(Parcel in) {
        mTrackId    = in.readString();
        mArtistName = in.readString() ;
        mAlbumName  = in.readString() ;
        mTrackName  = in.readString() ;
        mImageURI   = in.readString() ;
        mPreviewURI = in.readString() ;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTrackId);
        out.writeString(mArtistName);
        out.writeString(mAlbumName);
        out.writeString(mTrackName);
        out.writeString(mImageURI);
        out.writeString(mPreviewURI);
    }
}
