package com.sinoservices.common.fragment;

import com.sinoservices.common.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: MessageFragment
 * @Description: ��Ϣҳ��
 * @author Jerry
 * @date 2015��4��26�� ����9:55:37
 *
 */
public class MessageFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}
}
