package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.MusicItem;
import com.xwj.xwjplayer.views.MusicListView;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {

    private List<MusicItem> musicItemList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MusicListView mMusicListView;

    public MusicAdapter(MusicListView musicListView, Context context, List<MusicItem> list) {
        mMusicListView = musicListView;
        this.mContext = context;
        this.musicItemList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.music_item, parent, false);
        return new MusicViewHolder(mMusicListView, mContext, itemView);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        holder.bindView(musicItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicItemList.size();
    }
}
