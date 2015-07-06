package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mark on 06/07/15.
 *
 * Sub class of ArrayAdapter to handle array items that aren't simple strings.
 * In this case each item in the list will have an image and an artist name.
 *
 * Starting point was example code found here:
 * http://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 *
 */
public class ArtistItemArrayAdapter extends ArrayAdapter<ArtistItem> {
    private static final String LOG_TAG = ArtistItemArrayAdapter.class.getSimpleName() ;

    private ViewHolder mViewHolder = null ;

    public ArtistItemArrayAdapter(Context context, List<ArtistItem> items) {
        super(context,0,items) ;
    }

    private static class ViewHolder {
        private TextView artistName;
        private ImageView artistImage ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item_artist, parent, false);

            mViewHolder = new ViewHolder() ;
            mViewHolder.artistName  = (TextView) convertView.findViewById(R.id.list_item_artist_textview);
            // mViewHolder.artistImage = (ImageView) convertView.findViewById(R.id.ItemView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ArtistItem item = getItem(position);
        if (item!= null) {
            mViewHolder.artistName.setText(item.getArtistName());
            Log.d(LOG_TAG, "getView(pos="+position+", artist="+item.getArtistName()+", URL="+item.getImageURL()) ;
        }

        return convertView;
    }


}
