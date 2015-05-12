package com.sinoservices.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sinoservices.common.R;

/**
 * @ClassName: APPGlobalSettingActivity
 * @Description: App全局设置页面
 * @author Jerry
 * @date 2015年5月9日 下午1:25:07
 */
public class APPGlobalSettingActivity extends Activity implements OnClickListener{
	/**返回按钮**/
	private Button global_setting_back_btn;
	/**个人信息跳转LinearLayout**/
	private LinearLayout global_setting_ly_person;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_setting);
		
		initViews();
		setListener();
	}
	private void initViews() {
		global_setting_back_btn=(Button) findViewById(R.id.global_setting_back_btn);
		global_setting_ly_person=(LinearLayout) findViewById(R.id.global_setting_ly_person);
	}
	private void setListener() {
		global_setting_back_btn.setOnClickListener(this);
		global_setting_ly_person.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.global_setting_back_btn:
			//返回按钮
			finish();
			break;
		case R.id.global_setting_ly_person:
			//跳转到个人信息页面
			Intent intent=new Intent(APPGlobalSettingActivity.this,PersonDataActivity.class);
		    startActivity(intent);
			break;
		default:
			break;
		}
	}
}
