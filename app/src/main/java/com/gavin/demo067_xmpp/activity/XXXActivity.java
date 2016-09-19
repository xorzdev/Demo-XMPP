package com.gavin.demo067_xmpp.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gavin.demo067_xmpp.fragment.XXXFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment home
 *
 * Created by gavin.xiong on 2016/5/18.
 */
public abstract class XXXActivity extends BaseActivity {

    public FragmentManager fragmentManager;
    public List<XXXFragment> list;
    public int contentResId;
    private OnBackPressedListener onBackPressedListener;

    public XXXActivity(@LayoutRes int resId) {
        super(resId);
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPassed();
        } else if (list != null && list.size() > 1) {
            finishFragment();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 初始化 Fragment 管理器
     */
    public void initFragmentManager(@IdRes int contentResId) {
        fragmentManager = getSupportFragmentManager();
        list = new ArrayList<>();
        this.contentResId = contentResId;
    }

    /**
     * 切换下一 Fragment（当前Fragment不保留）
     *
     * @param newFragment 将要显示的下一页面
     */
    public void replaceFragment(XXXFragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(contentResId, newFragment);
        transaction.commit();
        // 去除最后一个被代替的？
        if (list.size() > 0) {
            list.remove(list.size() - 1);
        }
        // 向列表中添加一个fragment
        list.add(newFragment);
    }

    /**
     * 进入下一页面（当前Fragment保留）
     *
     * @param newFragment 新Fragment
     * @param bundle      bundle
     */
    public void nextFragment(XXXFragment newFragment, Bundle bundle) {
        if (list != null && !list.isEmpty()) {
            nextFragment(list.get(list.size() - 1), newFragment, bundle);
        }
    }

    /**
     * 进入下一页面（当前Fragment保留）
     *
     * @param oldFragment 要被隐藏的当前页面
     * @param newFragment 将要显示的下一页面
     * @param bundle      传递参数 ，如果不需要传值，直接赋null
     */
    public void nextFragment(XXXFragment oldFragment, XXXFragment newFragment, Bundle bundle) {
//        menu.showContent();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != bundle) {
            newFragment.setArguments(bundle);
        }
        // 先判断是否被add过
        if (newFragment.isAdded()) { // 隐藏当前的fragment，显示下一个
            transaction.hide(oldFragment).show(newFragment).commitAllowingStateLoss();
            list.remove(newFragment);
        } else { // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(oldFragment).add(contentResId, newFragment).commitAllowingStateLoss();
        }
        newFragment.onResume();
        // 向列表中添加一个fragment
        list.add(newFragment);
    }

    /**
     * 销毁当前fragment，并显示上一页面
     */
    public void finishFragment() {
        if (list.size() > 1) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            XXXFragment currFragment = list.get(list.size() - 1);// 获取当前的fragment
            XXXFragment oldFragment = list.get(list.size() - 2);// 获取上一级的fragment
            oldFragment.onResume();
            // 移除当前的fragment，显示上一级
            transaction.remove(currFragment).show(oldFragment).commit();
            // currFragment = null;
            // 从列表中移除一个fragment
            list.remove(list.size() - 1);
        } else {
            this.finish();
        }
    }

    /**
     * 销毁当前fragment，并显示上一页面 并且刷新上一页面
     */
    public void finishFragmentAndRefresh() {
        if (list.size() > 1) {
            finishFragment();
            list.get(list.size() - 1).onRefreshPage();
        }
    }

    /**
     * 销毁指定fragment
     *
     * @param fragment 要被销毁的fragment
     */
    public void finishFragment(XXXFragment fragment) {
        if (list.size() > 1) {
            XXXFragment currFragment = list.get(list.size() - 1);// 获取当前的fragment
            if (fragment == currFragment) {
                finishFragment();
            } else {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment).commit();
                list.remove(fragment);
            }
        } else {
            this.finish();
        }
    }

    public interface OnBackPressedListener {
        void onBackPassed();
    }

}
