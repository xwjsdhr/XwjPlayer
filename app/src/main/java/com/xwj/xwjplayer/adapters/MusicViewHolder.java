package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.MusicItem;
import com.xwj.xwjplayer.service.MusicService;
import com.xwj.xwjplayer.utils.CommonUtils;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.MusicItemView;
import com.xwj.xwjplayer.views.MusicListView;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MusicItemView {
    public TextView mTvSongName, mTvArtist, mTvDuration;

    private Context mContext;

    private MusicItem mMusicItem;

    private boolean mIsPlaying = false;
    private MusicListView mMusicListView;

    public MusicViewHolder(MusicListView musicListView, Context context, View itemView) {
        super(itemView);
        mContext = context;
        mMusicListView = musicListView;
        mTvSongName = (TextView) itemView.findViewById(R.id.tv_item_music_name);
        mTvArtist = (TextView) itemView.findViewById(R.id.tv_item_music_artist);
        mTvDuration = (TextView) itemView.findViewById(R.id.tv_item_music_duration);
        itemView.setOnClickListener(this);
    }

    public void bindView(MusicItem musicItem) {
        mMusicItem = musicItem;
        mTvSongName.setText(musicItem.getSongName());
        mTvArtist.setText(musicItem.getArtist());
        mTvDuration.setText(CommonUtils.getTimeString(musicItem.getDuration()));
    }

    @Override
    public void onClick(View v) {
        if (mMusicItem != null) {
            play(mMusicItem);
        }
    }

    @Override
    public void play(MusicItem musicItem) {
        Intent intent = new Intent(mContext, MusicService.class);
        intent.putExtra(Constant.ACTION_PLAY_PAUSE, Constant.ACTION_MUSIC_PLAY);
        intent.putExtra(Constant.MUSIC_DATA, musicItem.getData());
        intent.putExtra(Constant.MUSIC_ITEM, musicItem);
        musicItem.setPlaying(true);
        mContext.startService(intent);
        mMusicListView.bindBottomView(mMusicItem);
        mMusicListView.setPauseIcon();
    }

}
