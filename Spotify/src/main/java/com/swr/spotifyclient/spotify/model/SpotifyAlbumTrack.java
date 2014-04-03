package com.swr.spotifyclient.spotify.model;

import com.swr.spotifyclient.spotify.util.MyLog;
import com.swr.spotifyclient.spotify.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Steve on 4/1/2014.
 */
public class SpotifyAlbumTrack extends SpotifyAbstractModel{

    protected static final long serialVersionUID = -5859276137857969179L;

    protected String date = null;
    protected String country = null;
    protected String trackUrl = null;
    protected String trackName = null;
    protected String artistName = null;
    protected String artistUrl = null;
    protected String albumName = null;
    protected String albumUrl = null;
    protected String artworkUrl = null;
    protected int numStreams = 0;

    public SpotifyAlbumTrack() { }

    public SpotifyAlbumTrack(String date, String country, String trackUrl, String trackName, String artistName, String artistUrl, String albumName, String albumUrl, String artworkUrl, int numStreams) {
        this.date = date;
        this.country = country;
        this.trackUrl = trackUrl;
        this.trackName = trackName;
        this.artistName = artistName;
        this.artistUrl = artistUrl;
        this.albumName = albumName;
        this.albumUrl = albumUrl;
        this.artworkUrl = artworkUrl;
        this.numStreams = numStreams;
    }



/*

{"tracks":[
{"date":"2014-03-30",
"country":"US",
"track_url":"https:\/\/play.spotify.com\/track\/5jrdCoLpJSvHHorevXBATy",
"track_name":"Dark Horse",
"artist_name":"Katy Perry",
"artist_url":"https:\/\/play.spotify.com\/artist\/6jJ0s89eD6GaHleKKya26X",
"album_name":"PRISM",
"album_url":"https:\/\/play.spotify.com\/album\/5MQBzs5YlZlE28mD9yUItn",
"artwork_url":"http:\/\/o.scdn.co\/300\/96fb8b4d972bba2ac566820985437aa39109b512",
"num_streams":1835681},


{"date":"2014-03-30","country":"US","track_url":"https:\/\/play.spotify.com\/track\/3bTZ9geYjyj9uBIT6gL5N6","track_name":"All of Me","artist_name":"John Legend","artist_url":"https:\/\/play.spotify.com\/artist\/5y2Xq6xcjJb2jVM54GHK3t","album_name":"All of Me","album_url":"https:\/\/play.spotify.com\/album\/1YdXQgntClL3BhIXB0xpgs","artwork_url":"http:\/\/o.scdn.co\/300\/d4b391b75d9f80032fb7124e47ff44291181f63d","num_streams":1795214},{"date":"2014-03-30","country":"US","track_url":"https:\/\/play.spotify.com\/track\/6nCdW7lwO9AlPOepk689AF","track_name":"Talk Dirty (feat. 2 Chainz)","artist_name":"Jason Derulo","artist_url":"https:\/\/play.spotify.com\/artist\/07YZf4WDAMNwqr4jfgOZ8y","album_name":"Tattoos EP","album_url":"https:\/\/play.spotify.com\/album\/7p1zzVGLSTHcUddCtgQKL9","artwork_url":"http:\/\/o.scdn.co\/300\/a0e09d80d515fe5bee10c928a8cdd19c4dd545ca","num_streams":1682458},{"date":"2014-03-30","country":"US","track_url":"https:\/\/play.spotify.com\/track\/4Ub8UsjWuewQrPhuepfVpd","track_name":"Pompeii","artist_name":"Bastille","artist_url":"https:\/\/play.spotify.com\/artist\/7EQ0qTo7fWT7DPxmxtSYEc","album_name":"All This Bad Blood","album_url":"https:\/\/play.spotify.com\/album\/5G6oMu9zNW2acdV0lqzI3L","artwork_url":"http:\/\/o.scdn.co\/300\/1b0243d40283611bdecf280cb6142307fe97c606","num_streams":1517004},{"date":"2014-03-30","country":"US","track_url":"https:\/\/play.spotify.com\/track\/6kOXFFNTzraDTj6fceHenx","track_name":"Let It Go","artist_name":"Various Artists","artist_url":"https:\/\/play.spotify.com\/artist\/73Np75Wv2tju61Eo9Zw4IR","album_name":"Frozen","album_url":"https:\/\/play.spotify.com\/album\/2lfqRceJLjF8rSeo5P7SWW","artwork_url":"http:\/\/o.scdn.co\/300\/d638963e6221888cccea6bcc108b76116571f735","num_streams":1501015},{"date":"2014-03-30","country":"US","track_url":"https:\/\/play.spotify.com\/track\/2stPxcgjdSImK7Gizl8ZUN","track_name":"The Man","artist_name":"Aloe Blacc","artist_url":"https:\/\/play.spotify.com\/artist\/0id62QV2SZZfvBn9xpmuCl","album_name":"Lift Your Spirit","album_url":"https:\/\/play.spotify.com\/album\/14JRI2yc9nKosojndoQxTv","artwork_url":"http:\/\/o.scdn.co\/300\/788afaaa762c8a613c21b883ae4f00019cd471ad","num_streams":1279796},{"date":"2014-03-30","country":"US","track_url":"https:

 */

