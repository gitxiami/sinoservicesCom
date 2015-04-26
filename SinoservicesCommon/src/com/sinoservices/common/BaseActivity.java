package com.sinoservices.common;

import com.sinoservices.common.view.TitleBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * @ClassName: BaseActivity
 * @Description: ����activity
 * @author Jerry
 * @date 2015��4��23�� ����3:17:22
 *
 */
public class BaseActivity extends Activity {
	/**��������������**/
	private RelativeLayout title_container;
	/**���������µĽ�������**/
	private RelativeLayout content_container;
	/**��������װ��**/
	private TitleBar titleBar;
	/**������ʵ�ʲ���**/
	private View bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_base);
		/**ʵ������������װ��**/ 
		titleBar=new TitleBar(BaseActivity.this);
		bar=titleBar.getTitlebar();
		initView();
	}
    /**��ʼ��view**/
	private void initView() {
		title_container=(RelativeLayout) findViewById(R.id.title_container);	
		title_container.addView(bar);
		content_container=(RelativeLayout) findViewById(R.id.content_container);
	}
    /**��ȡ��������װ��**/
	public TitleBar getTitleBar() {
		return titleBar;
	}
    /**��ȡ����������**/
	public RelativeLayout getContent_container() {
		return content_container;
	}
	/**activity�л�����,��**/
	public void pullInAnimation(){
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	/**activity�л���������**/
	public void pullOutAnimation(){
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}
}
