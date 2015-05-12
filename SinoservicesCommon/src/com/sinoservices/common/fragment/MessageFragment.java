package com.sinoservices.common.fragment;

import com.sinoservices.common.R;
import com.sinoservices.common.activity.ChatActivity;
import com.sinoservices.common.activity.FragmentCallBack;
import com.sinoservices.common.adapter.RecentChatAdapter;
import com.sinoservices.common.db.ChatProvider;
import com.sinoservices.common.db.ChatProvider.ChatConstants;
import com.sinoservices.common.swipelistview.BaseSwipeListViewListener;
import com.sinoservices.common.swipelistview.SwipeListView;
import com.sinoservices.common.util.L;
import com.sinoservices.common.util.XMPPHelper;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
/**
 * @ClassName: MessageFragment
 * @Description: 消息页面
 * @author Jerry
 * @date 2015年4月26日 上午9:55:37
 *
 */
public class MessageFragment extends Fragment implements OnClickListener {
	private Handler mainHandler = new Handler();
	private ContentObserver mChatObserver = new ChatObserver();
	private ContentResolver mContentResolver;
	private SwipeListView mSwipeListView;
	private RecentChatAdapter mRecentChatAdapter;
	private FragmentCallBack mFragmentCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mFragmentCallBack = (FragmentCallBack) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContentResolver = getActivity().getContentResolver();
		mRecentChatAdapter = new RecentChatAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		mRecentChatAdapter.requery();
		mContentResolver.registerContentObserver(ChatProvider.CONTENT_URI,
				true, mChatObserver);
	}

	@Override
	public void onPause() {
		super.onPause();
		mContentResolver.unregisterContentObserver(mChatObserver);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		mSwipeListView = (SwipeListView) view
				.findViewById(R.id.recent_listview);
		mSwipeListView.setEmptyView(view.findViewById(R.id.recent_empty));
		mSwipeListView.setAdapter(mRecentChatAdapter);
		mSwipeListView.setSwipeListViewListener(mSwipeListViewListener);

	}

	public void updateRoster() {
		mRecentChatAdapter.requery();
	}

	private class ChatObserver extends ContentObserver {
		public ChatObserver() {
			super(mainHandler);
		}

		public void onChange(boolean selfChange) {
			updateRoster();
			L.i("liweiping", "selfChange" + selfChange);
		}
	}

	BaseSwipeListViewListener mSwipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onClickFrontView(int position) {
                 //跳转到聊天界面
				Cursor clickCursor = mRecentChatAdapter.getCursor();
				clickCursor.moveToPosition(position);
				String jid = clickCursor.getString(clickCursor
						.getColumnIndex(ChatConstants.JID));
				Uri userNameUri = Uri.parse(jid);
				Intent toChatIntent = new Intent(getActivity(), ChatActivity.class);
				toChatIntent.setData(userNameUri);
				toChatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME,
						XMPPHelper.splitJidAndServer(jid));
				startActivity(toChatIntent);
		}

		@Override
		public void onClickBackView(int position) {
			mSwipeListView.closeOpenedItems();// 关闭打开的项
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.ivTitleBtnRightImage:
		// XXService xxService = mFragmentCallBack.getService();
		// if (xxService == null || !xxService.isAuthenticated()) {
		// return;
		// }
		// new AddRosterItemDialog(mFragmentCallBack.getMainActivity(),
		// xxService).show();// 添加联系人
		// break;
		default:
			break;
		}
	}
}
