package com.swr.spotifyclient.spotify.task;

import com.swr.spotifyclient.spotify.model.SpotifyAbstractModel;
import com.swr.spotifyclient.spotify.model.SpotifyAlbumTrack;
import com.swr.spotifyclient.spotify.util.C;
import com.swr.spotifyclient.spotify.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Steve on 4/1/2014.
 */
public class AlbumsTask extends AbstrackTask {

    public static final int TOP_STREAMED = 1;
    public static final int TOP_VIRAL = 2;

    int type = 0;

    @Override
    protected String getUrl(){

        String url = null;
        switch (type){

            case TOP_STREAMED:
                url = C.url.spotifyStreamedURL;
                break;

            case TOP_VIRAL:
                url = C.url.spotifyTopURL;
                break;

            default:
                url = C.url.spotifyStreamedURL;
                break;
        }

        MyLog.d(TAG, "URL = " + url);
        return url;
    }

    protected SpotifyAlbumTrack getItem(JSONObject json_data) throws JSONException {

        MyLog.d(TAG, "Parsing Album");
        SpotifyAlbumTrack item = SpotifyAlbumTrack.initWithJSOnData(json_data);

        MyLog.d(TAG, item.toString());

        return item;
    }

    @Override
    protected boolean addItem(SpotifyAbstractModel item) {
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
