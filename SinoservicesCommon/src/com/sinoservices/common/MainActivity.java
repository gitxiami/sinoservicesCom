package com.sinoservices.common;

import com.sinoservices.common.view.TitleBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActivity {
	/** 标题栏 **/
	private TitleBar titlebar;
	/** 内容容器 **/
	private RelativeLayout content_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		titlebar = getTitleBar();
		content_container = getContent_container();
		initViews();
	}

	/** 初始化 **/
	private void initViews() {
		/** 设置标题栏背景为黑色的 **/
		//titlebar.getTitle_bar_contain().setBackgroundColor(Color.BLACK);
		/** 隐藏左边回退按钮和文字 **/
		titlebar.getLeft_container().setVisibility(View.GONE);
		/** 隐藏右边图标和文字 **/
		titlebar.getRight_container().setVisibility(View.GONE);
		/** 改变标题栏标题文字 **/
		titlebar.getCenter_title_tv().setText("安得宝");
		titlebar.getCenter_title_iv().setVisibility(View.GONE);
		/**实例化出本页布局**/
		View view = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.activity_main, null);
		/**加进去容器类中**/
		content_container.addView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
}
