package com.sinoservices.common.activity;

import com.sinoservices.common.MainActivity;
import com.sinoservices.common.service.XXService;
/**
 * @ClassName: FragmentCallBack 
 * @Description: FragmentCallBack接口，用于获得XXService与MainActivity实例
 * @author Jerry 
 * @date 2015年5月6日 下午10:16:48 
 *
 */
public interface FragmentCallBack {
	public XXService getService();
	public MainActivity getMainActivity();
}
