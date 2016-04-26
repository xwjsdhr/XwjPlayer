package com.xwj.xwjplayer.views.impl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.entitys.VideoItem;

/**
 * Created by xiaweijia on 16/4/21.
 */
public class EditDialogFragment extends DialogFragment implements View.OnClickListener {

    private static EditDialogFragment dialogFragment = null;
    private VideoItem mVideoItem;
    private OnDialogDeleteListener mOnDialogDeleteListener;

    public EditDialogFragment() {
    }

    public static EditDialogFragment newInstance(Bundle bundle) {
        if (dialogFragment == null) {
            dialogFragment = new EditDialogFragment();
        }
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    private TextView mTvDelete, mTvPlay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit, container, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvDelete = (TextView) view.findViewById(R.id.tv_dialog_delete);
        mTvPlay = (TextView) view.findViewById(R.id.tv_dialog_play);
        getDialog().setTitle("编辑");
        mTvDelete.setOnClickListener(this);
        mTvPlay.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        VideoItem v = (VideoItem) bundle.getSerializable("video_item");
        if (v != null) {
            mVideoItem = v;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_delete:
                mOnDialogDeleteListener.onVideoItemDelete(this, mVideoItem);
                break;
            case R.id.tv_dialog_play:
                break;
        }
    }

    public interface OnDialogDeleteListener {
        void onVideoItemDelete(EditDialogFragment editDialogFragment, VideoItem videoItem);
    }

    public void setOnDialogDeleteListener(OnDialogDeleteListener dialogDeleteListener) {
        this.mOnDialogDeleteListener = dialogDeleteListener;
    }
}
