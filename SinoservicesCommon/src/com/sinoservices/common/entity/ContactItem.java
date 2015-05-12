package com.sinoservices.common.entity;

/**
 * @ClassName: Item
 * @Description: TODO
 */
public class ContactItem {

	/**
	 * 是否是标题
	 */
	private boolean isTitle = false;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 内容拼音
	 */
	private String pinyin;

	/**
	 * @return the isTitle
	 */
	public boolean isTitle() {
		return isTitle;
	}

	/**
	 * @param isTitle
	 *            the isTitle to set
	 */
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the pinyin
	 */
	public String getPinyin() {
		return pinyin;
	}

	/**
	 * @param pinyin
	 *            the pinyin to set
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
