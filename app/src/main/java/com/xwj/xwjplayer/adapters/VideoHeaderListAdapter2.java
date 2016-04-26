package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.xwj.xwjplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/4/21.
 */
public class VideoHeaderListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter {

    private List<VideoItem> mVideoItemList;

    public VideoHeaderListAdapter2(Context context, List<VideoItem> videoItemList) {
        this.mVideoItemList = videoItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long getHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mVideoItemList.size();
    }
}
