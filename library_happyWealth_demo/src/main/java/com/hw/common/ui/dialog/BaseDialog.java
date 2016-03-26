package com.hw.common.ui.dialog;

import com.hw.common.R;
import com.hw.common.utils.basicUtils.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author liweicai
 */
public class BaseDialog extends Dialog {
	private View dialog_content;
//	private View view_sp_left,view_sp_right;
	private TextView tv_dialog_title;
	public Button btn_left, btn_right, btn_middle;
	public LinearLayout btn_bottom,dialog_title;
	protected Window window = null;

	public BaseDialog(Context context, int r) {
		super(context, R.style.Translucent_NoTitle);
		LayoutInflater inflater = LayoutInflater.from(context);
		View base = inflater.inflate(R.layout.dialog_base, null);
		setContentView(base);

		RelativeLayout content = (RelativeLayout) base.findViewById(R.id.dialog_content);

		dialog_content = inflater.inflate(r, null);
		content.addView(dialog_content, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		btn_left = (Button) base.findViewById(R.id.b_left);
		btn_right = (Button) base.findViewById(R.id.b_right);
		btn_middle = (Button) base.findViewById(R.id.b_middle);
//		view_sp_left = (View) base.findViewById(R.id.view_sp_left);
//		view_sp_right = (View) base.findViewById(R.id.view_sp_right);
		tv_dialog_title = (TextView) base.findViewById(R.id.tv_dialog_title);
		btn_bottom = (LinearLayout) base.findViewById(R.id.dialog_bottom);
		dialog_title = (LinearLayout) base.findViewById(R.id.dialog_title);
		btn_left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	protected void setAnim(){
		window = getWindow(); //得到对话框  
        window.setWindowAnimations(R.style.dialogFadeAnim); //设置窗口弹出动画  
        WindowManager.LayoutParams wl = window.getAttributes();  
        wl.x = 0; //x小于0左移，大于0右移  
        wl.y = 0; //y小于0上移，大于0下移    
//        wl.alpha = 0.6f; //设置透明度  
//        wl.gravity = Gravity.BOTTOM; //设置重力  
        window.setAttributes(wl); 
	}

	public BaseDialog setLeftBtn(String t, android.view.View.OnClickListener onClickListener) {
		btn_left.setText(t);
		if (onClickListener != null) {
			btn_left.setOnClickListener(onClickListener);
		}
		return this;
	}

	public BaseDialog setMiddleBtn(String t, android.view.View.OnClickListener onClickListener) {
		btn_middle.setText(t);
//		view_sp_left.setVisibility(View.VISIBLE);
		if (onClickListener != null) {
			btn_middle.setOnClickListener(onClickListener);
		}
		return this;
	}

	public BaseDialog setRightBtn(String t, android.view.View.OnClickListener onClickListener) {
		btn_right.setText(t);
//		view_sp_right.setVisibility(View.VISIBLE);
		if (onClickListener != null) {
			btn_right.setOnClickListener(onClickListener);
		}
		return this;
	}
	
	public void setTitle(String str){
		if(StringUtils.isEmpty(str)){
			dialog_title.setVisibility(View.GONE);
		}else{
			dialog_title.setVisibility(View.VISIBLE);
			tv_dialog_title.setText(str);
		}
	}
	
	public void hiddenAllBtn() {
		btn_bottom.setVisibility(View.GONE);
	}

	public void hiddenLeftBtn() {
		btn_left.setVisibility(View.GONE);
//		view_sp_left.setVisibility(View.GONE);
	}

	public void hiddenLRightBtn() {
		btn_right.setVisibility(View.GONE);
//		view_sp_right.setVisibility(View.GONE);
	}

	public void hiddenMiddleBtn() {
		btn_middle.setVisibility(View.GONE);
//		view_sp_right.setVisibility(View.GONE);
	}

	public View getLayout() {
		return dialog_content;
	}
}
