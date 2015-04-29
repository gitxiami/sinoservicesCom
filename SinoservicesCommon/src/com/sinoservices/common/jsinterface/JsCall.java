package com.sinoservices.common.jsinterface;

import java.util.List;
import com.sinoservices.common.R;
import com.sinoservices.common.alipay.AliPayManager;
import com.sinoservices.common.push.BaiDuPushManager;
import com.sinoservices.gaodemap.activity.GeocoderActivity;
import com.sinoservices.gaodemap.activity.LocationActivity;
import com.sinoservices.gaodemap.activity.NaviActivity;
import com.sinoservices.gaodemap.activity.OfflineActivity;
import com.sinoservices.gaodemap.activity.PoiAroundSearchActivity;
import com.sinoservices.gaodemap.activity.PoiKeywordSearchActivity;
import android.app.Activity;
import com.sinoservices.common.push.bdPushUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
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

	@JavascriptInterface
	@Override
	public void setTags() {
		// 设置标签
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		// layout.setBackgroundColor(Color.WHITE);
		final EditText textviewGid = new EditText(context);
		textviewGid.setHint("请输入多个标签，以英文逗号隔开");
		layout.addView(textviewGid);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setPositiveButton("设置标签",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Push: 设置tag调用方式
						List<String> tags = bdPushUtil.getTagsList(textviewGid
								.getText().toString());
						// PushManager.setTags(context, tags);
						baiDuPushManager = BaiDuPushManager
								.getInstance(context);
						baiDuPushManager.setTags(tags);
					}
				});
		builder.show();
	}

	@JavascriptInterface
	@Override
	public void delTags() {
		// 清除标签
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		final EditText textviewGid = new EditText(context);
		textviewGid.setHint("请输入多个标签，以英文逗号隔开");
		layout.addView(textviewGid);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setPositiveButton("清除标签",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Push: 删除tag调用方式
						List<String> tags = bdPushUtil.getTagsList(textviewGid
								.getText().toString());
						// PushManager.delTags(context, tags);
						baiDuPushManager = BaiDuPushManager
								.getInstance(context);
						baiDuPushManager.deleteTags(tags);
					}
				});
		builder.show();
	}
	
	@JavascriptInterface
	@Override
	public void choosePushStyle() {
		// 选择推送样式
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogview = inflater.inflate(R.layout.dialog_bdpush_choosestyle,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogview);
		builder.setTitle("通知样式");
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
//		baiDuPushManager = BaiDuPushManager.getInstance(context);
//		baiDuPushManager.choosePushStyle(0);
	}

	@JavascriptInterface
	@Override
	public void openClosePush(String status) {
		// 百度推送状态切换
		System.out.println("百度推送状态切换");
		if (status != null && status.equals("1")) {
			baiDuPushManager.initWithApiKey();
		} else if (status != null && status.equals("0")) {
			baiDuPushManager.StopBaiDuPush();
		}
	}

	@JavascriptInterface
	@Override
	public void setNoDisturbPushTime() {
		// 设置免打扰推送时间
		/** 一天之中的可允许推送范围的起点时间 **/
		final TimePicker startTime;
		/** 一天之中的可允许推送范围的结束时间点 **/
		final TimePicker endTime;
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogview = inflater
				.inflate(R.layout.dialog_bdpush_settime, null);
		startTime = (TimePicker) dialogview.findViewById(R.id.start_time);
		endTime = (TimePicker) dialogview.findViewById(R.id.end_time);
		// 设置为24小时格式
		startTime.setIs24HourView(DateFormat.is24HourFormat(context));
		endTime.setIs24HourView(DateFormat.is24HourFormat(context));
		// 设置免打扰时间默认为零点到早上七点
		startTime.setCurrentHour(0);
		startTime.setCurrentMinute(0);
		endTime.setCurrentHour(7);
		endTime.setCurrentMinute(0);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogview);
		builder.setTitle("免打扰时间");
		builder.setIcon(R.drawable.bdpush_settime_title_icon_bg);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int startHour = startTime.getCurrentHour();
				int endHour = endTime.getCurrentHour();
				int startMinute = startTime.getCurrentMinute();
				int endMinute = startTime.getCurrentMinute();
				if (startHour > endHour) {
					Toast.makeText(context, "开始时间不能大于结束时间", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				try {
					baiDuPushManager = BaiDuPushManager.getInstance(context);
					baiDuPushManager.setNoDisturbPushTime(startHour,
							startMinute, endHour, endMinute);
					Toast.makeText(context, "设置成功！", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.show();
	}

	/** ==================支付宝支付======================= **/
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
		aliPayManager = AliPayManager.getInstance(context, mHandler);
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