    public static final SpotifyAlbumTrack initWithJSOnData(JSONObject data) throws JSONException {

        MyLog.d(TAG, "Starting");

        if(data == null){
            return null;
        }

        SpotifyAlbumTrack album = new SpotifyAlbumTrack();

        String str = data.optString("date");
        if(!Util.isEmpty(str)){
            album.setDate(str);
        }

        str = data.optString("country");
        if(!Util.isEmpty(str)){
            album.setCountry(str);
        }

        str = data.optString("track_name");
        if(!Util.isEmpty(str)){
            album.setTrackName(str);
        }

        str = data.optString("track_url");
        if(!Util.isEmpty(str)){
            album.setTrackUrl(str);
        }


        str = data.optString("artist_name");
        if(!Util.isEmpty(str)){
            album.setArtistName(str);
        }

        str = data.optString("artist_url");
        if(!Util.isEmpty(str)){
            album.setArtistUrl(str);
        }

        str = data.optString("album_name");
        if(!Util.isEmpty(str)){
            album.setAlbumName(str);
        }

        str = data.optString("album_url");
        if(!Util.isEmpty(str)){
            album.setAlbumUrl(str);
        }

        str = data.optString("artwork_url");
        if(!Util.isEmpty(str)){
            album.setArtworkUrl(str);
        }

        int i = data.optInt("num_streams");
        album.setNumStreams(i);

        MyLog.d(TAG, "Ending");

        return album;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public int getNumStreams() {
        return numStreams;
    }

    public void setNumStreams(int numStreams) {
        this.numStreams = numStreams;
    }



    @Override
    public String toString() {
        return "SpotifyAlbumTrack{" +
                "date=" + date +
                ", country='" + country + '\'' +
                ", trackUrl='" + trackUrl + '\'' +
                ", trackName='" + trackName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", artistUrl='" + artistUrl + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artworkUrl='" + artworkUrl + '\'' +
                ", numStreams=" + numStreams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpotifyAlbumTrack)) return false;

        SpotifyAlbumTrack that = (SpotifyAlbumTrack) o;

        if (!albumName.equals(that.albumName)) return false;
        if (artistName != null ? !artistName.equals(that.artistName) : that.artistName != null)
            return false;
        if (artistUrl != null ? !artistUrl.equals(that.artistUrl) : that.artistUrl != null)
            return false;
        if (artworkUrl != null ? !artworkUrl.equals(that.artworkUrl) : that.artworkUrl != null)
            return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (!date.equals(that.date)) return false;
        if (trackName != null ? !trackName.equals(that.trackName) : that.trackName != null)
            return false;
        if (trackUrl != null ? !trackUrl.equals(that.trackUrl) : that.trackUrl != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (trackUrl != null ? trackUrl.hashCode() : 0);
        result = 31 * result + (trackName != null ? trackName.hashCode() : 0);
        result = 31 * result + (artistName != null ? artistName.hashCode() : 0);
        result = 31 * result + (artistUrl != null ? artistUrl.hashCode() : 0);
        result = 31 * result + albumName.hashCode();
        result = 31 * result + (artworkUrl != null ? artworkUrl.hashCode() : 0);
        return result;
    }
}
