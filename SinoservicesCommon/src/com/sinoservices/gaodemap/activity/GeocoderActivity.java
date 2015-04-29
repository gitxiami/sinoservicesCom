package com.sinoservices.gaodemap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
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
import com.sinoservices.gaodemap.util.ToastUtil;

public class GeocoderActivity extends Activity implements
		OnGeocodeSearchListener, OnClickListener {
	private MapView mapView;
	private AMap aMap;
	private Marker geoMarker;
	private Marker regeoMarker;
	private GeocodeSearch geocoderSearch;
	private ProgressDialog progDialog = null;
	private String addressName;
	private String cityName;
	private LatLonPoint latLonPoint;
	private EditText edt_lat, edt_lon, edt_address;
	Button geoButton, regeoButton;
	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_geocoder);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		edt_address = (EditText) this.findViewById(R.id.geo_city);
		edt_lat = (EditText) this.findViewById(R.id.regeo_lat);
		edt_lon = (EditText) this.findViewById(R.id.regeo_lon);
		geoButton = (Button) findViewById(R.id.geo_button);
		regeoButton = (Button) findViewById(R.id.regeo_button);
		geoButton.setOnClickListener(this);
		regeoButton.setOnClickListener(this);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
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

	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);
	}

	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.geo_button:
			cityName = edt_address.getText().toString();
			getLatlon(cityName);
			// getLatlon("方恒国际中心");
			break;
		case R.id.regeo_button:
			double lat = Double.valueOf(edt_lat.getText().toString());
			double lon = Double.valueOf(edt_lon.getText().toString());
			latLonPoint = new LatLonPoint(lat, lon);
			// latLonPoint = new LatLonPoint(39.90865, 116.39751);
			getAddress(latLonPoint);
			break;
		}
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
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				ToastUtil.show(GeocoderActivity.this, addressName);
			} else {
				ToastUtil.show(GeocoderActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(GeocoderActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(GeocoderActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(GeocoderActivity.this,
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
				addressName = "经纬度值:" + latLonPoint + "\n位置描述:"
						+ result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				ToastUtil.show(GeocoderActivity.this, addressName);
			} else {
				ToastUtil.show(GeocoderActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(GeocoderActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(GeocoderActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(GeocoderActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}
}
