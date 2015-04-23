package com.sinoservices.common;

import com.sinoservices.common.view.TitleBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActivity {
	/** ������ **/
	private TitleBar titlebar;
	/** �������� **/
	private RelativeLayout content_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		titlebar = getTitleBar();
		content_container = getContent_container();
		initViews();
	}

	/** ��ʼ�� **/
	private void initViews() {
		/** ���ñ���������Ϊ��ɫ�� **/
		//titlebar.getTitle_bar_contain().setBackgroundColor(Color.BLACK);
		/** ������߻��˰�ť������ **/
		titlebar.getLeft_container().setVisibility(View.GONE);
		/** �����ұ�ͼ������� **/
		titlebar.getRight_container().setVisibility(View.GONE);
		/** �ı�������������� **/
		titlebar.getCenter_title_tv().setText("���ñ�");
		titlebar.getCenter_title_iv().setVisibility(View.GONE);
		/**ʵ��������ҳ����**/
		View view = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.activity_main, null);
		/**�ӽ�ȥ��������**/
		content_container.addView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
}
