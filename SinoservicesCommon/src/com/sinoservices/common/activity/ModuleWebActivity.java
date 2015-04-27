package com.sinoservices.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.sinoservices.common.BaseActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.entity.ModuleEntity;
import com.sinoservices.common.view.TitleBar;

/**
 * @ClassName: ModuleWebActivity
 * @Description: 应用模块webView加载页面
 * @author Jerry
 * @date 2015年4月27日 下午5:24:57
 */
public class ModuleWebActivity extends BaseActivity {
	/** webView **/
	private WebView module_webview;
	/** 标题栏封装类 **/
	private TitleBar titleBar;
	/** 标题栏实际布局 **/
	private View bar;
	/** 标题栏底下的界面容器 **/
	private RelativeLayout content_container;
	/** 界面实际内容布局 **/
	private View view;
	private Intent intent=null;
	private ModuleEntity moduleEntity=null;
	private WebSettings webSettings ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		//获取上个页面传来的数据
		moduleEntity=(ModuleEntity) intent.getSerializableExtra("ModuleEntity");
		titleBar = getTitleBar();
		bar = titleBar.getTitlebar();
		content_container = getContent_container();
		view = LayoutInflater.from(this).inflate(R.layout.activity_module_web,
				null);
		initViews(view);
		
		initWebContent(moduleEntity);
	}
    /**初始化加载页面**/
	private void initWebContent(final ModuleEntity moduleEntity2) {
		if(moduleEntity2!=null&&moduleEntity2.getModulename()!=null&&moduleEntity2.getModuleurl()!=null){
			titleBar.getCenter_title_tv().setText(moduleEntity2.getModulename());
			module_webview.loadUrl(moduleEntity2.getModuleurl());
//			titleBar.getCenter_title_tv().setText("微信支付");
//			module_webview.loadUrl("http://commonserver.duapp.com/wxpay.jsp");
			module_webview .setWebViewClient(new MyWebViewClient());
		}
	}

	private void initViews(View v) {
		module_webview=(WebView) v.findViewById(R.id.module_webview);
		webSettings = module_webview.getSettings();  
        webSettings.setBuiltInZoomControls(false); 
        webSettings.setJavaScriptEnabled(true);
        
		// 设置右边部分不可见
		titleBar.getRight_container().setVisibility(View.GONE);
		// 设置中间标题图案不可见
		titleBar.getCenter_title_iv().setVisibility(View.GONE);
		// 设置标题栏文字
		titleBar.getCenter_title_tv().setText("某某模块");
		// 设置标题栏背景颜色
		// titleBar.getTitle_bar_contain().setBackgroundColor(R.color.title_bar_bg_color);
		content_container.addView(v, new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		titleBar.getLeft_container().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回按钮事件
				finish();
				pullOutAnimation();
			}
		});
	}
	//自定义客户端用于阻止调用系统默认浏览器
	private class MyWebViewClient extends WebViewClient {  
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            return(false);  
        }  
    }  
}
