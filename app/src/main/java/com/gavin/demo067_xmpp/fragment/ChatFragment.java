package com.gavin.demo067_xmpp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.adapter.ChatAdapter;
import com.gavin.demo067_xmpp.adapter.TextWatcherAdapter;
import com.gavin.demo067_xmpp.db.service.IMMessageService;
import com.gavin.demo067_xmpp.db.util.DbUtil;
import com.gavin.demo067_xmpp.model.IMMessage;
import com.gavin.demo067_xmpp.utils.InputUtil;
import com.gavin.demo067_xmpp.utils.L;
import com.gavin.demo067_xmpp.utils.NetUtil;
import com.gavin.demo067_xmpp.utils.T;
import com.gavin.demo067_xmpp.widget.ChatEditText;
import com.gavin.demo067_xmpp.xmpp.NetCallBackListener;
import com.gavin.demo067_xmpp.xmpp.XMPPConnectManager;
import com.gavin.demo067_xmpp.xmpp.XMPPManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 聊天主界面
 *
 * @author gavin.xiong
 * @date 2016/7/20
 */
public class ChatFragment extends XXXFragment implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.edt_input)
    ChatEditText edt_input;// 输入框
    @Bind(R.id.tv_post)
    TextView tv_post;// 发送按钮
    @Bind(R.id.img_add)
    ImageView img_add;// 添加按钮
    @Bind(R.id.rl_face)
    RelativeLayout faceRelativeLayout;// 表情选择框

    private ArrayList<IMMessage> chatMessageList = new ArrayList<>();// 聊天信息集合
    private ChatAdapter mAdapter;// 聊天信息列表适配器

    private XMPPConnectManager user;
    private AbstractXMPPConnection connection;
    private Chat chat;
    private ChatManager chatManager;
    private MessageBroadCastReceiver mReceiver;// 消息接收广播
    private IMMessageService messageService;
    private boolean tag_face = false;// 表情选择框打开的tag

    private String name;
    private String passName;

    private int offset = 0;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    @Override
    public void initData() {
        name = getArguments().getString(Constants.IntentExtra.USER_NAME);
        passName = getArguments().getString(Constants.IntentExtra.USER_USER);

        user = XMPPConnectManager.getInstance();
        connection = user.getConnection();
        chatMessageList = new ArrayList<>();
        mAdapter = new ChatAdapter(mA, chatMessageList);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        // 建立聊天连接
        if (connection != null) {
            chatManager = XMPPConnectManager.getInstance().getChatManager();
            chat = chatManager.createChat(passName);
        }

        // 声明控件以及添加监听
        findIdAndSetClick();

        messageService = DbUtil.getImMessageService();
        // 获取当前对象的聊天信息
        searchChatList();
    }

    @Override
    public void bindViews() {
        toolbar.setTitle(passName);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_2x);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputUtil.hide(mA, mA.getCurrentFocus());
                mA.onBackPressed();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                searchChatList();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (SCROLL_STATE_TOUCH_SCROLL == i) {
                    InputUtil.hide(mA, mA.getCurrentFocus());
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /**
         * 注册消息接收广播
         */
        if (mReceiver == null) {
            mReceiver = new MessageBroadCastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);
        intentFilter.addAction(Constants.XMPP.RECEIVE_MESSAGE);
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 注销广播
        if (mReceiver != null) {
            mA.unregisterReceiver(mReceiver);
        }
    }

    /**
     * 获取当前对象的聊天信息
     *
     * @author gavin.xiong
     * @date 2016/7/20
     */
    private void searchChatList() {
        List<IMMessage> list = messageService.queryChartList(passName, offset);
        if (list != null && !list.isEmpty()) {
            chatMessageList.addAll(0, messageService.queryChartList(passName, offset));
            mAdapter.notifyDataSetChanged();
            listView.setSelection(list.size());
            offset += Constants.XMPP.CHAT_LIMIT;
        }
    }

    /**
     * 声明控件以及添加监听
     *
     * @author gavin.xiong
     * @date 2016/7/20
     */
    private void findIdAndSetClick() {
        // 添加状态改变监听
        edt_input.addTextChangedListener(new TextWatcherAdapter(edt_input, new TextWatcherAdapter.TextWatcherListener() {
            @Override
            public void onTextChanged(EditText view, String text) {
                if (TextUtils.isEmpty(text.trim())) {
                    // 显示添加按钮
                    img_add.setVisibility(View.VISIBLE);
                    tv_post.setVisibility(View.GONE);
                } else {
                    // 显示发送按钮
                    img_add.setVisibility(View.GONE);
                    tv_post.setVisibility(View.VISIBLE);
                }
            }
        }));
        edt_input.setOnClickListener(this);
        // 设置输入框被点击的监听
        edt_input.setFaceIconOnClickListener(new ChatEditText.FaceIconOnClickListener() {

            @Override
            public void clickFace(boolean isFace) {
                if (!isFace) {
                    // 点击的不是表情图标
//                    InputUtil.show(mA);
                    // 关闭表情选择框
                    faceRelativeLayout.setVisibility(View.GONE);
                    tag_face = false;
                } else {
                    // 点击了表情图标
//                    InputUtil.hide(mA, edt_input);
                    if (tag_face) {
                        // 关闭表情选择
                        faceRelativeLayout.setVisibility(View.GONE);
                        tag_face = false;
                    } else {
                        // 打开表情选择框
                        faceRelativeLayout.setVisibility(View.VISIBLE);
                        tag_face = true;
                    }

                }
                listView.setSelection(chatMessageList.size() - 1);
            }
        });
        tv_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:// 发送按钮
//                InputUtil.hide(mA, edt_input);
                if (!NetUtil.isNetworkAvailable(mA)) {
                    T.s("网络断开连接");
                    return;
                }
                if (connection == null || chatManager == null) {
                    T.s("服务器已断开");
                    return;
                }
                sendMessage();
                edt_input.setText("");
                break;
            case R.id.edt_input:
                listView.setSelection(chatMessageList.size() - 1);
                break;
            default:
                break;
        }
    }

    /**
     * 发送信息
     *
     * @author gavin.xiong
     * @date 2016/7/20
     */
    private void sendMessage() {
        final IMMessage imMessage = new IMMessage();
        imMessage.setName(passName.split("@")[0]);
        imMessage.setPassName(passName);
        imMessage.setTime(System.currentTimeMillis());
        imMessage.setContent(edt_input.getText().toString());
        imMessage.setType(0);
        imMessage.setId(System.currentTimeMillis() + "");
        imMessage.setSource(1);
        imMessage.setStatus(1);
        imMessage.setMaster(XMPPConnectManager.getInstance().getUserInfo().getUserPassName());

        Snackbar.make(toolbar, edt_input.getText(), Snackbar.LENGTH_LONG).show();
        L.v(edt_input.getText());

        chatMessageList.add(imMessage);
        mAdapter.notifyDataSetChanged();
        listView.setSelection(chatMessageList.size() - 1);
        XMPPManager.postMessage(mA, imMessage, chat, new NetCallBackListener() {

            @Override
            public void responseSuccess(int i, Object o) {
                messageService.save(imMessage);
            }

            @Override
            public void responseFailed(int i) {
                handler.obtainMessage(i).sendToTarget();
            }

            @Override
            public void responseError(int i) {

            }
        });
    }

    /**
     * 接收消息
     *
     * @author gavin.xiong
     * @date 2016/7/20
     */
    private void receiverMessage(IMMessage msg) {
        if (listView.getLastVisiblePosition() == (chatMessageList.size() - 1)) {
            // 判断当前的滑动条是否在底部
            // 在底部
            chatMessageList.add(msg);
            mAdapter.notifyDataSetChanged();
            listView.setSelection(chatMessageList.size() - 1);
        } else {
            chatMessageList.add(msg);
            mAdapter.notifyDataSetChanged();
        }

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case Constants.XMPP.REQUEST_FAILD:
                    T.s("发送失败");
                    break;
            }
            return false;
        }
    });

    /**
     * 消息接收广播 接收来自服务器的消息提醒
     *
     * @author gavin.xiong
     * @date 2016/7/20
     */
    private class MessageBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.XMPP.RECEIVE_MESSAGE.equals(intent.getAction())) {
                IMMessage imMessage = (IMMessage) intent.getSerializableExtra(Constants.XMPP.IM_MESSAGE);
                // 判断是否为当前用户返回的消息
                if (imMessage.getPassName().equals(passName)) {
                    // 0代表已阅
                    mReceiver.setResultCode(0);
                    // 处理接收到的消息
                    receiverMessage(imMessage);
                } else {
                    // 1代表未读
                    mReceiver.setResultCode(1);
                }
            }
        }
    }

}
