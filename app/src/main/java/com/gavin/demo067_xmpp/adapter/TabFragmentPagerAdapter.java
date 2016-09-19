package com.gavin.demo067_xmpp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gavin.demo067_xmpp.fragment.XXXFragment;

import java.util.ArrayList;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<XXXFragment> fragmentList = null;

	public TabFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<XXXFragment> list){
		super(fm);
		this.fragmentList = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
}
