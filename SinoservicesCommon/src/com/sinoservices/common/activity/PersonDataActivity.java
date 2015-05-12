package com.sinoservices.common.activity;

import com.sinoservices.common.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @ClassName: PersonDataActivity 
 * @Description: 个人资料页面
 * @author Jerry 
 * @date 2015年5月9日 下午2:53:10 
 */
public class PersonDataActivity extends Activity implements OnClickListener{
	/**返回按钮**/
	private Button persondata_back_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persondata);
		
		initViews();
		setListener();
	}

	private void initViews() {
		persondata_back_btn=(Button) findViewById(R.id.persondata_back_btn);
	}

	private void setListener() {
		persondata_back_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.persondata_back_btn:
			//返回
			finish();
			break;

		default:
			break;
		}
	}
}
