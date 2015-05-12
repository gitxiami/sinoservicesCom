package com.sinoservices.common.service;
/**连接状态改变回调接口**/
public interface IConnectionStatusCallback {
	/**connectedState:连接状态码、reason：原因**/
	public void connectionStatusChanged(int connectedState, String reason);
}
