package com.gavin.demo067_xmpp.db.util;


import com.gavin.demo067_xmpp.db.dao.IMMessageDao;
import com.gavin.demo067_xmpp.db.service.IMMessageService;

/**
 * 数据库管理助手
 *
 * @author gavin.xiong
 * @date 2016/7/19
 */
public class DbUtil {
    private static IMMessageService imMessageService;

    private static IMMessageDao getIMMessageDao() {
        return DbCore.getDaoSession().getIMMessageDao();
    }

    public static IMMessageService getImMessageService() {
        if (imMessageService == null) {
            imMessageService = new IMMessageService(getIMMessageDao());
        }
        return imMessageService;
    }
}
