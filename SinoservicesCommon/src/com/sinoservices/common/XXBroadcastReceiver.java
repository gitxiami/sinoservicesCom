package com.sinoservices.common;

import java.util.ArrayList;
import com.sinoservices.common.service.XXService;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
/**广播接收器主要用于接收网络连接状态以及开关机广播**/
public class XXBroadcastReceiver extends BroadcastReceiver {
	public static final String BOOT_COMPLETED_ACTION = "com.way.action.BOOT_COMPLETED";
	/**事件接口监听者集合**/
	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		//网络状态改变，通知所有接口
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (mListeners.size() > 0)
				for (EventHandler handler : mListeners) {
					handler.onNetChange();
				}
		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			//系统关掉
			Intent xmppServiceIntent = new Intent(context, XXService.class);
			//停止服务
			context.stopService(xmppServiceIntent);
		} else {
			if (!TextUtils.isEmpty(PreferenceUtils.getPrefString(context,
					PreferenceConstants.PASSWORD, ""))
					&& PreferenceUtils.getPrefBoolean(context,
							PreferenceConstants.AUTO_START, true)) {
				//密码不为空并且是自动登录
				Intent i = new Intent(context, XXService.class);
				i.setAction(BOOT_COMPLETED_ACTION);
				context.startService(i);
			}
		}
	}
    /**EventHandler内部接口**/
	public static abstract interface EventHandler {
		/**网络状态改变**/
		public abstract void onNetChange();
	}
}
