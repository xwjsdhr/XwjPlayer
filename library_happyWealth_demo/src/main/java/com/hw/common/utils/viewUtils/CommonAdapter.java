package com.hw.common.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;

@SuppressLint("NewApi")
public abstract class CommonAdapter<T> extends BaseAdapter  {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public void swapItems(int positionOne, int positionTwo) {
		T temp = getItem(positionOne);
		set(positionOne, getItem(positionTwo));
		set(positionTwo, temp);
	}
	
	public void set(int position, T item) {
		mDatas.set(position, item);
		notifyDataSetChanged();
	}
	
	public CommonAdapter(Context context, int itemLayoutId){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = new ArrayList<T>();
		this.mItemLayoutId = itemLayoutId;
	}
	
	public void clear(){
		mDatas.clear();
		notifyDataSetChanged();
	}
	
	public void add(List<T> list){
		if(list == null) return;
		mDatas.addAll(list);
		notifyDataSetChanged();
	}
	
	public void refresh(List<T> list){
		if(list == null) return;
		if(mDatas.size() > 0) mDatas.clear();
		mDatas.addAll(list);
		notifyDataSetChanged();
	}
	
	public void refresh(List<T> list, Boolean isAnim){
		if(list == null) return;
		if(mDatas.size() > 0) mDatas.clear();
		mDatas.addAll(list);
		if(isAnim) lastAnimatedPosition = -1;
		notifyDataSetChanged();
	}
	
	public void startAnim(){
		lastAnimatedPosition = -1;
		notifyDataSetChanged();
	}
	
	public List<T> getDatas(){
		return mDatas;
	}

	@Override
	public int getCount(){
		return mDatas.size();
	}

	@Override
	public T getItem(int position){
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		if(getItem(position) != null) getView(viewHolder, getItem(position));
		
//		runEnterAnimation(viewHolder.getConvertView(),position);
		
//		AnimatorSet set = new AnimatorSet() ; 
//      ObjectAnimator alpha = ObjectAnimator.ofFloat(viewHolder.getConvertView(), "alpha", 0.3f, 1f).setDuration(800);
//      alpha.setInterpolator(new DecelerateInterpolator());
//      ObjectAnimator trans = ObjectAnimator.ofFloat(viewHolder.getConvertView(), "translationY", 100, 0).setDuration(800);
//      set.playTogether(trans,alpha);
//      set.start();
      
//      TranslateAnimation anim = new TranslateAnimation(0, 0, 100, 0);
//      anim.setInterpolator(new OvershootInterpolator(1.1f));
//      anim.setDuration(800);
//      viewHolder.getConvertView().startAnimation(anim);
//      anim.start();
//		AnimationUtils.tada(viewHolder.getConvertView()).start();
//		ObjectAnimator.ofFloat(viewHolder.getConvertView(), "translationY", 100, 0).setDuration(500).start();
		return viewHolder.getConvertView();

	}
	
	 private int lastAnimatedPosition = -1;
	 private boolean animationsLocked = false;
	    private boolean delayEnterAnimation = true;
	private void runEnterAnimation(View view, int position) {
//        if (animationsLocked) return;

//		MLogUtil.e("lastAnimatedPosition "+lastAnimatedPosition);
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

	public abstract void getView(ViewHolder helper, T item);

	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent){
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

}
