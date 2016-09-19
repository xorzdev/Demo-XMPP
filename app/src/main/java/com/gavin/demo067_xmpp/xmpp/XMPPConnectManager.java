package com.gavin.demo067_xmpp.xmpp;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.model.UserInfo;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


/**
 * 用户管理类
 *
 * @author yeliangliang
 * @ClassName: User
 * @date 2015-7-27 上午11:06:22
 */
public class XMPPConnectManager {

    private XMPPTCPConnectionConfiguration configuration = null;

    private AbstractXMPPConnection connection = null;
    // 用户信息
    private UserInfo userInfo = null;
    // 单例
    private static XMPPConnectManager xmppConnectManager = null;

    public static XMPPConnectManager getInstance() {
        if (xmppConnectManager == null) {
            xmppConnectManager = new XMPPConnectManager();
        }
        return xmppConnectManager;
    }

    /**
     * 获取XMPP配置器
     *
     * @param passName
     * @param passWord
     * @return XMPPTCPConnectionConfiguration
     * @author yeliangliang
     * @date 2015-7-27 上午11:41:50
     * @version V1.0
     */
    public XMPPTCPConnectionConfiguration getConfiguration(String passName, String passWord) {
        if (configuration == null) {
            configuration = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(passName, passWord)
                    .setHost(Constants.XMPP.SERVICE_HOST)
                    .setServiceName(Constants.XMPP.SERVICE_NAME)
                    .setPort(Constants.XMPP.SERVICE_PORT)
                    .setConnectTimeout(1000 * 30)
                    .setSecurityMode(SecurityMode.disabled)
                    .setSendPresence(false) // 设置用户是否上线
                    .build();
        }
        return configuration;
    }

    /**
     * 获取服务器连接器
     *
     * @return AbstractXMPPConnection
     * @author yeliangliang
     * @date 2015-7-27 下午1:37:30
     * @version V1.0
     */
    public AbstractXMPPConnection getConnection() {
        if (connection != null && connection.isConnected()) {
            return connection;
        }
        return null;
    }

    /**
     * 设置服务器连接器
     *
     * @param c 连接配置
     * @return AbstractXMPPConnection
     * @author yeliangliang
     * @date 2015-7-27 下午1:37:40
     * @version V1.0
     */
    public AbstractXMPPConnection setConnection(XMPPTCPConnectionConfiguration c) {
        this.connection = new XMPPTCPConnection(c);
        /**
         * 实现断线重连
         */
        ReconnectionManager manager = ReconnectionManager.getInstanceFor(connection);
        manager.enableAutomaticReconnection();
        manager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        manager.setFixedDelay(1000 * 5);
//        StanzaFilter filter = new StanzaFilter() {
//
//            @Override
//            public boolean accept(Stanza arg0) {
//                return true;
//            }
//        };
//        StanzaListener listener = new StanzaListener() {
//
//            @Override
//            public void processPacket(Stanza arg0) throws NotConnectedException {
//            }
//        };
//        connection.addPacketInterceptor(listener, filter);
//      connection.addConnectionListener(new ConnectionListener() {
        return this.connection;
    }

    /**
     * 设置用户信息
     *
     * @param u
     * @return UserInfo
     * @author yeliangliang
     * @date 2015-7-27 下午1:41:13
     * @version V1.0
     */
    public UserInfo setUserInfo(UserInfo u) {
        this.userInfo = u;
        return this.userInfo;
    }

    /**
     * 获取用户信息
     *
     * @return UserInfo
     * @author yeliangliang
     * @date 2015-7-27 下午1:41:58
     * @version V1.0
     */
    public UserInfo getUserInfo() {
        if (userInfo == null) {
            return null;
        }
        return this.userInfo;
    }

    /**
     * 获取聊天管理器
     *
     * @return ChatManager
     */
    public ChatManager getChatManager() {
        return ChatManager.getInstanceFor(connection);
    }
}
