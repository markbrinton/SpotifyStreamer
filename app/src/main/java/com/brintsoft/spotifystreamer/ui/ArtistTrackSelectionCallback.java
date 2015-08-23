package com.brintsoft.spotifystreamer.ui;

import com.brintsoft.spotifystreamer.model.ArtistTrack;

import java.util.ArrayList;

/**
 * Callback for when a track is selected.
 */
public interface ArtistTrackSelectionCallback {
    public void onArtistTrackSelected(ArrayList<ArtistTrack> tracks, int index) ;
}
