package com.xwj.xwjplayer.views.impl;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.VideoPlayPresenter;
import com.xwj.xwjplayer.presenters.impl.VideoPlayPresenterImpl;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.VideoPlayView;


/**
 * 视频播放界面
 * Created by xiaweijia on 16/3/16.
 */
public class VideoPlayActivity extends AppCompatActivity implements VideoPlayView, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private VideoView mVideoView;
    private VideoPlayPresenter videoPlayPresenter;
    private ImageButton mIbToggle, mIbPre, mIbNext;
    private SeekBar mSbDuration;
    private LinearLayout mLlCenterPanel;
    private LinearLayout mLlTopBar, mLlBottomBar;


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
        mIbToggle = (ImageButton) this.findViewById(R.id.ib_video_play_toggle);
        mIbPre = (ImageButton) this.findViewById(R.id.ib_video_play_pre);
        mIbNext = (ImageButton) this.findViewById(R.id.ib_video_play_next);
        mSbDuration = (SeekBar) this.findViewById(R.id.sb_video_duration);
        mLlCenterPanel = (LinearLayout) this.findViewById(R.id.ll_center_panel);
        mLlTopBar = (LinearLayout) this.findViewById(R.id.ll_video_play_top_bar);
        mLlBottomBar = (LinearLayout) this.findViewById(R.id.ll_bottom_bar);

        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnClickListener(this);
        mVideoView.setMediaController(null);
        mIbToggle.setOnClickListener(this);
        mIbPre.setOnClickListener(this);
        mIbNext.setOnClickListener(this);
        mLlCenterPanel.setOnClickListener(this);
        mSbDuration.setOnSeekBarChangeListener(this);
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
    public void startSeeking(int duration) {
//        mSbDuration.setProgress(duration);
    }

    @Override
    public void stopSeeking() {

    }

    @Override
    public int getProgress() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void hideTopBar() {
        mLlTopBar.setVisibility(View.GONE);
    }

    @Override
    public void showTopBar() {
        mLlTopBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        mLlBottomBar.setVisibility(View.GONE);
    }

    @Override
    public void showBottomBar() {
        mLlBottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isBarShown() {
        return mLlBottomBar.isShown() && mLlTopBar.isShown();
    }

    @Override
    public void setDuration(int duration) {
        mSbDuration.setMax(duration);
    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public void seekTo(int progress) {
        mVideoView.seekTo(progress);
    }

    @Override
    public void onClick(View v) {
        videoPlayPresenter.onClick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayPresenter.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        videoPlayPresenter.onProgressChanged(seekBar, progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
