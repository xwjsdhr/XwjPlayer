package com.xwj.xwjplayer.interator.impl;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.xwj.xwjplayer.entitys.MusicItem;
import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.interator.QueryListener;
import com.xwj.xwjplayer.interator.QueryMusicInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class QueryMusicInteractorImpl implements QueryMusicInteractor {
    private static final String TAG = QueryMusicInteractorImpl.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;

    public QueryMusicInteractorImpl(Context context) {
        this.mContext = context;
        mHandler = new Handler(context.getMainLooper());
    }

    @Override
    public void query(final QueryListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<MusicItem> list = new ArrayList<>();
                String[] projection = new String[]{
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AlbumColumns.ALBUM_ID
                };
                Cursor cursor = mContext.getContentResolver()
                        .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,
                                null, null);
                while (cursor.moveToNext()) {
                    String data = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                    );
                    long duration = cursor.getLong(
                            cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    );
                    long size = cursor.getLong(
                            cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                    );
                    String artist = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    );
                    String songName = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                    );
                    Long albumId = cursor.getLong(
                            cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    );
                    MusicItem musicItem = new MusicItem();
                    musicItem.setData(data);
                    musicItem.setDuration(duration);
                    musicItem.setSize(size);
                    musicItem.setArtist(artist);
                    musicItem.setSongName(songName);
                    musicItem.setAlbumId(albumId);

                    Uri albumUri = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(albumUri, albumId);

                    musicItem.setAlbumPath(albumArtUri);
                    list.add(musicItem);
                }
                cursor.close();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(list);
                    }
                });
            }
        }).start();
    }
}
