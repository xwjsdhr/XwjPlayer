package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Picasso;
import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.utils.CommonUtils;
import com.xwj.xwjplayer.utils.Constant;
import com.xwj.xwjplayer.utils.VideoRequestHandler;
import com.xwj.xwjplayer.views.impl.VideoPlayActivity;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiaweijia on 16/3/23.
 */
public class VideoHeaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener {

    private static final int HEADER_LAYOUT = 1;
    private static final int ITEM_LAYOUT = 2;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<VideoItem> mList;
    private OnItemLongClickListener onItemLongClickListener;

    public VideoHeaderListAdapter(Context context, List<VideoItem> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEADER_LAYOUT) {
            View itemView = mInflater.inflate(R.layout.header_layout, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(itemView);
            return headerViewHolder;
        } else if (viewType == ITEM_LAYOUT) {
            View itemView = mInflater.inflate(R.layout.video_item, parent, false);
            ListViewHolder listViewHolder = new ListViewHolder(itemView);
            return listViewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {

            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).mTvInitial.setText(Character.toString(mList.get(position).getInitialsOfVideoName()));
            } else {
                ((ListViewHolder) holder).mVideoItem = mList.get(position);
                ((ListViewHolder) holder).mTvDuration.setText(CommonUtils.getTimeString(mList.get(position).getVideoDuration()));
                ((ListViewHolder) holder).mTvDisplayName.setText(mList.get(position).getVideoName());
                ((ListViewHolder) holder).mTvSize.setText(Formatter.formatFileSize(mContext, mList.get(position).getSize()));

                String dataUrl = mList.get(position).getDataUrl();
                ((ListViewHolder) holder).mIvThumbnail.setImageURI(Uri.fromFile(new File(dataUrl)));
                ((ListViewHolder) holder).itemView.setTag(((ListViewHolder) holder).mVideoItem);
                ((ListViewHolder) holder).itemView.setOnLongClickListener(this);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(mList.get(position).getDataUrl())) {
            return HEADER_LAYOUT;
        } else {
            return ITEM_LAYOUT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public boolean onLongClick(View v) {
        if (onItemLongClickListener != null) {
            VideoItem videoItem = (VideoItem) v.getTag();
            onItemLongClickListener.onItemLongClick(videoItem);
        }
        return true;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView mTvInitial;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTvInitial = (TextView) itemView.findViewById(R.id.tv_item_initial);
        }
    }


    static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = ListViewHolder.class.getSimpleName();
        public TextView mTvDisplayName;
        public TextView mTvDuration;
        public TextView mTvSize;
        public SimpleDraweeView mIvThumbnail;
        private Context mContext;
        public VideoItem mVideoItem;

        public ListViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

            mTvDisplayName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTvDuration = (TextView) itemView.findViewById(R.id.tv_item_duration);
            mTvSize = (TextView) itemView.findViewById(R.id.tv_item_size);
            mIvThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_video_item_thumbnail);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.VIDEO_PLAY_URI, mVideoItem.getDataUrl());
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra(Constant.BUNDLE_NAME, bundle);
                mContext.startActivity(intent);
            }
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(VideoItem videoItem);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.onItemLongClickListener = itemLongClickListener;
    }
}
