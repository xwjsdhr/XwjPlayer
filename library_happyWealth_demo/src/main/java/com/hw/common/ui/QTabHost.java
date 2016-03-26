package com.hw.common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class QTabHost extends LinearLayout{
	private OnQTabChangeListener onQTabChangeListener;
	private Integer currentIndex=0;
	private View[] heads;
	private TabAdapter adapter;
	private int selectIndex = 0;
	private OnQTabChangeListener emptyLinstener=new OnQTabChangeListener() {
		public boolean onQTabChange(int clickIndex) {
			return true;
		}
	};
	
	public QTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public QTabHost(Context context) {
		super(context);
		init();
	}
	
	
	private void init(){
		this.setOrientation(LinearLayout.HORIZONTAL);
	}
	
	public void setAdapter(TabAdapter ad){
		if(ad==null){
			return;
		}
		if(heads != null){
			selectIndex = getSelectedView();
		}
		
		removeAllViews();
		this.adapter=ad;
		int count=adapter.getCount();
		heads=new View[count];
		for(int i=0;i<count;i++){
			View view=adapter.getHead(i);
			view.setClickable(true);
			LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(10, LayoutParams.MATCH_PARENT,1);
			view.setLayoutParams(llp);
			if(i==selectIndex){
				view.setSelected(true);
			}
			view.setOnClickListener(clickListener);
			heads[i]=view;
			this.addView(view, llp);
		}
		
	}
	
	private int getSelectedView(){
		for(int i=0;i<heads.length;i++){
			if(heads[i].isSelected()){
				return i;
			}
		}
		return 0;
	}
	
	public void setCurrentIndex(int currentIndex){
		clickListener.onClick(heads[currentIndex]);
	}
	
	public void setItemSelected(int currentIndex){
		heads[currentIndex].setSelected(true);
	}
	
	public void setOnQTabChangeListener(OnQTabChangeListener onQTabChangeListener){
		if(onQTabChangeListener==null){
			this.onQTabChangeListener=emptyLinstener;
		}else{
			this.onQTabChangeListener=onQTabChangeListener;
		}
	}
	
	public OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int clickIndex=-1;
			for(int i=0;i<heads.length;i++){
				if(v.equals(heads[i])){
					clickIndex=i;
					break;
				}
			}
			if(clickIndex!=-1&&clickIndex!=currentIndex){
				if(onQTabChangeListener.onQTabChange(clickIndex)){
					for(int i=0;i<heads.length;i++){
						if(clickIndex==i){
							heads[i].setSelected(true);
						}else{
							heads[i].setSelected(false);
						}
					}
					currentIndex=clickIndex;
				}
			}
		}
	};
	
	public interface OnQTabChangeListener{
		public boolean onQTabChange(int clickIndex);
	}
	
	public interface TabAdapter{
		public int getCount();
		public View getHead(int count);
	}
}
