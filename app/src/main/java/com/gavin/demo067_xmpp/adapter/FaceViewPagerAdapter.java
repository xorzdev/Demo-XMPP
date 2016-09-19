package com.gavin.demo067_xmpp.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * 表情选择框的ViewPager适配器
 * 
 * @ClassName: FaceViewPagerAdapter
 * @author yeliangliang
 * @date 2015-8-14 下午4:36:13
 */
public class FaceViewPagerAdapter extends PagerAdapter {

	private ArrayList<GridView> mList;

	public FaceViewPagerAdapter(ArrayList<GridView> list) {
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mList.get(position));
		return mList.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mList.get(position));
	}
}
