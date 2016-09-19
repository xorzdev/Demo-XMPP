package com.gavin.demo067_xmpp.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	public View rootView;
	private SparseArray<View> views;

	public ViewHolder(View view) {
		if (null == views) {
			views = new SparseArray<>();
		}
		this.rootView = view;
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = rootView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 *            要设值的控件id
	 * @param text
	 *            要设置的文本
	 * @return
	 */
	public ViewHolder setText(int viewId, CharSequence text) {
		TextView tv = $(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置TextView的值
	 *
	 * @param viewId
	 *            要设值的控件id
	 * @param textId
	 *            要设置的文本
	 * @return
	 */
	public ViewHolder setText(int viewId, int textId) {
		TextView tv = $(viewId);
		tv.setText(textId);
		return this;
	}

	/**
	 * 设置TView背景
	 *
	 * @param viewId
	 *            要设值的控件id
	 * @param resId
	 *            要设置的文本
	 * @return
	 */
	public ViewHolder setBackground(int viewId, int resId) {
		View v = $(viewId);
		v.setBackgroundResource(resId);
		return this;
	}

	/**
	 * 设置ImageView的值
	 * 
	 * @param viewId
	 *            要设值的控件id
	 * @param id
	 *            地址
	 * @return
	 */
	public ViewHolder setImage(int viewId, int id) {
		ImageView iv = $(viewId);
		iv.setImageResource(id);
		return this;
	}

	/**
	 * 设置ImageButton的值
	 * 
	 * @param viewId
	 *            要设值的控件id
	 * @param resId
	 *            要设置的图片资源id
	 * @return
	 */
	public ViewHolder setImageButton(int viewId, int resId) {
		ImageButton ib = $(viewId);
		ib.setImageResource(resId);
		return this;
	}

	/**
	 * 设置控件的可见性
	 * 
	 * @param viewId
	 *            控件id
	 * @param isVisible
	 *            是否可见
	 * @return
	 */
	public ViewHolder setVisibility(int viewId, boolean isVisible) {
		View tv = $(viewId);
		if (isVisible) {
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.GONE);
		}
		return this;
	}

	/**
	 * 设置控件的点击监听
	 * 
	 * @param viewId
	 *            控件id
	 * @param onClickListener
	 *            监听器
	 * @return
	 */
	public ViewHolder setOnclickListener(int viewId, OnClickListener onClickListener) {
		View tv = $(viewId);
		tv.setOnClickListener(onClickListener);
		return this;
	}
}
