package com.sinoservices.common.fragment;

import com.sinoservices.common.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @ClassName: ApppFragment
 * @Description: 应用页面
 * @author Felix
 * @date 2015年4月27日 上午9:00:00
 *
 */
public class AppFragment extends Fragment {
	
	View view;
	Button appCommonBt, appStoreBt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_app, container, false);
		
		appCommonBt = (Button) view.findViewById(R.id.bt_app_common);
		appStoreBt = (Button) view.findViewById(R.id.bt_app_store);
		
		setDefaultBtBackground();
		appCommonBt.setBackgroundResource(R.drawable.left_btn_select_bg);
		appCommonBt.setTextColor(Color.WHITE);
		changeFragment(new AppCommonFragment());
		
		appCommonBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setDefaultBtBackground();
				appCommonBt.setTextColor(Color.WHITE);
				appCommonBt.setBackgroundResource(R.drawable.left_btn_select_bg);
				changeFragment(new AppCommonFragment());
			}
		});
		
		
		appStoreBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setDefaultBtBackground();
				appStoreBt.setTextColor(Color.WHITE);
				appStoreBt.setBackgroundResource(R.drawable.right_btn_select_bg);
				changeFragment(new AppStoreFragment());
				
			}
		});
		
		return view;
	}
	
	private void setDefaultBtBackground() {
		appCommonBt.setTextColor(this.getResources().getColor(R.color.app_fragment_button_color));
		appStoreBt.setTextColor(this.getResources().getColor(R.color.app_fragment_button_color));
		appCommonBt.setBackgroundResource(R.drawable.left_btn_nor_bg);
		appStoreBt.setBackgroundResource(R.drawable.right_btn_nor_bg);
	}
	
	/** 切换Fragment **/
	private void changeFragment(Fragment targetFragment) {
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.app_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}
}
