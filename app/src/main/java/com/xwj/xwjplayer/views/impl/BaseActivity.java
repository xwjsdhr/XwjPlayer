package com.xwj.xwjplayer.views.impl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xiaweijia on 16/3/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        setListeners();
        loadData();
    }

    protected abstract void loadData();

    protected abstract void setListeners();

    protected abstract void init();

    protected abstract void initViews();

}
