package com.xwj.xwjplayer.entitys;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class MusicItem implements Serializable {

    private String data;
    private String songName;
    private String artist;
    private long duration;
    private long size;
    private Uri albumPath;
    private Long albumId;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Uri getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(Uri albumPath) {
        this.albumPath = albumPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


}
