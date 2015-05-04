package com.sinoservices.common.ring.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoservices.common.R;
import com.sinoservices.common.ring.bluetooth.BTDeviceManager;

public class BTEDAdapter extends BaseAdapter {

	private Context mContext = null;
	private BTDeviceManager mBTDeviceManager = null;
	private Resources mResource;

	public BTEDAdapter(Context context, BTDeviceManager mBTDeviceManager) {
		super();
		this.mContext = context;
		this.mBTDeviceManager = mBTDeviceManager;
		this.mResource = mContext.getResources();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		BluetoothDevice btDevice; 
		for (int i = 0; i < mBTDeviceManager.getNumberBTDevices(); i++) { 
			btDevice = mBTDeviceManager.getIndexDevices(i);
			if (BluetoothDevice.BOND_BONDED == btDevice.getBondState()) { 
				count++;
			}
		}
		return mBTDeviceManager.getNumberBTDevices();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.simple_list_item, parent,
					false);
		}
		TextView textviewBTName = (TextView) convertView
				.findViewById(R.id.device_name);
		TextView textviewBTAddr = (TextView) convertView
				.findViewById(R.id.device_addr);
		BluetoothDevice btDevice = mBTDeviceManager.getIndexDevices(position);
//		if (BluetoothDevice.BOND_BONDED == btDevice.getBondState()) {
			textviewBTName.setText(btDevice.getName() + "       [ÒÑÁ¬½Ó]");
			textviewBTName.setTextColor(Color.BLUE);
			textviewBTAddr.setTextColor(Color.BLUE);
			textviewBTAddr.setText(btDevice.getAddress());
			convertView.setTag(position);
//		}
		return convertView;
	}

}
