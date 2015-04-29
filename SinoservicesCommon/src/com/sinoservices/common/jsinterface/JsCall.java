package com.sinoservices.common.jsinterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.alipay.sdk.app.PayTask;
import com.sinoservices.common.alipay.Constant;
import com.sinoservices.common.alipay.SignUtils;
import com.sinoservices.common.push.BaiDuPushManager;
import com.sinoservices.gaodemap.activity.GeocoderActivity;
import com.sinoservices.gaodemap.activity.LocationActivity;
import com.sinoservices.gaodemap.activity.NaviActivity;
import com.sinoservices.gaodemap.activity.OfflineActivity;
import com.sinoservices.gaodemap.activity.PoiAroundSearchActivity;
import com.sinoservices.gaodemap.activity.PoiKeywordSearchActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		// 订单
		String orderInfo = getOrderInfo(subject, body, price);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = Constant.SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	@JavascriptInterface
	@Override
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask((Activity) context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = Constant.SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	@JavascriptInterface
	@Override
	public void getSDKVersion() {
		PayTask payTask = new PayTask((Activity) context);
		String version = payTask.getVersion();
		Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
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
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/** ==================支付宝支付end=======================**/
	/** =====================高德地图=========================== **/
	/* 定位 */
	public void location(){
		Intent intent=new Intent(context,LocationActivity.class);
		context.startActivity(intent);
	}
	/* 导航 */
	public void navi(){
		Intent intent=new Intent(context,NaviActivity.class);
		context.startActivity(intent);
	}
	/* 地理编码 */
	public void geocoder(){
		Intent intent=new Intent(context,GeocoderActivity.class);
		context.startActivity(intent);
	}
	/* 关键字搜索 */
	public void poikeyword(){
		Intent intent=new Intent(context,PoiKeywordSearchActivity.class);
		context.startActivity(intent);
	}
	/* 周边搜索 */
	public void poiaround(){
		Intent intent=new Intent(context,PoiAroundSearchActivity.class);
		context.startActivity(intent);
	}
	/* 离线地图 */
	public void offlinemap(){
		Intent intent=new Intent(context,OfflineActivity.class);
		context.startActivity(intent);
	}
	/** =====================高德地图end=========================== **/
}
