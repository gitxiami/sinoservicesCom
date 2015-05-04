package com.sinoservices.gaodemap.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.navi.AMapNaviViewOptions;
import com.sinoservices.common.R;
import com.sinoservices.gaodemap.util.Utils;

/**
 * �������ý���
 * 
 */
public class NaviSettingActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	// ----------------View

	private ImageView mBackView;// ���ذ�ť
	private RadioGroup mDayNightGroup;// ��ҹģʽ����ģʽ
	private RadioGroup mDeviationGroup;// ƫ������
	private RadioGroup mJamGroup;// ӵ������
	private RadioGroup mTrafficGroup;// ��ͨ����
	private RadioGroup mCameraGroup;// ����ͷ����
	private RadioGroup mScreenGroup;// ��Ļ����

	private boolean mDayNightFlag = Utils.DAY_MODE;
	private boolean mDeviationFlag = Utils.YES_MODE;
	private boolean mJamFlag = Utils.YES_MODE;
	private boolean mTrafficFlag = Utils.OPEN_MODE;
	private boolean mCameraFlag = Utils.OPEN_MODE;
	private boolean mScreenFlag = Utils.YES_MODE;
	private int mThemeStyle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_navisetting);
		Bundle bundle = getIntent().getExtras();
		processBundle(bundle);
		initView();
		initListener();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mBackView = (ImageView) findViewById(R.id.setting_back_image);
		mDayNightGroup = (RadioGroup) findViewById(R.id.day_night_group);
		mDeviationGroup = (RadioGroup) findViewById(R.id.deviation_group);
		mJamGroup = (RadioGroup) findViewById(R.id.jam_group);
		mTrafficGroup = (RadioGroup) findViewById(R.id.traffic_group);
		mCameraGroup = (RadioGroup) findViewById(R.id.camera_group);
		mScreenGroup = (RadioGroup) findViewById(R.id.screen_group);

	}

	/**
	 * ��ʼ�������¼�
	 */
	private void initListener() {
		mBackView.setOnClickListener(this);
		mDayNightGroup.setOnCheckedChangeListener(this);
		mDeviationGroup.setOnCheckedChangeListener(this);
		mJamGroup.setOnCheckedChangeListener(this);
		mTrafficGroup.setOnCheckedChangeListener(this);
		mCameraGroup.setOnCheckedChangeListener(this);
		mScreenGroup.setOnCheckedChangeListener(this);

	}

	/**
	 * ���ݵ������洫�������������õ�ǰ�������ʾ״̬
	 */
	private void setViewContent() {
		if (mDayNightGroup == null) {
			return;
		}
		if (mDayNightFlag) {
			mDayNightGroup.check(R.id.nightradio);
		} else {
			mDayNightGroup.check(R.id.dayratio);
		}
		if (mDeviationFlag) {
			mDeviationGroup.check(R.id.deviationyesradio);
		} else {
			mDeviationGroup.check(R.id.deviationnoradio);
		}

		if (mJamFlag) {
			mJamGroup.check(R.id.jam_yes_radio);
		} else {
			mJamGroup.check(R.id.jam_no_radio);
		}

		if (mTrafficFlag) {
			mTrafficGroup.check(R.id.trafficyesradio);
		} else {
			mTrafficGroup.check(R.id.trafficnoradio);
		}

		if (mCameraFlag) {
			mCameraGroup.check(R.id.camerayesradio);
		} else {
			mCameraGroup.check(R.id.cameranoradio);
		}

		if (mScreenFlag) {
			mScreenGroup.check(R.id.screenonradio);
		} else {
			mScreenGroup.check(R.id.screenoffradio);
		}
	}

	/**
	 * ��������bundle
	 * 
	 * @param bundle
	 */
	private void processBundle(Bundle bundle) {
		if (bundle != null) {
			mThemeStyle = bundle.getInt(Utils.THEME,
					AMapNaviViewOptions.DEFAULT_COLOR_TOPIC);
			mDayNightFlag = bundle.getBoolean(Utils.DAY_NIGHT_MODE);
			mDeviationFlag = bundle.getBoolean(Utils.DEVIATION);
			mJamFlag = bundle.getBoolean(Utils.JAM);
			mTrafficFlag = bundle.getBoolean(Utils.TRAFFIC);
			mCameraFlag = bundle.getBoolean(Utils.CAMERA);
			mScreenFlag = bundle.getBoolean(Utils.SCREEN);

		}
	}

	/**
	 * ���ݵ�ǰ������������ã�����bundle
	 * 
	 * @return
	 */
	private Bundle getBundle() {
		Bundle bundle = new Bundle();
		bundle.putBoolean(Utils.DAY_NIGHT_MODE, mDayNightFlag);
		bundle.putBoolean(Utils.DEVIATION, mDeviationFlag);
		bundle.putBoolean(Utils.JAM, mJamFlag);
		bundle.putBoolean(Utils.TRAFFIC, mTrafficFlag);
		bundle.putBoolean(Utils.CAMERA, mCameraFlag);
		bundle.putBoolean(Utils.SCREEN, mScreenFlag);
		bundle.putInt(Utils.THEME, mThemeStyle);
		return bundle;
	}

	// �¼�������
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_back_image:

			Intent intent = new Intent(NaviSettingActivity.this,
					NaviCustomActivity.class);
			intent.putExtras(getBundle());
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			break;
		}

	}

	/**
	 * ���ؼ�����
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(NaviSettingActivity.this,
					NaviCustomActivity.class);
			intent.putExtras(getBundle());
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------����������д����---------------------------

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setViewContent();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		// ��ҹģʽ
		case R.id.dayratio:
			mDayNightFlag = Utils.DAY_MODE;
			break;
		case R.id.nightradio:
			mDayNightFlag = Utils.NIGHT_MODE;
			break;
		// ƫ������
		case R.id.deviationyesradio:
			mDeviationFlag = Utils.YES_MODE;
			break;
		case R.id.deviationnoradio:
			mDeviationFlag = Utils.NO_MODE;
			break;
		// ӵ������
		case R.id.jam_yes_radio:
			mJamFlag = Utils.YES_MODE;
			break;
		case R.id.jam_no_radio:
			mJamFlag = Utils.NO_MODE;
			break;
		// ��ͨ����
		case R.id.trafficyesradio:
			mTrafficFlag = Utils.OPEN_MODE;
			break;
		case R.id.trafficnoradio:
			mTrafficFlag = Utils.CLOSE_MODE;
			break;
		// ����ͷ����
		case R.id.camerayesradio:
			mCameraFlag = Utils.OPEN_MODE;
			break;
		case R.id.cameranoradio:
			mCameraFlag = Utils.CLOSE_MODE;
			break;
		// ��Ļ����
		case R.id.screenonradio:
			mScreenFlag = Utils.YES_MODE;
			break;
		case R.id.screenoffradio:
			mScreenFlag = Utils.NO_MODE;
			break;
		}

	}
}
