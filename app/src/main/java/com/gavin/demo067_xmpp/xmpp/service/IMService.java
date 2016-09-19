package com.gavin.demo067_xmpp.xmpp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.db.service.IMMessageService;
import com.gavin.demo067_xmpp.db.util.DbUtil;
import com.gavin.demo067_xmpp.model.IMMessage;
import com.gavin.demo067_xmpp.utils.L;
import com.gavin.demo067_xmpp.utils.NetUtil;
import com.gavin.demo067_xmpp.utils.T;
import com.gavin.demo067_xmpp.utils.ThreadPoolUtil;
import com.gavin.demo067_xmpp.xmpp.XMPPConnectManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/17.
 */
public class IMService extends Service implements NetUtil.OnNetStatusChangeListener {
    private Context context;
    // XMPPConnect
    private AbstractXMPPConnection connection;
    // 聊天管理器
    private ChatManager chatManager = null;
    private ChatManagerListener chatManagerListener;
    private ChatMessageListener chatMessageListener;

    private IMMessageService imMessageService;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        imMessageService = DbUtil.getImMessageService();

        // 註冊网络连接廣播
        NetUtil.getInstance().addNetStatusListener(context, this);
        // 初始化IM消息監聽
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                setChatMessageListener();
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);

        getOfflineMessage();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 註銷廣播接收器
        NetUtil.getInstance().removeNetStatusListener(context, this);
        if (chatManager != null) {
            chatManager.removeChatListener(chatManagerListener);
        }
    }

    @Override
    public void onNetChange(NetworkInfo netInfo) {
        if (connection == null) {
            return;
        }
        if (netInfo != null && netInfo.isAvailable()) {
            // 有網絡但是連接斷開,遞歸連接
            if (!connection.isConnected()) {
                reConnect();
            } else {
                setChatMessageListener();
            }
        } else {
            T.s("网络断开,用户已离线!");
        }
    }

    /**
     * 设置聊天管理器
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-3 下午6:20:45
     * @version V1.0
     */
    public void setChatMessageListener() {
        connection = XMPPConnectManager.getInstance().getConnection();
        if (connection == null) {
            return;
        }
        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.removeChatListener(chatManagerListener);
        chatManagerListener = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                if (!createdLocally) {
                    setChatMessageListener(chat);
                }
            }
        };
        chatManager.addChatListener(chatManagerListener);
    }

    /**
     * 设置消息监听器
     *
     * @param chat
     */
    private void setChatMessageListener(Chat chat) {
        chat.removeMessageListener(chatMessageListener);
        chatMessageListener = new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                String content = message.getBody();
                if (!TextUtils.isEmpty(content)) {
                    L.e("processMessage - " + content);
                    IMMessage imMessage = new IMMessage(message);
                    imMessageService.save(imMessage);
                    // 发送广播
                    Intent intent = new Intent(Constants.XMPP.RECEIVE_MESSAGE);
                    intent.putExtra(Constants.XMPP.IM_MESSAGE, imMessage);
                    sendOrderedBroadcast(intent, Constants.XMPP.PERMISSION_MESSAGE);
                }
            }
        };
        chat.addMessageListener(chatMessageListener);
    }

    /**
     * 递归重连，直连上为止.*
     */
    private void reConnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!connection.isConnected()) {
                        connection.connect();
                        if (connection.isConnected()) {
                            Presence presence = new Presence(Presence.Type.available);
                            connection.sendStanza(presence);
                        }
                    }
                } catch (Exception e) {
                    L.e("connection failed!" + e.toString());
                    reConnect();
                }
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }

    /**
     * 获取离线消息
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-11 下午5:21:09
     * @version V1.0
     */
    private void getOfflineMessage() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
//                    OfflineMessageManager offlineManager = new OfflineMessageManager(XMPPConnectManager.getInstance().getConnection());
//                    List<Message> offList = offlineManager.getMessages();
//                    List<IMMessage> imList = new ArrayList<>();
//                    for (Message msg : offList) {
//                        L.v(imList);
//                        imList.add(new IMMessage(msg));
//                        try {
//                            imMessageService.save(new IMMessage(msg));
//                        } catch (Exception e) {
//
//                        }
//                    }
//                    imMessageService.save(imList);
//                    // 干掉服务器中的离线消息
//                    offlineManager.deleteMessages();
                    // 上线
                    Presence presence = new Presence(Presence.Type.available);
                    XMPPConnectManager.getInstance().getConnection().sendStanza(presence);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(r);

        try {
            // 上线
            Presence presence = new Presence(Presence.Type.available);
            XMPPConnectManager.getInstance().getConnection().sendStanza(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取消息时间
     *
     * @param message
     * @return
     */
    public long getMessageTime(Message message) {
        DelayInformation delay = message.getExtension(DelayInformation.ELEMENT, DelayInformation.NAMESPACE);
        if (delay == null) return 0;
        if (delay.getStamp() == null) return 0;
        return delay.getStamp().getTime();
    }

}
