package com.xwj.xwjplayer.views.impl;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.VideoPlayPresenter;
import com.xwj.xwjplayer.presenters.impl.VideoPlayPresenterImpl;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.VideoPlayView;


/**
 * Created by xiaweijia on 16/3/16.
 */
public class VideoPlayActivity extends Activity implements VideoPlayView, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnTouchListener, View.OnClickListener {
    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private VideoView mVideoView;
    private VideoPlayPresenter videoPlayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initViews();
        videoPlayPresenter = new VideoPlayPresenterImpl(this, this);
        videoPlayPresenter.onCreate();
    }

    private void initViews() {
        mVideoView = (VideoView) this.findViewById(R.id.video_view);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnClickListener(this);
        mVideoView.setOnTouchListener(this);
        mVideoView.setMediaController(new MediaController(this));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "正在准备。。");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "准备完成..");
        this.finish();
    }

    @Override
    public void setVideoPath(String videoPath) {
        mVideoView.setVideoPath(videoPath);
    }

    @Override
    public Intent getIntentObj() {
        return this.getIntent();
    }

    @Override
    public void start() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return videoPlayPresenter.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        videoPlayPresenter.onClick(v);
    }
}
