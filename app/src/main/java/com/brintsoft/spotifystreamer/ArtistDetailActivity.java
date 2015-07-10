package com.brintsoft.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Artist detail shows additional information for a selected artist.
 * At the moment that info is the top 10 tracks for the artist.
 */

public class ArtistDetailActivity extends ActionBarActivity {
    public static final String EXTRA_TEXT_ARTIST_ID   = "ARTIST_DETAIL.ARTIST_ID" ;
    public static final String EXTRA_TEXT_ARTIST_NAME = "ARTIST_DETAIL.ARTIST_NAME" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
