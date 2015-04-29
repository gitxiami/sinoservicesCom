package com.sinoservices.common.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sinoservices.common.BaseActivity;
import com.sinoservices.common.R;
import com.sinoservices.common.alipay.Constant;
import com.sinoservices.common.alipay.PayResult;
import com.sinoservices.common.entity.ModuleEntity;
import com.sinoservices.common.jsinterface.JsCall;
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
	/**接收上个页面传来的Intent**/
	private Intent intent=null;
	/**接收上个页面传来的参数实体**/
	private ModuleEntity moduleEntity=null;
	/**websetting设置**/
	private WebSettings webSettings ;
	/**js与java交互类**/
	private JsCall jsCall;
	
	@SuppressLint("SetJavaScriptEnabled")
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
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(ModuleWebActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ModuleWebActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(ModuleWebActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case Constant.SDK_CHECK_FLAG: {
				Toast.makeText(ModuleWebActivity.this, "" + msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			}
			default:
				break;
			}
		};
	};
	
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
		jsCall = new JsCall(ModuleWebActivity.this, mHandler);
		module_webview=(WebView) v.findViewById(R.id.module_webview);
		webSettings = module_webview.getSettings();  
        webSettings.setBuiltInZoomControls(false); 
        module_webview.getSettings().setJavaScriptEnabled(true);
        module_webview.addJavascriptInterface(jsCall, "jscall");
        
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
