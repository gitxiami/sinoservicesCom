package com.sinoservices.gaodemap.navi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.sinoservices.common.MainActivity;
import com.sinoservices.common.R;

/**
 * Demo��ʼ�����棬������������յ㣬����·�����㵼����
 * 
 */
public class NaviStartActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, OnMapClickListener {
	// --------------View�����ؼ�---------------------
	private MapView mMapView;// ��ͼ�ؼ�
	private RadioGroup mNaviMethodGroup;// ���мݳ�ѡ��ؼ�
	private AutoCompleteTextView mStartPointText;// �������
	private EditText mWayPointText;// ;��������
	private EditText mEndPointText;// �յ�����
	private AutoCompleteTextView mStrategyText;// �г���������
	private Button mRouteButton;// ·���滮��ť
	private Button mNaviButton;// ģ�⵼����ť
	private ProgressDialog mProgressDialog;// ·���滮������ʾ״̬
	private ProgressDialog mGPSProgressDialog;// GPS������ʾ״̬
	private ImageView mStartImage;// ���������ť
	private ImageView mWayImage;// ;��������ť
	private ImageView mEndImage;// �յ�����ť
	private ImageView mStrategyImage;// �г����Ե����ť
	// ��ͼ�͵��������߼���
	private AMap mAmap;
	private AMapNavi mAmapNavi;
	// ---------------------����---------------------
	private String[] mStrategyMethods;// ��¼�г����Ե�����
	private String[] mPositionMethods;// ��¼����ҵ�λ�á���ͼ��ѡ����
	// �ݳ�·���滮��㣬;���㣬�յ��list
	private List<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private List<NaviLatLng> mWayPoints = new ArrayList<NaviLatLng>();
	private List<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// ��¼��㡢�յ㡢;����λ��
	private NaviLatLng mStartPoint = new NaviLatLng();
	private NaviLatLng mEndPoint = new NaviLatLng();
	private NaviLatLng mWayPoint = new NaviLatLng();
	// ��¼��㡢�յ㡢;�����ڵ�ͼ����ӵ�Marker
	private Marker mStartMarker;
	private Marker mWayMarker;
	private Marker mEndMarker;
	private Marker mGPSMarker;
	private boolean mIsGetGPS = false;// ��¼GPS��λ�Ƿ�ɹ�
	// private boolean mIsStart = false;// ��¼�Ƿ����ҵ�λ�÷���·���滮

	private ArrayAdapter<String> mPositionAdapter;

	private AMapNaviListener mAmapNaviListener;

	// ��¼��ͼ����¼���Ӧ���������ѡ��ͬ����ͼ��Ӧ��ͬ
	private int mMapClickMode = MAP_CLICK_NO;
	private static final int MAP_CLICK_NO = 0;// ��ͼ�����ܵ���¼�
	private static final int MAP_CLICK_START = 1;// ��ͼ����������
	private static final int MAP_CLICK_WAY = 2;// ��ͼ�������;����
	private static final int MAP_CLICK_END = 3;// ��ͼ��������յ�

	// ��¼�������࣬���ڼ�¼��ǰѡ���Ǽݳ����ǲ���
	private int mTravelMethod = DRIVER_NAVI_METHOD;
	private static final int DRIVER_NAVI_METHOD = 0;// �ݳ�����
	private static final int WALK_NAVI_METHOD = 1;// ���е���

	private int mNaviMethod;
	private static final int NAVI_METHOD = 0;// ִ��ģ�⵼������
	private static final int ROUTE_METHOD = 1;// ִ�м�����·����

	private int mStartPointMethod = BY_MY_POSITION;
	private static final int BY_MY_POSITION = 0;// ���ҵ�λ����Ϊ���
	private static final int BY_MAP_POSITION = 1;// �Ե�ͼ��ѡ�ĵ�Ϊ���
	// ����·��״̬
	private final static int GPSNO = 0;// ʹ���ҵ�λ�ý��м��㡢GPS��λ��δ�ɹ�״̬
	private final static int CALCULATEERROR = 1;// ����·������ʧ��״̬
	private final static int CALCULATESUCCESS = 2;// ����·������ɹ�״̬
	// ��λ
	private LocationManagerProxy mLocationManger;

