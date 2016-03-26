package com.xwj.xwjplayer.presenters.impl;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xwj.xwjplayer.IMusicService;
import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.MusicItem;
import com.xwj.xwjplayer.interator.QueryListener;
import com.xwj.xwjplayer.interator.QueryMusicInteractor;
import com.xwj.xwjplayer.interator.impl.QueryMusicInteractorImpl;
import com.xwj.xwjplayer.presenters.MusicListPresenter;

import com.xwj.xwjplayer.service.MusicService;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.MusicListView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class MusicListPresenterImpl implements MusicListPresenter {
    private static final String TAG = MusicListPresenterImpl.class.getSimpleName();
    private Context mContext;
    //private QueryMusicInteractor queryMusicInteractor;
    private MusicListView musicListView;
    private WeakReference<QueryMusicInteractor> weakReference;
    IMusicService musicService;
    private boolean isPlaying = true;
    private SeekingReceiver mSeekingReceiver;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (musicService == null) {
                musicService = IMusicService.Stub.asInterface(service);
                try {
                    musicListView.setDuration(musicService.getDuration());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public MusicListPresenterImpl(Context context, MusicListView musicListView) {
        this.mContext = context;
        QueryMusicInteractor queryMusicInteractor = new QueryMusicInteractorImpl(context);
        weakReference = new WeakReference<QueryMusicInteractor>(queryMusicInteractor);

        this.musicListView = musicListView;
        mSeekingReceiver = new SeekingReceiver();
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter("com.xwj.action.seeking");
        mContext.registerReceiver(mSeekingReceiver, intentFilter);
        musicListView.showProgress();
        weakReference.get().query(new QueryListener<MusicItem>() {

            @Override
            public void onSuccess(List<MusicItem> list) {
                musicListView.bindViews(list);
                musicListView.hideProgress();
                Intent intent = new Intent(mContext, MusicService.class);
                mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                musicListView.hideProgress();
            }
        });

    }

    @Override
    public void onDestroyed() {
        mContext.unbindService(conn);
        mContext.unregisterReceiver(mSeekingReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_music_play_or_pause:
                if (isPlaying) {
                    Log.e(TAG, "暂停音乐");
                    musicListView.pauseMusic();
                    isPlaying = false;
                } else {
                    Log.e(TAG, "开始音乐");
                    musicListView.startMusic();
                    isPlaying = true;
                }
                break;
            default:
                break;
        }
    }

    public class SeekingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            musicListView.startSeeking(intent.getIntExtra(Constant.MUSIC_DURATION, 0));
        }
    }
}
