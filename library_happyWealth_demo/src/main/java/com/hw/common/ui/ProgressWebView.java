package com.hw.common.ui;

import com.hw.common.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	/** 进度条 */
	private ProgressBar progressbar = null;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化进度条
		progressbar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
//		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		// 设置进度条风格
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5, 0, 0));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());

	}

	public class WebChromeClient extends android.webkit.WebChromeClient {

		public void onProgressChanged(WebView view, int newProgress) {
			if(newProgress == 100) {
				// 加载完成隐藏进度条
				progressbar.setVisibility(GONE);
			} else {
				if(progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}
	
	public boolean onTouchEvent(MotionEvent ev) {
	    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
	        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
	    }
	    return super.onTouchEvent(ev);
	}

	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
