package com.gavin.demo067_xmpp.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.gavin.demo067_xmpp.model.IMMessage;

import com.gavin.demo067_xmpp.db.dao.IMMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig iMMessageDaoConfig;

    private final IMMessageDao iMMessageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        iMMessageDaoConfig = daoConfigMap.get(IMMessageDao.class).clone();
        iMMessageDaoConfig.initIdentityScope(type);

        iMMessageDao = new IMMessageDao(iMMessageDaoConfig, this);

        registerDao(IMMessage.class, iMMessageDao);
    }
    
    public void clear() {
        iMMessageDaoConfig.clearIdentityScope();
    }

    public IMMessageDao getIMMessageDao() {
        return iMMessageDao;
    }

}