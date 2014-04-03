package com.swr.spotifyclient.spotify;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swr.spotifyclient.spotify.model.SpotifyAlbum;
import com.swr.spotifyclient.spotify.model.SpotifyAlbumTrack;
import com.swr.spotifyclient.spotify.task.AbstrackTask;
import com.swr.spotifyclient.spotify.util.ImageLoader;
import com.swr.spotifyclient.spotify.util.MyLog;
import com.swr.spotifyclient.spotify.util.QuickViewImageLoader;


public class DetailActivity extends AbstractActivity {

    protected SpotifyAlbum album = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        extractExtras();
        initLoader();

        if(album != null){


            ImageView iv = (ImageView) findViewById(R.id.thumbnail_image);
            if(iv != null){
                String url = album.getArtworkUrl();
                MyLog.d(TAG, "Showing video image: " + url);

                imageLoader.DisplayImage(url, iv, false);
            }

            TextView tv = (TextView) findViewById(R.id.album_name);
            if(tv != null){
                tv.setText(album.getAlbumName());
            }

            tv = (TextView) findViewById(R.id.artist_name);
            if(tv != null){
                tv.setText(album.getArtistName());
            }

            List<SpotifyAlbumTrack> tracks = album.getTracks();
            if(tracks != null && tracks.size() > 0){

                tv = (TextView)findViewById(R.id.track1);
                tv.setVisibility(View.INVISIBLE);
                tv = (TextView)findViewById(R.id.track2);
                tv.setVisibility(View.INVISIBLE);
                tv = (TextView)findViewById(R.id.track3);
                tv.setVisibility(View.INVISIBLE);


                int count = 0;
                for (SpotifyAlbumTrack track: tracks) {

                    tv = null;
                    switch (count){
                        case 0:
                            tv = (TextView)findViewById(R.id.track1);
                            break;

                        case 1:
                            tv = (TextView)findViewById(R.id.track2);
                            break;

                        case 2:
                            tv = (TextView)findViewById(R.id.track3);
                            break;

                    }
                    count++;

                    if(tv != null){
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("" + count + ". " + track.getTrackName());
                    }

                    MyLog.d(TAG, "Track: " + track);

                }
            }
        }
    }

    protected void initLoader(){
        imageLoader = new ImageLoader(getApplicationContext());
    }

    private void extractExtras() {


        album = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Serializable ser = extras.getSerializable("ALBUM");
            if (ser != null && ser instanceof SpotifyAlbum) {

                album = (SpotifyAlbum) ser;
                MyLog.d(TAG, "" + album);

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setMedia(List media) {

    }
}
