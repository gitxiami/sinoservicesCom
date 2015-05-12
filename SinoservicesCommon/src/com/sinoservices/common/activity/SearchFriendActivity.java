package com.sinoservices.common.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sinoservices.common.R;

/**
 * @ClassName: SearchFriendActivity
 * @Description: ����������ѽ���
 * @author Jerry
 * @date 2015��5��11�� ����3:59:20
 *
 */
public class SearchFriendActivity extends Activity implements OnClickListener,
		TextWatcher {
	/** ������ **/
	private EditText search_friends_et;
	/** ���ص�������ʾ���� **/
	private LinearLayout search_friends_ly;
	/** ����ʵʱ��ʾ���������� **/
	private TextView search_friends_tv_result;
    /**��ѯ����б���**/
	private ListView search_firends_result_lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_firends);

		initViews();

		setListener();
	}

	private void initViews() {
		search_friends_et = (EditText) findViewById(R.id.search_friends_et);
		search_friends_ly = (LinearLayout) findViewById(R.id.search_friends_ly);
		search_friends_tv_result = (TextView) findViewById(R.id.search_friends_tv_result);
		search_firends_result_lv=(ListView) findViewById(R.id.search_firends_result_lv);
		
	}

	private void setListener() {
		search_friends_et.setOnClickListener(this);
		search_friends_et.addTextChangedListener(this);
		search_friends_ly.setOnClickListener(this);
		search_firends_result_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_friends_et:
			// ������
			break;
		case R.id.search_friends_ly:
			// �������
			if (search_friends_ly.getVisibility() == View.VISIBLE) {
				// �ɼ��ģ�ִ����������
				Toast.makeText(SearchFriendActivity.this,
						"��ʼ������" + search_friends_tv_result.getText(), 0).show();
				//���ؼ���
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SearchFriendActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		// ʵʱ��ȡ�ı�������ֵ����ѯopenfire
		// Toast.makeText(SearchFriendActivity.this, s.toString(), 0).show();
		if (s != null && !s.toString().equals("")) {
			// ��Ϊ�գ���ʾ�������
			search_friends_ly.setVisibility(View.VISIBLE);
			search_friends_tv_result.setText(s.toString());
		} else {
			search_friends_ly.setVisibility(View.GONE);
		}
	}
}
