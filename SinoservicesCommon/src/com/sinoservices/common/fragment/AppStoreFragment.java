package com.sinoservices.common.fragment;

import com.sinoservices.common.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: ApppFragment
 * @Description: Ӧ��ҳ��
 * @author Felix
 * @date 2015��4��27�� ����9:00:00
 *
 */
public class AppStoreFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_app_store, container, false);
	}
	
}
