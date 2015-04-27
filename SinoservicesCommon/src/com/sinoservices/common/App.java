package com.sinoservices.common;

import com.baidu.frontia.FrontiaApplication;
import com.sinoservices.common.push.BaiDuPushManager;
import com.sinoservices.common.util.LogUtil;

import android.app.Application;

/**
 * @ClassName: App
 * @Description: 自定义app类
 * @date 2015年4月27日 下午1:58:18
 */
public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化百度云推送
		initBDPush();
		//初始化日志管理器
		initLogMode(Global.LOGTAG,Global.ISDEBUG);
	}
	
	/**
	 * @Title: initBDPush 
	 * @Description: 初始化百度推送管理类
	 * @return void 
	 * @throws
	 */
	public void initBDPush(){
		FrontiaApplication.initFrontiaApplication(App.this);
		BaiDuPushManager.getInstance(App.this);
	}
    /**
     * @Title: initLogMode 
     * @Description: 初始化日志管理器
     * @param @param tag
     * @param @param isDebug 
     * @return void 
     * @throws
     */
	public void initLogMode(String tag,boolean isDebug){
		LogUtil.getInstance(tag, isDebug);
	}
	
	
}
