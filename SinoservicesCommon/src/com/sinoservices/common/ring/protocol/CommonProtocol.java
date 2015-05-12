package com.sinoservices.common.ring.protocol;

import java.util.UUID;

public class CommonProtocol {
	// 用于写数据的UUID
	public final static UUID SERVICE_UUID_WRITE = UUID
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public final static UUID CHARACTERISTIC_UUID_WRITE = UUID
			.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	
	// 用于读数据的UUID
	public final static UUID SERVICE_UUID_READ = UUID
			.fromString("0000fff0-0000-1000-8000-00805F9B34FB");
	public final static UUID CHARACTERISTIC_UUID_READ = UUID
			.fromString("0000fff4-0000-1000-8000-00805F9B34FB");
	
	public final static int MAX_BLE_DEVICE_NUM = 2;
	
}
