package com.sinoservices.common.ring.activity;

import java.io.IOException;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.sinoservices.common.App;
import com.sinoservices.common.R;
import com.sinoservices.common.ring.adapter.BTAdapter;
import com.sinoservices.common.ring.adapter.BTEDAdapter;
import com.sinoservices.common.ring.bluetooth.BTDeviceManager;
import com.sinoservices.common.ring.bluetooth.BtBroadcastReciver;

@SuppressLint({ "HandlerLeak", "NewApi" })
public class BtSettingActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, AdapterView.OnItemClickListener {
	private final String TAG = "BtSettingActivity";
	
	private Switch isOpen;
	private Switch isShowOpen;
	private BluetoothAdapter mBluetoothAdapter;
	private final int REQUEST_ENABLE_BT = 1;
	private LinearLayout serchBluetooth;

	private ListView mListViewDevices;
	private ListView pairedListView;
	private BTDeviceManager mBTDeviceManager;// 蓝牙管理器、

	private IntentFilter mIntentFilter;// intent拦截器
	private BtBroadcastReciver mBtBroadcastReciver;

	private final int MSG_TYPE_BT_STATE_CHANGED = 1;
	private final int MSG_TYPE_START_DISCOVERY = 2;
	private final int MSG_TYPE_FINISH_DISCOVERY = 3;
	private final int MSG_TYPE_FIND_DEVICE = 4;

	private Handler mHandler;// 线程

	private BTAdapter mBTAdapter;
	private BTEDAdapter mBTAdapterED;
	private ProgressBar progressBar = null;
	protected Button btnBack;// 返回
	private String clss = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
		super.onCreate(savedInstanceState);
		Log.d(TAG, "===================onCreate()===================");
		setContentView(R.layout.blue_setting);
		
