package com.sinoservices.common;

import com.sinoservices.common.ResideMenu.ResideMenu;
import com.sinoservices.common.ResideMenu.ResideMenuInfo;
import com.sinoservices.common.ResideMenu.ResideMenuItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		View.OnClickListener {
	private ResideMenu resideMenu;

	private ResideMenuItem itemDaiban;
	private ResideMenuItem itemRiCheng;
	private ResideMenuItem itemMail;
	private ResideMenuItem itemContact;

	private ResideMenuInfo info;
	/** �����ĸ�ѡ�ť **/
	private LinearLayout main_ly_tab1;
	private LinearLayout main_ly_tab2;
	private LinearLayout main_ly_tab3;
	private LinearLayout main_ly_tab4;

	private boolean is_closed = false;
	private long mExitTime;
	private Button leftMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		setUpMenu();

		setListener();
		
	}

	private void setUpMenu() {
		leftMenu = (Button) findViewById(R.id.title_bar_left_menu);
		main_ly_tab1 = (LinearLayout) findViewById(R.id.main_ly_tab1);
		main_ly_tab2 = (LinearLayout) findViewById(R.id.main_ly_tab2);
		main_ly_tab3 = (LinearLayout) findViewById(R.id.main_ly_tab3);
		main_ly_tab4 = (LinearLayout) findViewById(R.id.main_ly_tab4);

		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_background);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);
		// ��ֹʹ���Ҳ�˵�
		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		// create menu items;
		itemDaiban = new ResideMenuItem(this, R.drawable.residmenu_itme1_bg, "�ҵĴ���");
		itemRiCheng = new ResideMenuItem(this, R.drawable.residmenu_itme2_bg, "�ҵ��ճ�");
		itemMail = new ResideMenuItem(this, R.drawable.residmenu_itme3_bg, "�ҵ��ʼ�");
		itemContact = new ResideMenuItem(this, R.drawable.residmenu_itme4_bg, "�ҵ���ϵ��");

		resideMenu.addMenuItem(itemDaiban, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemRiCheng, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemMail, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);

		info = new ResideMenuInfo(this, R.drawable.icon_profile, "�׸���", "32 ��");
	}

	private void setListener() {
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

		if (view.getId() == R.id.main_ly_tab1) {
			// changeFragment(new NewsFragment());
			Toast.makeText(MainActivity.this, "��磬���ٵ�����", 0).show();
		} else if (view.getId() == R.id.main_ly_tab2) {
			// changeFragment(new ContactsFragment());
			Toast.makeText(MainActivity.this, "��㣬���ٵ�����", 0).show();
		} else if (view.getId() == R.id.main_ly_tab3) {
			// changeFragment(new DongtaiFragment());
			Toast.makeText(MainActivity.this, "���裬���ٵ�����", 0).show();
		} else if (view.getId() == R.id.main_ly_tab4) {
			// changeFragment(new DongtaiFragment());
			Toast.makeText(MainActivity.this, "���̣����ٵ�����", 0).show();
		}
	}

	/** �л�Fragment **/
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

	// What good method is to access resideMenu��
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	// �����ֻ��ϵ�BACK��
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �жϲ˵��Ƿ�ر�
			if (is_closed) {
				// �ж����ε����ʱ������Ĭ������Ϊ2�룩
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();

					mExitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
					super.onBackPressed();
				}
			} else {
				resideMenu.closeMenu();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
