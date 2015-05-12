package com.sinoservices.common.ring.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sinoservices.common.ring.log.Loger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


final public class BTDeviceManager{
	
	private static final String TAG="BTDeviceManager";
	private final List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
	private BluetoothAdapter mBluetoothAdapter;
	private Set<BluetoothDevice> mPairedDevices;
	
	public BTDeviceManager(){
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mPairedDevices = mBluetoothAdapter.getBondedDevices();
		
		for (BluetoothDevice tempDevice : mPairedDevices){
			addDevice(tempDevice);
		}
		
	}

	/**
	 * 
	 * @param device
	 * @return find this device , null is not find
	 */
	public BluetoothDevice findDevice(BluetoothDevice device){
		
		for (BluetoothDevice tempDevice : mDevices) {
            if (tempDevice.getAddress().equals(device.getAddress()) ) {
                return device;
            }
        }
		
		return null;
	}
	
	/**
	 * 
	 * @param addr : remote device address
	 * @return find this device, null is not find
	 */
	public BluetoothDevice findDevice(String addr){
		
		for (BluetoothDevice tempDevice : mDevices) {
            if (tempDevice.getAddress().equals(addr) ) {
                return tempDevice;
            }
        }
		
		return null;
	}
	
	/**
	 * 
	 * @param device : device member
	 * @return null : add fail else return this device
	 */
	public BluetoothDevice addDevice(BluetoothDevice device){
		
		if (null == device){
			Loger.getLogger(TAG).e( "addDevice is null!");
			return null;
		}
		
		if (null != findDevice(device)){
			//maybe update this device
			removeDevice(device);
			mDevices.add(device);
			
			Loger.getLogger(TAG).e("already add in BTDeviceManager");
		}else{
			mDevices.add(device);
		}		
		
		return device;
	}
	
	/**
	 * 
	 * @param device : device member
	 * @return null : remove fail
	 */
	private BluetoothDevice removeDevice(BluetoothDevice device){
		
		
		for (BluetoothDevice tempDevice : mDevices) {
            if (tempDevice.getAddress().equals(device.getAddress()) ) {                
            	mDevices.remove(tempDevice);
            	return tempDevice;            	
            }
        }
		
		return null;
	}
	
	/**
	 * 
	 * @return get BT devices List
	 */
	public List<BluetoothDevice> getDevices(){
		return mDevices;
	}
	
	public int getNumberBTDevices(){
		return mDevices.size();
	}
	
	/**
	 * 
	 * @param id 
	 * @return get device according to id index of devices list
	 */
	public BluetoothDevice getIndexDevices(int id){
	
		BluetoothDevice tempDevice=null;		

		int cnt=0;
		for (BluetoothDevice t : mDevices) {
            if (id == cnt) {
            	tempDevice = t;
            	break;
            }
            
            cnt++;
        }
		
		return tempDevice;
	}
	
	public void clearAllDevices(){
		mDevices.clear();
	}
}