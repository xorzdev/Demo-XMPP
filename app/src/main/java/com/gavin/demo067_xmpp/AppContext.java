package com.gavin.demo067_xmpp;

import android.app.Application;

import com.gavin.demo067_xmpp.db.util.DbCore;


/**
 * Created by gavin.xiong on 2016/5/18.
 */
public class AppContext extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        DbCore.init(this);
    }

    public static Application getApplication() {
        return application;
    }
}
