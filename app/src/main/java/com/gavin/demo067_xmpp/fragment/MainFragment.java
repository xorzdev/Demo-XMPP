package com.gavin.demo067_xmpp.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.adapter.TabFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * 登录页
 * <p/>
 * Created by gavin.xiong on 2016/5/19.
 */
public class MainFragment extends XXXFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.bottomNavigation)
    BottomNavigation bottomNavigation;

    private ArrayList<XXXFragment> list;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add(new MessageFragment2());
        list.add(new FriendFragment2());
        list.add(new TestFragment());
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void bindViews() {
        toolbar.setTitle("XMPP");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_2x);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mA.onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.main);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setSelectedIndex(position, true);
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1) {
                viewPager.setCurrentItem(i1);
            }
            @Override
            public void onMenuItemReselect(@IdRes int i, int i1) { }
        });
    }

}
