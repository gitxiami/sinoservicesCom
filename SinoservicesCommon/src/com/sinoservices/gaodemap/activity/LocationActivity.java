package com.sinoservices.gaodemap.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.sinoservices.common.R;
import com.sinoservices.gaodemap.util.AMapUtil;
import com.sinoservices.gaodemap.util.OffLineMapUtils;
import com.sinoservices.gaodemap.util.ToastUtil;

public class LocationActivity extends Activity implements LocationSource,
		AMapLocationListener, OnGeocodeSearchListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private GeocodeSearch geocoderSearch;
	private LatLonPoint latLonPoint;
	private String addressName;
	private String name;
	private Marker locationMarker;// ��λ�״�Сͼ��
	private ProgressDialog progDialog = null;
	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_location);
		MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.location_mapView);
		mapView.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f,
					0.5f).icon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			setUpMap();
		}
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
		// getAddress(latLonPoint);
		getLatlon(name);
	}

	private void setUpMap() {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icons(giflist).period(50));
		locationMarker.isInfoWindowShown();
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(0.1f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
			}
		}
		name = amapLocation.getAddress();
		latLonPoint = new LatLonPoint(amapLocation.getLatitude(),
				amapLocation.getLongitude());
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				locationMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				addressName = "��γ��ֵ:" + address.getLatLonPoint() + "\nλ������:"
						+ address.getFormatAddress();
				ToastUtil.show(LocationActivity.this, addressName);
			} else {
				ToastUtil.show(LocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(LocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(LocationActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(LocationActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				city = result.getRegeocodeAddress().getCity();
				addressName = "��γ��ֵ:" + result.getRegeocodeQuery().getPoint()
						+ "\nλ������:"
						+ result.getRegeocodeAddress().getFormatAddress();
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				locationMarker.setPosition(AMapUtil
						.convertToLatLng(latLonPoint));
				ToastUtil.show(LocationActivity.this, addressName);
			} else {
				ToastUtil.show(LocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(LocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(LocationActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(LocationActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * ��ʾ�������Ի���
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("���ڻ�ȡ��ַ");
		progDialog.show();
	}

	/**
	 * ���ؽ������Ի���
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * ��Ӧ�������
	 */
	public void getLatlon(final String name) {
		showDialog();

		GeocodeQuery query = new GeocodeQuery(name, city);// ��һ��������ʾ��ַ���ڶ���������ʾ��ѯ���У����Ļ�������ȫƴ��citycode��adcode��
		geocoderSearch.getFromLocationNameAsyn(query);// ����ͬ�������������
	}

	/**
	 * ��Ӧ��������
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
		geocoderSearch.getFromLocationAsyn(query);// ����ͬ��������������
	}

}
