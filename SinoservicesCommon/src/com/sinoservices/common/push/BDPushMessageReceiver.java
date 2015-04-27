package com.sinoservices.common.push;

import java.util.List;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;

/**
 * Push��Ϣ����receiver�����д����Ҫ�Ļص������� һ����˵�� onBind�Ǳ���ģ���������startWork����ֵ��
 * onMessage��������͸����Ϣ�� onSetTags��onDelTags��onListTags��tag��ز����Ļص���
 * onNotificationClicked��֪ͨ�����ʱ�ص��� onUnbind��stopWork�ӿڵķ���ֵ�ص�
 * 
 * ����ֵ�е�errorCode���������£� 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * �����������Ϸ��ش���ʱ��������Ͳ����������⣬����ͬһ����ķ���ֵrequestId��errorCode��ϵ����׷�����⡣
 * 
 */
public class BDPushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = BDPushMessageReceiver.class
			.getSimpleName();

	/**
	 * ����PushManager.startWork��sdk����push
	 * server�������������������첽�ġ�������Ľ��ͨ��onBind���ء� �������Ҫ�õ������ͣ���Ҫ�������ȡ��channel
	 * id��user id�ϴ���Ӧ��server�У��ٵ���server�ӿ���channel id��user id�������ֻ������û����͡�
	 * 
	 * @param context
	 *            BroadcastReceiver��ִ��Context
	 * @param errorCode
	 *            �󶨽ӿڷ���ֵ��0 - �ɹ�
	 * @param appid
	 *            Ӧ��id��errorCode��0ʱΪnull
	 * @param userId
	 *            Ӧ��user id��errorCode��0ʱΪnull
	 * @param channelId
	 *            Ӧ��channel id��errorCode��0ʱΪnull
	 * @param requestId
	 *            �����˷��������id����׷������ʱ���ã�
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		// L.d(TAG, responseString);
//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONBIND;
//		msg.obj = responseString;
//		handler.sendMessage(msg);

	}

	/**
	 * PushManager.stopWork() �Ļص�������
	 * 
	 * @param context
	 *            ������
	 * @param errorCode
	 *            �����롣0��ʾ�������ͽ�󶨳ɹ�����0��ʾʧ�ܡ�
	 * @param requestId
	 *            ������������͵������id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		// L.d(TAG, responseString);

	}

	/**
	 * ����͸����Ϣ�ĺ�����
	 * 
	 * @param context
	 *            ������
	 * @param message
	 *            ���͵���Ϣ
	 * @param customContentString
	 *            �Զ�������,Ϊ�ջ���json�ַ���
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "͸����Ϣ message=\"" + message;
		// + "\" customContentString=" + customContentString;
		// L.d(TAG, customContentString);

		// // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ͸����Ϣ����ʱ�Զ������������õļ���ֵ
		// if (!TextUtils.isEmpty(customContentString)) {
		// JSONObject customJson = null;
		// try {
		// customJson = new JSONObject(customContentString);
		// String myvalue = null;
		// if (!customJson.isNull("mykey")) {
		// myvalue = customJson.getString("mykey");
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONMESSAGE;
//		msg.obj = messageString;
//		handler.sendMessage(msg);
	}

	/**
	 * ����֪ͨ����ĺ�����ע������֪ͨ���û����ǰ��Ӧ���޷�ͨ���ӿڻ�ȡ֪ͨ�����ݡ�
	 * 
	 * @param context
	 *            ������
	 * @param title
	 *            ���͵�֪ͨ�ı���
	 * @param description
	 *            ���͵�֪ͨ������
	 * @param customContentString
	 *            �Զ������ݣ�Ϊ�ջ���json�ַ���
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		// L.d(TAG, notifyString);

		// �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ֪ͨ����ʱ�Զ������������õļ���ֵ
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONTIFICATIONCLICK;
//		msg.obj = notifyString;
//		handler.sendMessage(msg);
	}

	/**
	 * setTags() �Ļص�������
	 * 
	 * @param context
	 *            ������
	 * @param errorCode
	 *            �����롣0��ʾĳЩtag�Ѿ����óɹ�����0��ʾ����tag�����þ�ʧ�ܡ�
	 * @param successTags
	 *            ���óɹ���tag
	 * @param failTags
	 *            ����ʧ�ܵ�tag
	 * @param requestId
	 *            ������������͵������id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		// L.d(TAG, responseString);

//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONSETTAGS;
//		msg.obj = responseString;
//		handler.sendMessage(msg);
	}

	/**
	 * delTags() �Ļص�������
	 * 
	 * @param context
	 *            ������
	 * @param errorCode
	 *            �����롣0��ʾĳЩtag�Ѿ�ɾ���ɹ�����0��ʾ����tag��ɾ��ʧ�ܡ�
	 * @param successTags
	 *            �ɹ�ɾ����tag
	 * @param failTags
	 *            ɾ��ʧ�ܵ�tag
	 * @param requestId
	 *            ������������͵������id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		// L.d(TAG, responseString);

//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONDELTAGS;
//		msg.obj = responseString;
//		handler.sendMessage(msg);
	}

	/**
	 * listTags() �Ļص�������
	 * 
	 * @param context
	 *            ������
	 * @param errorCode
	 *            �����롣0��ʾ�о�tag�ɹ�����0��ʾʧ�ܡ�
	 * @param tags
	 *            ��ǰӦ�����õ�����tag��
	 * @param requestId
	 *            ������������͵������id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		// L.d(TAG, responseString);

//		Handler handler = BaiDuPushMsgHandler.getmHandler();
//		Message msg = handler.obtainMessage();
//		msg.what = BaiDuPushMsgHandler.MSG_ONLISTTAGS;
//		msg.obj = responseString;
//		handler.sendMessage(msg);
	}
}
