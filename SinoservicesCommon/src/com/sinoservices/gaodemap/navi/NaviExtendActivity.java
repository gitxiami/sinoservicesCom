package com.sinoservices.gaodemap.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.sinoservices.common.R;

/**
 * ģ�⵼����ʾ����
 * 
 */
public class NaviExtendActivity extends Activity implements AMapNaviListener {
	private AMapNaviView mAmapAMapNaviView;

	private TextView mRouteInfoText;
	private TextView mNaviInfoText;
	public final static String[] strActions = { "��", "�Գ�", "��ת", "��ת", "��ǰ����ʻ",
			"��ǰ����ʻ", "�����ʻ", "�Һ���ʻ", "��ת��ͷ", "ֱ��", "����;����", "���뻷��", "ʻ������",
			"���������", "�����շ�վ", "����Ŀ�ĵ�", "�������", "����", "����", "ͨ�����к��", "ͨ����������",
			"ͨ������ͨ��", "ͨ���㳡", "����·б����" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_naviextend);
		mRouteInfoText = (TextView) findViewById(R.id.extend_route_info);
		mNaviInfoText = (TextView) findViewById(R.id.extend_navi_info);
		init(savedInstanceState);
		MainApplication.getInstance().addActivity(this);

	}

	/**
	 * ��ʼ��
	 * 
	 * @param savedInstanceState
	 */
	private void init(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.extendnavimap);
		AMapNaviViewOptions viewOptions = mAmapAMapNaviView.getViewOptions();
		viewOptions.setLayoutVisible(false);

		mAmapAMapNaviView.onCreate(savedInstanceState);

		TTSController.getInstance(this).startSpeaking();

		// ����ģ���ٶ�
		AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
		// ����ģ�⵼��
		AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
		AMapNavi.getInstance(this).setAMapNaviListener(this);

		AMapNaviPath naviPath = AMapNavi.getInstance(this).getNaviPath();
		if (naviPath != null) {
			double length = ((int) (naviPath.getAllLength() / (double) 1000 * 10))
					/ (double) 10;
			// ������� �����Ӽ�
			int time = (naviPath.getAllTime() + 59) / 60;
			StringBuffer sbf = new StringBuffer();
			sbf.append("·����ʱ�䣺");
			sbf.append(time + "����");
			sbf.append("  ·���ܾ��룺");
			sbf.append(length + "����");

			mRouteInfoText.setText(sbf.toString());
		}

	}

	/**
	 * 
	 * ���ؼ������¼�
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// /////////////////////////////////////////////////////////////////////////////
			Intent intent = new Intent(NaviExtendActivity.this,
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
	public void onArriveDestination() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteSuccess() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onEndEmulatorNavi() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo naveinfo) {
		if (naveinfo == null) {
			return;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append("��ǰ·����");
		sbf.append(naveinfo.getCurrentRoadName());
		sbf.append(" ����·����");
		sbf.append(naveinfo.getNextRoadName());
		sbf.append(" ��ǰ����������");
		sbf.append(strActions[naveinfo.m_Icon]);
		sbf.append(" ��һ��������룺");
		sbf.append(naveinfo.getCurStepRetainDistance());
		sbf.append(" ʣ���ọ́�");
		sbf.append(naveinfo.getPathRetainDistance());
		if (naveinfo.getCameraDistance() != -1) {
			sbf.append(" ����ͷ���ͣ�");
			if (naveinfo.getCameraType() == 0) {
				sbf.append("����");
			}
			if (naveinfo.getCameraType() == 1) {
				sbf.append("���");
			}
			sbf.append(" ����ͷ���룺");
			sbf.append(naveinfo.getCameraDistance());
		}
		mNaviInfoText.setText(sbf.toString());

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {

		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {

		// TODO Auto-generated method stub

	}

}
