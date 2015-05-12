package com.sinoservices.common.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.sinoservices.common.R;
import com.sinoservices.common.db.RosterProvider;
import com.sinoservices.common.db.RosterProvider.RosterConstants;
import com.sinoservices.common.entity.Roster;
import com.sinoservices.common.util.PinYinUtils;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import com.sinoservices.common.util.StatusMode;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: ContactAdapter 
 * @Description: ��ϵ���б�
 * @author Jerry 
 * @date 2015��5��11�� ����11:41:26 
 *
 */
public class ContactAdapter extends BaseAdapter {
	// ������״̬
	private static final String OFFLINE_EXCLUSION = RosterConstants.STATUS_MODE
			+ " != " + StatusMode.offline.ordinal();
	// ��������
	private static final String COUNT_AVAILABLE_MEMBERS = "SELECT COUNT() FROM "
			+ RosterProvider.TABLE_ROSTER
			+ " inner_query"
			+ " WHERE inner_query."
			+ RosterConstants.GROUP
			+ " = "
			+ RosterProvider.QUERY_ALIAS
			+ "."
			+ RosterConstants.GROUP
			+ " AND inner_query." + OFFLINE_EXCLUSION;
	// ������
	private static final String COUNT_MEMBERS = "SELECT COUNT() FROM "
			+ RosterProvider.TABLE_ROSTER + " inner_query"
			+ " WHERE inner_query." + RosterConstants.GROUP + " = "
			+ RosterProvider.QUERY_ALIAS + "." + RosterConstants.GROUP;
	private static final String[] GROUPS_QUERY_COUNTED = new String[] {
			RosterConstants._ID,
			RosterConstants.GROUP,
			"(" + COUNT_AVAILABLE_MEMBERS + ") || '/' || (" + COUNT_MEMBERS
					+ ") AS members" };
	// ��ϵ�˲�ѯ����
	private static final String[] ROSTER_QUERY = new String[] {
			RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
			RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, };
	//������
	private Context mContext;
	//���ݹ۲���
	private ContentResolver mContentResolver;
	//�Ƿ���ʾ������ϵ��
	private boolean mIsShowOffline;
	//�����б�
	private List<Roster> friendList;
	//ɸѡ��ĺ����б�
	private List<Roster> friendListsort=new ArrayList<Roster>();
	private ArrayList<String> initialList = new ArrayList<String>();
	private LayoutInflater mInflater;
	public ContactAdapter(Context context) {
		super();
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		mContentResolver = context.getContentResolver();
		mIsShowOffline = PreferenceUtils.getPrefBoolean(mContext,
				PreferenceConstants.SHOW_OFFLINE, true);
		//��ȡ�����б�
		this.friendList=getChildrenRosters(null);
	}
    /**��ȡ�����б�**/
	public List<Roster> getChildrenRosters(String groupname) {
		List<Roster> childList = new ArrayList<Roster>();
		String selectWhere = RosterConstants.GROUP + " = ?";
		if (!mIsShowOffline)
			selectWhere += " AND " + OFFLINE_EXCLUSION;
		Cursor childCursor = mContentResolver.query(RosterProvider.CONTENT_URI,
				ROSTER_QUERY, null, null, null);
		childCursor.moveToFirst();
		while (!childCursor.isAfterLast()) {
			Roster roster = new Roster();
			roster.setJid(childCursor.getString(childCursor
					.getColumnIndexOrThrow(RosterConstants.JID)));
			roster.setAlias(childCursor.getString(childCursor
					.getColumnIndexOrThrow(RosterConstants.ALIAS)));
			roster.setStatus_message(childCursor.getString(childCursor
					.getColumnIndexOrThrow(RosterConstants.STATUS_MESSAGE)));
			roster.setStatusMode(childCursor.getString(childCursor
					.getColumnIndexOrThrow(RosterConstants.STATUS_MODE)));
			childList.add(roster);
			childCursor.moveToNext();
		}
		childCursor.close();
		//ɸѡ
		childList=lisSort(childList);
		notifyDataSetChanged();
		return childList;
	}
	
	/**ɸѡ����**/
	public List<Roster> lisSort(List<Roster> childList){
		friendListsort.clear();
		for(int i=0;i<childList.size();i++){
			String pinyin = PinYinUtils.convertWordGroup(childList.get(i).getJid());
			char initial = pinyin.toUpperCase().charAt(0);
			if (!initialList.contains(String.valueOf(initial))) {
				Roster roster=new Roster();
				roster.setTitle(true);
				roster.setPinyin(String.valueOf(initial).toLowerCase());
				friendListsort.add(roster);
				initialList.add(String.valueOf(initial));
			}
			Roster roster=new Roster();
			roster.setTitle(false);
			roster.setAlias(childList.get(i).getAlias());
			roster.setJid(childList.get(i).getJid());
			roster.setStatus_message(childList.get(i).getStatusMessage());
			roster.setStatusMode(childList.get(i).getStatusMode());
			roster.setPinyin(pinyin.toLowerCase());
			friendListsort.add(roster);
		}
		Collections.sort(friendListsort, new Comparator<Roster>() {
			@Override
			public int compare(Roster lhs, Roster rhs) {
				if (lhs.getPinyin().equals("#")) {
					return -1;
				}
				return lhs.getPinyin().compareTo(rhs.getPinyin());
			}
		});
		return friendListsort;
	}
	
	@Override
	public int getCount() {
		return friendListsort.size();
	}

	@Override
	public Object getItem(int position) {
		return friendListsort.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_lv_item, parent,
					false);
			holdView = new HoldView();
			holdView.grouplv = (LinearLayout) convertView
					.findViewById(R.id.contact_lv_item_group);
			holdView.grouptv = (TextView) convertView
					.findViewById(R.id.contact_lv_item_group_tv_name);

			holdView.childlv = (LinearLayout) convertView
					.findViewById(R.id.contact_lv_item_child);
			holdView.childhead = (ImageView) convertView
					.findViewById(R.id.contact_lv_item_child_iv_head);
			holdView.childname = (TextView) convertView
					.findViewById(R.id.contact_lv_item_child_tv_name);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		// �ж��Ƿ�Ϊ����
		if (friendList.get(position).isTitle()) {
			// �Ǳ���
			holdView.grouplv.setVisibility(View.VISIBLE);
			holdView.childlv.setVisibility(View.GONE);
			holdView.grouptv.setText(friendListsort.get(position).getPinyin().toUpperCase());
		} else {
			// �Ǳ���
			holdView.childlv.setVisibility(View.VISIBLE);
			holdView.grouplv.setVisibility(View.GONE);
			holdView.childhead.setBackground(mContext.getResources()
					.getDrawable(R.drawable.contact_item_head_icon));
			holdView.childname.setText(friendListsort.get(position).getJid());
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		//�ж�����Ǳ��������ø�ѡ��ܵ��
		if (friendListsort.get(position).isTitle()) {
			return false;
		}
		return super.isEnabled(position);
	}

	public class HoldView {
		public TextView grouptv;
		public ImageView childhead;
		public TextView childname;
		public LinearLayout grouplv;
		public LinearLayout childlv;
	}
	/**�ṩ���ⲿ���ʻ�ú����б�**/
	public ArrayList<Roster> getFriends(){
		if(friendListsort!=null&&friendListsort.size()>0){
			return (ArrayList<Roster>) friendListsort;
		}
		return null;
	}
}
