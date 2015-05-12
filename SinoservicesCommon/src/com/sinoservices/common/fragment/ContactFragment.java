package com.sinoservices.common.fragment;

import java.util.ArrayList;
import com.sinoservices.common.MainActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.XXBroadcastReceiver;
import com.sinoservices.common.XXBroadcastReceiver.EventHandler;
import com.sinoservices.common.activity.ChatActivity;
import com.sinoservices.common.activity.FragmentCallBack;
import com.sinoservices.common.adapter.ContactAdapter;
import com.sinoservices.common.db.RosterProvider;
import com.sinoservices.common.entity.Roster;
import com.sinoservices.common.service.IConnectionStatusCallback;
import com.sinoservices.common.service.XXService;
import com.sinoservices.common.util.NetUtil;
import com.sinoservices.common.util.PreferenceConstants;
import com.sinoservices.common.util.PreferenceUtils;
import com.sinoservices.common.util.T;
import com.sinoservices.common.util.XMPPHelper;
import com.sinoservices.common.view.ContactSideBar;
import com.sinoservices.common.view.ContactSideBar.OnTouchingChangedListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @ClassName: ContactFragment
 * @Description: ͨ��¼ҳ��
 * @author Jerry
 * @date 2015��4��26�� ����10:20:00
 */
public class ContactFragment extends Fragment implements OnClickListener,
		IConnectionStatusCallback, EventHandler, FragmentCallBack {
	/**��ϵ���ұ߻������**/
	private ContactSideBar sideBar;
	/**ҳ���м�������ı��������ұ����ʱ�����ʾ**/
	private TextView textView;
	/**��ϵ���б�listview**/
	private ListView listView;
	
	private LayoutInflater inflater;
	/**listview��ͷ������**/
	private View contact_head_view;
    /**xmpp���ķ���**/
	private XXService mXxService;
	/**���ڸ��º����б�**/
	private Handler mainHandler = new Handler();
	/**��ϵ���б�������**/
	private ContactAdapter contactAdapter;
	/**�����б�۲���**/
	private ContentObserver mRosterObserver = new RosterObserver();
	
	ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXxService = ((XXService.XXBinder) service).getService();
			mXxService.registerConnectionStatusCallback(ContactFragment.this);
			// ��ʼ����xmpp������
			if (!mXxService.isAuthenticated()) {
				String usr = PreferenceUtils.getPrefString(getActivity(),
						PreferenceConstants.ACCOUNT, "");
				String password = PreferenceUtils.getPrefString(getActivity(),
						PreferenceConstants.PASSWORD, "");
				mXxService.Login(usr, password);
			} else {
				
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews();
	}

	private void initViews() {
		sideBar = (ContactSideBar) getView().findViewById(R.id.sideBar);
		textView = (TextView) getView().findViewById(R.id.textview);
		listView = (ListView) getView().findViewById(R.id.listview);
		inflater = getActivity().getLayoutInflater();
		contact_head_view = inflater.inflate(R.layout.contact_lv_head, null);
		// ����ͷ������¼�
		initListHeadView();
		listView.addHeaderView(contact_head_view, null, false);
		sideBar.setShowChooseText(textView);
		contactAdapter = new ContactAdapter(getActivity());
		listView.setAdapter(contactAdapter);
		sideBar.setOnTouchingChangedListener(new OnTouchingChangedListener() {
			@Override
			public void onTouchingChanged(String s) {
				ArrayList<Roster> list = contactAdapter.getFriends();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i)!=null&&list.get(i).getPinyin().equals(s)) {
						listView.setSelection(i);
						return;
					}
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<Roster> list = contactAdapter.getFriends();
				if(list.get(position-1)!=null&&list.get(position-1).getJid()!=null&&!list.get(position-1).getJid().equals("")){
					String jid = list.get(position-1).getJid();
					Uri userNameUri = Uri.parse(jid);
					//��ת���������
					Intent toChatIntent = new Intent(getActivity(), ChatActivity.class);
					toChatIntent.setData(userNameUri);
					toChatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME,
							XMPPHelper.splitJidAndServer(jid));
					startActivity(toChatIntent);
				}
			}
		});
	}

	/** ����listviewͷ���ĵ���¼� **/
	private void initListHeadView() {
		LinearLayout head_item1 = (LinearLayout) contact_head_view
				.findViewById(R.id.contact_lv_head_item1);
		LinearLayout head_item2 = (LinearLayout) contact_head_view
				.findViewById(R.id.contact_lv_head_item2);
		LinearLayout head_item3 = (LinearLayout) contact_head_view
				.findViewById(R.id.contact_lv_head_item3);
		head_item1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "�������Ⱥ�飡", 0).show();
			}
		});

		head_item2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "������������飡", 0).show();
			}
		});

		head_item3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "���������֯������", 0).show();
			}
		});
	}

	@Override
	public XXService getService() {
		return mXxService;
	}

	@Override
	public MainActivity getMainActivity() {
		return null;
	}

	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(getActivity()) == NetUtil.NETWORN_NONE) {
			T.showShort(getActivity(), "���粻���ã�");
		}
	}

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		// ����״̬�ı�

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
    /**���º����б�-ˢ������������**/
	public void updateRoster() {
		contactAdapter.getChildrenRosters(null);
	}
    /**���ѻ���������۲���**/
	private class RosterObserver extends ContentObserver {
		public RosterObserver() {
			super(mainHandler);
		}
		public void onChange(boolean selfChange) {
			if (contactAdapter != null)
				mainHandler.postDelayed(new Runnable() {
					public void run() {
						//����
						updateRoster();
					}
				}, 100);
		}
	}
	/**�󶨷���**/
	private void bindXMPPService() {
		getActivity().bindService(new Intent(getActivity(), XXService.class),
				mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}
    /**����󶨷���**/
	private void unbindXMPPService() {
		try {
			getActivity().unbindService(mServiceConnection);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		bindXMPPService();
		getActivity().getContentResolver().registerContentObserver(
				RosterProvider.CONTENT_URI, true, mRosterObserver);
		XXBroadcastReceiver.mListeners.add(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().getContentResolver().unregisterContentObserver(mRosterObserver);
		unbindXMPPService();
		XXBroadcastReceiver.mListeners.remove(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}