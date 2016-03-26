package com.xwj.xwjplayer.entitys;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by xiaweijia on 16/3/15.
 */
public class VideoItem implements Serializable {
    private String dataUrl;
    private String videoName;
    private Long videoDuration;
    private Long size;
    //private Bitmap thumbnail;
    private Character initialsOfVideoName;

    public Character getInitialsOfVideoName() {
        return initialsOfVideoName;
    }

    public void setInitialsOfVideoName(Character initialsOfVideoName) {
        this.initialsOfVideoName = initialsOfVideoName;
    }

//    public Bitmap getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(Bitmap thumbnail) {
//        this.thumbnail = thumbnail;
//    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "dataUrl='" + dataUrl + '\'' +
                ", videoName='" + videoName + '\'' +
                ", videoDuration=" + videoDuration +
                ", size=" + size +
                ", initialsOfVideoName=" + initialsOfVideoName +
                '}';
    }
}
