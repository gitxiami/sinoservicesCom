package com.sinoservices.common.jsinterface;

import java.util.List;

import android.view.View;

/**
 * @ClassName: JsCall 
 * @Description: js��javaͨ�Žӿ�
 * @author Jerry 
 * @date 2015��4��28�� ����8:55:50 
 */
public interface JsCallDao {
  /** ==================�ٶ�����=======================**/
  /**�򿪸�ý�����**/
  public void openRichMediaList();
  /**���ñ�ǩ**/
  public void setTags(List<String> tags);
  /**ɾ����ǩ**/
  public void delTags(List<String> tags);
  
  /** ==================�ٶ�����end=======================**/
  
  
  /** ==================֧����֧��=======================**/
  /*call alipay sdk pay. ����SDK֧��*/
  public void pay(String subject, String body, String price);
  /*check whether the device has authentication alipay account.
  	��ѯ�ն��豸�Ƿ����֧������֤�˻�*/
  public void check(View v);
  /*get the sdk version. ��ȡSDK�汾��*/
  public void getSDKVersion();
  /*create the order info. ����������Ϣ*/
  public String getOrderInfo(String subject, String body, String price);
  /*get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��*/
  public String getOutTradeNo();
  /*sign the order info. �Զ�����Ϣ����ǩ��*/
  public String sign(String content);
  /*get the sign type we use. ��ȡǩ����ʽ*/
  public String getSignType();
  /** ==================֧����֧��end=======================**/
  
}
