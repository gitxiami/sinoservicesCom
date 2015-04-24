package com.sinoservices.common.ResideMenu;

import com.sinoservices.common.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * @ClassName: ResideMenuInfo 
 * @Description: ����û���Ϣ��
 * @date 2015��4��24�� ����1:34:55 
 */
public class ResideMenuInfo extends LinearLayout {

	/** ͷ�� */
	private ImageView iv_icon;
	/** �û��ǳ� */
	private TextView tv_username;
    /**�ȼ�**/
	private TextView tv_dengji;
    /**�ұ߲˵�**/
	private ImageView iv_right_btn;
	
	public ResideMenuInfo(Context context) {
		super(context);
		initViews(context);
	}

	public ResideMenuInfo(Context context, int icon, String title, String dengji) {
		super(context);
		initViews(context);
		iv_icon.setImageResource(icon);
		tv_username.setText(title);
		tv_dengji.setText(dengji);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu_info, this);
		iv_icon = (ImageView) findViewById(R.id.image_icon);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_dengji = (TextView) findViewById(R.id.tv_dengji);
		iv_right_btn=(ImageView) findViewById(R.id.iv_right_btn);
	}

	/**
	 * set the icon color;
	 * 
	 * @param icon
	 */
	public void setIcon(int icon) {
		iv_icon.setImageResource(icon);
	}

	/**
	 * set the title with string;
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv_username.setText(title);
	}

	/**
	 * set the title with string;
	 * 
	 * @param dengji
	 */
	public void setDengJi(String dengji) {
		tv_dengji.setText(dengji);
	}
}