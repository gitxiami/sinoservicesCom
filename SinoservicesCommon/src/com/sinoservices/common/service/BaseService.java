package com.sinoservices.common.service;

import java.util.HashMap;
import java.util.Map;
import com.sinoservices.common.R;
import com.sinoservices.common.activity.ChatActivity;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
/**
 * @ClassName: BaseService 
 * @Description: 基类Service服务类-封装了通知栏提示等消息
 * @date 2015年5月5日 上午9:37:00 
 */
public abstract class BaseService extends Service {
    /**TAG标签**/
	private static final String TAG = "BaseService";
	private static final String APP_NAME = "xx";
	/**最大提示信息长度**/
	private static final int MAX_TICKER_MSG_LEN = 50;
	protected static int SERVICE_NOTIFICATION = 1;
    
	/**NotificationManager管理类**/
	private NotificationManager mNotificationManager;
	/**Notification类**/
	private Notification mNotification;
	/**通知意图**/
	private Intent mNotificationIntent;
	/**震动器类**/
	private Vibrator mVibrator;
	/**WakeLock锁**/
	protected WakeLock mWakeLock;
    /**用来保存对应的发送人的Notification的信息数量**/
	private Map<String, Integer> mNotificationCount = new HashMap<String, Integer>(
			2);
	
	private Map<String, Integer> mNotificationId = new HashMap<String, Integer>(
			2);
	private int mLastNotificationId = 2;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);
		addNotificationMGR();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
    
	private void addNotificationMGR() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//是跳到聊天页面的Intent的
		mNotificationIntent = new Intent(this, ChatActivity.class);
	}
    /**提示客户端**/
	protected void notifyClient(String fromJid, String fromUserName,
			String message, boolean showNotification) {
		if (!showNotification) {
			return;
		}
		mWakeLock.acquire();
		setNotification(fromJid, fromUserName, message);
		setLEDNotification();
		int notifyId = 0;
		if (mNotificationId.containsKey(fromJid)) {
			notifyId = mNotificationId.get(fromJid);
		} else {
			mLastNotificationId++;
			notifyId = mLastNotificationId;
			mNotificationId.put(fromJid, Integer.valueOf(notifyId));
		}
		boolean vibraNotify = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.VIBRATIONNOTIFY, true);
		if (vibraNotify) {
			//判断是否有震动提示
			mVibrator.vibrate(400);
		}
		//调用管理类启动notification
		mNotificationManager.notify(notifyId, mNotification);
        //释放锁
		mWakeLock.release();
	}
	
    /**包装准备提示内容信息**/
	private void setNotification(String fromJid, String fromUserId,
			String message) {
		int mNotificationCounter = 0;
		if (mNotificationCount.containsKey(fromJid)) {
			mNotificationCounter = mNotificationCount.get(fromJid);
		}
		mNotificationCounter++;
		mNotificationCount.put(fromJid, mNotificationCounter);
		String author;
		if (null == fromUserId || fromUserId.length() == 0) {
			author = fromJid;
		} else {
			author = fromUserId;
		}
		String title = author;
		String ticker;
		boolean isTicker = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.TICKER, true);
		if (isTicker) {
			int newline = message.indexOf('\n');
			int limit = 0;
			String messageSummary = message;
			if (newline >= 0)
				limit = newline;
			if (limit > MAX_TICKER_MSG_LEN
					|| message.length() > MAX_TICKER_MSG_LEN)
				limit = MAX_TICKER_MSG_LEN;
			if (limit > 0)
				messageSummary = message.substring(0, limit) + " [...]";
			ticker = title + ":\n" + messageSummary;
		} else
			ticker = author;
		mNotification = new Notification(R.drawable.notify_newmessage, ticker,
				System.currentTimeMillis());
		Uri userNameUri = Uri.parse(fromJid);
		mNotificationIntent.setData(userNameUri);
		mNotificationIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME,
				fromUserId);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// need to set flag FLAG_UPDATE_CURRENT to get extras transferred
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mNotification.setLatestEventInfo(this, title, message, pendingIntent);
		if (mNotificationCounter > 1)
			mNotification.number = mNotificationCounter;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	private void setLEDNotification() {
		//获取配置是否显示led提示
		boolean isLEDNotify = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.LEDNOTIFY, true);
		if (isLEDNotify) {
			//如果有led提示，则设置相应参数
			mNotification.ledARGB = Color.MAGENTA;
			mNotification.ledOnMS = 300;
			mNotification.ledOffMS = 1000;
			mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
	}

	public void resetNotificationCounter(String userJid) {
		mNotificationCount.remove(userJid);
	}
	
    /**清除对应jid通知栏消息**/
	public void clearNotification(String Jid) {
		int notifyId = 0;
		if (mNotificationId.containsKey(Jid)) {
			notifyId = mNotificationId.get(Jid);
			mNotificationManager.cancel(notifyId);
		}
	}
}
