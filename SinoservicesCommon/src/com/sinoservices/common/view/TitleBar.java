package com.sinoservices.common.view;

import com.sinoservices.common.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: TitleBar
 * @Description: 自定义标题栏封装类
 * @author Jerry
 * @date 2015年4月23日 下午3:01:01
 *
 */
public class TitleBar {
	private Context mContext;
	private View titlebar;
	private LayoutInflater mInflater;

	private LinearLayout left_container;
	private LinearLayout center_container;
	private LinearLayout right_container;
	private RelativeLayout title_bar_contain;

	private ImageView left_back_iv;
	private ImageView center_title_iv;
	private ImageView right_iv;

	private TextView left_back_tv;
	private TextView center_title_tv;
	private TextView right_tv;
	
	public TitleBar(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		titlebar=mInflater.inflate(R.layout.title_bar, null);
		initViews(titlebar);
	}
    /**
     * @Title: initViews 
     * @Description: 初始化views
     * @param @param titlebar 
     * @return void 
     * @throws
     */
	private void initViews(View titlebar) {
		left_container = (LinearLayout) titlebar.findViewById(R.id.bar_left_ly_container);
		center_container = (LinearLayout) titlebar.findViewById(R.id.bar_center_ly_container);
		right_container = (LinearLayout) titlebar.findViewById(R.id.bar_right_ly_container);
		title_bar_contain = (RelativeLayout) titlebar.findViewById(R.id.title_bar_contain);

		left_back_iv = (ImageView) titlebar.findViewById(R.id.bar_left_back_iv);
		center_title_iv = (ImageView) titlebar.findViewById(R.id.bar_title_center_iv);
		right_iv = (ImageView) titlebar.findViewById(R.id.bar_right_iv);

		left_back_tv = (TextView) titlebar.findViewById(R.id.bar_left_back_tv);
		center_title_tv = (TextView) titlebar.findViewById(R.id.bar_title_center_tv);
		right_tv = (TextView) titlebar.findViewById(R.id.bar_right_tv);
	}
	public Context getmContext() {
		return mContext;
	}
	public View getTitlebar() {
		return titlebar;
	}
	public LayoutInflater getmInflater() {
		return mInflater;
	}
	public LinearLayout getLeft_container() {
		return left_container;
	}
	public LinearLayout getCenter_container() {
		return center_container;
	}
	public LinearLayout getRight_container() {
		return right_container;
	}
	public RelativeLayout getTitle_bar_contain() {
		return title_bar_contain;
	}
	public ImageView getLeft_back_iv() {
		return left_back_iv;
	}
	public ImageView getCenter_title_iv() {
		return center_title_iv;
	}
	public ImageView getRight_iv() {
		return right_iv;
	}
	public TextView getLeft_back_tv() {
		return left_back_tv;
	}
	public TextView getCenter_title_tv() {
		return center_title_tv;
	}
	public TextView getRight_tv() {
		return right_tv;
	}

}
