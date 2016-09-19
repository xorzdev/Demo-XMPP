package com.gavin.demo067_xmpp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.gavin.demo067_xmpp.AppManager;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.utils.T;
import com.gavin.demo067_xmpp.utils.ThreadPoolUtil;

import butterknife.ButterKnife;


/**
 * 基类 activity
 * <p/>
 * Created by gavin.xiong on 2016/5/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {

    private int layoutId = -1;
    private SparseArray<View> array;
    private ProgressDialog progressDialog;

    public BaseActivity(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(layoutId);
        ButterKnife.bind(this);
        initData();
        bindViews();
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 获取组件
     *
     * @param resId resId
     * @return ? extend View
     */
    @SuppressWarnings("unchecked")
    public <A extends View> A $(@IdRes int resId) {
        if (array == null) {
            array = new SparseArray<>();
        }
        View view = array.get(resId);
        if (view == null) {
            view = findViewById(resId);
            array.put(resId, view);
        }
        return (A) view;
    }



    /**
     * 显示进度对话框
     *
     * @param msg msg
     */
    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private long exitTime = 0;
    private long lastTime = 0;
    public void exitApp() {
        exitTime = System.currentTimeMillis() - lastTime;
        if (exitTime <= 2000) {
            // 释放所有Activity
            AppManager.getAppManager().finishAllActivity();
            // 关闭所有正在执行的线程
            ThreadPoolUtil.closeAllThreadPool();
            System.exit(0);
        } else {
            lastTime = System.currentTimeMillis();
            T.s("再按一次返回键，退出程序");
        }
    }
}
