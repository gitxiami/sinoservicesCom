package com.sinoservices.common.ring.global;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	

	public static String getFormatDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
		return sdf.format(new Date());
	}
	
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;

	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	
	/**
	 * �����Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isStorageUseful() {
		// ��ȡSdCard״̬
		String state = android.os.Environment.getExternalStorageState();
		// �ж�SdCard�Ƿ���ڲ����ǿ��õ�
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ȡָ�����ȵ�����ַ��������ּӴ�Сд��ĸ��
	 * 
	 * @param length
	 *            ����ַ�������
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * ��ȡ�豸ID
	 * 
	 * ���Ȼ�ȡwif��mac û��macȡ�豸id �����û�����ȡ���8λ�����洢�����ع��´ε���
	 * 
	 * @param context
	 * @return
	 */
	public static final String getDeviceId(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String macAddress = wifiInfo.getMacAddress();
		if (macAddress != null) {
			return macAddress.replaceAll(":", "");
		}

		String deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		if (deviceId != null) {
			return deviceId;
		}

		SharedPreferences prefs = context.getSharedPreferences("DeviceInfo",
				Context.MODE_PRIVATE);
		deviceId = prefs.getString("deviceId", null);
		if (deviceId != null) {
			return deviceId;
		}

		deviceId = getRandomString(8);
		Editor edit = prefs.edit();
		edit.putString("deviceId", deviceId);
		edit.commit();
		return deviceId;
	}

	/**
	 * ��ȡ�豸��Ӫ��
	 * 
	 * 0:�й��ƶ�,1:�й���ͨ,2:�й�����,3:δ֪
	 * 
	 * @param context
	 * @return
	 */
	public static int getDeviceOperator(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002")
					|| operator.equals("46007")) {
				return 0;
			} else if (operator.equals("46001")) {
				return 1;
			} else if (operator.equals("46003")) {
				return 2;
			}

		}
		return 3;
	}
}
