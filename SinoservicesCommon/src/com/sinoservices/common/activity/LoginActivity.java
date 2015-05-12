package com.sinoservices.common.activity;

import com.sinoservices.common.Global;
import com.sinoservices.common.MainActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.service.IConnectionStatusCallback;
import com.sinoservices.common.service.XXService;
import com.sinoservices.common.util.DialogUtil;
import com.sinoservices.common.util.L;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import com.sinoservices.common.util.T;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @ClassName: LoginActivity
 * @Description: ��¼ҳ��-��¼��xmpp������
 * @author Jerry
 * @date 2015��5��6�� ����10:20:05
 */
public class LoginActivity extends FragmentActivity implements
		IConnectionStatusCallback, TextWatcher {

	public static final String LOGIN_ACTION = "com.way.action.LOGIN";
	private static final int LOGIN_OUT_TIME = 0;
	/** ��¼��ť **/
	private Button mLoginBtn;
	/** �û����� **/
	private EditText mAccountEt;
	/** �û����� **/
	private EditText mPasswordEt;
	/** �Ƿ񱣴����� **/
	private CheckBox mAutoSavePasswordCK;
	/** �Ƿ������¼ **/
	private CheckBox isHide_Login;
	/** ���ķ���XXService **/
	private XXService mXxService;
	/** �Ի��� **/
	private Dialog mLoginDialog;
	/** ���ӳ�ʱ�����߳� **/
	private ConnectionOutTimeProcess mLoginOutTimeProcess;
	/** ���� **/
	private String mAccount;
	/** ���� **/
	private String mPassword;
	/** ���Ӷ��� **/
	private Animation mTipsAnimation;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// ��¼��ʱ
			case LOGIN_OUT_TIME:
				if (mLoginOutTimeProcess != null
						&& mLoginOutTimeProcess.running)
					mLoginOutTimeProcess.stop();
				if (mLoginDialog != null && mLoginDialog.isShowing())
					mLoginDialog.dismiss();
				T.showShort(LoginActivity.this, R.string.timeout_try_again);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��������
		startService(new Intent(LoginActivity.this, XXService.class));
		// �󶨷���
		bindXMPPService();
		setContentView(R.layout.activity_login);
		initView();
	}

	/** �󶨷��� **/
	private void bindXMPPService() {
		L.i(LoginActivity.class, "[SERVICE] bind");
		Intent mServiceIntent = new Intent(this, XXService.class);
		mServiceIntent.setAction(LOGIN_ACTION);
		bindService(mServiceIntent, mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	/** �������� **/
	private void unbindXMPPService() {
		try {
			unbindService(mServiceConnection);
			L.i(LoginActivity.class, "[SERVICE] Unbind");
		} catch (IllegalArgumentException e) {
			L.e(LoginActivity.class, "Service wasn't bound!");
		}
	}

	ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// ��ȡmXxServiceʵ��
			mXxService = ((XXService.XXBinder) service).getService();
			// �ѱ�������ӵ�����״̬�ı���¼������ص�
			mXxService.registerConnectionStatusCallback(LoginActivity.this);
			// ��ʼ����xmpp������
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// ����󶨻ص�
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}
	};

	/** ��ʼ��views **/
	private void initView() {
		mTipsAnimation = AnimationUtils.loadAnimation(this, R.anim.connection);
		// �Ƿ��ס����ck
		mAutoSavePasswordCK = (CheckBox) findViewById(R.id.auto_save_password);
		// �Ƿ������¼ck
		isHide_Login = (CheckBox) findViewById(R.id.hide_login);
		// �û���et
		mAccountEt = (EditText) findViewById(R.id.account_input);
		// ����et
		mPasswordEt = (EditText) findViewById(R.id.password);
		// ��¼��ť
		mLoginBtn = (Button) findViewById(R.id.login);
		// ��ȡsp�б�����˺�����
		String account = PreferenceUtils.getPrefString(this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(this,
				PreferenceConstants.PASSWORD, "");
		// ��ȡsp�б���ĵ�¼ģʽ�Ƿ�����
		String ishide = PreferenceUtils.getPrefString(this,
				PreferenceConstants.STATUS_MODE, "");

		if (ishide != null && !ishide.equals("") && ishide.equals("xa")) {
			isHide_Login.setChecked(true);
		}

		// ��Ϊ�վ��Զ���д��ȥ
		if (!TextUtils.isEmpty(account))
			mAccountEt.setText(account);
		if (!TextUtils.isEmpty(password))
			mPasswordEt.setText(password);

		mAccountEt.addTextChangedListener(this);
		mLoginDialog = DialogUtil.getLoginDialog(this);
		mLoginOutTimeProcess = new ConnectionOutTimeProcess();
	}

	/** ��¼�¼� **/
	public void onLoginClick(View v) {
		// ��ȡ������ֵ
		mAccount = mAccountEt.getText().toString();
		mPassword = mPasswordEt.getText().toString();
		SaveServer(mAccount);

		if (TextUtils.isEmpty(mAccount)) {
			T.showShort(this, R.string.null_account_prompt);
			return;
		}
		if (TextUtils.isEmpty(mPassword)) {
			T.showShort(this, R.string.password_input_prompt);
			return;
		}

		if (mLoginOutTimeProcess != null && !mLoginOutTimeProcess.running)
			//������¼��ʱ�����߳�
			mLoginOutTimeProcess.start();
		if (mLoginDialog != null && !mLoginDialog.isShowing())
			// ������¼�еĽ��ȿ�
			mLoginDialog.show();
		if (mXxService != null) {
			// ���ú��ķ�����ִ�е�¼����
			mXxService.Login(mAccount, mPassword);
		}
	}

	/** ��ϳ�name@xx��ʽ���浽sp�� **/
	private String SaveServer(String account) {
		String serverurl = Global.XMPPSERVER;
		account = account + "@" + serverurl;
		PreferenceUtils.setPrefString(this, PreferenceConstants.Server,
				serverurl);
		return account;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		//�����Ҫ�ж�������û������볤�ȿ������ﴦ��
	}

	/** ���浽�˺š����롢��¼״̬��sp�� **/
	private void save2Preferences() {
		boolean isAutoSavePassword = mAutoSavePasswordCK.isChecked();
		// �ʺ���һֱ�����
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
				mAccount);
		//�ж��Ƿ񱣴�����
		if (isAutoSavePassword)
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					mPassword);
		else
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					"");
		if (isHide_Login.isChecked())
			PreferenceUtils.setPrefString(this,
					PreferenceConstants.STATUS_MODE, PreferenceConstants.XA);
		else
			PreferenceUtils.setPrefString(this,
					PreferenceConstants.STATUS_MODE,
					PreferenceConstants.AVAILABLE);
	}

	/** ��¼��ʱ�����߳� ��Ҫ���ڴ����¼��ʱ**/
	class ConnectionOutTimeProcess implements Runnable {
		public boolean running = false;
		private long startTime = 0L;
		private Thread thread = null;
		ConnectionOutTimeProcess() {
		}
		public void run() {
			while (true) {
				if (!this.running)
					return;
				if (System.currentTimeMillis() - this.startTime > 20 * 1000L) {
					mHandler.sendEmptyMessage(LOGIN_OUT_TIME);
				}
				try {
					Thread.sleep(10L);
				} catch (Exception localException) {
				}
			}
		}
		public void start() {
			try {
				this.thread = new Thread(this);
				this.running = true;
				this.startTime = System.currentTimeMillis();
				this.thread.start();
			} finally {
			}
		}
		public void stop() {
			try {
				this.running = false;
				this.thread = null;
				this.startTime = 0L;
			} finally {
			}
		}
	}

	/** ����״̬����ص� **/
	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		if (mLoginDialog != null && mLoginDialog.isShowing())
			//�رնԻ���
			mLoginDialog.dismiss();
		if (mLoginOutTimeProcess != null && mLoginOutTimeProcess.running) {
			//ֹͣ��¼��ʱ�����߳�
			mLoginOutTimeProcess.stop();
			mLoginOutTimeProcess = null;
		}
		if (connectedState == XXService.CONNECTED) {
			// ���ӳɹ������浽sp�У�����������ҳ��
			save2Preferences();
			//����������
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else if (connectedState == XXService.DISCONNECTED)
			// ��ʾ��¼ʧ��
			T.showLong(LoginActivity.this, getString(R.string.request_failed)
					+ reason);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ����󶨷���
		unbindXMPPService();
		if (mLoginOutTimeProcess != null) {
			//ֹͣ��¼��ʱ�����߳�
			mLoginOutTimeProcess.stop();
			mLoginOutTimeProcess = null;
		}
	}
}
