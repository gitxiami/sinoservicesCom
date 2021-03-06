package com.sinoservices.common.jsinterface;

import java.util.List;

import android.view.View;

/**
 * @ClassName: JsCall
 * @Description: js与java通信接口
 * @author Jerry
 * @date 2015年4月28日 上午8:55:50
 */
public interface JsCallDao {
	/** ==================百度推送======================= **/
	/** 开关推送 **/
	public void openClosePush(String status);

	/** 打开富媒体界面 **/
	public void openRichMediaList();

	/** 设置标签 **/
	public void setTags();

	/** 删除标签 **/
	public void delTags();

	/** 选择推送样式 **/
	public void choosePushStyle();
    
	/**设置推送时间范围**/
	public void setNoDisturbPushTime();
	/** ==================百度推送end======================= **/

	/** ==================支付宝支付======================= **/
	/* call alipay sdk pay. 调用SDK支付 */
	public void pay(String subject, String body, String price);

	/*
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 */
	public void check();

	/* get the sdk version. 获取SDK版本号 */
	public void getSDKVersion();

	/* create the order info. 创建订单信息 */
	public String getOrderInfo(String subject, String body, String price);

	/* get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范） */
	public String getOutTradeNo();

	/* sign the order info. 对订单信息进行签名 */
	public String sign(String content);

	/* get the sign type we use. 获取签名方式 */
	public String getSignType();

	/** ==================支付宝支付end======================= **/
	
	/** ==================微信支付======================= **/
	
	/* 微信APP支付生成预支付订单 */
	public void wxGenPrePayOrder(String body, String price);
	
	/* 调用微信支付 */
	public void wxPay();
	/** ==================微信支付end======================= **/
	
	/** =====================高德地图=========================== **/
	/* 定位 */
	public void location();
	/* 导航 */
	public void navi();
	/* 地理编码 */
	public void geocoder();
	/* 关键字搜索 */
	public void poikeyword();
	/* 周边搜索 */
	public void poiaround();
	/* 离线地图 */
	public void offlinemap();
	/** =====================高德地图end=========================== **/
}
