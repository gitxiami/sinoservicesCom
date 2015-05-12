package com.sinoservices.common.smack;

import com.sinoservices.common.exception.XXException;

/**xmpp���������ӿ�**/
public interface Smack {
	/**��¼**/
	public boolean login(String account, String password) throws XXException;
    /**�˳�**/
	public boolean logout();
    /**�Ƿ�����������**/
	public boolean isAuthenticated();

	public void addRosterItem(String user, String alias, String group)
			throws XXException;

	public void removeRosterItem(String user) throws XXException;

	public void renameRosterItem(String user, String newName)
			throws XXException;

	public void moveRosterItemToGroup(String user, String group)
			throws XXException;

	public void renameRosterGroup(String group, String newGroup);

	public void requestAuthorizationForRosterItem(String user);

	public void addRosterGroup(String group);

	public void setStatusFromConfig();

	public void sendMessage(String user, String message);

	public void sendServerPing();

	public String getNameForJID(String jid);
	
}
