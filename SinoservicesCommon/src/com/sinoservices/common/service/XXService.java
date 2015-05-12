package com.sinoservices.common.service;

import java.util.HashSet;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import com.sinoservices.common.MainActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.XXBroadcastReceiver;
import com.sinoservices.common.XXBroadcastReceiver.EventHandler;
import com.sinoservices.common.activity.BaseActivity;
import com.sinoservices.common.activity.BaseActivity.BackPressHandler;
import com.sinoservices.common.activity.LoginActivity;
import com.sinoservices.common.exception.XXException;
import com.sinoservices.common.smack.SmackImpl;
import com.sinoservices.common.util.L;
import com.sinoservices.common.util.NetUtil;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import com.sinoservices.common.util.T;

public class XXService extends BaseService implements EventHandler,
		BackPressHandler {
	/**已经连接CONNECTED**/
	public static final int CONNECTED = 0;
	/**没有连接DISCONNECTED**/
	public static final int DISCONNECTED = -1;
	/**连接中CONNECTING**/
	public static final int CONNECTING = 1;
	/** 连接超时**/
	public static final String PONG_TIMEOUT = "pong timeout";
	/**网络错误**/
	public static final String NETWORK_ERROR = "network error";
	/**手动退出**/
	public static final String LOGOUT = "logout";
	/**登录失败**/
	public static final String LOGIN_FAILED = "login failed";
	/**没有警告的断开连接**/
	public static final String DISCONNECTED_WITHOUT_WARNING = "disconnected without warning";
    /**mBinder**/
	private IBinder mBinder = new XXBinder();
	/**连接状态改变回调接口**/
	private IConnectionStatusCallback mConnectionStatusCallback;
	/**xmpp实际api操作类**/
	private SmackImpl mSmackable;
	/**连接线程**/
	private Thread mConnectingThread;
	private Handler mMainHandler = new Handler();
	/**是否第一次登录**/
	private boolean mIsFirstLoginAction;
	/** 自动重连 start**/
	private static final int RECONNECT_AFTER = 5;
	/** 最大重连时间间隔**/
	private static final int RECONNECT_MAXIMUM = 10 * 60;
	/**重连ALARM**/
	private static final String RECONNECT_ALARM = "com.way.xx.RECONNECT_ALARM";
	// private boolean mIsNeedReConnection = false; // 是否需要重连
	/** 连接状态**/
	private int mConnectedState = DISCONNECTED; 
	/**重连时间**/
	private int mReconnectTimeout = RECONNECT_AFTER;
	/**重连提醒**/
	private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
	/**重连PendingIntent**/
	private PendingIntent mPAlarmIntent;
	/**自动重连广播**/
	private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();
    /**Activity管理类**/
	private ActivityManager mActivityManager;
	/**包名**/
	private String mPackageName;
	
	private HashSet<String> mIsBoundTo = new HashSet<String>();

	/**
	 * 注册界面和聊天界面时连接状态变化回调
	 * 
	 * @param cb
	 */
	public void registerConnectionStatusCallback(IConnectionStatusCallback cb) {
		mConnectionStatusCallback = cb;
	}
    /**销毁回调**/
	public void unRegisterConnectionStatusCallback() {
		mConnectionStatusCallback = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		L.i(XXService.class, "[SERVICE] onBind");
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
				&& TextUtils.equals(action, LoginActivity.LOGIN_ACTION)) {
			//第一次登录
			mIsFirstLoginAction = true;
		} else {
			//不是第一次登录
			mIsFirstLoginAction = false;
		}
		//返回mBinder给绑定该服务者
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
				&& TextUtils.equals(action, LoginActivity.LOGIN_ACTION)) {
			//第一次登录
			mIsFirstLoginAction = true;
		} else {
			//不是第一次登录
			mIsFirstLoginAction = false;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		//解除绑定
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.remove(chatPartner);
		}
		return true;
	}

	public class XXBinder extends Binder {
		public XXService getService() {
			return XXService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//把本服务加入到广播状态事件监听回调中
		XXBroadcastReceiver.mListeners.add(this);
		//把本服务加入到baseactivity的resume和Pause的事件监听回调中
		BaseActivity.mListeners.add(this);
		
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		mPackageName = getPackageName();
		mPAlarmIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		//注册重连广播
		registerReceiver(mAlarmReceiver, new IntentFilter(RECONNECT_ALARM));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null
				&& intent.getAction() != null
				&& TextUtils.equals(intent.getAction(),
						XXBroadcastReceiver.BOOT_COMPLETED_ACTION)) {
			//开机广播
			String account = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.ACCOUNT, "");
			String password = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.PASSWORD, "");
			if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password))
				//执行登录操作
				Login(account, password);
		}
		mMainHandler.removeCallbacks(monitorStatus);
		//检查应用是否在后台运行线程
		mMainHandler.postDelayed(monitorStatus, 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		XXBroadcastReceiver.mListeners.remove(this);
		BaseActivity.mListeners.remove(this);
		((AlarmManager) getSystemService(Context.ALARM_SERVICE))
				.cancel(mPAlarmIntent);// 取消重连闹钟
		unregisterReceiver(mAlarmReceiver);// 注销广播监听
		logout();
	}

	/**登录方法**/
	public void Login(final String account, final String password) {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			//无网络状态-UI线程反馈连接失败
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (mConnectingThread != null) {
			//已经有连接线程正在加载中了
			L.i("a connection is still goign on!");
			return;
		}
		//启动连接线程
		mConnectingThread = new Thread() {
			@Override
			public void run() {
				try {
					postConnecting();
					mSmackable = new SmackImpl(XXService.this);
					if (mSmackable.login(account, password)) {
						// 登陆成功
						postConnectionScuessed();
					} else {
						// 登陆失败
						postConnectionFailed(LOGIN_FAILED);
					}
				} catch (XXException e) {
					String message = e.getLocalizedMessage();
					// 登陆失败
					if (e.getCause() != null)
						message += "\n" + e.getCause().getLocalizedMessage();
					postConnectionFailed(message);
					L.i(XXService.class, "YaximXMPPException in doConnect():");
					e.printStackTrace();
				} finally {
					if (mConnectingThread != null)
						synchronized (mConnectingThread) {
							mConnectingThread = null;
						}
				}
			}

		};
		mConnectingThread.start();
	}

	// 退出
	public boolean logout() {
		// mIsNeedReConnection = false;// 手动退出就不需要重连闹钟了
		boolean isLogout = false;
		if (mConnectingThread != null) {
			synchronized (mConnectingThread) {
				try {
					mConnectingThread.interrupt();
					mConnectingThread.join(50);
				} catch (InterruptedException e) {
					L.e("doDisconnect: failed catching connecting thread");
				} finally {
					mConnectingThread = null;
				}
			}
		}
		if (mSmackable != null) {
			isLogout = mSmackable.logout();
			mSmackable = null;
		}
		connectionFailed(LOGOUT);// 手动退出
		return isLogout;
	}

	// 发送消息
	public void sendMessage(String user, String message) {
		if (mSmackable != null)
			mSmackable.sendMessage(user, message);
		else
			SmackImpl.sendOfflineMessage(getContentResolver(), user, message);
	}

	// 是否连接上服务器
	public boolean isAuthenticated() {
		if (mSmackable != null) {
			return mSmackable.isAuthenticated();
		}
		return false;
	}

	// 清除通知栏-调用BaseService底层方法
	public void clearNotifications(String Jid) {
		clearNotification(Jid);
	}

	/**
	 * 非UI线程连接失败反馈
	 * 
	 * @param reason
	 */
	public void postConnectionFailed(final String reason) {
		mMainHandler.post(new Runnable() {
			public void run() {
				connectionFailed(reason);
			}
		});
	}

	// 设置连接状态
	public void setStatusFromConfig() {
		mSmackable.setStatusFromConfig();
	}

	// 新增联系人
	public void addRosterItem(String user, String alias, String group) {
		try {
			mSmackable.addRosterItem(user, alias, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in addRosterItem(): " + e.getMessage());
		}
	}

	// 新增分组
	public void addRosterGroup(String group) {
		mSmackable.addRosterGroup(group);
	}

	// 删除联系人
	public void removeRosterItem(String user) {
		try {
			mSmackable.removeRosterItem(user);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in removeRosterItem(): " + e.getMessage());
		}
	}

	// 将联系人移动到其他组
	public void moveRosterItemToGroup(String user, String group) {
		try {
			mSmackable.moveRosterItemToGroup(user, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in moveRosterItemToGroup(): " + e.getMessage());
		}
	}

	// 重命名联系人
	public void renameRosterItem(String user, String newName) {
		try {
			mSmackable.renameRosterItem(user, newName);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in renameRosterItem(): " + e.getMessage());
		}
	}

	// 重命名组
	public void renameRosterGroup(String group, String newGroup) {
		mSmackable.renameRosterGroup(group, newGroup);
	}

	/**
	 * UI线程反馈连接失败
	 * 
	 * @param reason
	 */
	private void connectionFailed(String reason) {
		L.i(XXService.class, "connectionFailed: " + reason);
		mConnectedState = DISCONNECTED;// 更新当前连接状态
		if (TextUtils.equals(reason, LOGOUT)) {// 如果是手动退出
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}
		// 回调
		if (mConnectionStatusCallback != null) {
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					reason);
			if (mIsFirstLoginAction)// 如果是第一次登录,就算登录失败也不需要继续
				return;
		}

		// 无网络连接时,直接返回
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}

		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		// 无保存的帐号密码时，也直接返回
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
			L.d("account = null || password = null");
			return;
		}
		// 如果不是手动退出并且需要重新连接，则开启重连闹钟
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.AUTO_RECONNECT, true)) {
			L.d("connectionFailed(): registering reconnect in "
					+ mReconnectTimeout + "s");
			((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(
					AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
							+ mReconnectTimeout * 1000, mPAlarmIntent);
			mReconnectTimeout = mReconnectTimeout * 2;
			if (mReconnectTimeout > RECONNECT_MAXIMUM)
				mReconnectTimeout = RECONNECT_MAXIMUM;
		} else {
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
		}

	}

	private void postConnectionScuessed() {
		mMainHandler.post(new Runnable() {
			public void run() {
				connectionScuessed();
			}

		});
	}

	private void connectionScuessed() {
		mConnectedState = CONNECTED;// 已经连接上
		mReconnectTimeout = RECONNECT_AFTER;// 重置重连的时间

		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	/**连接中，通知界面线程做一些处理**/
	private void postConnecting() {
		mMainHandler.post(new Runnable() {
			public void run() {
				connecting();
			}
		});
	}

	private void connecting() {
		mConnectedState = CONNECTING;// 连接中
		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	/**收到新消息**/
	public void newMessage(final String from, final String message) {
		mMainHandler.post(new Runnable() {
			public void run() {
				if (!PreferenceUtils.getPrefBoolean(XXService.this,
						PreferenceConstants.SCLIENTNOTIFY, false))
					//播放提醒声音
					MediaPlayer.create(XXService.this, R.raw.office).start();
				if (!isAppOnForeground())
					//如果app不在前台，则显示通知栏提示有新消息到来
					notifyClient(from, mSmackable.getNameForJID(from), message,
							!mIsBoundTo.contains(from));
			}
		});
	}

	/**联系人改变**/
	public void rosterChanged() {
		if (mSmackable == null)
			return;
		if (mSmackable != null && !mSmackable.isAuthenticated()) {
			L.i("rosterChanged(): disconnected without warning");
			connectionFailed(DISCONNECTED_WITHOUT_WARNING);
		}
	}

	/**
	 * 更新通知栏信息
	 * 
	 * @param message
	 */
	public void updateServiceNotification(String message) {
		if (!PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.FOREGROUND, true))
			return;
		String title = PreferenceUtils.getPrefString(this,
				PreferenceConstants.ACCOUNT, "");
		Notification n = new Notification(R.drawable.contact_item_head_icon,
				title, System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		n.contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(this, title, message, n.contentIntent);
		startForeground(SERVICE_NOTIFICATION, n);
	}

	/**判断程序是否在后台运行的任务**/
	Runnable monitorStatus = new Runnable() {
		public void run() {
			try {
				L.i("monitorStatus is running... " + mPackageName);
				mMainHandler.removeCallbacks(monitorStatus);
				// 如果在后台运行并且连接上了
				if (!isAppOnForeground()) {
					L.i("app run in background...");
					// if (isAuthenticated())
					updateServiceNotification(getString(R.string.run_bg_ticker));
					return;
				} else {
					stopForeground(true);
				}
				// mMainHandler.postDelayed(monitorStatus, 1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
    
	/**判断服务是否在前台运行**/
	public boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

	/**自动重连广播**/
	private class ReconnectAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
			L.d("Alarm received.");
			if (!PreferenceUtils.getPrefBoolean(XXService.this,
					PreferenceConstants.AUTO_RECONNECT, true)) {
				return;
			}
			if (mConnectedState != DISCONNECTED) {
				L.d("Reconnect attempt aborted: we are connected again!");
				return;
			}
			String account = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.ACCOUNT, "");
			String password = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.PASSWORD, "");
			if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
				L.d("account = null || password = null");
				return;
			}
			//执行登录操作
			Login(account, password);
		}
	}
    /**网络连接状态结果回调**/
	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			// 如果是网络断开，不作处理
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (isAuthenticated())// 如果已经连接上，直接返回
			return;
		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password))// 如果没有帐号，也直接返回
			return;
		if (!PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.AUTO_RECONNECT, true))// 不需要重连
			return;
		Login(account, password);// 重连
	}

	@Override
	public void activityOnResume() {
		L.i("activity onResume ...");
		mMainHandler.post(monitorStatus);
	}

	@Override
	public void activityOnPause() {
		L.i("activity onPause ...");
		mMainHandler.postDelayed(monitorStatus, 1000L);
	}
}
