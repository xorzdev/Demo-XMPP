package com.gavin.demo067_xmpp.db.util;

import android.content.Context;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.db.dao.DaoMaster;
import com.gavin.demo067_xmpp.db.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;


/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-01
 * Time: 10:47
 */
public class DbCore {
    private static final String DEFAULT_DB_NAME = Constants.General.DB_NAME;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static Context mContext;
    private static String DB_NAME;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;

        enableQueryBuilderLog();
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static void enableQueryBuilderLog(){

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
}
