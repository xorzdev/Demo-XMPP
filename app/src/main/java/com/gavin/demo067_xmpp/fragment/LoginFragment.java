package com.gavin.demo067_xmpp.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.activity.MainActivity;
import com.gavin.demo067_xmpp.xmpp.service.IMService;
import com.gavin.demo067_xmpp.utils.InputUtil;
import com.gavin.demo067_xmpp.xmpp.NetCallBackListener;
import com.gavin.demo067_xmpp.xmpp.XMPPManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登录页
 * <p/>
 * Created by gavin.xiong on 2016/5/19.
 */
public class LoginFragment extends XXXFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_name)
    EditText et_name;
    @Bind(R.id.et_pass)
    EditText et_pass;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void initData() {
        // 如果意外被杀死 先关闭服务
        Intent imService = new Intent(mA, IMService.class);
        mA.stopService(imService);
    }

    @Override
    public void bindViews() {
        toolbar.setTitle("登录");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_2x);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mA.onBackPressed();
            }
        });
    }

    @OnClick({R.id.button, R.id.button2})
    public void onClick(View view) {
        InputUtil.hide(mA, mA.getCurrentFocus());
        switch (view.getId()) {
            case R.id.button:
                loginXMPP(et_name.getText().toString(), et_pass.getText().toString());
                break;
            case R.id.button2:
                break;
            case R.id.tv_forgot:
                break;
        }
    }

    /**
     * 登录xmpp
     *
     * @param name
     * @param pass
     */
    private void loginXMPP(String name, String pass) {
        mA.showProgressDialog("正在登录...");
        XMPPManager.requestLogin(mA, name, pass, new NetCallBackListener() {
            @Override
            public void responseSuccess(int i, Object o) {
                mA.closeProgressDialog();
                if (i == Constants.XMPP.REQUEST_SUCCESS) {
                    // 登录成功
                    // Snackbar.make(toolbar, "登录成功", Snackbar.LENGTH_LONG).show();
                    mA.replaceFragment(new MainFragment());
                    ((MainActivity)mA).initIM();
                }
            }

            @Override
            public void responseFailed(int i) {
                mA.closeProgressDialog();
                Snackbar.make(toolbar, "登录失败", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void responseError(int i) {
                mA.closeProgressDialog();
                Snackbar.make(toolbar, "登录失败", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
