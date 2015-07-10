package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Sub class of ArrayAdapter to handle array items that aren't simple strings.
 * In this case each item in the list will have an image and a track & album name.
 *
 * Starting point was example code found here:
 * http://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 *
 * Created by mark on 06/07/15.
 */

public class ArtistTrackArrayAdapter extends ArrayAdapter<ArtistTrack> {
    private static final String LOG_TAG = ArtistTrackArrayAdapter.class.getSimpleName() ;

    private ViewHolder mViewHolder = null ;
    private Context    mContext ;

    public ArtistTrackArrayAdapter(Context context, List<ArtistTrack> items) {
        super(context,0,items) ;
        mContext = context ;
    }

    private static class ViewHolder {
        private TextView trackName;
        private ImageView albumImage ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item_track, parent, false);

            mViewHolder = new ViewHolder() ;
            mViewHolder.trackName  = (TextView) convertView.findViewById(R.id.list_item_track_textview);
            mViewHolder.albumImage = (ImageView) convertView.findViewById(R.id.list_item_album_image);

            convertView.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ArtistTrack item = getItem(position);
        if (item!= null) {
            String url = item.getImageURL() ;
            Log.d(LOG_TAG, "getView(pos=" + position + ", track=" + item.getTrackName() + ", URL=" + url) ;

            if( url==null ) {
                // TODO: maybe use a default "no image available" image, rather than leave a blank space?
            }

            String trackAndAlbum = item.getTrackName() + "\n" + item.getAlbumName() ;
            mViewHolder.trackName.setText(trackAndAlbum);
            Picasso.with(mContext).load(item.getImageURL()).into(mViewHolder.albumImage);
        }

        return convertView;
    }
}
