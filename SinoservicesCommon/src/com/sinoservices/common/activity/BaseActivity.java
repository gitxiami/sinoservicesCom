package com.sinoservices.common.activity;

import java.util.ArrayList;

import com.sinoservices.common.App;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
/**
 * @ClassName: BaseActivity 
 * @Description: xmpp基础baseactivity页面
 * @date 2015年5月5日 下午9:30:32 
 *
 */
public class BaseActivity extends FragmentActivity {
	/**所有BackPressHandler事件监听者集合**/
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
	
    /**BackPressHandler内部接口**/
	public static abstract interface BackPressHandler {
		public abstract void activityOnResume();
		public abstract void activityOnPause();
	}
	
	/**
	 * 通用消息提示-带语音
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
