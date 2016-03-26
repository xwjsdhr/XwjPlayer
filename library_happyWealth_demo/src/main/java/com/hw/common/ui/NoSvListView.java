package com.hw.common.ui;

import android.widget.ListView;

public class NoSvListView extends ListView {
	public NoSvListView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(false);
	}
	
	
	/*@Override
	protected void handleDataChanged() {
		ListAdapter adapter=this.getAdapter();
		System.out.println("handleDataChangedhandleDataChanged!!!!!!!!!!!");
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, this);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = totalHeight + (this.getDividerHeight() * (this.getCount() - 1));
		this.setLayoutParams(params);
		super.handleDataChanged();
	}*/
	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
