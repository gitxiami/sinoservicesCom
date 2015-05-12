package com.sinoservices.common.entity;

public class Roster {
	private String jid;
	private String alias;
	private String statusMode;
	private String statusMessage;
    /**是否是标题**/
	private boolean isTitle = false;
    /**内容拼音**/
	private String pinyin;
	
	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getStatusMode() {
		return statusMode;
	}

	public void setStatusMode(String statusMode) {
		this.statusMode = statusMode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatus_message(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
}
