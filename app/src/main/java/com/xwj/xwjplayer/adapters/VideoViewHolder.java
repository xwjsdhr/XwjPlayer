package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.views.VideoItemView;
import com.xwj.xwjplayer.views.impl.VideoPlayActivity;
import com.xwj.xwjplayer.views.impl.VideoPlayerActivity;


/**
 * Created by xiaweijia on 16/3/16.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder implements VideoItemView, View.OnClickListener {

    public TextView mTvDisplayName;
    public TextView mTvDuration;
    public TextView mTvSize;
    public ImageView mIvThumbnail;
    private Context mContext;
    private VideoItem videoItem;

    public VideoViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mTvDisplayName = (TextView) itemView.findViewById(R.id.tv_item_name);
        mTvDuration = (TextView) itemView.findViewById(R.id.tv_item_duration);
        mTvSize = (TextView) itemView.findViewById(R.id.tv_item_size);
        mIvThumbnail = (ImageView) itemView.findViewById(R.id.iv_video_item_thumbnail);
        itemView.setOnClickListener(this);
    }

    @Override
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra(Constant.BUNDLE_NAME, bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void bindView(VideoItem item) {
        videoItem = item;
        //this.mIvThumbnail.setImageBitmap(item.getThumbnail());
        this.mTvDuration.setText(String.valueOf(item.getVideoDuration()));
        this.mTvDisplayName.setText(item.getVideoName());
        this.mTvSize.setText(Formatter.formatFileSize(mContext, item.getSize()));
    }

    @Override
    public void onClick(View v) {
        if (v == itemView) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.VIDEO_PLAY_URI, videoItem.getDataUrl());
            startActivity(VideoPlayActivity.class, bundle);
        }
    }
}
