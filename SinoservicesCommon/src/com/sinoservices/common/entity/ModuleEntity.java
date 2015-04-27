package com.sinoservices.common.entity;

import com.sinoservices.common.db.Entity;

/**
 * @ClassName: ModuleEntity
 * @Description: Ӧ��ģ������ģ�ͱ�
 * @author Jerry
 * @date 2015��4��27�� ����4:40:21
 * 
 */
public class ModuleEntity extends Entity {

	private String modulename;
	private String moduleurl;
	private String modulestatus;
	private int moduleid;

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public String getModuleurl() {
		return moduleurl;
	}

	public void setModuleurl(String moduleurl) {
		this.moduleurl = moduleurl;
	}

	public String getModulestatus() {
		return modulestatus;
	}

	public void setModulestatus(String modulestatus) {
		this.modulestatus = modulestatus;
	}

	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}

	public ModuleEntity() {
	}

}
