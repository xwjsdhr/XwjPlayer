package com.xwj.xwjplayer.views.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.adapters.VideoAdapter;
import com.xwj.xwjplayer.adapters.VideoHeaderListAdapter;
import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.presenters.VideoListPresenter;
import com.xwj.xwjplayer.presenters.impl.VideoListPresenterImpl;
import com.xwj.xwjplayer.views.VideoListView;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements VideoListView, VideoHeaderListAdapter.OnItemLongClickListener {


    private static final float TOOLBAR_ELEVATION = 3.0f;
    // private VideoAdapter mVideoAdapter;
    private VideoHeaderListAdapter videoHeaderListAdapter;
    private List<VideoItem> videoItemList;
    private RecyclerView mRvVideoList;
    private ProgressBar mProgressBar;
    private VideoListPresenter videoListPresenter;
    private Toolbar tToolbar;

    private FragmentManager mFragmentManager;
    private EditDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        videoItemList = new ArrayList<>();
        videoHeaderListAdapter = new VideoHeaderListAdapter(this, videoItemList);
        initViews();

        mFragmentManager = this.getSupportFragmentManager();
        videoListPresenter = new VideoListPresenterImpl(this, this);
        videoListPresenter.onStart();
    }

    private void initViews() {
        tToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(tToolbar);
        tToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoListActivity.this.finish();
            }
        });
        mProgressBar = (ProgressBar) this.findViewById(R.id.progress);
        mRvVideoList = (RecyclerView) this.findViewById(R.id.rv_main_video_list);
        mRvVideoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // Keeps track of the overall vertical offset in the list
            int verticalOffset;

            // Determines the scroll UP/DOWN direction
            boolean scrollingUp;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    } else {
                        if (tToolbar.getTranslationY() < tToolbar.getHeight() * -0.6 && verticalOffset > tToolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    }
                }
            }

            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                scrollingUp = dy > 0;
                int toolbarYOffset = (int) (dy - tToolbar.getTranslationY());
                tToolbar.animate().cancel();
                if (scrollingUp) {
                    if (toolbarYOffset < tToolbar.getHeight()) {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        tToolbar.setTranslationY(-toolbarYOffset);
                    } else {
                        toolbarSetElevation(0);
                        tToolbar.setTranslationY(-tToolbar.getHeight());
                    }
                } else {
                    if (toolbarYOffset < 0) {
                        if (verticalOffset <= 0) {
                            toolbarSetElevation(0);
                        }
                        tToolbar.setTranslationY(0);
                    } else {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        tToolbar.setTranslationY(-toolbarYOffset);
                    }
                }
            }

        });

        mRvVideoList.setLayoutManager(new LinearLayoutManager(this));
        mRvVideoList.setHasFixedSize(true);
        mRvVideoList.setAdapter(videoHeaderListAdapter);
        videoHeaderListAdapter.setOnItemLongClickListener(this);


    }


    @Override
    public void bindViews(List<VideoItem> videoItems) {
        videoItemList.addAll(videoItems);
        videoHeaderListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        mRvVideoList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mRvVideoList.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    //显示编辑对话框
    @Override
    public void showEditDialog(VideoItem videoItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("video_item", videoItem);
        dialogFragment = EditDialogFragment.newInstance(bundle);
        dialogFragment.setOnDialogDeleteListener(new EditDialogFragment.OnDialogDeleteListener() {
            @Override
            public void onVideoItemDelete(EditDialogFragment editDialogFragment, VideoItem videoItem) {
                Toast.makeText(VideoListActivity.this, videoItem.getDataUrl(), Toast.LENGTH_SHORT).show();
                int i = videoItemList.indexOf(videoItem);
                videoItemList.remove(i);
                videoHeaderListAdapter.notifyItemRemoved(i);
                editDialogFragment.dismiss();

            }
        });
        dialogFragment.show(mFragmentManager, EditDialogFragment.class.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoListPresenter.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        // setElevation() only works on Lollipop
        tToolbar.setElevation(elevation);
    }

    private void toolbarAnimateShow(final int verticalOffset) {
        tToolbar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarSetElevation(verticalOffset == 0 ? 0 : TOOLBAR_ELEVATION);
                    }
                });
    }

    private void toolbarAnimateHide() {
        tToolbar.animate()
                .translationY(-tToolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarSetElevation(0);
                    }
                });
    }

    @Override
    public void onItemLongClick(VideoItem videoItem) {
        videoListPresenter.onItemLongClick(videoItem);
    }

}