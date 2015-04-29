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
 * @Description: jsCall����ʵ����
 * @author Jerry 
 * @date 2015��4��28�� ����9:04:19 
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
		aliPayManager = AliPayManager.getInstance(context, mHandler);
		aliPayManager.pay(subject, body, price);
	}

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	@JavascriptInterface
	@Override
	public void check() {
		aliPayManager.check();
	}

	/**
	 * get the sdk version. ��ȡSDK�汾��
	 * 
	 */
	@JavascriptInterface
	@Override
	public void getSDKVersion() {
		aliPayManager.getSDKVersion();
	}

	/**
	 * create the order info. ����������Ϣ
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOrderInfo(String subject, String body, String price) {
		return aliPayManager.getOrderInfo(subject, body, price);
	}

	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getOutTradeNo() {
		return aliPayManager.getOutTradeNo();
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
		return aliPayManager.sign(content);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	@JavascriptInterface
	@Override
	public String getSignType() {
		return aliPayManager.getSignType();
	}
	
	/** ==================֧����֧��end=======================**/
}
