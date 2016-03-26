package com.hw.common.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.Toast;

public class DialogUtil {
	private static LoadingDialog progressDialog;
	private static long showTime = 0L;

	public static void showLoadingDialog(final Context context,String paramString) {
		showTime = System.currentTimeMillis();
		progressDialog = new LoadingDialog(context,paramString);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent) {
				if ((DialogUtil.progressDialog != null) && (System.currentTimeMillis() - DialogUtil.showTime >= 10000L)) {
					DialogUtil.progressDialog.dismiss();
					DialogUtil.showTime = 0L;
					Toast.makeText(context, "请求超时", Toast.LENGTH_SHORT).show();
				}
				return false;
			}

		});
		progressDialog.show();
	}

	public static void dismissLoadingDialog() {
		showTime = 0L;
		if(progressDialog != null){
			progressDialog.dismiss();
		} 
	}
}
