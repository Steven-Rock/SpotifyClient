package com.swr.spotifyclient.spotify.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 4/3/2014.
 */
public class SpotifyAlbum  extends SpotifyAbstractModel implements Comparable<SpotifyAlbum>{

    protected static final long serialVersionUID = -5859276137857969189L;

    protected String albumName = null;
    protected String artistName = null;
    protected String artworkUrl = null;
    protected int num = 0;

    protected List<SpotifyAlbumTrack> tracks = new ArrayList<SpotifyAlbumTrack>();

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public List<SpotifyAlbumTrack> getTracks() {
        return tracks;
    }

    public void addTrack(SpotifyAlbumTrack track) {

        if(!tracks.contains(track)) {
            tracks.add(track);
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int compareTo(SpotifyAlbum another) {
        if(another == null) return -1;

        return this.num < another.num ? -1
                : this.num > another.num ? 1
                : 0;
    }
}
