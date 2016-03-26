package com.xwj.xwjplayer.presenters.impl;

import android.content.Context;
import android.media.AudioManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.VideoPlayPresenter;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.VideoPlayView;

/**
 * Created by xiaweijia on 16/3/22.
 */
public class VideoPlayPresenterImpl implements VideoPlayPresenter {

    private static final String TAG = VideoPlayPresenterImpl.class.getSimpleName();
    private Context context;
    private AudioManager mAudioManager;
    private VideoPlayView mVideoPlayView;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int startX = 0;

    public VideoPlayPresenterImpl(Context context, VideoPlayView videoPlayView) {
        this.context = context;
        mVideoPlayView = videoPlayView;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Log.e(TAG, "VideoPlayPresenterImpl: " + displayMetrics.heightPixels + ":" + displayMetrics.widthPixels);
        Log.e(TAG, "VideoPlayPresenterImpl: " + mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }

    @Override
    public void onCreate() {
        String videoPlayStr = mVideoPlayView.getIntentObj().getBundleExtra(Constant.BUNDLE_NAME).getString(Constant.VIDEO_PLAY_URI);
        mVideoPlayView.setVideoPath(videoPlayStr);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.video_view) {
            if (mVideoPlayView.isPlaying()) {
                mVideoPlayView.pause();
            } else {
                mVideoPlayView.start();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) motionEvent.getX();
                Log.e(TAG, "onTouchEvent: " + (motionEvent.getX() - startX));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + (motionEvent.getX() - startX));
                float dx = motionEvent.getX() - startX;
                //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
