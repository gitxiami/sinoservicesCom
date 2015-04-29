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
 * @Description: Ӧ��ģ��webView����ҳ��
 * @author Jerry
 * @date 2015��4��27�� ����5:24:57
 */
public class ModuleWebActivity extends BaseActivity {
	/** webView **/
	private WebView module_webview;
	/** ��������װ�� **/
	private TitleBar titleBar;
	/** ������ʵ�ʲ��� **/
	private View bar;
	/** ���������µĽ������� **/
	private RelativeLayout content_container;
	/** ����ʵ�����ݲ��� **/
	private View view;
	/**�����ϸ�ҳ�洫����Intent**/
	private Intent intent=null;
	/**�����ϸ�ҳ�洫���Ĳ���ʵ��**/
	private ModuleEntity moduleEntity=null;
	/**websetting����**/
	private WebSettings webSettings ;
	/**js��java������**/
	private JsCall jsCall;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		//��ȡ�ϸ�ҳ�洫��������
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
				
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(ModuleWebActivity.this, "֧���ɹ�",
							Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ModuleWebActivity.this, "֧�����ȷ����",
								Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(ModuleWebActivity.this, "֧��ʧ��",
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
	
    /**��ʼ������ҳ��**/
	private void initWebContent(final ModuleEntity moduleEntity2) {
		if(moduleEntity2!=null&&moduleEntity2.getModulename()!=null&&moduleEntity2.getModuleurl()!=null){
			titleBar.getCenter_title_tv().setText(moduleEntity2.getModulename());
			module_webview.loadUrl(moduleEntity2.getModuleurl());
//			titleBar.getCenter_title_tv().setText("΢��֧��");
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
        
		// �����ұ߲��ֲ��ɼ�
		titleBar.getRight_container().setVisibility(View.GONE);
		// �����м����ͼ�����ɼ�
		titleBar.getCenter_title_iv().setVisibility(View.GONE);
		// ���ñ���������
		titleBar.getCenter_title_tv().setText("ĳĳģ��");
		// ���ñ�����������ɫ
		// titleBar.getTitle_bar_contain().setBackgroundColor(R.color.title_bar_bg_color);
		content_container.addView(v, new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		titleBar.getLeft_container().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ���ذ�ť�¼�
				finish();
				pullOutAnimation();
			}
		});
	}
	//�Զ���ͻ���������ֹ����ϵͳĬ�������
	private class MyWebViewClient extends WebViewClient {  
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            return(false);  
        }  
    }  
}
