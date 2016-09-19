package com.gavin.demo067_xmpp.db.service;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.db.dao.IMMessageDao;
import com.gavin.demo067_xmpp.model.IMMessage;
import com.gavin.demo067_xmpp.utils.L;
import com.gavin.demo067_xmpp.xmpp.XMPPConnectManager;
import com.gavin.demo067_xmpp.xmpp.XMPPManager;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class IMMessageService extends BaseService<IMMessage, Long> {
    public IMMessageService(IMMessageDao dao) {
        super(dao);
    }

    /**
     * 获取最新消息列表
     *
     * @return
     */
    public List<IMMessage> queryNewMessage() {
        // String sql = " SELECT * FROM IMMESSAGE WHERE TIME IN ( SELECT MAX( TIME ) FROM IMMESSAGE WHERE MASTER = ? GROUP BY PASS_NAME ) ORDER BY TIME DESC ";
        StringBuilder sb = new StringBuilder(" WHERE ")
                .append(IMMessageDao.Properties.Time.columnName)
                .append(" IN ( SELECT MAX ( ")
                .append(IMMessageDao.Properties.Time.columnName)
                .append(" ) FROM ")
                .append(IMMessageDao.TABLENAME)
                .append(" WHERE ")
                .append(IMMessageDao.Properties.Master.columnName)
                .append(" = ")
                .append(" '")
                .append(XMPPConnectManager.getInstance().getUserInfo().getUserPassName())
                .append("' ")
                .append(" GROUP BY ")
                .append(IMMessageDao.Properties.PassName.columnName)
                .append(" ) ORDER BY ")
                .append(IMMessageDao.Properties.Time.columnName)
                .append(" DESC ");
        List<IMMessage> list = query(sb.toString(), null);
        for (IMMessage i : list) {
            i.setUnReadCount(count(i.getPassName()));
        }
        L.v(list);
        return list;
    }

    /**
     * 获取某人未读消息的条数
     *
     * @param passName
     * @return
     */
    public int count(String passName) {
        // String sql = " SELECT status FROM IMMESSAGE WHERE PASS_NAME = passName AND status = 0 ";
        StringBuilder sb = new StringBuilder(" WHERE ")
                .append(IMMessageDao.Properties.PassName.columnName)
                .append(" = ? AND ")
                .append(IMMessageDao.Properties.Status.columnName)
                .append(" = ? ");
        return query(sb.toString(), passName, "0").size();
    }

    /**
     * 批量修改未读状态为已读
     *
     * @param passName
     */
    public void changeMessageStatus(String passName) {
        List<IMMessage> list = query(" WHERE "
                        + IMMessageDao.Properties.PassName.columnName + " = ? AND "
                        + IMMessageDao.Properties.Status.columnName + " = ? ",
                passName, "0");
        for (IMMessage i : list) {
            i.setStatus(1);
        }
        update(list);
    }

    public List<IMMessage> queryChartList(String passName, int offset) {
        List<IMMessage> list = queryBuilder()
                .where(
                        IMMessageDao.Properties.Master.eq(XMPPConnectManager.getInstance().getUserInfo().getUserPassName()),
                        IMMessageDao.Properties.PassName.eq(passName))
                .orderDesc(IMMessageDao.Properties.Time)
                .limit(Constants.XMPP.CHAT_LIMIT)
                .offset(offset)
                .list();
        Collections.reverse(list);
        return list;
    }


}
