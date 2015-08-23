package com.brintsoft.spotifystreamer.ui;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.brintsoft.spotifystreamer.R;
import com.brintsoft.spotifystreamer.model.ArtistItem;
import com.brintsoft.spotifystreamer.spotify.SpotifyArtistSearchTask;

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
    private boolean  mInDetailActivity ;
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
                // Hide the keyboard after user presses search button.
                hideKeyboard() ;

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

        // Set the selector so we get background colour in list toshow selection
        artistListView.setSelector(R.drawable.touch_selector);

        // Set action when an artist is selected from the list
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistItem item = mArtistListAdapter.getItem(position);
                Log.d(LOG_TAG, "You picked item: " + item + ", pos=" + position);

                // Delegate to the parent activity, as it knows how fragments/activities are arranged.
                ArtistSelectionCallback cb = (ArtistSelectionCallback)getActivity() ;
                cb.onArtistSelected(item) ;
            }
        });

        return rootView ;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void launchDetailActivity(String artistId, String artistName) {
        mInDetailActivity = true ;

        Intent detailIntent = new Intent(getActivity(), ArtistDetailActivity.class);
        detailIntent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_ID, artistId);
        detailIntent.putExtra(ArtistDetailActivity.EXTRA_TEXT_ARTIST_NAME, artistName);

        startActivity(detailIntent);
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

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_KEY_SEARCH, mSearchField.getText().toString()) ;
        editor.commit() ;
    }

    @Override
    public void onResume() {
        super.onResume();

        mInDetailActivity = false ;

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String searchText   = prefs.getString(PREFS_KEY_SEARCH, "") ;

        Log.d(LOG_TAG, "onResume() restoring search text '" + searchText + "'") ;

        if(searchText!=null && searchText.length()>0 ) {
            mSearchField.setText(searchText);
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
