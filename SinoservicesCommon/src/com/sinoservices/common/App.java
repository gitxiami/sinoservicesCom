package com.sinoservices.common;

import com.baidu.frontia.FrontiaApplication;
import com.sinoservices.common.push.BaiDuPushManager;

import android.app.Application;

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
	
}
