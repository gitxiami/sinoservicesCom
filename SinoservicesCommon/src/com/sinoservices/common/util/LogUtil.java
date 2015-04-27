package com.sinoservices.common.util;

import android.util.Log;

/**
 * @ClassName: LogUtil 
 * @Description: 日志打印帮助类
 * @date 2015年4月14日 下午1:31:00 
 */
public class LogUtil {
    /**单例模式**/
	private static LogUtil logUtil;
    /** 是否调试状态 默认为是**/
	public static boolean mIsDebug = true;
	/**日志打印标签  默认为"CommonLog" **/
	private static String mTAG = "CommonLog";
	
	private LogUtil()
	{
	}
	private LogUtil(String tag,boolean isDebug)
	{
		super();
		this.mIsDebug=isDebug;
		this.mTAG=tag;
	}
	/**
	 * @Title: getInstance 
	 * @Description: 获取日志打印类实例
	 * @param @param tag
	 * @param @param isDebug
	 * @return JPushLogUtil 
	 * @throws
	 */
	public synchronized static LogUtil getInstance(String tag,boolean isDebug){
		if(logUtil==null){
			new LogUtil(tag, isDebug);
		}
		return logUtil;
	}

	/**默认tag的函数i**/
	public static void i(String msg)
	{
		if (mIsDebug)
			Log.i(mTAG, msg);
	}
	/**默认tag的函数d**/
	public static void d(String msg)
	{
		if (mIsDebug)
			Log.d(mTAG, msg);
	}
	/**默认tag的函数e**/
	public static void e(String msg)
	{
		if (mIsDebug)
			Log.e(mTAG, msg);
	}
	/**默认tag的函数v**/
	public static void v(String msg)
	{
		if (mIsDebug)
			Log.v(mTAG, msg);
	}

	/**自定义tag的函数i**/
	public static void i(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**自定义tag的函数d**/
	public static void d(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**自定义tag的函数e**/
	public static void e(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**自定义tag的函数v**/
	public static void v(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
}
