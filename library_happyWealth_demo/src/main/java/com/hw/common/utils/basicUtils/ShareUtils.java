package com.hw.common.utils.basicUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

public class ShareUtils {
    // 使用方法 ShareUtils.shareAll(mContext,"我在用钱吧App哦~分享你也一起用吧！http://zjqsn.com/app/ 快去下载吧！",Uri.fromFile(new File("/mnt/sdcard/.com.newgame.sdk/471842328.png")));
	public static void shareAll(Activity activity, String share_content, String imagePath){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("image/*");
		
		sendIntent.putExtra("Kdescription", share_content);
		sendIntent.putExtra("sms_body", share_content);
		sendIntent.putExtra(Intent.EXTRA_TEXT, share_content);
		sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		activity.startActivity(Intent.createChooser(sendIntent, "分享"));
	}
	
	
	/**
	 * 分享
	 */
	public static void initShareIntent(Activity activity, String share_content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		List<ResolveInfo> resInfo = activity.getPackageManager()
				.queryIntentActivities(intent, 0);
		if (!resInfo.isEmpty()) {
			try {
				List<Intent> targetedShareIntents = new ArrayList<Intent>();
				for (ResolveInfo info : resInfo) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("image/*"); 
					
					ActivityInfo activityInfo = info.activityInfo;
					if (activityInfo.packageName.equals("com.tencent.WBlog")
							|| activityInfo.packageName.equals("com.xiaomi.channel")|| activityInfo.packageName.equals("im.yixin") || activityInfo.packageName.equals("com.sina.weibo")
							|| activityInfo.packageName.equals("cn.com.fetion") || activityInfo.packageName.equals("com.tencent.mm")|| activityInfo.packageName.equals("com.tencent.mobileqq")|| activityInfo.packageName.equals("com.qzone")
							) {
						sendIntent.putExtra("Kdescription", share_content);
						sendIntent.putExtra("sms_body", share_content);
						sendIntent.putExtra(Intent.EXTRA_TEXT, share_content);
						sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/mnt/sdcard/.com.newgame.sdk/471842328.png")));
						sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
						
						sendIntent.setPackage(activityInfo.packageName);
						targetedShareIntents.add(sendIntent);
					}
				}
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "选择分享方式");
				if (chooserIntent == null) {
					return;
				}
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				activity.startActivity(chooserIntent);
			} catch (Exception ex) {
				Toast.makeText(activity, "未能找到分享应用", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(activity, "未能找到分享应用", Toast.LENGTH_LONG).show();
		}
	}

}
