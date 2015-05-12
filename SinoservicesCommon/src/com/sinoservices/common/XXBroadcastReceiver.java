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
/**�㲥��������Ҫ���ڽ�����������״̬�Լ����ػ��㲥**/
public class XXBroadcastReceiver extends BroadcastReceiver {
	public static final String BOOT_COMPLETED_ACTION = "com.way.action.BOOT_COMPLETED";
	/**�¼��ӿڼ����߼���**/
	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		//����״̬�ı䣬֪ͨ���нӿ�
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (mListeners.size() > 0)
				for (EventHandler handler : mListeners) {
					handler.onNetChange();
				}
		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			//ϵͳ�ص�
			Intent xmppServiceIntent = new Intent(context, XXService.class);
			//ֹͣ����
			context.stopService(xmppServiceIntent);
		} else {
			if (!TextUtils.isEmpty(PreferenceUtils.getPrefString(context,
					PreferenceConstants.PASSWORD, ""))
					&& PreferenceUtils.getPrefBoolean(context,
							PreferenceConstants.AUTO_START, true)) {
				//���벻Ϊ�ղ������Զ���¼
				Intent i = new Intent(context, XXService.class);
				i.setAction(BOOT_COMPLETED_ACTION);
				context.startService(i);
			}
		}
	}
    /**EventHandler�ڲ��ӿ�**/
	public static abstract interface EventHandler {
		/**����״̬�ı�**/
		public abstract void onNetChange();
	}
}