	private AMapLocationListener mLocationListener = new AMapLocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
		}

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null
					&& location.getAMapException().getErrorCode() == 0) {
				mIsGetGPS = true;
				mStartPoint = new NaviLatLng(location.getLatitude(),
						location.getLongitude());

				mGPSMarker.setPosition(new LatLng(mStartPoint.getLatitude(),
						mStartPoint.getLongitude()));
				mStartPoints.clear();
				mStartPoints.add(mStartPoint);

				dissmissGPSProgressDialog();

				calculateRoute();
			} else {
				showToast("��λ�����쳣");
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_navistart);
		// ��ʼ��������Դ���ؼ����¼�����
		initResources();
		initView(savedInstanceState);
		initListener();
		initMapAndNavi();
		MainApplication.getInstance().addActivity(this);
	}

	// ----------���崦����--------------
	/**
	 * ��·�ķ���������ѡ����Խ����г��Ͳ������ַ�ʽ����·���滮
	 */
	private void calculateRoute() {
		if (mStartPointMethod == BY_MY_POSITION && !mIsGetGPS) {
			mLocationManger = LocationManagerProxy.getInstance(this);
			// ����һ�ζ�λ
			mLocationManger.requestLocationData(
					LocationProviderProxy.AMapNetwork, -1, 15,
					mLocationListener);
			showGPSProgressDialog();
			return;
		}
		mIsGetGPS = false;
		switch (mTravelMethod) {
		// �ݳ�����
		case DRIVER_NAVI_METHOD:
			int driverIndex = calculateDriverRoute();
			if (driverIndex == CALCULATEERROR) {
				showToast("·�߼���ʧ��,���������");
				return;
			} else if (driverIndex == GPSNO) {
				return;
			}
			break;
		// ���е���
		case WALK_NAVI_METHOD:
			int walkIndex = calculateWalkRoute();
			if (walkIndex == CALCULATEERROR) {
				showToast("·�߼���ʧ��,���������");
				return;
			} else if (walkIndex == GPSNO) {
				return;
			}
			break;
		}
		showProgressDialog();// ��ʾ·���滮�Ĵ���
	}

	/**
	 * ���г�·�߽��й滮
	 */
	private int calculateDriverRoute() {
		int driveMode = getDriveMode();
		int code = CALCULATEERROR;
		if (mAmapNavi.calculateDriveRoute(mStartPoints, mEndPoints, mWayPoints,
				driveMode)) {
			code = CALCULATESUCCESS;
		} else {

			code = CALCULATEERROR;
		}
		return code;
	}

	/**
	 * �Բ���·�߽��й滮
	 */
	private int calculateWalkRoute() {
		int code = CALCULATEERROR;
		if (mAmapNavi.calculateWalkRoute(mStartPoint, mEndPoint)) {
			code = CALCULATESUCCESS;
		} else {
			code = CALCULATEERROR;
		}
		return code;
	}

	/**
	 * ����ѡ�񣬻�ȡ�г�����
	 */
	private int getDriveMode() {
		String strategyMethod = mStrategyText.getText().toString();
		// �ٶ�����
		if (mStrategyMethods[0].equals(strategyMethod)) {
			return AMapNavi.DrivingDefault;
		}
		// ��������
		else if (mStrategyMethods[1].equals(strategyMethod)) {
			return AMapNavi.DrivingSaveMoney;
		}
		// �������
		else if (mStrategyMethods[2].equals(strategyMethod)) {
			return AMapNavi.DrivingShortDistance;
		}
		// ���߸���
		else if (mStrategyMethods[3].equals(strategyMethod)) {
			return AMapNavi.DrivingNoExpressways;
		}
		// ʱ������Ҷ��ӵ��
		else if (mStrategyMethods[4].equals(strategyMethod)) {
			return AMapNavi.DrivingFastestTime;
		} else if (mStrategyMethods[5].equals(strategyMethod)) {
			return AMapNavi.DrivingAvoidCongestion;
		} else {
			return AMapNavi.DrivingDefault;
		}
	}

	// -----------------��ʼ��-------------------
	/**
	 * ��ʼ����������View�ؼ�
	 * 
	 * @param savedInstanceState
	 */
	private void initView(Bundle savedInstanceState) {
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		mAmap = mMapView.getMap();
		mStartPointText = (AutoCompleteTextView) findViewById(R.id.navi_start_edit);

		mStartPointText
				.setDropDownBackgroundResource(R.drawable.whitedownborder);
		mWayPointText = (EditText) findViewById(R.id.navi_way_edit);
		mEndPointText = (EditText) findViewById(R.id.navi_end_edit);
		mStrategyText = (AutoCompleteTextView) findViewById(R.id.navi_strategy_edit);
		mStrategyText.setDropDownBackgroundResource(R.drawable.whitedownborder);
		mStartPointText.setInputType(InputType.TYPE_NULL);
		mWayPointText.setInputType(InputType.TYPE_NULL);
		mEndPointText.setInputType(InputType.TYPE_NULL);
		mStrategyText.setInputType(InputType.TYPE_NULL);

		ArrayAdapter<String> strategyAdapter = new ArrayAdapter<String>(this,
				R.layout.map_navi_strategy_inputs, mStrategyMethods);
		mStrategyText.setAdapter(strategyAdapter);

		mPositionAdapter = new ArrayAdapter<String>(this,
				R.layout.map_navi_strategy_inputs, mPositionMethods);
		mStartPointText.setAdapter(mPositionAdapter);

		mRouteButton = (Button) findViewById(R.id.navi_route_button);
		mNaviButton = (Button) findViewById(R.id.navi_navi_button);
		mNaviMethodGroup = (RadioGroup) findViewById(R.id.navi_method_radiogroup);

		mStartImage = (ImageView) findViewById(R.id.navi_start_image);
		mWayImage = (ImageView) findViewById(R.id.navi_way_image);
		mEndImage = (ImageView) findViewById(R.id.navi_end_image);
		mStrategyImage = (ImageView) findViewById(R.id.navi_strategy_image);
	}

	/**
	 * ��ʼ����Դ�ļ�����Ҫ���ַ���
	 */
	private void initResources() {
		Resources res = getResources();
		mStrategyMethods = new String[] {
				res.getString(R.string.navi_strategy_speed),
				res.getString(R.string.navi_strategy_cost),
				res.getString(R.string.navi_strategy_distance),
				res.getString(R.string.navi_strategy_nohighway),
				res.getString(R.string.navi_strategy_timenojam),
				res.getString(R.string.navi_strategy_costnojam) };
		mPositionMethods = new String[] { res.getString(R.string.mypoistion),
				res.getString(R.string.mappoistion) };
	}

	/**
	 * ��ʼ���������
	 */
	private void initListener() {
		// �ؼ�����¼�
		mStartPointText.setOnClickListener(this);
		mWayPointText.setOnClickListener(this);
		mEndPointText.setOnClickListener(this);
		mStrategyText.setOnClickListener(this);
		mRouteButton.setOnClickListener(this);
		mNaviButton.setOnClickListener(this);
		mStartImage.setOnClickListener(this);
		mWayImage.setOnClickListener(this);
		mEndImage.setOnClickListener(this);
		mStrategyImage.setOnClickListener(this);
		mNaviMethodGroup.setOnCheckedChangeListener(this);
		// ���õ�ͼ����¼�
		mAmap.setOnMapClickListener(this);
		// ������������¼�����
		mStartPointText.setOnItemClickListener(getOnItemClickListener());
	}

	/**
	 * ��ʼ����ͼ�͵����������
	 */
	private void initMapAndNavi() {
		// ��ʼ����������Դ
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// ������������
		// ����������ʼ

		mAmapNavi = AMapNavi.getInstance(this);// ��ʼ����������

		// ��ʼ��Marker��ӵ���ͼ
		mStartMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.start))));
		mWayMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.way))));
		mEndMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.end))));
		mGPSMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker))));
	}

	// ----------------------�¼�����---------------------------
	/**
	 * �ؼ�����¼�����
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ·���滮��ť�����¼�
		case R.id.navi_route_button:
			mNaviMethod = ROUTE_METHOD;
			calculateRoute();
			break;
		// ģ�⵼�������¼�
		case R.id.navi_navi_button:
			mNaviMethod = NAVI_METHOD;
			calculateRoute();
			break;
		// ������¼�
		case R.id.navi_start_image:
		case R.id.navi_start_edit:
			setTextDescription(mStartPointText, null);
			mPositionAdapter = new ArrayAdapter<String>(this,
					R.layout.map_navi_strategy_inputs, mPositionMethods);
			mStartPointText.setAdapter(mPositionAdapter);
			mStartPoints.clear();
			mStartMarker.setPosition(null);
			mStartPointText.showDropDown();
			break;
		// ;�������¼�
		case R.id.navi_way_image:
		case R.id.navi_way_edit:
			mMapClickMode = MAP_CLICK_WAY;
			mWayPoints.clear();
			mWayMarker.setPosition(null);
			setTextDescription(mWayPointText, "�����ͼ����;����");
			showToast("�����ͼ���;����");
			break;
		// �յ����¼�
		case R.id.navi_end_image:
		case R.id.navi_end_edit:
			mMapClickMode = MAP_CLICK_END;
			mEndPoints.clear();
			mEndMarker.setPosition(null);
			setTextDescription(mEndPointText, "�����ͼ�����յ�");
			showToast("�����ͼ����յ�");
			break;
		// ���Ե���¼�
		case R.id.navi_strategy_image:
		case R.id.navi_strategy_edit:
			mStrategyText.showDropDown();
			break;
		}
	}

	/**
	 * �ݳ�������ѡ��ť�¼�����
	 * */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		// �ݳ�ģʽ
		case R.id.navi_driver_button:
			mTravelMethod = DRIVER_NAVI_METHOD;
			// �ݳ�����;����Ͳ��Ե�����
			mWayPointText.setEnabled(true);
			mStrategyText.setEnabled(true);
			mWayImage.setEnabled(true);
			mStrategyImage.setEnabled(true);
			break;
		// ���е���ģʽ
		case R.id.navi_walk_button:
			mTravelMethod = WALK_NAVI_METHOD;
			mWayPointText.setEnabled(false);
			mStrategyText.setEnabled(false);
			mWayImage.setEnabled(false);
			mStrategyImage.setEnabled(false);
			mWayMarker.setPosition(null);
			mWayPoints.clear();
			break;
		}
	}

	/**
	 * ��ͼ����¼�����
	 * */
	@Override
	public void onMapClick(LatLng latLng) {
		// Ĭ�ϲ������κβ���
		if (mMapClickMode == MAP_CLICK_NO) {
			return;
		}
		// �������������㡢;���㡢�յ㲻ͬ�߼�����ͬ
		addPointToMap(latLng);
	}

	/**
	 * ��ͼ����¼����Ĵ����߼�
	 * 
	 * @param position
	 */
	private void addPointToMap(LatLng position) {
		NaviLatLng naviLatLng = new NaviLatLng(position.latitude,
				position.longitude);
		switch (mMapClickMode) {
		// ���
		case MAP_CLICK_START:
			mStartMarker.setPosition(position);
			mStartPoint = naviLatLng;
			mStartPoints.clear();
			mStartPoints.add(mStartPoint);
			setTextDescription(mStartPointText, "�ѳɹ��������");
			break;
		// ;����
		case MAP_CLICK_WAY:
			mWayMarker.setPosition(position);
			mWayPoints.clear();
			mWayPoint = naviLatLng;
			mWayPoints.add(mWayPoint);
			setTextDescription(mWayPointText, "�ѳɹ�����;����");
			break;
		// �յ�
		case MAP_CLICK_END:
			mEndMarker.setPosition(position);
			mEndPoints.clear();
			mEndPoint = naviLatLng;
			mEndPoints.add(mEndPoint);
			setTextDescription(mEndPointText, "�ѳɹ������յ�");
			break;
		}

	}

	/**
	 * ������������¼�����
	 * */
	private OnItemClickListener getOnItemClickListener() {
		return new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				switch (index) {
				// �ҵ�λ��Ϊ�����е�����·���滮
				case 0:
					mStartPointMethod = BY_MY_POSITION;
					break;
				// ��ͼ��ѡ�����е�����·���滮
				case 1:
					mStartPointMethod = BY_MAP_POSITION;
					mMapClickMode = MAP_CLICK_START;
					showToast("�����ͼ������");
					break;
				}
			}
		};
	}

	/**
	 * �����ص�����
	 * 
	 * @return
	 */
	private AMapNaviListener getAMapNaviListener() {
		if (mAmapNaviListener == null) {

			mAmapNaviListener = new AMapNaviListener() {
				@Override
				public void onTrafficStatusUpdate() {
				}

				@Override
				public void onStartNavi(int arg0) {
				}

				@Override
				public void onReCalculateRouteForYaw() {
				}

				@Override
				public void onReCalculateRouteForTrafficJam() {
				}

				@Override
				public void onLocationChange(AMapNaviLocation location) {
				}

				@Override
				public void onInitNaviSuccess() {
				}

				@Override
				public void onInitNaviFailure() {
				}

				@Override
				public void onGetNavigationText(int arg0, String arg1) {
				}

				@Override
				public void onEndEmulatorNavi() {
				}

				@Override
				public void onCalculateRouteSuccess() {
					dissmissProgressDialog();
					switch (mNaviMethod) {
					case ROUTE_METHOD:
						Intent intent = new Intent(NaviStartActivity.this,
								NaviRouteActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						break;
					case NAVI_METHOD:
						Intent standIntent = new Intent(NaviStartActivity.this,
								NaviEmulatorActivity.class);
						standIntent
								.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(standIntent);
						break;
					}
				}

				@Override
				public void onCalculateRouteFailure(int arg0) {
					dissmissProgressDialog();
					showToast("·���滮����");
				}

				@Override
				public void onArrivedWayPoint(int arg0) {
				}

				@Override
				public void onArriveDestination() {
				}

				@Override
				public void onGpsOpenStatus(boolean arg0) {
				}

				@Override
				public void onNaviInfoUpdated(AMapNaviInfo arg0) {
				}

				@Override
				public void onNaviInfoUpdate(NaviInfo arg0) {
				}
			};
		}
		return mAmapNaviListener;
	}

	/**
	 * ���ؼ������¼�
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(NaviStartActivity.this,
					MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			MainApplication.getInstance().deleteActivity(this);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ---------------UI����----------------
	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void setTextDescription(TextView view, String description) {
		view.setText(description);
	}

	/**
	 * ��ʾ���ȿ�
	 */
	private void showProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("��·�滮��");
		mProgressDialog.show();
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * ��ʾGPS���ȿ�
	 */
	private void showGPSProgressDialog() {
		if (mGPSProgressDialog == null)
			mGPSProgressDialog = new ProgressDialog(this);
		mGPSProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mGPSProgressDialog.setIndeterminate(false);
		mGPSProgressDialog.setCancelable(true);
		mGPSProgressDialog.setMessage("��λ��...");
		mGPSProgressDialog.show();
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissGPSProgressDialog() {
		if (mGPSProgressDialog != null) {
			mGPSProgressDialog.dismiss();
		}
	}

	// -------------�������ڱ�����д����----------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		// �������������д
		// ���������߼���Ϊ�˱�֤������ҳ������λ�ͼ��뵼���ص�
		AMapNavi.getInstance(this).setAMapNaviListener(getAMapNaviListener());
		mAmapNavi.startGPS();
		TTSController.getInstance(this).startSpeaking();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		// �������������д
		// �±��߼����Ƴ�����
		AMapNavi.getInstance(this)
				.removeAMapNaviListener(getAMapNaviListener());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();

	}

}
