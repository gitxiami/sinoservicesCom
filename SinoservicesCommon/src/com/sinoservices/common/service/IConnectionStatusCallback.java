package com.sinoservices.common.service;
/**����״̬�ı�ص��ӿ�**/
public interface IConnectionStatusCallback {
	/**connectedState:����״̬�롢reason��ԭ��**/
	public void connectionStatusChanged(int connectedState, String reason);
}