		isOpen = (Switch) findViewById(R.id.open);
		isOpen.setOnCheckedChangeListener(this);
		isShowOpen = (Switch) findViewById(R.id.showOpen);
		isShowOpen.setOnCheckedChangeListener(this);
		serchBluetooth = (LinearLayout) findViewById(R.id.serchBluetooth);
		serchBluetooth.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.main_model_progressBar1);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		isOpen.setChecked(mBluetoothAdapter.isEnabled());
		

		Intent intent = getIntent();
		// 获取数据
		clss = intent.getStringExtra("clss");

		mListViewDevices = (ListView) findViewById(R.id.ListViewDevices);
		pairedListView = (ListView) findViewById(R.id.list);
		pairedListView.setTag("2");
		initIntentFilter();// 初始化拦截器

		mBTDeviceManager = new BTDeviceManager();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case MSG_TYPE_BT_STATE_CHANGED:
					// updateSwitchBtnState(msg.arg1);
					if (mBTAdapter != null) {
						mBTAdapter.notifyDataSetChanged();
					}
					if (mBTAdapterED != null) {
						mBTAdapterED.notifyDataSetChanged();
					}

					break;
				case MSG_TYPE_START_DISCOVERY:
					progressBar.setVisibility(View.VISIBLE);
					// updateSearchBtnState(MSG_TYPE_START_DISCOVERY);
					if (mBTAdapter != null) {
						mBTAdapter.notifyDataSetChanged();
					}
					if (mBTAdapterED != null) {
						mBTAdapterED.notifyDataSetChanged();
					}

					break;
				case MSG_TYPE_FINISH_DISCOVERY:
					progressBar.setVisibility(View.GONE);
					// updateSearchBtnState(MSG_TYPE_FINISH_DISCOVERY);
					if (mBTAdapter != null) {
						mBTAdapter.notifyDataSetChanged();
					}
					break;
				case MSG_TYPE_FIND_DEVICE:
					// mBTDeviceManager.clearAllDevices();
					mBTDeviceManager.addDevice((BluetoothDevice) msg.obj);
					if (mBTAdapter != null) {
						mBTAdapter.notifyDataSetChanged();
					}
					if (mBTAdapterED != null) {
						mBTAdapterED.notifyDataSetChanged();
					}
					break;
				}
			}
		};

		mBtBroadcastReciver = new BtBroadcastReciver(mHandler);
		mBTAdapter = new BTAdapter(this, mBTDeviceManager);
		mListViewDevices.setAdapter(mBTAdapter);
		mListViewDevices.setOnItemClickListener(this);
		mListViewDevices.setTag("1");

		if (mBluetoothAdapter.isEnabled()) {
			// 本地蓝牙打开情况
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			mBluetoothAdapter.getAddress();// 本地蓝牙地址
			mBTAdapterED = new BTEDAdapter(this, App.getmBTDeviceManager());
			pairedListView.setAdapter(mBTAdapterED);
			pairedListView.setOnItemClickListener(this);
			pairedListView.setTag("2");
			startBTDiscovery();
		}

		this.registerReceiver(mBtBroadcastReciver, mIntentFilter);
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "===================onDestroy()===================");
		this.unregisterReceiver(mBtBroadcastReciver);
		super.onDestroy();
	}

	private void initIntentFilter() {
		Log.d(TAG, "===================initIntentFilter()===================");
		// 初始化拦截器‘
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		mIntentFilter
				.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===================onClick()===================");
		if (arg0.getId() == R.id.serchBluetooth) {
			if (isOpen.isChecked()) {
				startBTDiscovery();
			} else {
				// this.soundPlay("蓝牙已经关闭");
			}
		}
		if (arg0.getId() == R.id.btn_back) {
			Intent intent = new Intent();
			if (clss.equals("HycoWearableMainActivity")) {
				intent.setClass(this, HycoWearableMainActivity.class);
			} 
			startActivity(intent);
			this.finish();
		}
	}

	private void startBTDiscovery() {
		Log.d(TAG, "===================startBTDiscovery()===================");
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, R.string.power_on_first, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// switch searching state
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		} else {
			mBluetoothAdapter.startDiscovery();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===================onCheckedChanged()===================");
		// 蓝牙开关
		if (mBluetoothAdapter != null) {
			if (arg0.getId() == R.id.open) {
				if (arg1) {
					if (!mBluetoothAdapter.isEnabled()) {
						Intent enableBtIntent = new Intent(
								BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent,
								REQUEST_ENABLE_BT);
					}
				} else {

					mBluetoothAdapter.disable();
					mBTAdapter.AllremoveList();
				}
			} else if (arg0.getId() == R.id.showOpen) {
				// 开放可见性
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);

				if (arg1) {
					// 设置蓝牙可见性，最多300秒
					intent.putExtra(
							BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					this.startActivityForResult(intent, 1000);
				} else {

				}

			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "===================onItemClick()===================");
		// check power on
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, R.string.power_on_first, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		BluetoothDevice device = null;
		if (parent.getTag().toString().equals("1")) {
			device = mBTDeviceManager.getIndexDevices(position);
		} else if (parent.getTag().toString().equals("2")) {
			device = App.getmBTDeviceManager().getIndexDevices(position);
		}
		if (null == device) {
			Toast.makeText(this, R.string.start_communicate_error,
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 主动发起配对
		// try {
		// Boolean returnValue = false;
		// if (device.getBondState() == BluetoothDevice.BOND_NONE) {
		// //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
		// Method createBondMethod = BluetoothDevice.class
		// .getMethod("createBond");
		// Log.d("BlueToothTestActivity", "开始配对");
		// returnValue = (Boolean) createBondMethod.invoke(device);
		//
		// }else if(device.getBondState() == BluetoothDevice.BOND_BONDED){
		// connect(device);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		App.setDevice(device);
//		Log.d(TAG, "===================before setConnectedDevices()===================");
//		App.setConnectedDevices(device);
//		Log.d(TAG, "===================after setConnectedDevices()===================");
		App.getmBTDeviceManager().addDevice(device);
		// when connect remote device,you must cancel discovery .
		mBluetoothAdapter.cancelDiscovery();
		Intent intent = new Intent();
		if (clss.equals("HycoWearableMainActivity")) {
			intent.setClass(this, HycoWearableMainActivity.class);
		} 
		startActivity(intent);
		App.getSpeaker().speak("连接成功");
		this.finish();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "===================onKeyDown()===================");
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent();
			if (clss.equals("HycoWearableMainActivity")) {
				intent.setClass(this, HycoWearableMainActivity.class);
			} 
			startActivity(intent); 
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void connect(BluetoothDevice device) {
		Log.d(TAG, "===================connect()===================");
		// 固定的UUID
		final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		UUID uuid = UUID.fromString(SPP_UUID);
		BluetoothSocket socket;
		try {
			socket = device.createRfcommSocketToServiceRecord(uuid);
			socket.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mBTAdapterED.notifyDataSetChanged();

	}

}
