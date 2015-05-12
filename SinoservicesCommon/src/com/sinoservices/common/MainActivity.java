package com.sinoservices.common;

import com.sinoservices.common.XXBroadcastReceiver.EventHandler;
import com.sinoservices.common.ResideMenu.ResideMenu;
import com.sinoservices.common.ResideMenu.ResideMenuInfo;
import com.sinoservices.common.ResideMenu.ResideMenuItem;
import com.sinoservices.common.activity.APPGlobalSettingActivity;
import com.sinoservices.common.activity.AssistantActivity;
import com.sinoservices.common.activity.FragmentCallBack;
import com.sinoservices.common.activity.PersonDataActivity;
import com.sinoservices.common.activity.SearchFriendActivity;
import com.sinoservices.common.fragment.AppFragment;
import com.sinoservices.common.fragment.ContactFragment;
import com.sinoservices.common.fragment.MessageFragment;
import com.sinoservices.common.quickaction.ActionItem;
import com.sinoservices.common.quickaction.QuickAction;
import com.sinoservices.common.quickaction.QuickAction.OnActionItemClickListener;
import com.sinoservices.common.service.IConnectionStatusCallback;
import com.sinoservices.common.service.XXService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoservices.common.activity.BaseActivity;
/**
 * @ClassName: MainActivity 
 * @Description: 程序主Activity
 * @date 2015年5月6日 下午11:22:09 
 */
