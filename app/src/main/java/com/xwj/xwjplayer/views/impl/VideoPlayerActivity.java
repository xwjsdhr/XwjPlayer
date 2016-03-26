package com.xwj.xwjplayer.views.impl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.widget.VideoControllerView;

import java.io.IOException;

/**
 * Created by xiaweijia on 16/3/23.
 */
public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private VideoControllerView videoControllerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        surfaceView = (SurfaceView) this.findViewById(R.id.video_surface);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);

        mediaPlayer = new MediaPlayer();
        videoControllerView = new VideoControllerView(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String videoPlayStr = this.getIntent().getBundleExtra(Constant.BUNDLE_NAME).getString(Constant.VIDEO_PLAY_URI);
        try {
            mediaPlayer.setDataSource(videoPlayStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoControllerView.setMediaPlayer(this);
        videoControllerView.setAnchorView((FrameLayout) findViewById(R.id.video_surface_container));
        mp.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        videoControllerView.show();
        return false;
    }
}
