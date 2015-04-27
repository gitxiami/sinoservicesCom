package com.sinoservices.common;

import com.sinoservices.common.ResideMenu.ResideMenu;
import com.sinoservices.common.ResideMenu.ResideMenuInfo;
import com.sinoservices.common.ResideMenu.ResideMenuItem;
import com.sinoservices.common.activity.AssistantActivity;
import com.sinoservices.common.fragment.AppFragment;
import com.sinoservices.common.fragment.ContactFragment;
import com.sinoservices.common.fragment.MessageFragment;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
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

	private TextView main_bottom_tab1_tv = null;
	private TextView main_bottom_tab2_tv = null;
	private TextView main_bottom_tab3_tv = null;
	private TextView main_bottom_tab4_tv = null;

	private TextView main_title_bar_tv;
	
	private boolean is_closed = false;
	private long mExitTime;
	private Button leftMenu;

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
	private void setUpMenu() {
		leftMenu = (Button) findViewById(R.id.title_bar_left_menu);
		main_ly_tab1 = (LinearLayout) findViewById(R.id.main_ly_tab1);
		main_ly_tab2 = (LinearLayout) findViewById(R.id.main_ly_tab2);
		main_ly_tab3 = (LinearLayout) findViewById(R.id.main_ly_tab3);
		main_ly_tab4 = (LinearLayout) findViewById(R.id.main_ly_tab4);
		//��������
		main_title_bar_tv = (TextView) findViewById(R.id.main_title_bar_tv);
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
		itemDaiban = new ResideMenuItem(this, R.drawable.residmenu_itme1_bg,
				"�ҵĴ���");
		itemRiCheng = new ResideMenuItem(this, R.drawable.residmenu_itme2_bg,
				"�ҵ��ճ�");
		itemMail = new ResideMenuItem(this, R.drawable.residmenu_itme3_bg,
				"�ҵ��ʼ�");
		itemContact = new ResideMenuItem(this, R.drawable.residmenu_itme4_bg,
				"�ҵ���ϵ��");

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
			//��Ϣҳ��
			selectIndex(1);
		} else if (view.getId() == R.id.main_ly_tab2) {
			//ͨ��¼ҳ��
			selectIndex(2);
		} else if (view.getId() == R.id.main_ly_tab3) {
			//Ӧ��ҳ��
			selectIndex(3);
		} else if (view.getId() == R.id.main_ly_tab4) {
			//����ҳ��
			selectIndex(4);
		}
	}

	/**
	 * ��������ֵ�л�����ͬ��ҳ��
	 * 
	 * @param i
	 *            ������
	 */
	private void selectIndex(int i) {
		setTabBgNormal();
		switch (i) {
		case 1:
			//��Ϣҳ��
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv1_str));
			findViewById(R.id.main_bottom_tab1_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab1_select);
			main_bottom_tab1_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new MessageFragment());
			break;
		case 2:
			//ͨ��¼ҳ��
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv2_str));
			findViewById(R.id.main_bottom_tab2_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab2_select);
			main_bottom_tab2_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new ContactFragment());
			
			break;
		case 3:
			//Ӧ��ҳ��
			main_title_bar_tv.setText(getResources().getString(R.string.main_bottom_tv3_str));
			findViewById(R.id.main_bottom_tab3_iv_bg).setBackgroundResource(
					R.drawable.main_bottom_tab3_select);
			main_bottom_tab3_tv.setTextColor(getResources().getColor(R.color.main_bottom_tv_select_bg_color));
			changeFragment(new AppFragment());
			break;
		case 4:
			//����ҳ��
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
	/** ������ΪĬ��ûѡ�еı��� **/
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

	/** activity�л�����,�� **/
	public void pullInAnimation() {
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/** activity�л��������� **/
	public void pullOutAnimation() {
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}
}
