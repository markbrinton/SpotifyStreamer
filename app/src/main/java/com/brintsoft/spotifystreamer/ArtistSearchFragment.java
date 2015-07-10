package com.brintsoft.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a search field and list of artist search results.
 *
 */
public class ArtistSearchFragment extends Fragment {
    private static final String LOG_TAG = ArtistSearchFragment.class.getSimpleName() ;
    private static final String PREFS_KEY_SEARCH = "artist_search" ;

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

        // Perform a search when the user submits the text by pressing the search button.
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String s = mSearchField.getText().toString();
                Log.d(LOG_TAG, "onEditorAction called with event=" + event + ", s='" + s + "'");
                searchArtist(s);

                return false;
            }
        });

        List<ArtistItem> emptyData = new ArrayList<ArtistItem>();
        mArtistListAdapter = new ArtistItemArrayAdapter(getActivity().getBaseContext(), emptyData);

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistListView.setAdapter(mArtistListAdapter);

        // Launch the ArtistDetailActivity when an artist is selected from the list
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistItem item = mArtistListAdapter.getItem(position);
                Log.d(LOG_TAG, "You picked item: " + item + ", pos=" + position);

                Intent detailIntent = new Intent(getActivity(), ArtistDetailActivity.class);
                detailIntent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID,   item.getArtistId());
                detailIntent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME, item.getArtistName());

                startActivity(detailIntent);
            }
        });

        return rootView ;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String artist = mSearchField.getText().toString() ;

        Log.d(LOG_TAG, "onSaveInstanceState() saving artist = " + artist);
        savedInstanceState.putString("artist", artist);
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if( savedInstanceState!=null ) {
            String artist = savedInstanceState.getString("artist") ;
            Log.d(LOG_TAG, "onViewStateRestored() restore artist = "+artist);

            searchArtist(artist);
        }
        else {
            Log.d(LOG_TAG, "onViewStateRestored() restore with null");
        }
    }

    private void searchArtist(String artist) {
        Log.d(LOG_TAG, "searchArtist("+artist+")");
        SpotifyArtistSearchTask searchTask = new SpotifyArtistSearchTask(getActivity(),mArtistListAdapter) ;
        searchTask.execute(artist) ;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"onPause()") ;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String searchText = prefs.getString(PREFS_KEY_SEARCH, "") ;

        Log.d(LOG_TAG, "onResume() restoring search text '"+searchText+"'") ;
        mSearchField.setText(searchText);

        if(searchText!=null && searchText.length()>0 ) {
            searchArtist(searchText);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        String searchText = mSearchField.getText().toString() ;
        Log.d(LOG_TAG, "onStop() saving search text '"+searchText+"'") ;

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREFS_KEY_SEARCH, searchText) ;
        editor.commit() ;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()") ;
    }
}
