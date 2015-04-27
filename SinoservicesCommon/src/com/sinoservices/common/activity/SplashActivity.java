package com.sinoservices.common.activity;

import com.sinoservices.common.MainActivity;
import com.sinoservices.common.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
/**
 * @ClassName: SplashActivity 
 * @Description: ����ҳ��
 * @author Jerry 
 * @date 2015��4��27�� ����9:28:29 
 *
 */
public class SplashActivity extends Activity {
	private RelativeLayout splash_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		splash_id=(RelativeLayout) findViewById(R.id.splash_id);
		//����չʾ������
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(3000);
		splash_id.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				//��������������������
				Intent it = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(it);
				finish();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}
			
		});
	}
}
