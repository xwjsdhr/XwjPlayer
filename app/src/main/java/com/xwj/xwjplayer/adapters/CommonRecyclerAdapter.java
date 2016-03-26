package com.xwj.xwjplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerAdapter.CommonViewHolder> {

    private List<T> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommonRecyclerAdapter(Context context, List<T> list) {
        this.mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public CommonRecyclerAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getItemLayoutId(), parent, false);
        return new CommonViewHolder<T>(itemView) {
            @Override
            public void bindView(T t) {
                CommonRecyclerAdapter.this.bindView(this, t);
            }
        };
    }

    protected abstract void bindView(CommonViewHolder commonViewHolder, T t);

    protected abstract int getItemLayoutId();


    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public abstract class CommonViewHolder<T> extends RecyclerView.ViewHolder {

        public CommonViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindView(T t);

        public CommonViewHolder bindText(int viewId, String text) {
            if (itemView instanceof TextView) {
                TextView textView = (TextView) itemView.findViewById(viewId);
                textView.setText(text);
            } else if (itemView instanceof Button) {
                Button button = (Button) itemView.findViewById(viewId);
                button.setText(text);
            }
            return this;
        }

        public CommonViewHolder bindImageBitmap(int viewId, Bitmap bitmap) {
            if (itemView instanceof ImageView) {
                ImageView imageView = (ImageView) itemView.findViewById(viewId);
                imageView.setImageBitmap(bitmap);
            } else if (itemView instanceof ImageButton) {
                ImageButton imageButton = (ImageButton) itemView.findViewById(viewId);
                imageButton.setImageBitmap(bitmap);
            }
            return this;
        }
    }

    public void addAll(List<T> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void add(T t) {
        mList.add(0, t);
        this.notifyItemInserted(0);
    }

}
