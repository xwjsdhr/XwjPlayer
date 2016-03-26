package com.hw.common.ui.dialog;

import com.hw.common.R;
import com.hw.common.utils.basicUtils.StringUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


public class MessageDialog extends BaseDialog{
	private TextView tv_dialog_message;
	
	public MessageDialog(Context context) {
		super(context,R.layout.dialog_message);
		this.hiddenMiddleBtn();
		tv_dialog_message = (TextView) getLayout().findViewById(R.id.tv_dialog_message);
	}

	public MessageDialog setMessage(String message) {
		if(StringUtils.isEmpty(message)){
			tv_dialog_message.setVisibility(View.GONE);
		}else{
			tv_dialog_message.setText(message);
		}
		return this;
	}
}
