package com.brintsoft.spotifystreamer.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistTrack;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * DialogFragment for a simple media player.
 * Play/Pause, previous track, next track.
 *
 */
public class PlayerDialogFragment extends DialogFragment {
    private static final String LOG_TAG = PlayerDialogFragment.class.getSimpleName();

    public static final String ARG_TRACK_LIST  = "track_list";
    public static final String ARG_TRACK_INDEX = "track_index";
    public static final String ARG_TRACK_POS   = "track_pos";

    private ArrayList<ArtistTrack> mTracks ;
    private int mTrackIndex ;
    private ArtistTrack mCurrentTrack ;
    private int mCurrentTrackPos = 0 ;

    // Remember UI elements we have to update when the track is changed.
    private TextView  mArtistNameView ;
    private TextView  mAlbumNameView ;
    private TextView  mTrackNameView ;
    private ImageView mAlbumImageView ;
    private SeekBar   mSeekBar ;
    private TextView  mTrackPos ;
    private TextView  mTrackLength ;

    // Remember prev/play/next buttons so we can update/enable/disable them.
    private ImageButton mPrevButton = null ;
    private ImageButton mPlayButton = null ;
    private ImageButton mNextButton = null ;

    private MediaPlayer mPlayer = null ;
    private boolean mPlayerIsReady = false ;

    private Runnable mSeekBarUpdater = null ;
    private Handler  mHandler = null ;

    private OnFragmentInteractionListener mListener;

    /**
     * @param tracks The track to be played.
     * @param index The index of the track to be played.
     * @return A new instance of fragment PlayerDialogFragment.
     */
    public static PlayerDialogFragment newInstance(ArrayList<ArtistTrack> tracks, int index) {
        PlayerDialogFragment fragment = new PlayerDialogFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_TRACK_LIST, tracks) ;
        args.putInt(ARG_TRACK_INDEX, index); ;
        fragment.setArguments(args);

