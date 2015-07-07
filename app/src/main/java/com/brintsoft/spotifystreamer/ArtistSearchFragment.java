package com.brintsoft.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSearchFragment extends Fragment {
    private static final String LOG_TAG = ArtistSearchFragment.class.getSimpleName() ;

    private EditText mSearchField ;
    private ArtistItemArrayAdapter mArtistListAdapter = null ;

    public ArtistSearchFragment() {
        Log.d(LOG_TAG,"constructor") ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSearchField = (EditText)rootView.findViewById(R.id.text_search) ;
        Log.d(LOG_TAG, "onCreateView() rootView = " + rootView + ", search field = " + mSearchField) ;

        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (true || actionId == EditorInfo.IME_ACTION_SEND) {
                    String s = mSearchField.getText().toString();
                    Log.i(LOG_TAG, "onEditorAction called with event=" + event + ", s='" + s + "'");
                    searchArtist(s);
                }
                return false;
            }
        });

        List<ArtistItem> emptyData = new ArrayList<ArtistItem>();
        mArtistListAdapter = new ArtistItemArrayAdapter(getActivity().getBaseContext(), emptyData);

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistListView.setAdapter(mArtistListAdapter);

        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistItem item = mArtistListAdapter.getItem(position);
                Log.i(LOG_TAG,"You picked item: "+item+", pos="+position) ;

                Intent detailIntent = new Intent(getActivity(),ArtistDetailActivity.class) ;
                detailIntent.putExtra(Intent.EXTRA_TEXT,item.getArtistName()) ;
                startActivity(detailIntent) ;
            }
        });

        return rootView ;
    }

    private void searchArtist(String artist) {
        Log.i(LOG_TAG, "searchArtist("+artist+")");
        SpotifyArtistSearchTask searchTask = new SpotifyArtistSearchTask(getActivity(),mArtistListAdapter) ;
        searchTask.execute(artist) ;
    }

}
