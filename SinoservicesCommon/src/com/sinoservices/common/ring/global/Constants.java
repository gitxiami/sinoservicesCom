package com.sinoservices.common.ring.global;

import java.io.File;

import android.os.Environment;

public class Constants {

	/** 应用的目录 **/
	public static final String APP_DIR = Environment.getExternalStorageDirectory() + File.separator + "Hyco";
	public static final String FILE_DIR = APP_DIR + File.separator + "file";
	public static final String IMAGE_DIR = APP_DIR + File.separator + "image";
	public static final String PHOTO_DIR = APP_DIR + File.separator + "photo";
	public static final String LOG_DIR = APP_DIR + File.separator + "log";
	
}
