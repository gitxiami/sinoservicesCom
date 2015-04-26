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
 * @Description: 基类activity
 * @author Jerry
 * @date 2015年4月23日 下午3:17:22
 *
 */
public class BaseActivity extends Activity {
	/**标题栏整个容器**/
	private RelativeLayout title_container;
	/**标题栏底下的界面容器**/
	private RelativeLayout content_container;
	/**标题栏封装类**/
	private TitleBar titleBar;
	/**标题栏实际布局**/
	private View bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_base);
		/**实例化标题栏封装类**/ 
		titleBar=new TitleBar(BaseActivity.this);
		bar=titleBar.getTitlebar();
		initView();
	}
    /**初始化view**/
	private void initView() {
		title_container=(RelativeLayout) findViewById(R.id.title_container);	
		title_container.addView(bar);
		content_container=(RelativeLayout) findViewById(R.id.content_container);
	}
    /**获取标题栏封装类**/
	public TitleBar getTitleBar() {
		return titleBar;
	}
    /**获取布局容器类**/
	public RelativeLayout getContent_container() {
		return content_container;
	}
	/**activity切换动画,进**/
	public void pullInAnimation(){
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	/**activity切换动画，退**/
	public void pullOutAnimation(){
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}
}
