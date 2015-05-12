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
 * @Description: 登录页面-登录到xmpp服务器
 * @author Jerry
 * @date 2015年5月6日 下午10:20:05
 */
public class LoginActivity extends FragmentActivity implements
		IConnectionStatusCallback, TextWatcher {

	public static final String LOGIN_ACTION = "com.way.action.LOGIN";
	private static final int LOGIN_OUT_TIME = 0;
	/** 登录按钮 **/
	private Button mLoginBtn;
	/** 用户名称 **/
	private EditText mAccountEt;
	/** 用户密码 **/
	private EditText mPasswordEt;
	/** 是否保存密码 **/
	private CheckBox mAutoSavePasswordCK;
	/** 是否隐身登录 **/
	private CheckBox isHide_Login;
	/** 核心服务XXService **/
	private XXService mXxService;
	/** 对话框 **/
	private Dialog mLoginDialog;
	/** 连接超时处理线程 **/
	private ConnectionOutTimeProcess mLoginOutTimeProcess;
	/** 名称 **/
	private String mAccount;
	/** 密码 **/
	private String mPassword;
	/** 连接动画 **/
	private Animation mTipsAnimation;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// 登录超时
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
		// 启动服务
		startService(new Intent(LoginActivity.this, XXService.class));
		// 绑定服务
		bindXMPPService();
		setContentView(R.layout.activity_login);
		initView();
	}

	/** 绑定服务 **/
	private void bindXMPPService() {
		L.i(LoginActivity.class, "[SERVICE] bind");
		Intent mServiceIntent = new Intent(this, XXService.class);
		mServiceIntent.setAction(LOGIN_ACTION);
		bindService(mServiceIntent, mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	/** 解除服务绑定 **/
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
			// 获取mXxService实例
			mXxService = ((XXService.XXBinder) service).getService();
			// 把本界面添加到连接状态改变的事件监听回调
			mXxService.registerConnectionStatusCallback(LoginActivity.this);
			// 开始连接xmpp服务器
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 解除绑定回调
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}
	};

	/** 初始化views **/
	private void initView() {
		mTipsAnimation = AnimationUtils.loadAnimation(this, R.anim.connection);
		// 是否记住密码ck
		mAutoSavePasswordCK = (CheckBox) findViewById(R.id.auto_save_password);
		// 是否隐身登录ck
		isHide_Login = (CheckBox) findViewById(R.id.hide_login);
		// 用户名et
		mAccountEt = (EditText) findViewById(R.id.account_input);
		// 密码et
		mPasswordEt = (EditText) findViewById(R.id.password);
		// 登录按钮
		mLoginBtn = (Button) findViewById(R.id.login);
		// 获取sp中保存的账号密码
		String account = PreferenceUtils.getPrefString(this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(this,
				PreferenceConstants.PASSWORD, "");
		// 获取sp中保存的登录模式是否隐身
		String ishide = PreferenceUtils.getPrefString(this,
				PreferenceConstants.STATUS_MODE, "");

		if (ishide != null && !ishide.equals("") && ishide.equals("xa")) {
			isHide_Login.setChecked(true);
		}

		// 不为空就自动填写进去
		if (!TextUtils.isEmpty(account))
			mAccountEt.setText(account);
		if (!TextUtils.isEmpty(password))
			mPasswordEt.setText(password);

		mAccountEt.addTextChangedListener(this);
		mLoginDialog = DialogUtil.getLoginDialog(this);
		mLoginOutTimeProcess = new ConnectionOutTimeProcess();
	}

	/** 登录事件 **/
	public void onLoginClick(View v) {
		// 获取输入框的值
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
			//启动登录超时处理线程
			mLoginOutTimeProcess.start();
		if (mLoginDialog != null && !mLoginDialog.isShowing())
			// 跳出登录中的进度框
			mLoginDialog.show();
		if (mXxService != null) {
			// 调用核心服务类执行登录操作
			mXxService.Login(mAccount, mPassword);
		}
	}

	/** 组合成name@xx格式保存到sp中 **/
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
		//如果需要判断输入的用户名密码长度可在这里处理
	}

	/** 保存到账号、密码、登录状态到sp中 **/
	private void save2Preferences() {
		boolean isAutoSavePassword = mAutoSavePasswordCK.isChecked();
		// 帐号是一直保存的
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
				mAccount);
		//判断是否保存密码
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

	/** 登录超时处理线程 主要用于处理登录超时**/
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

	/** 连接状态结果回调 **/
	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		if (mLoginDialog != null && mLoginDialog.isShowing())
			//关闭对话框
			mLoginDialog.dismiss();
		if (mLoginOutTimeProcess != null && mLoginOutTimeProcess.running) {
			//停止登录超时处理线程
			mLoginOutTimeProcess.stop();
			mLoginOutTimeProcess = null;
		}
		if (connectedState == XXService.CONNECTED) {
			// 连接成功，保存到sp中，并且跳到主页面
			save2Preferences();
			//跳到主界面
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else if (connectedState == XXService.DISCONNECTED)
			// 提示登录失败
			T.showLong(LoginActivity.this, getString(R.string.request_failed)
					+ reason);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 解除绑定服务
		unbindXMPPService();
		if (mLoginOutTimeProcess != null) {
			//停止登录超时处理线程
			mLoginOutTimeProcess.stop();
			mLoginOutTimeProcess = null;
		}
	}
}
