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

	@JavascriptInterface
	@Override
	public void setTags() {
		// ���ñ�ǩ
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		// layout.setBackgroundColor(Color.WHITE);
		final EditText textviewGid = new EditText(context);
		textviewGid.setHint("����������ǩ����Ӣ�Ķ��Ÿ���");
		layout.addView(textviewGid);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setPositiveButton("���ñ�ǩ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Push: ����tag���÷�ʽ
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
		// �����ǩ
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		final EditText textviewGid = new EditText(context);
		textviewGid.setHint("����������ǩ����Ӣ�Ķ��Ÿ���");
		layout.addView(textviewGid);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setPositiveButton("�����ǩ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Push: ɾ��tag���÷�ʽ
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
		// ѡ��������ʽ
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogview = inflater.inflate(R.layout.dialog_bdpush_choosestyle,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogview);
		builder.setTitle("֪ͨ��ʽ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
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
		// �ٶ�����״̬�л�
		System.out.println("�ٶ�����״̬�л�");
		if (status != null && status.equals("1")) {
			baiDuPushManager.initWithApiKey();
		} else if (status != null && status.equals("0")) {
			baiDuPushManager.StopBaiDuPush();
		}
	}

	@JavascriptInterface
	@Override
	public void setNoDisturbPushTime() {
		// �������������ʱ��
		/** һ��֮�еĿ��������ͷ�Χ�����ʱ�� **/
		final TimePicker startTime;
		/** һ��֮�еĿ��������ͷ�Χ�Ľ���ʱ��� **/
		final TimePicker endTime;
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogview = inflater
				.inflate(R.layout.dialog_bdpush_settime, null);
		startTime = (TimePicker) dialogview.findViewById(R.id.start_time);
		endTime = (TimePicker) dialogview.findViewById(R.id.end_time);
		// ����Ϊ24Сʱ��ʽ
		startTime.setIs24HourView(DateFormat.is24HourFormat(context));
		endTime.setIs24HourView(DateFormat.is24HourFormat(context));
		// ���������ʱ��Ĭ��Ϊ��㵽�����ߵ�
		startTime.setCurrentHour(0);
		startTime.setCurrentMinute(0);
		endTime.setCurrentHour(7);
		endTime.setCurrentMinute(0);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogview);
		builder.setTitle("�����ʱ��");
		builder.setIcon(R.drawable.bdpush_settime_title_icon_bg);
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int startHour = startTime.getCurrentHour();
				int endHour = endTime.getCurrentHour();
				int startMinute = startTime.getCurrentMinute();
				int endMinute = startTime.getCurrentMinute();
				if (startHour > endHour) {
					Toast.makeText(context, "��ʼʱ�䲻�ܴ��ڽ���ʱ��", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				try {
					baiDuPushManager = BaiDuPushManager.getInstance(context);
					baiDuPushManager.setNoDisturbPushTime(startHour,
							startMinute, endHour, endMinute);
					Toast.makeText(context, "���óɹ���", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.show();
	}

	/** ==================֧����֧��======================= **/
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
		aliPayManager = AliPayManager.getInstance(context, mHandler);
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
