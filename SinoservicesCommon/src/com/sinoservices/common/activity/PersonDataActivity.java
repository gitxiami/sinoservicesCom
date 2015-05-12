package com.sinoservices.common.activity;

import com.sinoservices.common.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @ClassName: PersonDataActivity 
 * @Description: ��������ҳ��
 * @author Jerry 
 * @date 2015��5��9�� ����2:53:10 
 */
public class PersonDataActivity extends Activity implements OnClickListener{
	/**���ذ�ť**/
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
			//����
			finish();
			break;

		default:
			break;
		}
	}
}
