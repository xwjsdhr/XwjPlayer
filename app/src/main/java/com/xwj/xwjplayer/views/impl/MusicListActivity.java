package com.xwj.xwjplayer.views.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.adapters.MusicAdapter;
import com.xwj.xwjplayer.entitys.MusicItem;
import com.xwj.xwjplayer.presenters.MusicListPresenter;
import com.xwj.xwjplayer.presenters.impl.MusicListPresenterImpl;
import com.xwj.xwjplayer.service.MusicService;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.MusicListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐列表界面
 * Created by xiaweijia on 16/3/16.
 */
public class MusicListActivity extends AppCompatActivity implements MusicListView, View.OnClickListener {
    private static final String TAG = MusicListActivity.class.getSimpleName();
    private RecyclerView mRvMusicList;
    private List<MusicItem> list = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private MusicListPresenter musicListPresenter;
    private ProgressDialog progressDialog;

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private AppCompatSeekBar mAppCompatSeekBar;
    private TextView mTvCurrSongName, mTvArtistName;
    private AppCompatImageButton mIbPlayOrPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initViews();
        musicAdapter = new MusicAdapter(this, this, list);
        musicListPresenter = new MusicListPresenterImpl(this, this);
        mRvMusicList.setAdapter(musicAdapter);
        musicListPresenter.onCreate();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicListActivity.this.finish();
            }
        });
        mRvMusicList = (RecyclerView) this.findViewById(R.id.rv_music_list);
        mSlidingUpPanelLayout = (SlidingUpPanelLayout) this.findViewById(R.id.sliding_layout);
        mAppCompatSeekBar = (AppCompatSeekBar) this.findViewById(R.id.sb_music_duration);
        mTvCurrSongName = (TextView) this.findViewById(R.id.tv_song_name);
        mIbPlayOrPause = (AppCompatImageButton) this.findViewById(R.id.ib_music_play_or_pause);
        mTvArtistName = (TextView) this.findViewById(R.id.tv_artist_name);
        mSlidingUpPanelLayout.setTouchEnabled(true);
        mRvMusicList.setLayoutManager(new LinearLayoutManager(this));
        mRvMusicList.setHasFixedSize(true);
        mIbPlayOrPause.setOnClickListener(this);
    }


    @Override
    public void bindViews(List<MusicItem> musicItems) {
        list.addAll(musicItems);
        musicAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("");
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void startSeeking(int currentDuration) {
        mAppCompatSeekBar.setProgress(currentDuration);
    }

    @Override
    public void bindBottomView(MusicItem musicItem) {
        mTvCurrSongName.setText(musicItem.getSongName());
        mTvArtistName.setText(musicItem.getArtist());
        mAppCompatSeekBar.setMax((int) musicItem.getDuration());
    }

    @Override
    public void togglePlayAndPause() {

    }

    @Override
    public void startMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(Constant.ACTION_PLAY_PAUSE, Constant.ACTION_MUSIC_PLAY);
        this.startService(intent);
        mIbPlayOrPause.setImageResource(R.drawable.ic_pause_circle_outline_black_36dp);
    }

    @Override
    public void pauseMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(Constant.ACTION_PLAY_PAUSE, Constant.ACTION_MUSIC_PAUSE);
        this.startService(intent);
        mIbPlayOrPause.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
    }

    @Override
    public void setPauseIcon() {
        mIbPlayOrPause.setImageResource(R.drawable.ic_pause_circle_outline_black_36dp);
    }

    @Override
    public void setStartIcon() {
        mIbPlayOrPause.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
    }

    @Override
    public void setDuration(int duration) {
        Log.e(TAG, "setDuration: ");
        mAppCompatSeekBar.setMax(duration);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicListPresenter.onDestroyed();
    }

    @Override
    public void onClick(View v) {
        musicListPresenter.onClick(v);
    }
}
