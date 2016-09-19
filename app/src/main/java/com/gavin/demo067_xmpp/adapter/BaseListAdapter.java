package com.gavin.demo067_xmpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基类适配器
 *
 * Created by gavin.xiong on 2016/4/5.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

	protected List<T> mList;
	private LayoutInflater inflater;
	private int layoutId;
	public Context mContext;

	public BaseListAdapter(Context mContext, List<T> mList, int layoutId) {
		inflater = LayoutInflater.from(mContext);
		this.mList = mList;
		this.layoutId = layoutId;
		this.mContext = mContext;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public T getItem(int arg0) {
		return mList != null ? mList.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(layoutId, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		convert(holder, getItem(position), position);
		return view;
	}

	protected abstract void convert(ViewHolder holder, T t, int position);
}
