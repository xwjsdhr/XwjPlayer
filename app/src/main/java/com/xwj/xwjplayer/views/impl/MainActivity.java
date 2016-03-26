package com.xwj.xwjplayer.views.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.MainPresenter;
import com.xwj.xwjplayer.presenters.impl.MainPresenterImpl;
import com.xwj.xwjplayer.views.MainView;


/**
 * Created by xiaweijia on 16/3/16.
 */
public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private ImageButton mIbVideoList, mIbMusicList;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        Log.e("tag", "onCreate");
        initViews();
        mMainPresenter = new MainPresenterImpl(this, this);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIbMusicList = (ImageButton) this.findViewById(R.id.ib_music_list);
        mIbVideoList = (ImageButton) this.findViewById(R.id.ib_video_list);
        mIbMusicList.setOnClickListener(this);
        mIbVideoList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mMainPresenter.onClick(v);
    }

    @Override
    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }
}
