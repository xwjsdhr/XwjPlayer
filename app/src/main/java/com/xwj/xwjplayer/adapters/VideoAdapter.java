package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.VideoItem;

import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by xiaweijia on 16/3/15.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private Context mContext;
    private List<VideoItem> mVideoItemList;
    private LayoutInflater mInflater;


    public VideoAdapter(Context context, List<VideoItem> videoItemList) {
        this.mContext = context;
        this.mVideoItemList = videoItemList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        long duration = mVideoItemList.get(position).getVideoDuration();
        holder.bindView(mVideoItemList.get(position));

    }

    @Override
    public int getItemCount() {
        return mVideoItemList.size();
    }
}
