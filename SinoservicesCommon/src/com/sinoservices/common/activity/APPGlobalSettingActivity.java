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
 * @Description: Appȫ������ҳ��
 * @author Jerry
 * @date 2015��5��9�� ����1:25:07
 */
public class APPGlobalSettingActivity extends Activity implements OnClickListener{
	/**���ذ�ť**/
	private Button global_setting_back_btn;
	/**������Ϣ��תLinearLayout**/
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
			//���ذ�ť
			finish();
			break;
		case R.id.global_setting_ly_person:
			//��ת��������Ϣҳ��
			Intent intent=new Intent(APPGlobalSettingActivity.this,PersonDataActivity.class);
		    startActivity(intent);
			break;
		default:
			break;
		}
	}
}
