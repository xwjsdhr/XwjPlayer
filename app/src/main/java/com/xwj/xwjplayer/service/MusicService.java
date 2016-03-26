package com.xwj.xwjplayer.service;

import android.app.Service;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xwj.xwjplayer.IMusicService;
import com.xwj.xwjplayer.utils.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.MathContext;


/**
 * Created by xiaweijia on 16/3/21.
 */
public class MusicService extends Service implements IMusicService, MediaPlayer.OnPreparedListener {
    private static final String TAG = MusicService.class.getSimpleName();
    private static final int ACTION_START = 1;
    private static final int ACTION_PAUSE = 2;

    private MediaPlayer mediaPlayer;
    private String mCurrentMusicPath = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_START:
                    Intent intent = null;
                    if (intent == null) {
                        intent = new Intent();
                        intent.setAction("com.xwj.action.seeking");
                    }
                    try {
                        intent.putExtra(Constant.MUSIC_DURATION, MusicService.this.getCurrentDuration());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    MusicService.this.sendBroadcast(intent);
                    this.sendEmptyMessageDelayed(ACTION_START, 1000);
                    break;
                case ACTION_PAUSE:
                    break;

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        if (Constant.ACTION_MUSIC_PLAY == intent.getIntExtra(Constant.ACTION_PLAY_PAUSE, 0)) {
            if (intent.getStringExtra(Constant.MUSIC_DATA) != null) {
                mCurrentMusicPath = intent.getStringExtra(Constant.MUSIC_DATA);
                try {
                    play(mCurrentMusicPath);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                mediaPlayer.start();
            }

        } else if (Constant.ACTION_MUSIC_PAUSE == intent.getIntExtra(Constant.ACTION_PLAY_PAUSE, 1)) {
            pauseMusic();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void pauseMusic() {
        try {
            this.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean playMusic(Intent intent) {
        if (mCurrentMusicPath == null) {
            mCurrentMusicPath = intent.getStringExtra(Constant.MUSIC_DATA);
            Log.e(TAG, mCurrentMusicPath);
            try {
                this.play(mCurrentMusicPath);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            if (intent.getStringExtra(Constant.MUSIC_DATA) == null) {
                try {
                    this.play(mCurrentMusicPath);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                if (!mCurrentMusicPath.equals(intent.getStringExtra(Constant.MUSIC_DATA))) {
                    mCurrentMusicPath = intent.getStringExtra(Constant.MUSIC_DATA);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    try {
                        this.play(mCurrentMusicPath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return new Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public void play(String musicPath) throws RemoteException {
                MusicService.this.play(musicPath);
            }

            @Override
            public void pause() throws RemoteException {
                MusicService.this.pause();
            }

            @Override
            public void stop() throws RemoteException {
                MusicService.this.stop();
            }

            @Override
            public void pre() throws RemoteException {
                MusicService.this.pre();
            }

            @Override
            public void next() throws RemoteException {
                MusicService.this.next();
            }

            @Override
            public int getCurrentDuration() throws RemoteException {
                return MusicService.this.getCurrentDuration();
            }

            @Override
            public int getDuration() throws RemoteException {
                return MusicService.this.getDuration();
            }
        };
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(ACTION_START);
        Log.e(TAG, "onPrepared");
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void play(String musicPath) throws RemoteException {
        if (!mediaPlayer.isPlaying()) {

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicPath);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(musicPath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(this);
        }

    }

    @Override
    public void pause() throws RemoteException {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.e(TAG, "pause    " + mCurrentMusicPath);
        }
    }

    @Override
    public void stop() throws RemoteException {

    }

    @Override
    public void pre() throws RemoteException {

    }

    @Override
    public void next() throws RemoteException {

    }

    @Override
    public int getCurrentDuration() throws RemoteException {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() throws RemoteException {
        return mediaPlayer.getDuration();
    }

    @Override
    public IBinder asBinder() {
        return new Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
                MusicService.this.basicTypes(anInt, aLong, aBoolean, aFloat, aDouble, aString);
            }

            @Override
            public void play(String musicPath) throws RemoteException {
                MusicService.this.play(musicPath);
            }

            @Override
            public void pause() throws RemoteException {
                MusicService.this.pause();
            }

            @Override
            public void stop() throws RemoteException {
                MusicService.this.stop();
            }

            @Override
            public void pre() throws RemoteException {
                MusicService.this.pre();
            }

            @Override
            public void next() throws RemoteException {
                MusicService.this.next();
            }

            @Override
            public int getCurrentDuration() throws RemoteException {
                return MusicService.this.getCurrentDuration();
            }

            @Override
            public int getDuration() throws RemoteException {
                return MusicService.this.getDuration();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        handler.removeCallbacksAndMessages(null);
        Log.e(TAG, "onDestroy");
    }
}
