package com.sinoservices.common.push;
import java.util.List;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.sinoservices.common.Global;
import com.sinoservices.common.R;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;

/**
 * �ٶ������͹�����
 * 
 * @author Jerry
 *
 */
public class BaiDuPushManager {
	private static Context mContext;
	private static BaiDuPushManager baiDuPushManager;

	private BaiDuPushManager() {

	}

	private BaiDuPushManager(Context context) {
		super();
		this.mContext = context;
		initBaiDuPush();
	}

	public static BaiDuPushManager getInstance(Context context) {
		if (baiDuPushManager == null) {
			baiDuPushManager = new BaiDuPushManager(context);
		}
		return baiDuPushManager;
	}

	/**
	 * ��ʼ���ٶ�����������
	 */
	private void initBaiDuPush() {
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
				mContext, R.layout.baidupush_notification_custom_builder,
				R.id.notification_icon, R.id.notification_title,
				R.id.notification_text) {
		};
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(mContext.getApplicationInfo().icon);
		cBuilder.setLayoutDrawable(R.drawable.ic_launcher);
		// Push: �������ڵ���λ�����ͣ����Դ�֧�ֵ���λ�õ����͵Ŀ���
		// PushManager.enableLbs(mContext);
		PushManager.setNotificationBuilder(mContext, 1, cBuilder);
		
		initWithApiKey();
	}

	/**
	 * Push: ���˺ų�ʼ������api key��
	 * ���˺ŵ�½
	 */
	public static void initWithApiKey() {
		PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY,
				Global.BAIDU_PUSH_API);
	}

	/**
	 * �򿪸�ý���б����
	 */
	public static void openRichMediaList() {
		// Push: �򿪸�ý����Ϣ�б�
		Intent sendIntent = new Intent();
		sendIntent.setClassName(mContext,
				"com.baidu.android.pushservice.richmedia.MediaListActivity");
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(sendIntent);
		
	}

	/**
	 * ���ñ�ǩ,��Ӣ�Ķ��Ÿ���
	 */
	public static void setTags(List<String> tags) {
		// Push: ����tag���÷�ʽ
		PushManager.setTags(mContext, tags);
	}

	/**
	 * ɾ��tag����
	 */
	public static void deleteTags(List<String> tags) {
		PushManager.delTags(mContext, tags);
	}
    /**
     * �԰ٶ��˺ŵ�½
     * @param accessToken
     */
	public static void LoginByBaiDu(String accessToken) {
		PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_ACCESS_TOKEN,
				accessToken);
	}
	/**
	 * @Title: StopBaiDuPush 
	 * @Description: ֹͣ
	 * @return void 
	 * @throws
	 */
	public static void StopBaiDuPush(){
		PushManager.stopWork(mContext);
	}

    /**
     * @Title: setNoDisturbPushTime 
     * @Description: ���������ʱ��
     * @param @param startHour
     * @param @param startMinute
     * @param @param endHour
     * @param @param endMinute 
     * @return void 
     * @throws
     */
	public static void setNoDisturbPushTime(int startHour,int startMinute,int endHour,int endMinute){
		PushManager.setNoDisturbMode(mContext, startHour, startMinute, endHour, endMinute);
	}
	/**
	 * @Title: choosePushStyle 
	 * @Description: ��ʽѡ��
	 * @param  what ������ʽ��0 �Զ�����ʽ��1
	 * @return void 
	 * @throws
	 */
	public static void choosePushStyle(int what){
		switch (what) {
		case 0:
			CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
					mContext, R.layout.baidupush_notification_custom_builder,
					R.id.notification_icon, R.id.notification_title,
					R.id.notification_text) {
			};
			cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
			cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE);
			cBuilder.setStatusbarIcon(mContext.getApplicationInfo().icon);
			cBuilder.setLayoutDrawable(R.drawable.ic_launcher);
			// Push: �������ڵ���λ�����ͣ����Դ�֧�ֵ���λ�õ����͵Ŀ���
			// PushManager.enableLbs(mContext);
			PushManager.setNotificationBuilder(mContext, 1, cBuilder);
			break;
		case 1:
				CustomPushNotificationBuilder cBuilder2 = new CustomPushNotificationBuilder(
						mContext, R.layout.baidupush_notification_custom_builder,
						R.id.notification_icon, R.id.notification_title,
						R.id.notification_text) {
				};
				cBuilder2.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
				cBuilder2.setNotificationDefaults(Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE);
				cBuilder2.setStatusbarIcon(R.drawable.bdpush_notify_icon_bg);
				cBuilder2.setLayoutDrawable(R.drawable.bdpush_notify_icon_bg);
				PushManager.setNotificationBuilder(mContext, 2, cBuilder2);
			break;
		default:
			break;
		}
	}

}
