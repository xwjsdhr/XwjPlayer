package com.hw.common.ui.dialog;

import com.hw.common.R;

import android.content.Context;
import android.widget.ProgressBar;

public class DownProgressDialog extends BaseDialog{
	private ProgressBar mProgress;
	 
	public DownProgressDialog(Context context) {
		super(context,R.layout.dialog_down_progress);
		this.hiddenMiddleBtn();
		setCanceledOnTouchOutside(false);
		mProgress = (ProgressBar) getLayout().findViewById(R.id.progress);
		setAnim();
	}

	public DownProgressDialog setProgress(int progress) {
		mProgress.setProgress(progress);
		setTitle("正在更新..." + progress + "%");
		return this;
	}
	
	public DownProgressDialog setProgress(String title,int progress) {
		mProgress.setProgress(progress);
		setTitle(title);
		return this;
	}

}
