package com.sinoservices.gaodemap.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.sinoservices.common.R;
import com.sinoservices.gaodemap.util.Utils;

/**
 * ģ�⵼����ʾ����
 * 
 */
public class NaviEmulatorActivity extends Activity implements
		AMapNaviViewListener {
	private AMapNaviView mAmapAMapNaviView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_navistander);
		init(savedInstanceState);
		MainApplication.getInstance().addActivity(this);

	}

	/**
	 * ��ʼ��
	 * 
	 * @param savedInstanceState
	 */
	private void init(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.standernavimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);// ����������ʼ
		TTSController.getInstance(this).startSpeaking();

		// ����ģ���ٶ�
		AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
		// ����ģ�⵼��
		AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
		// ����ʵʱ����
		// AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);

	}

	/**
	 * �������淵�ذ�ť����
	 * */
	@Override
	public void onNaviCancel() {
		// /////////////////////////////////////////////////////////
		Intent intent = new Intent(NaviEmulatorActivity.this,
				NaviStartActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		MainApplication.getInstance().deleteActivity(this);
		finish();

	}

	@Override
	public void onNaviSetting() {

	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * ���ؼ������¼�
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(NaviEmulatorActivity.this,
					NaviStartActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			MainApplication.getInstance().deleteActivity(this);
			;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------�������ڷ���---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAmapAMapNaviView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mAmapAMapNaviView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAmapAMapNaviView.onDestroy();
		// ������� ֹͣ��������
		TTSController.getInstance(this).stopSpeaking();
	}

	@Override
	public void onNaviTurnClick() {
		// //////////////////////////////////////////////////////////////
		Intent intent = new Intent(NaviEmulatorActivity.this,
				NaviStartActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		Bundle bundle = new Bundle();
		bundle.putInt(Utils.ACTIVITYINDEX, Utils.EMULATORNAVI);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLockMap(boolean arg0) {

		// TODO Auto-generated method stub

	}

}
