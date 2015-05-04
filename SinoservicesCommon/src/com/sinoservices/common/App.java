package com.sinoservices.common;


import java.io.File;
import java.util.ArrayList;

import com.baidu.frontia.FrontiaApplication;
import com.sinoservices.common.push.BaiDuPushManager;
import com.sinoservices.common.ring.bluetooth.BTDeviceManager;
import com.sinoservices.common.ring.global.Constants;
import com.sinoservices.common.ring.global.CrashHandler;
import com.sinoservices.common.ring.global.TextSpeaker;
import com.sinoservices.common.ring.protocol.CommonProtocol;
import com.sinoservices.common.util.LogUtil;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Environment;

/**
 * @ClassName: App
 * @Description: �Զ���app��
 * @date 2015��4��27�� ����1:58:18
 */
public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		//��ʼ���ٶ�������
		initBDPush();
		//��ʼ����־������
		initLogMode(Global.LOGTAG,Global.ISDEBUG);

		
		/*ring*/
		instance = this;
		speaker= new TextSpeaker(this);
		mBTDeviceManager = new BTDeviceManager();
		// ��ʼ��ȫ���쳣�������
		CrashHandler.getInstance().init(this);
		initAppPath();
	}
	
	
	/**
	 * @Title: initBDPush 
	 * @Description: ��ʼ���ٶ����͹�����
	 * @return void 
	 * @throws
	 */
	public void initBDPush(){
		FrontiaApplication.initFrontiaApplication(App.this);
		BaiDuPushManager.getInstance(App.this);
	}
    /**
     * @Title: initLogMode 
     * @Description: ��ʼ����־������
     * @param @param tag
     * @param @param isDebug 
     * @return void 
     * @throws
     */
	public void initLogMode(String tag,boolean isDebug){
		LogUtil.getInstance(tag, isDebug);
	}
	
	/*ring*/
	private static App instance;
	public static App getInstance() {
		return instance;
	}
	
	private static TextSpeaker speaker ;
	public static TextSpeaker getSpeaker(){
		return speaker;
	}
	
	// BluetoothDevice which has connected
	private static BluetoothDevice device;
	public static BluetoothDevice getDevice() {
		return device;
	}
	public static void setDevice(BluetoothDevice device) {
		App.device = device;
	}
	
	// BluetoothGatts what have connected
	private static ArrayList<BluetoothGatt> connectedGatts = new ArrayList<BluetoothGatt>(CommonProtocol.MAX_BLE_DEVICE_NUM);
	public static ArrayList<BluetoothGatt> getConnectedGatts() {
		return connectedGatts;
	}
	public static void setConnectedGatts(BluetoothGatt connectedGatts) {
		App.connectedGatts.add(connectedGatts);
	}
	
	
	private static BTDeviceManager mBTDeviceManager;// ����������
	public static BTDeviceManager getmBTDeviceManager() {
		return mBTDeviceManager;
	}
	public static void setmBTDeviceManager(BTDeviceManager mBTDeviceManager) {
		App.mBTDeviceManager = mBTDeviceManager;
	}
/*	
	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		speaker= new TextSpeaker(this);
		mBTDeviceManager = new BTDeviceManager();
		// ��ʼ��ȫ���쳣�������
		CrashHandler.getInstance().init(this);
		initAppPath();

	}
*/	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	private void initAppPath(){
		// ��ʼ��Ӧ�ó����ļ�Ŀ¼
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(Constants.APP_DIR + File.separator);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}
	/*ring end*/
}
