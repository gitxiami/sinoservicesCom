package com.sinoservices.common.fragment;

import com.sinoservices.common.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: ContactFragment 
 * @Description: ͨ��¼ҳ��
 * @author Jerry 
 * @date 2015��4��26�� ����10:20:00 
 */
public class ContactFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}
}
