package com.sinoservices.common.jsinterface;

import java.util.List;

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
  
}
