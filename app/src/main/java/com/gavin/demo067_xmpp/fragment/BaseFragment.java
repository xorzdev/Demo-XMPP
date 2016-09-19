package com.gavin.demo067_xmpp.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gavin.demo067_xmpp.activity.BaseActivity;
import com.gavin.demo067_xmpp.activity.BaseViewInterface;

import butterknife.ButterKnife;

/**
 * Fragment 基类
 * <p/>
 * Created by gavin.xiong on 2016/5/18.
 */
public abstract class BaseFragment extends Fragment implements BaseViewInterface {

    public BaseActivity mActivity;
    public View mView;
    protected SparseArray<View> array;
    private int layoutId = -1;

    public BaseFragment(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(layoutId, null);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        initData();
        bindViews();
    }

    /**
     * 获取组件
     *
     * @param resId 资源id
     * @return ? extend View
     */
    @SuppressWarnings("unchecked")
    public <A extends View> A $(int resId) {
        if (array == null) {
            array = new SparseArray<>();
        }
        View view = array.get(resId);
        if (view == null) {
            view = mView.findViewById(resId);
            array.put(resId, view);
        }
        return (A) view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(mView);
    }
}
