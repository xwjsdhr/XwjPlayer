package com.xwj.xwjplayer.interator.impl;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.github.promeg.pinyinhelper.Pinyin;
import com.xwj.xwjplayer.adapters.VideoAdapter;
import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.interator.QueryListener;
import com.xwj.xwjplayer.interator.QueryVideoInteractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Created by xiaweijia on 16/3/15.
 */
public class QueryVideoInteractorImpl implements QueryVideoInteractor {
    private static final String TAG = QueryVideoInteractorImpl.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;
    private Character mLastIntial = null;
    private Thread thread;
    //private MediaMetadataRetriever mediaMetadataRetriever;

    public QueryVideoInteractorImpl(Context context) {
        this.mContext = context;
        mHandler = new Handler(context.getMainLooper());
        //mediaMetadataRetriever = new MediaMetadataRetriever();
    }

    @Override
    public void query(final QueryListener listener) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<VideoItem> listRes = new ArrayList<VideoItem>();
                List<VideoItem> list = new ArrayList<>();
                String[] projection = new String[]{
                        MediaStore.Video.VideoColumns.DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION,
                        MediaStore.Video.VideoColumns.SIZE,
                        MediaStore.Video.VideoColumns.DATA
                };
                Cursor cursor = mContext.getContentResolver()
                        .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                                null, null);
                while (cursor.moveToNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    VideoItem videoItem = new VideoItem();
                    String videoName = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    Character initialOfVideoName = !Pinyin.isChinese(videoName.charAt(0)) ? Character.toUpperCase(videoName.charAt(0))
                            : Pinyin.toPinyin(videoName.charAt(0)).charAt(0);

                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    list.add(videoItem);
                }

                cursor.close();

                Collections.sort(list, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        if (Pinyin.isChinese(lhs.getInitialsOfVideoName())) {
                            if (Pinyin.isChinese(rhs.getInitialsOfVideoName())) {
                                return ((Character) Pinyin.toPinyin(lhs.getInitialsOfVideoName()).charAt(0))
                                        .compareTo(((Character) Pinyin.toPinyin(rhs.getInitialsOfVideoName()).charAt(0)));
                            } else {
                                return ((Character) Pinyin.toPinyin(lhs.getInitialsOfVideoName()).charAt(0))
                                        .compareTo(rhs.getInitialsOfVideoName());
                            }
                        } else {
                            if (Pinyin.isChinese(rhs.getInitialsOfVideoName())) {
                                return lhs.getInitialsOfVideoName()
                                        .compareTo(((Character) Pinyin.toPinyin(rhs.getInitialsOfVideoName()).charAt(0)));
                            } else {
                                return lhs.getInitialsOfVideoName().compareTo(rhs.getInitialsOfVideoName());
                            }
                        }
                    }
                });

                Iterator<VideoItem> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    VideoItem videoItem = new VideoItem();
                    VideoItem item = iterator.next();

                    Character initialOfVideoName = item.getInitialsOfVideoName();
                    String videoName = item.getVideoName();
                    long duration = item.getVideoDuration();
                    long size = item.getSize();
                    String data = item.getDataUrl();

                    if (mLastIntial == null || !mLastIntial.equals(initialOfVideoName)) {
                        mLastIntial = initialOfVideoName;
                        VideoItem videoItem1 = new VideoItem();
                        if (Pinyin.isChinese(mLastIntial)) {
                            videoItem1.setInitialsOfVideoName(Pinyin.toPinyin(mLastIntial).charAt(0));
                        } else {
                            videoItem1.setInitialsOfVideoName(initialOfVideoName);
                        }
                        listRes.add(videoItem1);
                    }
                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    listRes.add(videoItem);
                }
                list = null;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(listRes);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }
}
