package com.swr.spotifyclient.spotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.swr.spotifyclient.spotify.model.SpotifyAlbum;
import com.swr.spotifyclient.spotify.model.SpotifyAlbumTrack;
import com.swr.spotifyclient.spotify.task.AlbumsTask;
import com.swr.spotifyclient.spotify.util.C;
import com.swr.spotifyclient.spotify.util.ImageLoader;
import com.swr.spotifyclient.spotify.util.MyLog;
import com.swr.spotifyclient.spotify.util.ProgressDialogRunner;
import com.swr.spotifyclient.spotify.util.QuickViewImageLoader;
import com.swr.spotifyclient.spotify.util.UIUtil;
import com.swr.spotifyclient.spotify.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AbstractActivity implements ProgressDialogRunner {

    protected AlbumsTask albumsTask = null;
    protected List<SpotifyAlbum> media = null;

    public View lastClicked = null;
    protected int selectedIndex = 0;
    protected int selectedPosition = -1;

    private MainActivity self = this;
    protected ImageAdapter adapter = new ImageAdapter(self);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MyLog.i(TAG, "Starting");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoader();
        setupActionBarText();

        loadData();
        MyLog.i(TAG, "Ending");
    }

    /**
     * Converts tracks to albums with tracks
     * @param mediaTracks
     * @return
     */
    protected List<SpotifyAlbum> convertTracksToAlbums(List<SpotifyAlbumTrack> mediaTracks){

        List<SpotifyAlbum> albums = new ArrayList<SpotifyAlbum>();
        if(mediaTracks == null || mediaTracks.size() == 0) return albums;

        String albumName = null;
        SpotifyAlbum album = null;
        Map<String, SpotifyAlbum> map = new HashMap<String, SpotifyAlbum>();

        int num = 1;
        for (SpotifyAlbumTrack track: mediaTracks){

            albumName = track.getAlbumName();
            if(albumName != null && albumName.trim().length() > 0){

                album = map.get(albumName);
                if(album == null){
                    album = new SpotifyAlbum();
                    album.setAlbumName(albumName);
                    album.setArtistName(track.getArtistName());
                    album.setArtworkUrl(track.getArtworkUrl());
                    album.setNum(num);
                    num++;
                    map.put(albumName, album);
                }
                album.addTrack(track);

            }

        }

        for (String str: map.keySet()){
            album = map.get(str);
            if(album != null){
                albums.add(album);
            }
        }

        Collections.sort(albums);
        for (SpotifyAlbum a: albums){
            MyLog.d(TAG, a.getAlbumName());
        }
        return albums;

    }

    private void resetTextColor(Button button) {
        if (button != null) {
            button.setTextColor(getResources().getColor(R.color.off_white));
        }
    }

    private void selectedTextColor(Button button) {
        if (button != null) {
            button.setTextColor(getResources().getColor(R.color.orange));
        }
    }



    public void mostStreamed(View view){

        MyLog.d(TAG, "Most Streamed Clicked");

        loadData();

        View streamedView = findViewById(R.id.streamedFilter_selected);
        View sharedView = findViewById(R.id.sharedFilter_selected);

        if (streamedView != null) streamedView.setVisibility(View.VISIBLE);
        if (sharedView != null) sharedView.setVisibility(View.INVISIBLE);

        Button streamedButton = (Button) findViewById(R.id.streamedFilter);
        Button sharedButton = (Button) findViewById(R.id.sharedFilter);

        selectedTextColor(streamedButton);
        resetTextColor(sharedButton);

        adapter.notifyDataSetChanged();
    }

    public void mostShared(View view){

        MyLog.d(TAG, "Most Shared Clicked");

        loadSharedData();

        View streamedView = findViewById(R.id.streamedFilter_selected);
        View sharedView = findViewById(R.id.sharedFilter_selected);

        if (streamedView != null) streamedView.setVisibility(View.INVISIBLE);
        if (sharedView != null) sharedView.setVisibility(View.VISIBLE);

        Button streamedButton = (Button) findViewById(R.id.streamedFilter);
        Button sharedButton = (Button) findViewById(R.id.sharedFilter);

        selectedTextColor(sharedButton);
        resetTextColor(streamedButton);

        adapter.notifyDataSetChanged();
    }

    public void setMedia(List mediaTracks){

        dismissProgressDialog();
        MyLog.d(TAG, "Data received");


        if(mediaTracks != null ){


            if(this.media != null ){
                this.media.clear();
            }

            this.media = convertTracksToAlbums(mediaTracks);
        }

        if (media != null && media.size() > 0) {
            imageIDs = UIUtil.initImageIds(media.size());
        }
        else{
            imageIDs = UIUtil.initImageIds(1);
        }

        MyLog.d(TAG, "Calling Handler");
        Message msg = handler.obtainMessage();
        msg.arg1 = 1;
        handler.sendMessage(msg);
    }



    protected void initLoader(){
        imageLoader = new ImageLoader(getApplicationContext());
        quickVideomageLoader = new QuickViewImageLoader(this.getApplicationContext());
    }

    protected void setupActionBarText() {
        setActionBarText("Spotify Albums");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void loadSharedData(){

        MyLog.i(TAG, "Starting");

        dismissProgressDialog();
        createCancelProgressDialog("Loading", "Loading Media", "Cancel");
        loadSharedAlbumData();

    }


    public void loadData(){

        MyLog.i(TAG, "Starting");

        dismissProgressDialog();
        createCancelProgressDialog("Loading", "Loading Media", "Cancel");
        loadAlbumData(AlbumsTask.TOP_STREAMED);

    }

    public void loadAlbumData(int type){

        albumsTask = new AlbumsTask();
        albumsTask.setType(type);
        albumsTask.setActivity(this);
        Util.runTask(albumsTask);

    }

    public void loadSharedAlbumData(){

        MyLog.i(TAG, "Starting");

        dismissProgressDialog();
        createCancelProgressDialog("Loading", "Loading Media", "Cancel");
        loadAlbumData(AlbumsTask.TOP_VIRAL);

    }

    public List<SpotifyAlbum> getData() {
        return media;
    }

    public class ImageAdapter extends BaseAdapter {

        private Context context;
        public ImageAdapter(Context c){
            context = c;
        }
        public int getCount() {
            if(media != null ) return media.size();
            return 0;
        }
        public Object getItem(int position) {
            if(media != null && media.size() >= position){
                return media.get(position);
            }
            return null;
        }

        public long getItemId(int position) {
            if(media != null && media.size() >= position){
                return position;
            }
            return -1;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View gridItemView = convertView;
            Activity act = self;

            if (gridItemView == null && act != null) {
                LayoutInflater inflater = getLayoutInflater();
                gridItemView = inflater.inflate(R.layout.grid_layout, parent,
                        false);

                int numColumns = ((GridView)parent).getNumColumns();
                int width = parent.getWidth() / numColumns;
                gridItemView.setLayoutParams(new GridView.LayoutParams(width, width));
            }
            if(gridItemView == null) return null;

            ImageView imageView = UIUtil.findImageView(R.id.grid_view_image, gridItemView);

            List<SpotifyAlbum> media = getData();
            if(media != null && media.size() > position){

                SpotifyAlbum mediaItem = media.get(position);

                // fetch image for video
                if(mediaItem != null && imageView != null && imageLoader != null){

                    String url = mediaItem.getArtworkUrl();
                    MyLog.d(TAG, "Showing video image: " + url);

                    imageLoader.DisplayImage(url, imageView, false);

                }
                // use default image
                else if(imageView != null && imageIDs != null && imageIDs.length >= position) {
                    imageView.setImageResource(imageIDs[position]);
                }
            }

            return gridItemView;
        }

    }


    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            MyLog.d(TAG, "Handler starting");
            dismissProgressDialog();


            selectedPosition = -1;
            //adapter = new ImageAdapter(self);

            final GridView gridView = (GridView) findViewById(R.id.library_gridview);
            gridView.setAdapter(adapter);

            final int windowSize = UIUtil.getWindowWidth(self);
            MyLog.d(TAG, "windowSize: " + windowSize);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                // grid_view_image_layout
                public void onItemClick(AdapterView parent, final View v,
                                        int position, long id) {


                    //if(!showVideoControls) return;

                    selectedPosition = position;
                    MyLog.d(TAG, "Grid View Clicked: " + position);



                    selectedIndex = position;
                    SpotifyAlbum videoItem = media.get(selectedIndex);
                    setTop(v.getTop());

                        if (lastClicked != null) {
                            //lastClicked.setBackgroundColor(C.color.dark_color);

                            ImageView iv3 = (ImageView) lastClicked.findViewById(R.id.grid_view_image);
                            if (iv3 != null) {
                                iv3.setBackgroundColor(C.color.transparent_color);
                            }
                        }

                        if (v != null) {
                            // v.setBackgroundColor(C.color.cyan_color);


                            ImageView iv2 = UIUtil.findImageView(R.id.grid_view_image, v);
                            if (iv2 != null) {
                                iv2.setBackgroundColor(C.color.cyan_color);
                            }

                        }


                        if (lastClicked != null) {
                            if (v == lastClicked) {
                                selectedIndex = position;
                            } else {
                                lastClicked = v;
                            }
                        } else {
                            lastClicked = v;
                        }


                        lastClicked = v;

                        //showDialog(DIALOG_VIDEO_CONTROLS);
                        runMediaDetailsActivity((SpotifyAlbum)videoItem);

                }
            });

            MyLog.d(TAG, "Done grid view on click handler");
        }
    };

    public void runMediaDetailsActivity(SpotifyAlbum album) {

        //if (name.equals("PickCrowd")) return;
        Intent intent = new Intent(this, com.swr.spotifyclient.spotify.DetailActivity.class);
        intent.putExtra("ALBUM", album);
        startActivity(intent);
    }
}
