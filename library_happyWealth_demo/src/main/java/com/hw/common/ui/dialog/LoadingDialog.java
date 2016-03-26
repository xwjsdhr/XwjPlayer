package com.hw.common.ui.dialog;

import com.hw.common.R;
import com.hw.common.utils.basicUtils.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	private TextView loading_text;
	public ProgressBar loading_progressBar;
	
	public LoadingDialog(Context context,String msg) {
		super(context, R.style.dialog_transparent_backgroud);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View base = inflater.inflate(R.layout.dialog_loading, null);
		setContentView(base);
		setCanceledOnTouchOutside(false);
//		loading_progressBar = (ProgressBar) base.findViewById(R.id.loading_progressBar);
		loading_text = (TextView) base.findViewById(R.id.loading_text);
		
		if(!StringUtils.isEmpty(msg)){
			loading_text.setText(msg);
		}else{
			loading_text.setVisibility(View.GONE);
		} 
	}
	

}
