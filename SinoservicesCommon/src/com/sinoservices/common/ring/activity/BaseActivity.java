package com.sinoservices.common.ring.activity;

import java.util.ArrayList;

import com.sinoservices.common.App;
import com.sinoservices.common.R;
import com.sinoservices.common.ring.bluetooth.Common;
import com.sinoservices.common.ring.protocol.CommonProtocol;
import com.sinoservices.common.ring.protocol.DataFrame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

/**
 * 应用程序Activity的基类: BaseActivity
 * 
 * @author Felix
 * @version 1.0
 * @created 2015-04-10
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends Activity implements OnClickListener{
	private final String TAG = "BaseActivity";
	
	protected BluetoothDevice device;// 蓝牙设备
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBluetoothDevice;
	private BluetoothGatt mBluetoothGatt = null;
	private BluetoothGattService currBtService = null;
	private BluetoothGattCharacteristic currBtCharacteristic = null;
	
	private Handler mHandler;
	private String clssName;
	
	protected boolean isbtMsg = false;
	
	private int mReadCharCnt = 0;
	// parse data
	private boolean mFrameStart = false;
	private int mFrameLength = 0;
	private int mFrameCursor = 0;
	private final int MAX_BUFFER_LEN = 512;
	private byte[] mFrameData = new byte[512];

    // 是否允许全屏
    private boolean mAllowFullScreen = true;
 
    public abstract void initWidget();
 
    public abstract void widgetClick(View v);
 
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }
 
    @Override
    public void onClick(View v) {
        widgetClick(v);
    }
 
    /***************************************************************************
     * 
     * 打印Activity生命周期
     * 
     ***************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "---------onCreate() ");
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        }
        initWidget();
        
		// Use this check to determine whether BLE is supported on the device.  Then you can
		// selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}        
        
        if (null != App.getDevice()) {
			// 连接蓝牙
        	device = App.getDevice();
			mBluetoothDevice = App.getDevice();
			try {
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				mBluetoothGatt = mBluetoothDevice.connectGatt(BaseActivity.this, true, btGattCallback);
				App.setConnectedGatts(mBluetoothGatt);
				Log.d(TAG, "connected to " + mBluetoothDevice.getName());

			} catch (Exception e) {
				e.printStackTrace();
				this.toastAndSpeak(e.getMessage());
			}
			
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					switch (msg.what) {
					case Common.MSG_CONNECT_OK:
						System.out.print("connect remote ok!");
						break;
					case Common.MSG_CONNECT_FAIL:
						System.out.print("connect fail. Name:"
								+ device.getName() + ",address:"
								+ device.getAddress());
						// toast("connect fail. Name:" + device.getName()
						// + ",address:" + device.getAddress());

						break;
					case Common.MSG_READ_BUFF:
						byte[] readBuf = (byte[]) msg.obj;
						// construct a string from the valid bytes in the buffer
						String readMessage = new String(readBuf, 0, msg.arg1);
						// toast(readMessage);
						isbtMsg = true;
						readBufferMsg((byte[]) msg.obj, msg.arg1);
						//
						// btMsg = readMessage;
						// showMsg(readMessage);
						break;
					case Common.MSG_CONNECT_EXCEPTION:
						System.out.print("read buf fail");
						break;
					}
					Log.d(TAG, "Handler init finish");
				}
			};
        }
    }
 
    private void readBufferMsg(byte[] bytes, int size) {
		// int size = bytes.length;

		if (mFrameCursor + size > MAX_BUFFER_LEN) {
			mFrameStart = false;
			mFrameCursor = 0;
			return;
		}

		if (mFrameStart) {
			System.arraycopy(bytes, 0, mFrameData, mFrameCursor, size);
			mFrameCursor += size;
		} else if (DataFrame.FRAME_START == bytes[0]) {
			mFrameStart = true;
			mFrameCursor = 0;
			mFrameLength = bytes[1];// 锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷确锟斤拷

			System.arraycopy(bytes, 0, mFrameData, mFrameCursor, size);
			mFrameCursor += size;
		}

		if (DataFrame.FRAME_END == bytes[size - 1]) {
			// find end flag, decode data
			DataFrame mDataFrame = new DataFrame();
			if (0 != mDataFrame.decodeData(mFrameData, mFrameCursor)) {

				byte[] data = mDataFrame.getRecvData();

				StringBuilder msg = new StringBuilder("");
				for (int i = 0; i < data.length; i++) {
					msg.append((char) data[i]);

				}

				mReadCharCnt += msg.length();
				// mDispRxMsg.append(msg + "\n");
				showMsg(msg + "");
				Log.d(TAG, "readBufferMsg(): msg=" + msg);
			}

			mFrameStart = true;
			mFrameCursor = 0;
		}

	}
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "---------onStart() ");
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "---------onResume() ");
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "---------onStop() ");
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "---------onPause() ");
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "---------onRestart() ");
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---------onDestroy() ");
    }
    
    public String getClssName() {
		return clssName;
	}

	public void setClssName(String clssName) {
		this.clssName = clssName;
	}
	
	/**
	 * 通用消息提示
	 * 
	 * @param resId
	 */
	public void toastAndSpeak(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		soundPlay(msg);
	}
	
	public void soundPlay(String msg) {
		App.getSpeaker().speak(msg);
	}
	
	protected void showMsg(String msg) {
		// 由子类实现，主要为显示数据到界面中的TextView中
	}
	
	// GATT callback
	public BluetoothGattCallback btGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			Log.d(TAG, "onConnectionStateChange()");
			// if connected successfully
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				// discover services
				Message msg = Message.obtain();
				msg.what = Common.MSG_CONNECT_OK;
				if (null == mHandler) {
					Log.d(TAG, "BluetoothGattCallback(): mHandler is null");
				}
				mHandler.sendMessage(msg);
				gatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				// HycoLog.e(TAG,"Disconnected");
				Message msg = Message.obtain();
				msg.what = Common.MSG_CONNECT_FAIL;
				mHandler.sendMessage(msg);
				// HycoLog.d(TAG, "send msg.");
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.d(TAG, "onServicesDiscovered()");
				// pick out the (app side) transmit channel
				currBtService = gatt.getService(CommonProtocol.SERVICE_UUID_READ);
				if (currBtService == null) {
					// HycoLog.e(TAG,"currBtService is null...");
				}
				currBtCharacteristic = currBtService
						.getCharacteristic(CommonProtocol.CHARACTERISTIC_UUID_READ);
				if (currBtCharacteristic == null) {

					System.out.print("currBtCharacteristic is null...");
				} else {
					gatt.setCharacteristicNotification(currBtCharacteristic,
							true);
				}
				System.out.print("Service discovery succeed");
			} else {

				System.out.print("Service discovery failed");
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic chara, int status) {
			Log.d(TAG, "onCharacteristicRead()");
			System.out.print("OnCharWrite" + gatt.getDevice().getName()
					+ " write " + chara.getUuid().toString() + " -> "
					+ new String(chara.getValue()));
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.d(TAG, "onCharacteristicChanged()");
			System.out.print("onCharacteristicChanged: ");
			System.out.print("OnCharWrite" + gatt.getDevice().getName()
					+ " write " + characteristic.getUuid().toString());
			if (clssName == null) {
				Log.d(TAG, "clssName == null");
				return;
			}
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()
			String readStr;

			final byte[] data = characteristic.getValue();
			buffer[0] = 0;
			bytes = data.length;
			Message msg = Message.obtain();
			msg.what = Common.MSG_READ_BUFF;
			msg.arg1 = bytes;
			msg.obj = data;
			mHandler.sendMessage(msg);
			Log.d(TAG, "mHandler.sendMessage(), data is " + data);

			NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification();

			// 自定义声音 声音文件放在ram目录下，没有此目录自己创建一个
			// notification.sound=Uri.parse("android.resource://" +
			// getPackageName() + "/" +R.raw.mm);
			// 使用系统默认声音用下面这条
			notification.defaults = Notification.DEFAULT_SOUND;
			manger.notify(1, notification);

			/*
			 * Uri notification =
			 * RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			 * Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
			 * notification); r.play();
			 */
		}
	};
	
	// add by Felix.Shi
		public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
	        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
	            Log.w(TAG, "BluetoothAdapter not initialized");
	            return;
	        }
	        mBluetoothGatt.writeCharacteristic(characteristic);
	        Log.d(TAG, "write finish!");
	    }
		
		public void sendCommand(byte[] WriteBytes){
			if (sendCommandDetail(WriteBytes)) {
				Log.d(TAG, "sendCommand successful");
			} else {
				Log.d(TAG, "sendCommand failed");
			}
		}
		

		// send command to BLE device
		public boolean sendCommandDetail(byte[] WriteBytes){
	    	BluetoothGattService currentBluetoothGattService = null;
	    	BluetoothGattCharacteristic currentBluetoothGattCharacteristic = null;
	    	ArrayList<BluetoothGatt> connectedGatts = App.getConnectedGatts();
	    	
	    	for (int i = 0; i < connectedGatts.size(); i++) {
	    		Log.d(TAG, "sendCommandDetail(): i=" + i);
	    		BluetoothGatt currGatt = connectedGatts.get(i);
	    		mBluetoothGatt = currGatt;
	    		
	    		

				currentBluetoothGattService = mBluetoothGatt.getService(CommonProtocol.SERVICE_UUID_WRITE);
				if (null == currentBluetoothGattService) {
					Log.d(TAG, "service is null!");
					return false;
				}
		    	currentBluetoothGattCharacteristic = currentBluetoothGattService.getCharacteristic(CommonProtocol.CHARACTERISTIC_UUID_WRITE);
		    	if (null == currentBluetoothGattCharacteristic) {
					Log.d(TAG, "Characteristic is null!");
					return false;
				}          
		        final int charaProp = currentBluetoothGattCharacteristic.getProperties();
		
		        //如果该char可写
		        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		
		            byte[] value = new byte[20];
		            value[0] = (byte) 0x00;
		
		            currentBluetoothGattCharacteristic.setValue(value[0],
		                    BluetoothGattCharacteristic.FORMAT_UINT8, 0);
		            currentBluetoothGattCharacteristic.setValue(WriteBytes);
		
		            writeCharacteristic(currentBluetoothGattCharacteristic);
		        
		        }
		        
		        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
		            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
		                Log.w(TAG, "BluetoothAdapter not initialized");
		                return false;
		            }
		            // 设置当指定characteristic值变化时，发出通知
		            mBluetoothGatt.setCharacteristicNotification(currentBluetoothGattCharacteristic, true);
		        }
			}
	    	
	        return true;
	    }
}
