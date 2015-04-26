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
 * @Description: 物流小助手页面
 * @author Jerry
 * @date 2015年4月26日 上午10:41:28
 *
 */
@SuppressLint("ResourceAsColor")
public class AssistantActivity extends BaseActivity {
	/** 标题栏封装类 **/
	private TitleBar titleBar;
	/** 标题栏实际布局 **/
	private View bar;
	/** 标题栏底下的界面容器 **/
	private RelativeLayout content_container;
    /**界面实际内容布局**/
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
	 * @Description: 初始化
	 * @param  view
	 * @return void
	 * @throws
	 */
	private void initViews(View view) {
		// 设置右边部分不可见
		titleBar.getRight_container().setVisibility(View.GONE);
		// 设置中间标题图案不可见
		titleBar.getCenter_title_iv().setVisibility(View.GONE);
		// 设置标题栏文字
		titleBar.getCenter_title_tv().setText("物流小助手");
		//设置标题栏背景颜色
        //titleBar.getTitle_bar_contain().setBackgroundColor(R.color.title_bar_bg_color);
		content_container.addView(view, new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
     
		titleBar.getLeft_container().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回按钮事件
				finish();
			}
		});
	}
	

}
