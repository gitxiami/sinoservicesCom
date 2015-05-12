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
 * @Description: 添加搜索好友界面
 * @author Jerry
 * @date 2015年5月11日 下午3:59:20
 *
 */
public class SearchFriendActivity extends Activity implements OnClickListener,
		TextWatcher {
	/** 搜索框 **/
	private EditText search_friends_et;
	/** 隐藏的搜索显示布局 **/
	private LinearLayout search_friends_ly;
	/** 搜索实时显示的输入内容 **/
	private TextView search_friends_tv_result;
    /**查询结果列表集合**/
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
			// 搜索框
			break;
		case R.id.search_friends_ly:
			// 点击搜索
			if (search_friends_ly.getVisibility() == View.VISIBLE) {
				// 可见的，执行搜索方法
				Toast.makeText(SearchFriendActivity.this,
						"开始搜索：" + search_friends_tv_result.getText(), 0).show();
				//隐藏键盘
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
		// 实时获取文本框输入值，查询openfire
		// Toast.makeText(SearchFriendActivity.this, s.toString(), 0).show();
		if (s != null && !s.toString().equals("")) {
			// 不为空，显示隐藏项布局
			search_friends_ly.setVisibility(View.VISIBLE);
			search_friends_tv_result.setText(s.toString());
		} else {
			search_friends_ly.setVisibility(View.GONE);
		}
	}
}
