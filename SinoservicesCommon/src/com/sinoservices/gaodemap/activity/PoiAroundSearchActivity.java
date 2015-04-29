package com.sinoservices.gaodemap.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.poisearch.Scenic;
import com.sinoservices.common.R;
import com.sinoservices.gaodemap.util.ToastUtil;

/**
 * AMapV2��ͼ�м򵥽���poisearch����
 */
public class PoiAroundSearchActivity extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter, OnItemSelectedListener,
		OnPoiSearchListener, OnMapClickListener, OnInfoWindowClickListener,
		OnClickListener {
	private AMap aMap;
	private ProgressDialog progDialog = null;// ����ʱ������
	private Spinner selectDeep;// ѡ������б�
	private String[] itemDeep = { "�Ƶ�", "����", "����", "ӰԺ" };
	private Spinner selectType;// ѡ�񷵻��Ƿ����Ź����Ż�
	private String[] itemTypes = { "����poi", "���Ź�", "���Ż�", "���Ź������Ż�" };
	private String deepType = "";// poi��������
	private int searchType = 0;// ��������
	private int tsearchType = 0;// ��ǰѡ����������
	private PoiResult poiResult; // poi���صĽ��
	private int currentPage = 0;// ��ǰҳ�棬��0��ʼ����
	private PoiSearch.Query query;// Poi��ѯ������
	private LatLonPoint lp = new LatLonPoint(24.27, 118.06);// Ĭ������
//	private LatLonPoint lp = new LatLonPoint(39.908127, 116.375257);// Ĭ�������㳡
	private Marker locationMarker; // ѡ��ĵ�
	private PoiSearch poiSearch;
	private PoiOverlay poiOverlay;// poiͼ��
	private List<PoiItem> poiItems;// poi����
	private Marker detailMarker;// ��ʾMarker������
	private Button nextButton;// ��һҳ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_poiaroundsearch_activity);
		/*
		 * �������ߵ�ͼ�洢Ŀ¼�����������ߵ�ͼ���ʼ����ͼ����; ʹ�ù����п���������, ���������������ߵ�ͼ�洢��·����
		 * ����Ҫ�����ߵ�ͼ���غ�ʹ�õ�ͼҳ�涼����·������
		 */
		// Demo��Ϊ�������������ʹ�����ص����ߵ�ͼ��ʹ��Ĭ��λ�ô洢���������Զ�������
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		init();
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			setUpMap();
			setSelectType();
			Button locationButton = (Button) findViewById(R.id.locationButton);
			locationButton.setOnClickListener(this);
			Button searchButton = (Button) findViewById(R.id.searchButton);
			searchButton.setOnClickListener(this);
			nextButton = (Button) findViewById(R.id.nextButton);
			nextButton.setOnClickListener(this);
			nextButton.setClickable(false);// Ĭ����һҳ��ť���ɵ�
			locationMarker = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point))
					.position(new LatLng(lp.getLatitude(), lp.getLongitude()))
					.title("����Ϊ���ĵ㣬�����ܱ�"));
			locationMarker.showInfoWindow();

		}
	}

	/**
	 * ���ó���ѡ��
	 */
	private void setUpMap() {
		selectDeep = (Spinner) findViewById(R.id.spinnerdeep);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemDeep);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDeep.setAdapter(adapter);
		selectDeep.setOnItemSelectedListener(this);// ���spinnerѡ�������¼�
		aMap.setOnMarkerClickListener(this);// ��ӵ��marker�����¼�
		aMap.setInfoWindowAdapter(this);// �����ʾinfowindow�����¼�

	}

	/**
	 * ����ѡ������
	 */
	private void setSelectType() {
		selectType = (Spinner) findViewById(R.id.searchType);// ��������
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(adapter);
		selectType.setOnItemSelectedListener(this);// ���spinnerѡ�������¼�
		aMap.setOnMarkerClickListener(this);// ��ӵ��marker�����¼�
		aMap.setInfoWindowAdapter(this);// �����ʾinfowindow�����¼�
	}

	/**
	 * ע�����
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(PoiAroundSearchActivity.this);
		aMap.setOnMarkerClickListener(PoiAroundSearchActivity.this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(PoiAroundSearchActivity.this);
	}

	/**
	 * ��ʾ���ȿ�
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("����������");
		progDialog.show();
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * ��ʼ����poi����
	 */
	protected void doSearchQuery() {
		showProgressDialog();// ��ʾ���ȿ�
		aMap.setOnMapClickListener(null);// ����poi����ʱ�������ͼ����¼�
		currentPage = 0;
		query = new PoiSearch.Query("", deepType, "");// ��һ��������ʾ�����ַ������ڶ���������ʾpoi�������ͣ�������������ʾpoi�������򣨿��ַ�������ȫ����
		query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(currentPage);// ���ò��һҳ

		searchType = tsearchType;

		switch (searchType) {
		case 0: {// ����poi
			query.setLimitDiscount(false);
			query.setLimitGroupbuy(false);
		}
			break;
		case 1: {// ���Ź�
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(false);
		}
			break;
		case 2: {// ���Ż�
			query.setLimitGroupbuy(false);
			query.setLimitDiscount(true);
		}
			break;
		case 3: {// ���Ź������Ż�
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(true);
		}
			break;
		}

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// ������������Ϊ��lp��ΪԲ�ģ�����Χ2000�׷�Χ
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// ���ö����poi������Χ
			 */
			poiSearch.searchPOIAsyn();// �첽����
		}
	}

	/**
	 * �����һҳpoi����
	 */
	public void nextSearch() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;

				query.setPageNum(currentPage);// ���ò��һҳ
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		}
	}

	/**
	 * �鵥��poi����
	 * 
	 * @param poiId
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker))
					.getPoiId());
		}
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	/**
	 * poiû�����������ݣ�����һЩ�Ƽ����е���Ϣ
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "�Ƽ�����\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "��������:" + cities.get(i).getCityName() + "��������:"
					+ cities.get(i).getCityCode() + "���б���:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(PoiAroundSearchActivity.this, infomation);

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == selectDeep) {
			deepType = itemDeep[position];

		} else if (parent == selectType) {
			tsearchType = position;
		}
		nextButton.setClickable(false);// �ı���������������������
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == selectDeep) {
			deepType = "�Ƶ�";
		} else if (parent == selectType) {
			tsearchType = 0;
		}
		nextButton.setClickable(false);// �ı���������������������
	}

	/**
	 * POI����ص�
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dissmissProgressDialog();// ���ضԻ���
		if (rCode == 0) {
			if (result != null) {// ����poi�Ľ��
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// ȡ��һ���Ź���Ϣ
							sb.append("\n�Ź���"
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// ȡ��һ���Ż���Ϣ
							sb.append("\n�Żݣ�"
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {

						sb = new StringBuffer("��ַ��" + result.getSnippet()
								+ "\n�绰��" + result.getTel() + "\n���ͣ�"
								+ result.getTypeDes());
					}
					// �ж�poi�����Ƿ��������Ϣ
					if (result.getDeepType() != null) {
						// ��������Ҫ�����Ϣ�����Բ鿴����Ĵ��룬���ڽ�����δ��ʾ��ش���
						// sb = getDeepInfo(result, sb);
						detailMarker.setSnippet(sb.toString());
					} else {
						ToastUtil.show(PoiAroundSearchActivity.this,
								"��Poi��û�������Ϣ");
					}
				}

			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(PoiAroundSearchActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * POI�����Ϣ��ȡ
	 */
	@SuppressWarnings("unused")
	private StringBuffer getDeepInfo(PoiItemDetail result,
			StringBuffer sbuBuffer) {
		switch (result.getDeepType()) {
		// ���������Ϣ
		case DINING:
			if (result.getDining() != null) {
				Dining dining = result.getDining();
				sbuBuffer
						.append("\n��ϵ��" + dining.getTag() + "\n��ɫ��"
								+ dining.getRecommend() + "\n��Դ��"
								+ dining.getDeepsrc());
			}
			break;
		// �Ƶ������Ϣ
		case HOTEL:
			if (result.getHotel() != null) {
				Hotel hotel = result.getHotel();
				sbuBuffer.append("\n��λ��" + hotel.getLowestPrice() + "\n������"
						+ hotel.getHealthRating() + "\n��Դ��"
						+ hotel.getDeepsrc());
			}
			break;
		// ���������Ϣ
		case SCENIC:
			if (result.getScenic() != null) {
				Scenic scenic = result.getScenic();
				sbuBuffer
						.append("\n��Ǯ��" + scenic.getPrice() + "\n�Ƽ���"
								+ scenic.getRecommend() + "\n��Դ��"
								+ scenic.getDeepsrc());
			}
			break;
		// ӰԺ�����Ϣ
		case CINEMA:
			if (result.getCinema() != null) {
				Cinema cinema = result.getCinema();
				sbuBuffer.append("\nͣ����" + cinema.getParking() + "\n��飺"
						+ cinema.getIntro() + "\n��Դ��" + cinema.getDeepsrc());
			}
			break;
		default:
			break;
		}
		return sbuBuffer;
	}

	/**
	 * POI�����ص�����
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// ���ضԻ���
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// ����poi�Ľ��
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
					poiResult = result;
					poiItems = poiResult.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// ����֮ǰ��ͼ��
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						nextButton.setClickable(true);// ������һҳ�ɵ�
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(PoiAroundSearchActivity.this,
								R.string.no_result);
					}
				}
			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(PoiAroundSearchActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onMapClick(LatLng latng) {
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
				.position(latng).title("���ѡ��Ϊ���ĵ�"));
		locationMarker.showInfoWindow();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * �����ǰ�ť
		 */
		case R.id.locationButton:
			aMap.clear();// ��������marker
			registerListener();
			break;
		/**
		 * ���������ť
		 */
		case R.id.searchButton:
			doSearchQuery();
			break;
		/**
		 * �����һҳ��ť
		 */
		case R.id.nextButton:
			nextSearch();
			break;
		default:
			break;
		}
	}
}
