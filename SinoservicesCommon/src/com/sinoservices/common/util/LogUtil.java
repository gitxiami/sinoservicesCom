package com.sinoservices.common.util;

import android.util.Log;

/**
 * @ClassName: LogUtil 
 * @Description: ��־��ӡ������
 * @date 2015��4��14�� ����1:31:00 
 */
public class LogUtil {
    /**����ģʽ**/
	private static LogUtil logUtil;
    /** �Ƿ����״̬ Ĭ��Ϊ��**/
	public static boolean mIsDebug = true;
	/**��־��ӡ��ǩ  Ĭ��Ϊ"CommonLog" **/
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
	 * @Description: ��ȡ��־��ӡ��ʵ��
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

	/**Ĭ��tag�ĺ���i**/
	public static void i(String msg)
	{
		if (mIsDebug)
			Log.i(mTAG, msg);
	}
	/**Ĭ��tag�ĺ���d**/
	public static void d(String msg)
	{
		if (mIsDebug)
			Log.d(mTAG, msg);
	}
	/**Ĭ��tag�ĺ���e**/
	public static void e(String msg)
	{
		if (mIsDebug)
			Log.e(mTAG, msg);
	}
	/**Ĭ��tag�ĺ���v**/
	public static void v(String msg)
	{
		if (mIsDebug)
			Log.v(mTAG, msg);
	}

	/**�Զ���tag�ĺ���i**/
	public static void i(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**�Զ���tag�ĺ���d**/
	public static void d(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**�Զ���tag�ĺ���e**/
	public static void e(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
	/**�Զ���tag�ĺ���v**/
	public static void v(String tag, String msg)
	{
		if (mIsDebug)
			Log.i(tag, msg);
	}
}
