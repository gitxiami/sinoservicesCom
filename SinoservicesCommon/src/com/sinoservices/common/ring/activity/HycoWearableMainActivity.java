package com.sinoservices.common.ring.activity;

import com.sinoservices.common.R;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

public class HycoWearableMainActivity extends BaseActivity implements OnClickListener{
	private final String TAG = "HycoWearableMainActivity";
	
	EditText barcodeNoEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
		super.onCreate(savedInstanceState);
		
		this.setClssName("HycoWearableMainActivity");
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_main_hyco_wearable);
		
		findViewById(R.id.backButton).setOnClickListener(this);
		findViewById(R.id.settingButton).setOnClickListener(this);
		
		barcodeNoEditText = (EditText) findViewById(R.id.barcodeNoEditText);
	}

	@Override
	public void widgetClick(View v) {
		if (R.id.backButton == v.getId()) {
			this.finish();
		} else if (R.id.settingButton == v.getId()) {
			this.soundPlay("设置");
			Intent intent = new Intent();
			intent.setClass(HycoWearableMainActivity.this, BtSettingActivity.class);
			intent.putExtra("clss","HycoWearableMainActivity"); 
			startActivity(intent);
			this.finish();
		}
/*		
		switch (v.getId()) {
		case R.id.backButton:
			this.finish();
			break;
		case R.id.settingButton:
			this.soundPlay("设置");
			Intent intent = new Intent();
			intent.setClass(HycoWearableMainActivity.this, BtSettingActivity.class);
			intent.putExtra("clss","HycoWearableMainActivity"); 
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
*/		
	}
	
	@Override
	protected void showMsg(String msg) {
		super.showMsg(msg);
		
		if (barcodeNoEditText.isFocused()) {
			barcodeNoEditText.setText(msg);
			sendCommand(msg.getBytes());
			Log.d(TAG, "sendCommand() finish");
		}
	}

}
