package com.hw.common.utils.basicUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SystemUtils {
	
	// 获取设备号
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	// 安装应用
	 public static void install(Context context,String url) {  
	        Intent intent = new Intent(Intent.ACTION_VIEW);  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setDataAndType(Uri.fromFile(new File(url)),"application/vnd.android.package-archive");  
	        context.startActivity(intent);  
	 }  
	
	// 卸载应用
	public static void unInstallApp(Context context,String packageName){
		if(isAvilible(context,packageName)){
			try {
				Uri uri = Uri.parse("package:"+packageName);
				Intent intent = new Intent(Intent.ACTION_DELETE, uri);
				context.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}
	
	// 是否安装了该应用
	private static boolean isAvilible( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

	// 激活设备管理
	public static void setAdminDevice(Context paramContext, Class<?> cls) {
		ComponentName localComponentName = new ComponentName(paramContext, cls);
		Intent localIntent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
		localIntent.putExtra("android.app.extra.DEVICE_ADMIN", localComponentName);
		localIntent.putExtra("android.app.extra.ADD_EXPLANATION", "卸载保护");
		paramContext.startActivity(localIntent);
	}

	/**
	 * 1. getPhoneNumber方法返回当前手机的电话号码， 同时必须在androidmanifest.xml中 加入
	 * android.permission.READ_PHONE_STATE 这个权限， 2.
	 * 主流的获取用户手机号码一般采用用户主动发送短信到SP或接收手机来获取。
	 * 
	 * @param context
	 *            <a href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a>
	 */
	public static String getPhoneNumber(Context context) {
		String phoneNumber;
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNumber = mTelephonyMgr.getLine1Number();
			if (!StringUtils.isEmpty(phoneNumber) && !phoneNumber.contains("000000") && phoneNumber.length() > 12) {
				phoneNumber = phoneNumber.substring(phoneNumber.length() - 11, phoneNumber.length());
			} else {
				phoneNumber = "";
			}
		} catch (Exception e) {
			phoneNumber = "";
		}

		return phoneNumber;
	}
	
	// 获取通话记录
	public static List<callRecordEntity> getCallRecordPhone(Context context){
		List<callRecordEntity> mRecordList = new ArrayList<callRecordEntity>();  
		 ContentResolver contentResolver = context.getContentResolver();  
         Cursor cursor = null;  
         try {  
             cursor = contentResolver.query(  
                     CallLog.Calls.CONTENT_URI, null, null, null,  
                     CallLog.Calls.DATE + " desc");  
             if (cursor == null)  
                 return null;  
             
             while (cursor.moveToNext()) {  
            	 callRecordEntity record = new callRecordEntity();  
                 record.name = cursor.getString(cursor  
                         .getColumnIndex(CallLog.Calls.CACHED_NAME));  
                 record.number = cursor.getString(cursor  
                         .getColumnIndex(CallLog.Calls.NUMBER));  
                 record.callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                 
                 switch (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))) {                 
                 case Calls.INCOMING_TYPE:                                                                        
                	 record.type = "呼入";                                                                                 
                     break;                                                                                       
                 case Calls.OUTGOING_TYPE:                                                                        
                	 record.type  = "呼出";                                                                                 
                     break;                                                                                       
                 case Calls.MISSED_TYPE:                                                                          
                	 record.type  = "未接";                                                                                 
                     break;                                                                                       
                 default:                                                                                         
                	 record.type  = "挂断";//应该是挂断.根据我手机类型判断出的                                                              
                     break;                                                                                       
                 }      
                 
                 record.lDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))));  
                 record.duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))+"";  
                 mRecordList.add(record);  
             }  
         } catch (Exception e) {
        	 if (cursor != null) {  
                 cursor.close();  
             }  
 		}finally {  
             if (cursor != null) {  
                 cursor.close();  
             }  
         }
         return mRecordList;
	}
	
	public static Boolean deleteCallRecord(Context context,String number){
		ContentResolver contentResolver = context.getContentResolver();  
        Cursor cursor = null;  
        try {
        	
        	cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, new String[]{"_id"}, "number=?",  new String[]{number},  "_id desc limit 1");  
        	if(cursor.moveToFirst()) {  
        	    int id = cursor.getInt(0);  
        	    MLogUtil.e("id "+id);
        	    contentResolver.delete(CallLog.Calls.CONTENT_URI, "_id=?", new String[] {id + ""});  
        	} 
        	return true;
        } catch (Exception e) {
       	 if (cursor != null) {  
                cursor.close();  
            }  
		}finally {  
            if (cursor != null) {  
                cursor.close();  
            }  
        }
        
		return false;
	}
	
	public static class callRecordEntity {
		private String name; //联系人姓名
		private String number; // //号码
		private String type; // 呼叫类型  
		private int callType; // 呼叫类型  
		private String lDate; // 呼叫时间
		private String duration; // 通话时间
		
		public int getCallType() {
			return callType;
		}
		public void setCallType(int callType) {
			this.callType = callType;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getlDate() {
			return lDate;
		}
		public void setlDate(String lDate) {
			this.lDate = lDate;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		
	}
	
	// 获取手机短信内容
	public static List<SmsMsg> getSmsInPhone(Context context) {
		final String SMS_URI_ALL = "content://sms/"; 		//所有短信
		final String SMS_URI_INBOX = "content://sms/inbox";	//收信箱
		final String SMS_URI_SEND = "content://sms/sent";	//发信箱
		final String SMS_URI_DRAFT = "content://sms/draft";	//草稿箱
		List<SmsMsg> smsMsgList = new ArrayList<SmsMsg>();
//		StringBuilder smsBuilder = new StringBuilder();
		try {
			ContentResolver cr = context.getContentResolver();
			String[] projection = new String[] { "_id", "address", "person",
					"body", "date", "type" };
			Uri uri = Uri.parse(SMS_URI_ALL);
			Cursor cur = cr.query(uri, projection, null, null, "date desc");
			if (cur.moveToFirst()) {
				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;
				int idCloum = cur.getColumnIndex("_id");
				int nameColumn = cur.getColumnIndex("person");//姓名
				int phoneNumberColumn = cur.getColumnIndex("address");//手机号
				int smsbodyColumn = cur.getColumnIndex("body");//短信内容
				int dateColumn = cur.getColumnIndex("date");//日期
				int typeColumn = cur.getColumnIndex("type");//收发类型 1表示接受 2表示发送
				do {
					name = cur.getString(nameColumn);
					phoneNumber = cur.getString(phoneNumberColumn);
					smsbody = cur.getString(smsbodyColumn);
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
					date = dateFormat.format(d);
					int typeId = cur.getInt(typeColumn);
					if (typeId == 1) {
						type = "接收";
					} else if (typeId == 2) {
						type = "发送";
					} else {
						type = "";
					}
//					smsBuilder.append("[");
//					smsBuilder.append(name + ",");
//					smsBuilder.append(phoneNumber + ",");
//					smsBuilder.append(smsbody + ",");
//					smsBuilder.append(date + ",");
//					smsBuilder.append(type);
//					smsBuilder.append("] ");
					if (smsbody == null)
						smsbody = "";
					smsMsgList.add(new SmsMsg(idCloum+"", name, phoneNumber,getPeopleNameFromPerson(context,phoneNumber), smsbody, date, type));
				} while (cur.moveToNext());
			} else {
//				smsBuilder.append("没有记录!");
			}
//			smsBuilder.append("获取彩信完成!");
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}
		return smsMsgList;
	}
	
	public static class SmsMsg {
		private String id;
		private String name;
		private String address; // 发件人号码
		private String addressName; // 发件人关联通讯录姓名
		private String msg;
		private String date;
		private String type;
		
		public SmsMsg(String id,String name,String address,String addressName,String msg,String date,String type){
			this.id = id;
			this.name = name;
			this.address = address;
			this.addressName = addressName;
			this.msg = msg;
			this.date = date;
			this.type = type;
		}
		public SmsMsg(){}
	}
	
	// 通过address手机号关联Contacts联系人的显示名字  
    private static String getPeopleNameFromPerson(Context context,String address){  
        if(StringUtils.isEmpty(address)){  
            return "( no address )\n";  
        }  
          
        String strPerson = "";  
        try {
        	 String[] projection = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};  
             
             Uri uri_Person = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, address);  // address 手机号过滤  
             Cursor cursor = context.getContentResolver().query(uri_Person, projection, null, null, null);  
               
             if(cursor.moveToFirst()){  
                 int index_PeopleName = cursor.getColumnIndex(Phone.DISPLAY_NAME);  
                 String strPeopleName = cursor.getString(index_PeopleName);  
                 strPerson = strPeopleName;  
             }  
             cursor.close();  
		} catch (Exception e) {
		}
       
          
        return strPerson;  
    }  

	// 读取手机短信
	public static class SmsContent extends ContentObserver {
		public static final String SMS_URI_INBOX = "content://sms/inbox";
		private Activity activity = null;
		private String smsContent = "";
		private String addr = "";
		private EditText verifyText = null;

		public SmsContent(Activity activity, Handler handler, EditText verifyText, String addr) {
			super(handler);
			this.activity = activity;
			this.verifyText = verifyText;
			this.addr = addr;
		}

		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Cursor cursor = null;// 光标
			try {
				// 读取收件箱中指定号码的短信
				// cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new
				// String[] { "_id", "address", "body", "read" },
				// "address=? and read=?", new String[] { addr , "0" },
				// "date desc");
				cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] { "_id", "address", "body", "read" }, "read=?", new String[] { "0" }, "date desc");
				if (cursor != null) {// 如果短信为未读模式
					cursor.moveToFirst();
					if (cursor.moveToFirst()) {
						String smsbody = cursor.getString(cursor.getColumnIndex("body"));
						String regEx = "[^0-9]";
						Pattern p = Pattern.compile(regEx);
						Matcher m = p.matcher(smsbody.toString());
						smsContent = m.replaceAll("").trim().toString();
						if (!StringUtils.isEmpty(smsContent)) {
							smsContent = smsContent.substring(0, 4);
						}
						verifyText.setText(smsContent);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	// 删除短信
	public static boolean deleteSMS(Context context, String smscontent) {
		try {
			// 准备系统短信收信箱的uri地址
			Uri uri = Uri.parse("content://sms/inbox");// 收信箱
			// 查询收信箱里所有的短信
			Cursor isRead = context.getContentResolver().query(uri, null, "read=" + 0, null, null);
			if (isRead != null) {
				while (isRead.moveToNext()) {
					String body = isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
					if (body.equals(smscontent)) {
						int id = isRead.getInt(isRead.getColumnIndex("_id"));
						context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + id, null);
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取通讯录
	public static List<phonePerson> getContacts(Context context) {
		List<phonePerson> list = new ArrayList<phonePerson>();
		Cursor cursor = null;
		try {
			Uri uri = ContactsContract.Data.CONTENT_URI;// 2.0以上系统使用ContactsContract.Data访问联系人
			cursor = context.getContentResolver().query(uri, null, null, null, "display_name");// 显示联系人时按显示名字排序
			cursor.moveToFirst();

			int Index_CONTACT_ID = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);// 获得CONTACT_ID在ContactsContract.Data中的列数
			int Index_DATA1 = cursor.getColumnIndex(ContactsContract.Data.DATA1);// 获得DATA1在ContactsContract.Data中的列数
			int Index_MIMETYPE = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);// 获得MIMETYPE在ContactsContract.Data中的列数

			while (cursor.getCount() > cursor.getPosition()) {
				phonePerson person = null;
				String id = cursor.getString(Index_CONTACT_ID);// 获得CONTACT_ID列的内容
				String info = cursor.getString(Index_DATA1);// 获得DATA1列的内容
				String mimeType = cursor.getString(Index_MIMETYPE);// 获得MIMETYPE列的内容

				// 遍历查询当前行对应的联系人信息是否已添加到list中
				for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getId() != null) {
						if (list.get(n).getId().equals(id)) {
							person = list.get(n);
							break;
						}
					}
				}

				if (person == null) {
					person = new phonePerson();
					person.setId(id);
					list.add(person);
				}
				if (mimeType.equals("vnd.android.cursor.item/email_v2"))// 该行数据为邮箱
				{
					person.setEmail(info);
				} else if (mimeType.equals("vnd.android.cursor.item/postal-address_v2"))// 该行数据为地址
				{
					person.setAddr(info);
				} else if (mimeType.equals("vnd.android.cursor.item/phone_v2"))// 该行数据为电话号码
				{
					if (person.getPhone() != null) {
						person.setPhone(person.getPhone() + "|" + info);
					} else {
						person.setPhone(info);
					}

				} else if (mimeType.equals("vnd.android.cursor.item/name"))// 该行数据为名字
				{
					person.setName(info);
				}
				
				if(StringUtils.isEmpty(person.getAddr())) person.setAddr("");
				if(StringUtils.isEmpty(person.getPhone())) person.setPhone("");
				if(StringUtils.isEmpty(person.getEmail())) person.setEmail("");
				if(StringUtils.isEmpty(person.getName())) person.setName("");
				
				cursor.moveToNext();
			}
			return list;
		} catch (Exception e) {
			return list;
		}finally {  
            if (cursor != null) {  
            	cursor.close();  
            }  
        }

	}

	private static List<String> INVALID_IMEIs = new ArrayList<String>();
	static {
		INVALID_IMEIs.add("358673013795895");
		INVALID_IMEIs.add("004999010640000");
		INVALID_IMEIs.add("00000000000000");
		INVALID_IMEIs.add("000000000000000");
	}

	public static boolean isValidImei(String imei) {
		if (StringUtils.isEmpty(imei))
			return false;
		if (imei.length() < 10)
			return false;
		if (INVALID_IMEIs.contains(imei))
			return false;
		return true;
	}

	public static void showSoftInput(final Context context, final EditText et) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et, 0);
			}
		}, 998);
	}

	private static final String INVALID_ANDROIDID = "9774d56d682e549c";

	// 获取设备唯一码，imei+android+mac
	public static String getUdid(Context context) {
		if (context == null) {
			return "";
		}
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager == null) {
			return "";
		}
		String imei = telephonyManager.getDeviceId();

		if (isValidImei(imei)) {

		} else {
			if (imei == null)
				imei = "";
		}

		String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		if (!StringUtils.isEmpty(androidId) && !INVALID_ANDROIDID.equals(androidId.toLowerCase())) {

		} else {
			if (androidId == null)
				androidId = "";

		}

		String macAddress = getWifiMacAddress(context);
		if (!StringUtils.isEmpty(macAddress)) {
			String udid = MD5.Md5(macAddress + Build.MODEL + Build.MANUFACTURER + Build.ID + Build.DEVICE);

		} else {
			macAddress = "";
		}

		return MD5.Md5(imei + androidId + macAddress);
	}

	public static String getWifiMacAddress(final Context context) {
		try {
			WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifimanager.getConnectionInfo().getMacAddress();
			if (StringUtils.isEmpty(mac))
				return null;
			return mac;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getRelation(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowMgr = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowMgr.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return String.valueOf(width) + "*" + String.valueOf(height);
	}

	// 打印所有的 intent extra 数据
	public static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
		}
		return sb.toString();
	}

	// TODO 40% unknown
	public static String getNetworkTypeName(int type) {
		String name = "Unknown";
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			name = "GPRS";
			break;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			name = "EDGE";
			break;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			name = "CDMA";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			name = "EVDO_0";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			name = "EVDO_A";
			break;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			name = "HSDPA";
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			name = "HSPA";
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			name = "HSUPA";
			break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			name = "UMTS";
			break;
		default:
		}

		return name;
	}

	public static boolean is2gNetwork(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = tm.getNetworkType();
		if (type == TelephonyManager.NETWORK_TYPE_GPRS || type == TelephonyManager.NETWORK_TYPE_EDGE) {
			return true;
		}
		return false;
	}

	public static int getCurrentSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	public static void sendEmail(Context context, String chooserTitle, String mailAddress, String subject, String preContent) {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mailAddress });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		String content = "\n\n=====================\n";
		content += "Device Environment: \n----\n" + preContent;
		emailIntent.putExtra(Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
	}

	// some apps only show content, some apps show both subject and content
	public static Intent getAndroidShareIntent(CharSequence chooseTitle, CharSequence subject, CharSequence content) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		shareIntent.putExtra(Intent.EXTRA_TEXT, content);
		return Intent.createChooser(shareIntent, chooseTitle);
	}

	public static Intent getAndroidImageShareIntent(CharSequence chooseTitle, String pathfile) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		share.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathfile));
		return Intent.createChooser(share, chooseTitle);
	}

	public static String getImsi(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	public static boolean canNetworkUseful(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	public static String getNetworkTypeName(Context context) {
		int kind = 0;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			kind = tm.getNetworkType();
		} catch (Exception e) {

		}
		return String.valueOf(kind);
	}

	public static long getRamSize(Context context) {
		return 0;
	}

	public static long getRomSize(Context context) {
		return 0;
	}

	public static boolean hasExtSdcard(Context context) {
		String[] paths;
		StorageManager service = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
		try {
			Method mMethod = service.getClass().getMethod("getVolumePaths");
			paths = (String[]) mMethod.invoke(service);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isRunningForeground(Context context) {
		String packageName = context.getPackageName();
		String topActivityClassName = getTopActivityName(context);
		if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public static class phonePerson {
		private String id;
		private String name;
		private String phone;
		private String email;
		private String addr;

		public phonePerson() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getAddr() {
			return addr;
		}

		public void setAddr(String addr) {
			this.addr = addr;
		}

	}

}
