package com.gavin.demo067_xmpp.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.gavin.demo067_xmpp.activity.XXXActivity;

/**
 * XXXFragment
 * <p/>
 * Created by gavin.xiong on 2016/4/20.
 */
public abstract class XXXFragment extends BaseFragment {

    public XXXActivity mA;

    public XXXFragment(@LayoutRes int layoutId) {
        super(layoutId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mA = (XXXActivity) getActivity();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 从activity remove时 清空view组
     */
    @Override
    public void onDetach() {
        super.onDetach();
        if (array != null) {
            array.clear();
            array = null;
        }
    }

    /**
     * 页面刷新
     */
    public void onRefreshPage() {

    }

}
