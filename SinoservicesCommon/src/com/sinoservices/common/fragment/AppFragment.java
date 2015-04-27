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
		
		setDefaultBtBackgroundColor();
		changeFragment(new AppCommonFragment());
		
		appCommonBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setDefaultBtBackgroundColor();
				changeFragment(new AppCommonFragment());
			}
		});
		
		
		appStoreBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setDefaultBtBackgroundColor();
				changeFragment(new AppStoreFragment());
				
			}
		});
		
		return view;
	}
	
	private void setDefaultBtBackgroundColor() {
//		appCommonBt.setBackgroundColor(Color.WHITE);
//		appStoreBt.setBackgroundColor(Color.WHITE);
	}
	
	/** 切换Fragment **/
	private void changeFragment(Fragment targetFragment) {
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.app_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}
}
