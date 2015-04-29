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
 * @Description: jsCall����ʵ����
 * @author Jerry 
 * @date 2015��4��28�� ����9:04:19 
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
		// �򿪸�ý���б�
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
		// ѡ��������ʽ

	}

	@JavascriptInterface
	@Override
	public void openClosePush(String status) {
		// TODO Auto-generated method stub
		System.out.println("�ٶ�����״̬�л�");
		if(status!=null&&status.equals("1")){
			baiDuPushManager.initWithApiKey();
		}else if(status!=null&&status.equals("0")){
			baiDuPushManager.StopBaiDuPush();			
		}
	}
	
	/** ==================֧����֧��=======================**/
	/**
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	@JavascriptInterface
	@Override
	public void pay(String subject, String body, String price) {
		// ����
		String orderInfo = getOrderInfo(subject, body, price);

		// �Զ�����RSA ǩ��
		String sign = sign(orderInfo);
		try {
			// �����sign ��URL����
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask((Activity) context);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = Constant.SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	@JavascriptInterface
	@Override
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask((Activity) context);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
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
	 * get the sdk version. ��ȡSDK�汾��
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
	 * create the order info. ����������Ϣ
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOrderInfo(String subject, String body, String price) {
		// ǩԼ���������ID
		String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

		// ǩԼ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// ����ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// ֧�����ͣ� �̶�ֵ
		orderInfo += "&payment_type=\"1\"";

		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";

		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		orderInfo += "&return_url=\"m.alipay.com\"";

		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
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
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	@JavascriptInterface
	@Override
	public String sign(String content) {
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/** ==================֧����֧��end=======================**/
	/** =====================�ߵµ�ͼ=========================== **/
	/* ��λ */
	public void location(){
		Intent intent=new Intent(context,LocationActivity.class);
		context.startActivity(intent);
	}
	/* ���� */
	public void navi(){
		Intent intent=new Intent(context,NaviActivity.class);
		context.startActivity(intent);
	}
	/* ������� */
	public void geocoder(){
		Intent intent=new Intent(context,GeocoderActivity.class);
		context.startActivity(intent);
	}
	/* �ؼ������� */
	public void poikeyword(){
		Intent intent=new Intent(context,PoiKeywordSearchActivity.class);
		context.startActivity(intent);
	}
	/* �ܱ����� */
	public void poiaround(){
		Intent intent=new Intent(context,PoiAroundSearchActivity.class);
		context.startActivity(intent);
	}
	/* ���ߵ�ͼ */
	public void offlinemap(){
		Intent intent=new Intent(context,OfflineActivity.class);
		context.startActivity(intent);
	}
	/** =====================�ߵµ�ͼend=========================== **/
}
