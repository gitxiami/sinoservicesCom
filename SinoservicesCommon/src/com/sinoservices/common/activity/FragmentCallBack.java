package com.sinoservices.common.activity;

import com.sinoservices.common.MainActivity;
import com.sinoservices.common.service.XXService;
/**
 * @ClassName: FragmentCallBack 
 * @Description: FragmentCallBack�ӿڣ����ڻ��XXService��MainActivityʵ��
 * @author Jerry 
 * @date 2015��5��6�� ����10:16:48 
 *
 */
public interface FragmentCallBack {
	public XXService getService();
	public MainActivity getMainActivity();
}
