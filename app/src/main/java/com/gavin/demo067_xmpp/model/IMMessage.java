package com.gavin.demo067_xmpp.model;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.xmpp.XMPPConnectManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

/**
 * 自定义xmpp消息实体体
 *
 * @author gavin.xiong
 * @date 2016/7/19
 */
@Entity
public class IMMessage implements Serializable {

    @Id(autoincrement = true)
    private Long _Id;
    @Unique
    private String id;// id
    private String name;// 姓名
    private String passName;// 账户
    private long time;// 时间
    private String content;// 内容
    private int type;   // 消息类型 0文本消息
    private int source;// 来源 0接收方 1发送方
    private int status;// 状态 0未读 1已读
    @Transient
    private int unReadCount;// 未读条数
    private String master; // 消息属于谁  考虑到可能会登录多个账号


    @Generated(hash = 116582306)
    public IMMessage(Long _Id, String id, String name, String passName, long time, String content, int type,
                     int source, int status, String master) {
        this._Id = _Id;
        this.id = id;
        this.name = name;
        this.passName = passName;
        this.time = time;
        this.content = content;
        this.type = type;
        this.source = source;
        this.status = status;
        this.master = master;
    }

    @Generated(hash = 1610895367)
    public IMMessage() {
    }

    /**
     * xmpp消息转为本地消息
     *
     * @param msg Message
     */
    public IMMessage(Message msg) {
        this.setId(msg.getStanzaId());
        this.setName(msg.getFrom().split("@")[0]);
        this.setPassName(this.getName() + "@" + Constants.XMPP.SERVICE_NAME);
        this.setContent(msg.getBody());
        this.setType(0);
        this.setSource(0);
        this.setStatus(0);
        long time = getMessageTime(msg);
        this.setTime(time > 0 ? time : System.currentTimeMillis());
        this.setMaster(XMPPConnectManager.getInstance().getUserInfo().getUserPassName());
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSource() {
        return this.source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPassName() {
        return this.passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long get_Id() {
        return this._Id;
    }

    public void set_Id(Long _Id) {
        this._Id = _Id;
    }


    public String getMaster() {
        return this.master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", passName='" + passName + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", source=" + source +
                ", status=" + status +
                ", master='" + master + '\'' +
                '}';
    }

    /**
     * 获取消息时间
     *
     * @param message 消息
     * @return id
     */
    public long getMessageTime(Message message) {
        DelayInformation delay = message.getExtension(DelayInformation.ELEMENT, DelayInformation.NAMESPACE);
        if (delay == null) return 0;
        if (delay.getStamp() == null) return 0;
        return delay.getStamp().getTime();
    }

}
