package com.sinoservices.common.ring.bluetooth;

/**
 * 
 * @author 
 * some common issue methods and members
 */
public class Common {
	
	//handler msg when client connect server success
	public final static int MSG_CONNECT_OK=1;
	//handler msg when client connect server fail
	public final static int MSG_CONNECT_FAIL=2;
	//hanlder msg when thread read some msg
	public final static int MSG_READ_BUFF=3;
	
	//hanlder msg when thread connect exception
	public final static int MSG_CONNECT_EXCEPTION=4;
	
	//use reflect method avoid connect fail
	public final static boolean CONNECT_USE_REFLECT_METHOD=true;
}