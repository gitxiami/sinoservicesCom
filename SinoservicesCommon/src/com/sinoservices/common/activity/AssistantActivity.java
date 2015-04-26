package com.sinoservices.common.activity;

import com.sinoservices.common.BaseActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.view.TitleBar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * @ClassName: AssistantActivity
 * @Description: ����С����ҳ��
 * @author Jerry
 * @date 2015��4��26�� ����10:41:28
 *
 */
@SuppressLint("ResourceAsColor")
public class AssistantActivity extends BaseActivity {
	/** ��������װ�� **/
	private TitleBar titleBar;
	/** ������ʵ�ʲ��� **/
	private View bar;
	/** ���������µĽ������� **/
	private RelativeLayout content_container;
    /**����ʵ�����ݲ���**/
	private View view ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titleBar = getTitleBar();
		bar = titleBar.getTitlebar();
		content_container = getContent_container();
	    view = LayoutInflater.from(this).inflate(
				R.layout.activity_assistant, null);
		initViews(view);

	}

	/**
	 * @Title: initViews
	 * @Description: ��ʼ��
	 * @param  view
	 * @return void
	 * @throws
	 */
	private void initViews(View view) {
		// �����ұ߲��ֲ��ɼ�
		titleBar.getRight_container().setVisibility(View.GONE);
		// �����м����ͼ�����ɼ�
		titleBar.getCenter_title_iv().setVisibility(View.GONE);
		// ���ñ���������
		titleBar.getCenter_title_tv().setText("����С����");
		//���ñ�����������ɫ
        //titleBar.getTitle_bar_contain().setBackgroundColor(R.color.title_bar_bg_color);
		content_container.addView(view, new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
     
		titleBar.getLeft_container().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ���ذ�ť�¼�
				finish();
			}
		});
	}
	

}
