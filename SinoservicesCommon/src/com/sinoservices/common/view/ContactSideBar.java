package com.sinoservices.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @ClassName: SideBar
 * @Description: �ұ߻����Զ������
 */
public class ContactSideBar extends View {
	// �����¼�
	private OnTouchingChangedListener onTouchingChangedListener = new OnTouchingChangedListener() {

		@Override
		public void onTouchingChanged(String s) {
			// TODO Auto-generated method stub

		}
	};
	// 26����ĸ
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private Paint paint = new Paint();
	private int position = -1;
	// չʾѡ�е���ĸ
	private TextView showChooseText;

	/**
	 * ΪPinYinSideBar������ʾ��ĸ��TextView
	 * 
	 * @param showChooseText
	 *            the showChooseText to set
	 */
	public void setShowChooseText(TextView showChooseText) {
		this.showChooseText = showChooseText;
	}

	public ContactSideBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ContactSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ContactSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// ��ȡ����ı䱳����ɫ
		int height = getHeight();
		int width = getWidth();
		int sigleHeight = (height / b.length)-1;
		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.BLACK);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setAntiAlias(true);
			paint.setTextSize(40);
			// ѡ��״̬
			if (i == position) {
				paint.setColor(Color.BLUE);
				paint.setTextSize(30);
				paint.setFakeBoldText(true);
			}
			// x��������м�-�ַ�����ȵ�һ��.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = sigleHeight * i + sigleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// ���û���
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		float y = event.getY() - getY();
		int oldPosition = position;
		// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.
		int c = 0;
		if (y >= 0 && y <= getHeight()) {
			c = (int) (y / getHeight() * b.length);
		} else if (y > getHeight()) {
			c = b.length - 1;
		}
		if (action == MotionEvent.ACTION_UP) {
			setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			position = -1;
			if (showChooseText != null) {
				showChooseText.setVisibility(View.INVISIBLE);
			}
		} else {
			setBackgroundDrawable(new ColorDrawable(Color.GRAY));
			setBackgroundColor(Color.GRAY);
			if (oldPosition != c) {
				if (c >= 0 && c < b.length) {
					if (onTouchingChangedListener != null) {
						onTouchingChangedListener.onTouchingChanged(b[c]);
					}
				}
				if (showChooseText != null) {
					showChooseText.setText(b[c]);
					showChooseText.setVisibility(View.VISIBLE);
				}
				position = c;
			}
		}
		invalidate();
		return true;
	}

	/**
	 * ���⹫���ķ���
	 * 
	 * @Title: setOnTouchingChangedListener
	 * @Description: TODO
	 * @param onTouchingChangedListener
	 * @return void
	 */
	public void setOnTouchingChangedListener(
			OnTouchingChangedListener onTouchingChangedListener) {
		this.onTouchingChangedListener = onTouchingChangedListener;
	}

	/**
	 * �ӿ�
	 * 
	 * @ClassName: OnTouchingChangedListener
	 * @Description: TODO
	 */
	public interface OnTouchingChangedListener {
		public void onTouchingChanged(String s);
	}
}
