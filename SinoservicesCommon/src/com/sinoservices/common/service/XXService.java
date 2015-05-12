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
	/**�Ѿ�����CONNECTED**/
	public static final int CONNECTED = 0;
	/**û������DISCONNECTED**/
	public static final int DISCONNECTED = -1;
	/**������CONNECTING**/
	public static final int CONNECTING = 1;
	/** ���ӳ�ʱ**/
	public static final String PONG_TIMEOUT = "pong timeout";
	/**�������**/
	public static final String NETWORK_ERROR = "network error";
	/**�ֶ��˳�**/
	public static final String LOGOUT = "logout";
	/**��¼ʧ��**/
	public static final String LOGIN_FAILED = "login failed";
	/**û�о���ĶϿ�����**/
	public static final String DISCONNECTED_WITHOUT_WARNING = "disconnected without warning";
    /**mBinder**/
	private IBinder mBinder = new XXBinder();
	/**����״̬�ı�ص��ӿ�**/
	private IConnectionStatusCallback mConnectionStatusCallback;
	/**xmppʵ��api������**/
	private SmackImpl mSmackable;
	/**�����߳�**/
	private Thread mConnectingThread;
	private Handler mMainHandler = new Handler();
	/**�Ƿ��һ�ε�¼**/
	private boolean mIsFirstLoginAction;
	/** �Զ����� start**/
	private static final int RECONNECT_AFTER = 5;
	/** �������ʱ����**/
	private static final int RECONNECT_MAXIMUM = 10 * 60;
	/**����ALARM**/
	private static final String RECONNECT_ALARM = "com.way.xx.RECONNECT_ALARM";
	// private boolean mIsNeedReConnection = false; // �Ƿ���Ҫ����
	/** ����״̬**/
	private int mConnectedState = DISCONNECTED; 
	/**����ʱ��**/
	private int mReconnectTimeout = RECONNECT_AFTER;
	/**��������**/
	private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
	/**����PendingIntent**/
	private PendingIntent mPAlarmIntent;
	/**�Զ������㲥**/
	private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();
    /**Activity������**/
	private ActivityManager mActivityManager;
	/**����**/
	private String mPackageName;
	
	private HashSet<String> mIsBoundTo = new HashSet<String>();

	/**
	 * ע�������������ʱ����״̬�仯�ص�
	 * 
	 * @param cb
	 */
	public void registerConnectionStatusCallback(IConnectionStatusCallback cb) {
		mConnectionStatusCallback = cb;
	}
    /**���ٻص�**/
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
			//��һ�ε�¼
			mIsFirstLoginAction = true;
		} else {
			//���ǵ�һ�ε�¼
			mIsFirstLoginAction = false;
		}
		//����mBinder���󶨸÷�����
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
			//��һ�ε�¼
			mIsFirstLoginAction = true;
		} else {
			//���ǵ�һ�ε�¼
			mIsFirstLoginAction = false;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		//�����
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
		//�ѱ�������뵽�㲥״̬�¼������ص���
		XXBroadcastReceiver.mListeners.add(this);
		//�ѱ�������뵽baseactivity��resume��Pause���¼������ص���
		BaseActivity.mListeners.add(this);
		
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		mPackageName = getPackageName();
		mPAlarmIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		//ע�������㲥
		registerReceiver(mAlarmReceiver, new IntentFilter(RECONNECT_ALARM));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null
				&& intent.getAction() != null
				&& TextUtils.equals(intent.getAction(),
						XXBroadcastReceiver.BOOT_COMPLETED_ACTION)) {
			//�����㲥
			String account = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.ACCOUNT, "");
			String password = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.PASSWORD, "");
			if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password))
				//ִ�е�¼����
				Login(account, password);
		}
		mMainHandler.removeCallbacks(monitorStatus);
		//���Ӧ���Ƿ��ں�̨�����߳�
		mMainHandler.postDelayed(monitorStatus, 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		XXBroadcastReceiver.mListeners.remove(this);
		BaseActivity.mListeners.remove(this);
		((AlarmManager) getSystemService(Context.ALARM_SERVICE))
				.cancel(mPAlarmIntent);// ȡ����������
		unregisterReceiver(mAlarmReceiver);// ע���㲥����
		logout();
	}

	/**��¼����**/
	public void Login(final String account, final String password) {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			//������״̬-UI�̷߳�������ʧ��
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (mConnectingThread != null) {
			//�Ѿ��������߳����ڼ�������
			L.i("a connection is still goign on!");
			return;
		}
		//���������߳�
		mConnectingThread = new Thread() {
			@Override
			public void run() {
				try {
					postConnecting();
					mSmackable = new SmackImpl(XXService.this);
					if (mSmackable.login(account, password)) {
						// ��½�ɹ�
						postConnectionScuessed();
					} else {
						// ��½ʧ��
						postConnectionFailed(LOGIN_FAILED);
					}
				} catch (XXException e) {
					String message = e.getLocalizedMessage();
					// ��½ʧ��
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

	// �˳�
	public boolean logout() {
		// mIsNeedReConnection = false;// �ֶ��˳��Ͳ���Ҫ����������
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
		connectionFailed(LOGOUT);// �ֶ��˳�
		return isLogout;
	}

	// ������Ϣ
	public void sendMessage(String user, String message) {
		if (mSmackable != null)
			mSmackable.sendMessage(user, message);
		else
			SmackImpl.sendOfflineMessage(getContentResolver(), user, message);
	}

	// �Ƿ������Ϸ�����
	public boolean isAuthenticated() {
		if (mSmackable != null) {
			return mSmackable.isAuthenticated();
		}
		return false;
	}

	// ���֪ͨ��-����BaseService�ײ㷽��
	public void clearNotifications(String Jid) {
		clearNotification(Jid);
	}

	/**
	 * ��UI�߳�����ʧ�ܷ���
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

	// ��������״̬
	public void setStatusFromConfig() {
		mSmackable.setStatusFromConfig();
	}

	// ������ϵ��
	public void addRosterItem(String user, String alias, String group) {
		try {
			mSmackable.addRosterItem(user, alias, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in addRosterItem(): " + e.getMessage());
		}
	}

	// ��������
	public void addRosterGroup(String group) {
		mSmackable.addRosterGroup(group);
	}

	// ɾ����ϵ��
	public void removeRosterItem(String user) {
		try {
			mSmackable.removeRosterItem(user);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in removeRosterItem(): " + e.getMessage());
		}
	}

	// ����ϵ���ƶ���������
	public void moveRosterItemToGroup(String user, String group) {
		try {
			mSmackable.moveRosterItemToGroup(user, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in moveRosterItemToGroup(): " + e.getMessage());
		}
	}

	// ��������ϵ��
	public void renameRosterItem(String user, String newName) {
		try {
			mSmackable.renameRosterItem(user, newName);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in renameRosterItem(): " + e.getMessage());
		}
	}

	// ��������
	public void renameRosterGroup(String group, String newGroup) {
		mSmackable.renameRosterGroup(group, newGroup);
	}

	/**
	 * UI�̷߳�������ʧ��
	 * 
	 * @param reason
	 */
	private void connectionFailed(String reason) {
		L.i(XXService.class, "connectionFailed: " + reason);
		mConnectedState = DISCONNECTED;// ���µ�ǰ����״̬
		if (TextUtils.equals(reason, LOGOUT)) {// ������ֶ��˳�
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}
		// �ص�
		if (mConnectionStatusCallback != null) {
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					reason);
			if (mIsFirstLoginAction)// ����ǵ�һ�ε�¼,�����¼ʧ��Ҳ����Ҫ����
				return;
		}

		// ����������ʱ,ֱ�ӷ���
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}

		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		// �ޱ�����ʺ�����ʱ��Ҳֱ�ӷ���
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
			L.d("account = null || password = null");
			return;
		}
		// ��������ֶ��˳�������Ҫ�������ӣ�������������
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
		mConnectedState = CONNECTED;// �Ѿ�������
		mReconnectTimeout = RECONNECT_AFTER;// ����������ʱ��

		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	/**�����У�֪ͨ�����߳���һЩ����**/
	private void postConnecting() {
		mMainHandler.post(new Runnable() {
			public void run() {
				connecting();
			}
		});
	}

	private void connecting() {
		mConnectedState = CONNECTING;// ������
		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	/**�յ�����Ϣ**/
	public void newMessage(final String from, final String message) {
		mMainHandler.post(new Runnable() {
			public void run() {
				if (!PreferenceUtils.getPrefBoolean(XXService.this,
						PreferenceConstants.SCLIENTNOTIFY, false))
					//������������
					MediaPlayer.create(XXService.this, R.raw.office).start();
				if (!isAppOnForeground())
					//���app����ǰ̨������ʾ֪ͨ����ʾ������Ϣ����
					notifyClient(from, mSmackable.getNameForJID(from), message,
							!mIsBoundTo.contains(from));
			}
		});
	}

	/**��ϵ�˸ı�**/
	public void rosterChanged() {
		if (mSmackable == null)
			return;
		if (mSmackable != null && !mSmackable.isAuthenticated()) {
			L.i("rosterChanged(): disconnected without warning");
			connectionFailed(DISCONNECTED_WITHOUT_WARNING);
		}
	}

	/**
	 * ����֪ͨ����Ϣ
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

	/**�жϳ����Ƿ��ں�̨���е�����**/
	Runnable monitorStatus = new Runnable() {
		public void run() {
			try {
				L.i("monitorStatus is running... " + mPackageName);
				mMainHandler.removeCallbacks(monitorStatus);
				// ����ں�̨���в�����������
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
    
	/**�жϷ����Ƿ���ǰ̨����**/
	public boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

	/**�Զ������㲥**/
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
			//ִ�е�¼����
			Login(account, password);
		}
	}
    /**��������״̬����ص�**/
	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			// ���������Ͽ�����������
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (isAuthenticated())// ����Ѿ������ϣ�ֱ�ӷ���
			return;
		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password))// ���û���ʺţ�Ҳֱ�ӷ���
			return;
		if (!PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.AUTO_RECONNECT, true))// ����Ҫ����
			return;
		Login(account, password);// ����
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
