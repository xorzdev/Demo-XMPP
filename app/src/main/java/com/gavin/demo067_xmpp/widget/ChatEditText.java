package com.gavin.demo067_xmpp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.gavin.demo067_xmpp.R;

/**
 * 自定义聊天输入框
 * 
 * @ClassName: LoginEditext
 * @author yeliangliang
 * @date 2015-7-22 下午3:13:51
 */
public class ChatEditText extends EditText implements OnTouchListener {
	private boolean tag = false;// face图标打开标识

	// 图标点击监听器
	public interface FaceIconOnClickListener {
		// isFace 点击是是否为表情图标
		void clickFace(boolean isFace);
	}

	/**
	 * 设置图标点击监听器
	 * 
	 * @param listener
	 * @author yeliangliang
	 * @date 2015-7-31 下午6:09:44
	 * @version V1.0
	 * @return void
	 */
	public void setFaceIconOnClickListener(FaceIconOnClickListener listener) {
		this.listener = listener;
	}

	private Drawable xD;
	private FaceIconOnClickListener listener;

	public ChatEditText(Context context) {
		super(context);
		init();
	}

	public ChatEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChatEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		this.l = l;
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener f) {
		this.f = f;
	}

	private OnTouchListener l;
	private OnFocusChangeListener f;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
					.getIntrinsicWidth());
			if (tappedX) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (!tag) {// 弹出了表情选择框
						displayIcon(true);
					} else {// 关闭了表情选择框
						displayIcon(false);
					}
					if (listener != null) {
						listener.clickFace(true);
					}
				}
				return true;
			} else {
				listener.clickFace(false);
				displayIcon(false);
				return false;
			}
		}
		if (l != null) {
			return l.onTouch(v, event);
		}
		return false;
	}

	private void init() {
		// 展示笑脸图标
		displayIcon(false);
		super.setOnTouchListener(this);
	}

	/**
	 * 展示笑脸图标
	 * 
	 * @param isSelected
	 * @author yeliangliang
	 * @date 2015-7-31 下午6:07:05
	 * @version V1.0
	 * @return void
	 */
	public void displayIcon(boolean isSelected) {
		tag = isSelected;
		xD = ContextCompat.getDrawable(getContext(), getDefaultClearIconId(isSelected));
		xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
		setCompoundDrawables(xD, getCompoundDrawables()[1], getCompoundDrawables()[2],
				getCompoundDrawables()[3]);
	}

	/**
	 * 获取图标
	 * 
	 * @param isPressed
	 *            是否按下
	 * @return
	 * @author yeliangliang
	 * @date 2015-7-31 下午5:58:37
	 * @version V1.0
	 * @return int
	 */
	private int getDefaultClearIconId(boolean isPressed) {
		if (isPressed) {
			return R.mipmap.ic_launcher;
		}
		return R.drawable.icon_chat_face_n;
	}
}
