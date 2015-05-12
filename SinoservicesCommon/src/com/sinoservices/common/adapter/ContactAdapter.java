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
 * @Description: 联系人列表
 * @author Jerry 
 * @date 2015年5月11日 上午11:41:26 
 *
 */
public class ContactAdapter extends BaseAdapter {
	// 不在线状态
	private static final String OFFLINE_EXCLUSION = RosterConstants.STATUS_MODE
			+ " != " + StatusMode.offline.ordinal();
	// 在线人数
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
	// 总人数
	private static final String COUNT_MEMBERS = "SELECT COUNT() FROM "
			+ RosterProvider.TABLE_ROSTER + " inner_query"
			+ " WHERE inner_query." + RosterConstants.GROUP + " = "
			+ RosterProvider.QUERY_ALIAS + "." + RosterConstants.GROUP;
	private static final String[] GROUPS_QUERY_COUNTED = new String[] {
			RosterConstants._ID,
			RosterConstants.GROUP,
			"(" + COUNT_AVAILABLE_MEMBERS + ") || '/' || (" + COUNT_MEMBERS
					+ ") AS members" };
	// 联系人查询序列
	private static final String[] ROSTER_QUERY = new String[] {
			RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
			RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, };
	//上下文
	private Context mContext;
	//内容观察者
	private ContentResolver mContentResolver;
	//是否显示离线联系人
	private boolean mIsShowOffline;
	//好友列表
	private List<Roster> friendList;
	//筛选后的好友列表
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
		//获取好友列表
		this.friendList=getChildrenRosters(null);
	}
    /**获取好友列表**/
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
		//筛选
		childList=lisSort(childList);
		notifyDataSetChanged();
		return childList;
	}
	
	/**筛选方法**/
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
		// 判断是否为标题
		if (friendList.get(position).isTitle()) {
			// 是标题
			holdView.grouplv.setVisibility(View.VISIBLE);
			holdView.childlv.setVisibility(View.GONE);
			holdView.grouptv.setText(friendListsort.get(position).getPinyin().toUpperCase());
		} else {
			// 非标题
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
		//判断如果是标题则设置该选项不能点击
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
	/**提供给外部访问获得好友列表**/
	public ArrayList<Roster> getFriends(){
		if(friendListsort!=null&&friendListsort.size()>0){
			return (ArrayList<Roster>) friendListsort;
		}
		return null;
	}
}
