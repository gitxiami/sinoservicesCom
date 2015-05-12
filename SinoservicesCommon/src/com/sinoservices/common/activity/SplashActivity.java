package com.sinoservices.common.activity;

import com.sinoservices.common.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * @ClassName: SplashActivity
 * @Description: ����ҳ��
 * @author Jerry
 * @date 2015��4��27�� ����9:28:29
 *
 */
public class SplashActivity extends Activity {
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		mHandler = new Handler();
		mHandler.postDelayed(gotoLoginAct, 2000);
	}
	Runnable gotoLoginAct = new Runnable() {
		@Override
		public void run() {
			//��ת����¼ҳ��
			startActivity(new Intent(SplashActivity.this, LoginActivity.class));
			finish();
		}
	};
}
