package com.sinoservices.common.jsinterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.alipay.sdk.app.PayTask;
import com.sinoservices.common.alipay.AliPayManager;
import com.sinoservices.common.alipay.Constant;
import com.sinoservices.common.alipay.SignUtils;
import com.sinoservices.common.push.BaiDuPushManager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
/**
 * @ClassName: JsCall 
 * @Description: jsCall具体实现类
 * @author Jerry 
 * @date 2015年4月28日 上午9:04:19 
 */
public class JsCall implements JsCallDao {
	private BaiDuPushManager baiDuPushManager;

	Context context;
	Handler mHandler;
	
	private AliPayManager aliPayManager;

	public JsCall() {
		super();
	}

	public JsCall(Context context, Handler mHandler) {
		super();
		this.context = context;
		this.mHandler = mHandler;
	}

	@JavascriptInterface
	@Override
	public void openRichMediaList() {
		// 打开富媒体列表
		baiDuPushManager = BaiDuPushManager.getInstance(context);
		baiDuPushManager.openRichMediaList();
	}

	@Override
	public void setTags(List<String> tags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delTags(List<String> tags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void choosePushStyle() {
		// 选择推送样式

	}

	@JavascriptInterface
	@Override
	public void openClosePush(String status) {
		// TODO Auto-generated method stub
		System.out.println("百度推送状态切换");
		if(status!=null&&status.equals("1")){
			baiDuPushManager.initWithApiKey();
		}else if(status!=null&&status.equals("0")){
			baiDuPushManager.StopBaiDuPush();			
		}
	}
	
	/** ==================支付宝支付=======================**/
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	@JavascriptInterface
	@Override
	public void pay(String subject, String body, String price) {
		aliPayManager = AliPayManager.getInstance(context, mHandler);
		aliPayManager.pay(subject, body, price);
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	@JavascriptInterface
	@Override
	public void check() {
		aliPayManager.check();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	@JavascriptInterface
	@Override
	public void getSDKVersion() {
		aliPayManager.getSDKVersion();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOrderInfo(String subject, String body, String price) {
		return aliPayManager.getOrderInfo(subject, body, price);
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOutTradeNo() {
		return aliPayManager.getOutTradeNo();
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	@JavascriptInterface
	@Override
	public String sign(String content) {
		return aliPayManager.sign(content);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getSignType() {
		return aliPayManager.getSignType();
	}
	
	/** ==================支付宝支付end=======================**/
}
