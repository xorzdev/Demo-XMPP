package com.gavin.demo067_xmpp.utils;

import android.widget.Toast;

import com.gavin.demo067_xmpp.AppContext;

public class T {

    private static Toast mToast = null;

    public static void s(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(AppContext.getApplication(), resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void s(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(AppContext.getApplication(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void l(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(AppContext.getApplication(), resId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void l(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(AppContext.getApplication(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

}