public class MainActivity extends BaseActivity implements
		View.OnClickListener, IConnectionStatusCallback, EventHandler,
		FragmentCallBack {
	
	private ResideMenu resideMenu;
	/**我的待办**/
	private ResideMenuItem itemDaiban;
	/**我的日程**/
	private ResideMenuItem itemRiCheng;
	/**我的邮件**/
	private ResideMenuItem itemMail;
	/**我的联系人**/
	private ResideMenuItem itemContact;

	private ResideMenuInfo info;
	/** 底下四个选项按钮 **/
	private LinearLayout main_ly_tab1;
	private LinearLayout main_ly_tab2;
	private LinearLayout main_ly_tab3;
	private LinearLayout main_ly_tab4;

	private TextView main_bottom_tab1_tv = null;
	private TextView main_bottom_tab2_tv = null;
	private TextView main_bottom_tab3_tv = null;
	private TextView main_bottom_tab4_tv = null;
    /**顶部标题tv**/
	private TextView main_title_bar_tv;
	/**顶部右边+号菜单**/
	private Button title_bar_right_menu;
	
	/**标识滑动抽屉是否是关闭状态**/
	private boolean is_closed = false;
	private long mExitTime;
	/**左边按钮**/
	private Button leftMenu;
    /**是否第一次进来**/
	private boolean isFirstIn=true;
	/**设置**/
	private LinearLayout layoutSetting;
	/**当前索引项**/
	private int currentIndex=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		setUpMenu();
		changeFragment(new MessageFragment());
		setListener();
		selectIndex(1);
	}
    /**设置左边菜单**/
	private void setUpMenu() {
		leftMenu = (Button) findViewById(R.id.title_bar_left_menu);
		main_ly_tab1 = (LinearLayout) findViewById(R.id.main_ly_tab1);
		main_ly_tab2 = (LinearLayout) findViewById(R.id.main_ly_tab2);
		main_ly_tab3 = (LinearLayout) findViewById(R.id.main_ly_tab3);
		main_ly_tab4 = (LinearLayout) findViewById(R.id.main_ly_tab4);
		//顶部标题文字
		main_title_bar_tv = (TextView) findViewById(R.id.main_title_bar_tv);
		//顶部右边加号按钮
		title_bar_right_menu=(Button) findViewById(R.id.title_bar_right_menu);
		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_background);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);
		// 禁止使用右侧菜单
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		// create menu items;
		itemDaiban = new ResideMenuItem(this, R.drawable.residmenu_itme1_bg,
				"我的待办");
		itemRiCheng = new ResideMenuItem(this, R.drawable.residmenu_itme2_bg,
				"我的日程");
		itemMail = new ResideMenuItem(this, R.drawable.residmenu_itme3_bg,
				"我的邮件");
		itemContact = new ResideMenuItem(this, R.drawable.residmenu_itme4_bg,
				"我的联系人");

		resideMenu.addMenuItem(itemDaiban, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemRiCheng, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemMail, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
		layoutSetting=resideMenu.getLayoutSetting();
		if(layoutSetting!=null){
			layoutSetting.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//设置按钮点击事件
					//Toast.makeText(MainActivity.this, "设置！", 0).show();
					Intent intent=new Intent(MainActivity.this,APPGlobalSettingActivity.class);
					startActivity(intent);
				}
			});
		}
		
		info = new ResideMenuInfo(this, R.drawable.icon_profile, "白富美", "32 级");
	}
    /**设置监听**/
	private void setListener() {
		/**顶部右边加号**/
		title_bar_right_menu.setOnClickListener(this);
		
		main_ly_tab1.setOnClickListener(this);
		main_ly_tab2.setOnClickListener(this);
		main_ly_tab3.setOnClickListener(this);
		main_ly_tab4.setOnClickListener(this);

		resideMenu.addMenuInfo(info);
		itemDaiban.setOnClickListener(this);
		itemRiCheng.setOnClickListener(this);
		itemMail.setOnClickListener(this);
		itemContact.setOnClickListener(this);
		info.setOnClickListener(this);
		leftMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			isFirstIn=false;
			is_closed = false;
			leftMenu.setVisibility(View.GONE);
		}

		@Override
		public void closeMenu() {
			is_closed = true;
			leftMenu.setVisibility(View.VISIBLE);
		}
	};

	@Override
	public void onClick(View view) {
		isFirstIn=false;
		if (view.getId() == R.id.main_ly_tab1) {
			//消息页面
			selectIndex(1);
		} else if (view.getId() == R.id.main_ly_tab2) {
			//通信录页面
			selectIndex(2);
		} else if (view.getId() == R.id.main_ly_tab3) {
			//应用页面
			selectIndex(3);
		} else if (view.getId() == R.id.main_ly_tab4) {
			//发现页面
			selectIndex(4);
		}else if(view==itemDaiban){
			//我的待办
			//Toast.makeText(MainActivity.this, "对不起尚未实现", 0).show();
			toast("对不起,待办功能尚未实现");
		}else if(view==itemRiCheng){
			//我的日程
			toast("对不起,日程功能尚未实现");
		}else if(view==itemMail){
			//我的邮件
			toast("对不起,邮件功能尚未实现");
		}else if(view==itemContact){
			//我的联系人
			toast("对不起,联系人功能尚未实现");
		}else if(view==info){
			//跳转到个人信息页面
			Intent intent=new Intent(MainActivity.this,PersonDataActivity.class);
			startActivity(intent);
		}else if(title_bar_right_menu==view){
			//顶部右边加号按钮点击事件
			responseRightMenu();
		}
	}
    /**根据当前所在的页面分别响应加号事件**/
	private void responseRightMenu() {
		switch (currentIndex) {
		case 1:
			//消息页面
			toast("消息页面加号事件");
			break;
		case 2:
			//通信录页面，添加好友功能、添加群里功能
			showRightMenuQuickActionBar(title_bar_right_menu);
			//toast("通信录页面加号事件");
			break;
		case 3:
			//应用界面
			toast("应用界面加号事件");
			break;
		default:
			break;
		}
	}
	/**点击加号执行的下来菜单显示**/
	private void showRightMenuQuickActionBar(View view) {
		QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);	
		quickAction.addActionItem(new ActionItem(0, "添加好友"));
		quickAction.addActionItem(new ActionItem(1, "我扫一扫"));
		quickAction.addActionItem(new ActionItem(2, "我搜一搜"));
		quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				switch (actionId) {
				case 0:
					//添加好友代码
					Intent intent=new Intent(MainActivity.this,SearchFriendActivity.class);
					startActivity(intent);
					//toast("添加好友");
					break;
					
					default:
					break;
				}
			}
		});
		quickAction.show(view);
	}
	/**
	 * 根据索引值切换到不同的页面
	 * 
	 * @param i
	 *            索引项
	 */
	private void selectIndex(int i) {
		setTabBgNormal();
		switch (i) {
		case 1:
			//消息页面
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv1_str));
			findViewById(R.id.main_bottom_tab1_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab1_select);
			main_bottom_tab1_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new MessageFragment());
			currentIndex=1;
			break;
		case 2:
			//通信录页面
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv2_str));
			findViewById(R.id.main_bottom_tab2_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab2_select);
			main_bottom_tab2_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new ContactFragment());
			currentIndex=2;
			break;
		case 3:
			//应用页面
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv3_str));
			findViewById(R.id.main_bottom_tab3_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab3_select);
			main_bottom_tab3_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new AppFragment());
			currentIndex=3;
			break;
		case 4:
			//发现页面
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv4_str));
			findViewById(R.id.main_bottom_tab4_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab4_select);
			main_bottom_tab4_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			Intent intent = new Intent(MainActivity.this,
					AssistantActivity.class);
			startActivity(intent);
			pullInAnimation();
			break;
		default:
			break;
		}
	}
	/** 都设置为默认没选中的背景 **/
	private void setTabBgNormal() {
		findViewById(R.id.main_bottom_tab1_iv_bg).setBackgroundResource(
				R.drawable.main_bottom_tab1_normal);
		findViewById(R.id.main_bottom_tab2_iv_bg).setBackgroundResource(
				R.drawable.main_bottom_tab2_normal);
		findViewById(R.id.main_bottom_tab3_iv_bg).setBackgroundResource(
				R.drawable.main_bottom_tab3_normal);
		findViewById(R.id.main_bottom_tab4_iv_bg).setBackgroundResource(
				R.drawable.main_bottom_tab4_normal);
		if (main_bottom_tab1_tv == null && main_bottom_tab2_tv == null
				&& main_bottom_tab3_tv == null && main_bottom_tab4_tv == null) {
			main_bottom_tab1_tv = (TextView) findViewById(R.id.main_bottom_tab1_tv);
			main_bottom_tab2_tv = (TextView) findViewById(R.id.main_bottom_tab2_tv);
			main_bottom_tab3_tv = (TextView) findViewById(R.id.main_bottom_tab3_tv);
			main_bottom_tab4_tv = (TextView) findViewById(R.id.main_bottom_tab4_tv);			
		}
		main_bottom_tab1_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_bg_color));
		main_bottom_tab2_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_bg_color));
		main_bottom_tab3_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_bg_color));
		main_bottom_tab4_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_bg_color));
		
	}

	/** 切换Fragment **/
	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	// 监听手机上的BACK键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 判断菜单是否关闭
			if (is_closed) {
				// 判断两次点击的时间间隔（默认设置为2秒）
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

					mExitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
					super.onBackPressed();
				}
			} else {
				if(isFirstIn){
					finish();
				}else{
					resideMenu.closeMenu();		
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** activity切换动画,进 **/
	public void pullInAnimation() {
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/** activity切换动画，退 **/
	public void pullOutAnimation() {
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}
	
	@Override
	public XXService getService() {
		return null;
	}
	
	@Override
	public MainActivity getMainActivity() {
		return null;
	}
	
	/**网络状态结果回调**/
	@Override
	public void onNetChange() {
		// TODO Auto-generated method stub
		
	}
	
	/**连接状态改变结果回调**/
	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		
	}
}
