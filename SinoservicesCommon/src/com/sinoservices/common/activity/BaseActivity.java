package com.sinoservices.common.activity;

import java.util.ArrayList;

import com.sinoservices.common.App;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
/**
 * @ClassName: BaseActivity 
 * @Description: xmpp����baseactivityҳ��
 * @date 2015��5��5�� ����9:30:32 
 *
 */
public class BaseActivity extends FragmentActivity {
	/**����BackPressHandler�¼������߼���**/
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();
	@Override
	protected void onResume() {
		super.onResume();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnResume();
			}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}
	
    /**BackPressHandler�ڲ��ӿ�**/
	public static abstract interface BackPressHandler {
		public abstract void activityOnResume();
		public abstract void activityOnPause();
	}
	
	/**
	 * ͨ����Ϣ��ʾ-������
	 * 
	 * @param resId
	 */
	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		soundPlay(msg);

	}
	public void soundPlay(String msg) {
		App.getSpeaker().speak(msg);
	}
}
