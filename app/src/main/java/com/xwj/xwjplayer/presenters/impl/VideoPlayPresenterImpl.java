package com.xwj.xwjplayer.presenters.impl;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.VideoPlayPresenter;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.VideoPlayView;

/**
 * Created by xiaweijia on 16/3/22.
 */
public class VideoPlayPresenterImpl implements VideoPlayPresenter {

    private static final String TAG = VideoPlayPresenterImpl.class.getSimpleName();
    private static final int ACTION_HIDE_BAR = 3;
    private Context mContext;
    private AudioManager mAudioManager;
    private VideoPlayView mVideoPlayView;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int startX = 0;
    private int mMaxVolume;

    private static final int ACTION_START_SEEKING = 1;
    private static final int ACTION_STOP_SEEKING = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_START_SEEKING:
                    mVideoPlayView.startSeeking(mVideoPlayView.getProgress()+1000);
                    Log.e(TAG, mVideoPlayView.getProgress() + "");
                    Log.e(TAG, mVideoPlayView.getDuration() + "");
                    handler.sendEmptyMessageDelayed(ACTION_START_SEEKING, 1000);
                    break;
                case ACTION_STOP_SEEKING:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case ACTION_HIDE_BAR:
                    mVideoPlayView.hideTopBar();
                    mVideoPlayView.hideBottomBar();
                    break;

            }
        }
    };

    public VideoPlayPresenterImpl(Context context, VideoPlayView videoPlayView) {
        this.mContext = context;
        mVideoPlayView = videoPlayView;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onCreate() {
        String videoPlayStr = mVideoPlayView.getIntentObj().getBundleExtra(Constant.BUNDLE_NAME).getString(Constant.VIDEO_PLAY_URI);
        mVideoPlayView.setVideoPath(videoPlayStr);
        mVideoPlayView.setDuration(mVideoPlayView.getDuration());
        handler.sendEmptyMessage(ACTION_START_SEEKING);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_video_play_toggle:
                if (mVideoPlayView.isPlaying()) {
                    mVideoPlayView.pause();
                    handler.sendEmptyMessage(ACTION_STOP_SEEKING);
                } else {
                    mVideoPlayView.start();
                    handler.sendEmptyMessage(ACTION_START_SEEKING);
                }
                break;
            case R.id.ib_video_play_pre:
                break;
            case R.id.ib_video_play_next:
                break;
            case R.id.ll_center_panel:
                if (mVideoPlayView.isBarShown()) {
                    mVideoPlayView.hideBottomBar();
                    mVideoPlayView.hideTopBar();

                } else {
                    mVideoPlayView.showBottomBar();
                    mVideoPlayView.showTopBar();
                    handler.sendEmptyMessageDelayed(ACTION_HIDE_BAR, 3000);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVideoPlayView.seekTo(progress);
        }
    }

}