        return fragment;
    }

    public PlayerDialogFragment() {
        // Required empty public constructor
        Log.d(LOG_TAG,"ctor") ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Stop this fragment from being recreated every time.
        // Bad idea - dialog is lost on screen rotation on a tablet.
        // setRetainInstance(true) ;

        Bundle args = getArguments() ;

        mPlayer = null ;

        if (args != null) {
            mTracks       = args.getParcelableArrayList(ARG_TRACK_LIST) ;
            mTrackIndex   = args.getInt(ARG_TRACK_INDEX) ;
            mCurrentTrack = mTracks.get(mTrackIndex) ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG,"onCreateView: track = "+mCurrentTrack+", player="+mPlayer) ;

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_dialog, container, false);

        mArtistNameView = (TextView)root.findViewById(R.id.dialog_artist_name_textview) ;
        mAlbumNameView  = (TextView)root.findViewById(R.id.dialog_album_name_textview) ;
        mTrackNameView  = (TextView)root.findViewById(R.id.dialog_track_name_textview) ;
        mAlbumImageView = (ImageView)root.findViewById(R.id.dialog_album_imageview) ;
        mSeekBar        = (SeekBar)root.findViewById(R.id.dialog_seekbar) ;
        mTrackPos       = (TextView)root.findViewById(R.id.dialog_track_position_textview) ;
        mTrackLength    = (TextView)root.findViewById(R.id.dialog_track_length_textview) ;

        mPrevButton = (ImageButton)root.findViewById(R.id.dialog_media_prev_button) ;
        mPlayButton = (ImageButton)root.findViewById(R.id.dialog_media_play_button) ;
        mNextButton = (ImageButton)root.findViewById(R.id.dialog_media_next_button) ;

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mediaPrevPressed(); }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaNextPressed();
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayPressed();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mPlayer != null) {
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mSeekBarUpdater = new Runnable() {
            @Override
            public void run() {
                if( mPlayer!=null ) {
                    updateSeekBar( mPlayer.getCurrentPosition() );
                    // Do it again after 1 second
                    mHandler.postDelayed(this,500) ;
                }
            }
        } ;

        if( savedInstanceState!=null ) {
            restoreTracks(savedInstanceState);
        }

        // Start off with disabled media buttons, until we're ready to play
        playerIsReady(false);
        startTrack(mCurrentTrack);

        return root ;
    }

    /** Start playing the specified track. Set text labels & album image, prepare the MediaPlayer. */
    private void startTrack(ArtistTrack track) {
        Context context = getActivity() ;

        mArtistNameView.setText(track.getArtistName());
        mAlbumNameView.setText(track.getAlbumName());
        mTrackNameView.setText(track.getTrackName());
        Picasso.with(context).load(track.getImageURL()).into(mAlbumImageView);

        preparePlayer(track.getPreviewURL()) ;
    }

    /** Update seekbar & track length field with duration of the track */
    private void setSeekBar(int duration) {
        Log.d(LOG_TAG, "setSeekBar duration = " + duration) ;
        mSeekBar.setMax(duration);

        String time = trackPosToTime(duration) ;
        mTrackLength.setText(time);
    }

    /** Update seekbar & track position */
    private void updateSeekBar(int position) {
        mSeekBar.setProgress(position);
        String time = trackPosToTime(position) ;
        mTrackPos.setText(time);
        mCurrentTrackPos = position ;
    }

    /** Convert track position (msecs) into time in format MM:SS */
    private String trackPosToTime(int pos) {
        int posInSecs = (int)(0.5 + pos/1000.0) ;
        int mins = posInSecs/60 ;
        int secs = posInSecs - (mins*60) ;

        String time = String.format("%02d:%02d", mins, secs) ;
        return time ;
    }

    /** Enable/disable the player controls */
    private void playerIsReady(boolean isReady) {
        // Flags tell us if the prev/next buttons should be disabled based on current track index.
        boolean hasPreviousTrack = mTrackIndex>0 ;
        boolean hasNextTrack     = (mTrackIndex+1)<mTracks.size() ;

        mPrevButton.setEnabled(isReady && hasPreviousTrack);
        mNextButton.setEnabled(isReady && hasNextTrack);

        mPlayButton.setEnabled(isReady);
        mPlayerIsReady = isReady ;
    }

    /** Set the button to show play or pause image based on the flag. */
    private void setPlayPauseButton(boolean isPlay) {
        int resId = isPlay? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause ;
        mPlayButton.setImageResource(resId);
    }

    /** Called when media player is ready to start streaming. */
    private void playerIsPrepared() {
        Log.d(LOG_TAG,"Media player is prepared.");

        // Media is ready to stream.  Enable the buttons.
        playerIsReady(true);

        // We can now get the track length and update the seekbar UI.
        int duration = mPlayer.getDuration() ;
        setSeekBar(duration) ;

        // If there's a remembered position then seek to it
        if( mCurrentTrackPos != 0 ) {
            mPlayer.seekTo(mCurrentTrackPos);
        }

        // Start playing automatically, as if the user pressed the play button
        mediaPlayPressed();

        if( mHandler == null ) {
            mHandler = new Handler() ;
        }

        // Start a handler that will update the seekbar UI every second
        mHandler.post(mSeekBarUpdater) ;
    }

    /** Called when player stop playing a track */
    private void playerIsFinished() {
        Log.d(LOG_TAG, "Playing has completed.");

        // Seek back to the start in case user wants to play it again.
        mCurrentTrackPos = 0 ;

        // Update the play/pause button when the media has stopped
        setPlayPauseButton(true);

        mPlayer.seekTo(mCurrentTrackPos);
    }

    /** Prepare the media player. Set the stream and start the download in the background. */
    private void preparePlayer(String streamURL) {
        if( mPlayer==null ) {
            mPlayer = new MediaPlayer() ;
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playerIsPrepared();
                }
            });

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playerIsFinished();
                }
            });
        }

        if( mPlayer.isPlaying() ) {
            mPlayer.stop() ;
        }

        playerIsReady(false);

        try {
            // Disable player buttons while we wait for the MediaPlayer to get started.
            playerIsReady(false);

            // Start fetching the data in the background.
            mPlayer.reset() ;
            mPlayer.setDataSource(streamURL);
            mPlayer.prepareAsync();
        }
        catch(IOException ioe) {
            // Something bad happened. Free up resources before user tries again.
            mPlayer.reset() ;
            mPlayer.release();
            mPlayer = null ;
        }
    }

    /** User pressed button to go the previous track */
    private void mediaPrevPressed() {
        Log.d(LOG_TAG, "mediaPrevPressed") ;

        // Move to the previous track.
        // Button is disabled if there is no previous track, so we won't go negative..
        mTrackIndex-- ;
        mCurrentTrack = mTracks.get(mTrackIndex) ;
        mCurrentTrackPos = 0 ;
        startTrack(mCurrentTrack);
    }

    /** User pressed button to go the next track */
    private void mediaNextPressed() {
        Log.d(LOG_TAG, "mediaNextPressed") ;

        // Move to the next track.
        // Button is disabled if there is no next track, so we won't go past the end.
        mTrackIndex++ ;
        mCurrentTrack = mTracks.get(mTrackIndex) ;
        mCurrentTrackPos = 0 ;
        startTrack(mCurrentTrack);
    }

    /** User pressed the play button */
    private void mediaPlayPressed() {
        Log.d(LOG_TAG,"mediaPlayPressed") ;

        if( mPlayer==null ) {
            Log.d(LOG_TAG, "mediaPlayPressed: No media to play") ;
            return ;
        }

        if( !mPlayerIsReady ) {
            Log.d(LOG_TAG, "mediaPlayPressed: Stream not ready yet") ;
            // Toast - media not ready yet
            return;
        }

        if (mPlayer.isPlaying() ) {
            Log.d(LOG_TAG,"mediaPlayPressed: Pausing stream") ;

            // Pause playing and show the play button
            mPlayer.pause();
            setPlayPauseButton(true);
        }
        else {
            Log.d(LOG_TAG,"mediaPlayPressed: Playing stream") ;

            // Start/continue playing and show the pause button
            mPlayer.start();
            setPlayPauseButton(false);
        }
    }

    /**
     * If the player dialog / activity is dismissed, then stop playing and release resources.
     *
     * FIXME: releasing the player after onPause & onStop means music is interrupted when device is rotated.
     * FIXME: it also means streaming is restarted. Cleanest solution is to move player into a service.
     */
    private void releasePlayer() {
        if( mPlayer!=null ) {
            if( mPlayer.isPlaying() ) {
                mPlayer.stop() ;
            }
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null ;
        }

        if( mHandler!=null ) {
            mHandler.removeCallbacks(mSeekBarUpdater);
            mHandler = null ;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"onPause()") ;
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onStop()") ;
        releasePlayer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
    }

    /** Restore list of tracks, current track index and position within track from the bundle. */
    private void restoreTracks(Bundle savedInstanceState) {
        ArrayList<ArtistTrack> trackList = savedInstanceState.getParcelableArrayList(ARG_TRACK_LIST) ;
        if( trackList!=null ) {
            Log.d(LOG_TAG, "restoreTracks() restored " + trackList.size() + " tracks") ;
            mTracks.addAll(trackList) ;
        }
        else {
            Log.d(LOG_TAG, "restoreTracks() no tracks to restore") ;
        }

        mTrackIndex = savedInstanceState.getInt(ARG_TRACK_INDEX) ;
        mCurrentTrackPos = savedInstanceState.getInt(ARG_TRACK_POS) ;

        mCurrentTrack = mTracks.get(mTrackIndex) ;
        Log.d(LOG_TAG, "restoreTracks() restored trackIndex: " + mTrackIndex+", trackPos: " + mCurrentTrackPos + ", track = "+mCurrentTrack) ;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        int numTracks = mTracks.size() ;
        Log.d(LOG_TAG, "onSaveInstanceState() saving " + numTracks + " tracks, index "+mTrackIndex+", trackPos "+mCurrentTrackPos);

        saveInstanceState.putParcelableArrayList(ARG_TRACK_LIST, mTracks);
        saveInstanceState.putInt(ARG_TRACK_INDEX, mTrackIndex);
        saveInstanceState.putInt(ARG_TRACK_POS, mCurrentTrackPos);
    }

}
