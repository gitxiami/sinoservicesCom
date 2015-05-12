package com.sinoservices.common.ring.bluetooth;

import com.sinoservices.common.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

//自定义广播类
public class BtBroadcastReciver extends BroadcastReceiver {

	private final int MSG_TYPE_BT_STATE_CHANGED = 1;
	private final int MSG_TYPE_START_DISCOVERY = 2;
	private final int MSG_TYPE_FINISH_DISCOVERY = 3;
	private final int MSG_TYPE_FIND_DEVICE = 4;
	private Handler mHandler;

	public BtBroadcastReciver(Handler mHandler) {
		this.mHandler = mHandler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();

		// deal with discovery action
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// get find device Intent
			// 获取查找到的蓝牙设备  
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			Message msg = Message.obtain();
			msg.what = MSG_TYPE_FIND_DEVICE;
			msg.obj = device;
			String dName=device.getName();
			mHandler.sendMessage(msg);

			// BT state changed,I will update my UI
		} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

			int bluetoothState = intent.getIntExtra(
					BluetoothAdapter.EXTRA_STATE, -1);

			Message msg = Message.obtain();
			msg.what = MSG_TYPE_BT_STATE_CHANGED;
			msg.arg1 = bluetoothState;
			mHandler.sendMessage(msg);
		} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			Message msg = Message.obtain();
			msg.what = MSG_TYPE_START_DISCOVERY;
			mHandler.sendMessage(msg);
		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			Message msg = Message.obtain();
			msg.what = MSG_TYPE_FINISH_DISCOVERY;
			mHandler.sendMessage(msg);
		} else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
				.equals(action)) {

			int state = intent.getIntExtra(
					BluetoothAdapter.EXTRA_CONNECTION_STATE,
					BluetoothAdapter.STATE_DISCONNECTED);

			if (state == BluetoothAdapter.STATE_CONNECTED) {
				// connecting to remote device
				Toast.makeText(context, R.string.listen_first,
						Toast.LENGTH_LONG).show();
				return;
			}
		}

	}

}
