package com.sinoservices.common.jsinterface;

import java.util.List;

/**
 * @ClassName: JsCall 
 * @Description: js与java通信接口
 * @author Jerry 
 * @date 2015年4月28日 上午8:55:50 
 */
public interface JsCallDao {
  /** ==================百度推送=======================**/
  /**打开富媒体界面**/
  public void openRichMediaList();
  /**设置标签**/
  public void setTags(List<String> tags);
  /**删除标签**/
  public void delTags(List<String> tags);
  
  /** ==================百度推送end=======================**/ 
  
}
