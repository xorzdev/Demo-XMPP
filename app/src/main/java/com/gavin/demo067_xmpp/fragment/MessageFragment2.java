package com.gavin.demo067_xmpp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.adapter.MessageAdapter2;
import com.gavin.demo067_xmpp.db.service.IMMessageService;
import com.gavin.demo067_xmpp.db.util.DbUtil;
import com.gavin.demo067_xmpp.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MessageFragment2 extends XXXFragment {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;

    private MessageAdapter2 adapter;
    private List<IMMessage> messageList = new ArrayList<>();

    private MessageBroadCastReceiver mReceiver;// 消息接收器
    private IMMessageService newMessageService;

    public MessageFragment2() {
        super(R.layout.fragment_test);
    }

    @Override
    public void initData() {
        refreshNewMessage();
    }

    @Override
    public void bindViews() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshNewMessage();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newMessageService.changeMessageStatus(messageList.get(position).getPassName());
                refreshNewMessage();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IntentExtra.USER_NAME, messageList.get(position).getName());
                bundle.putString(Constants.IntentExtra.USER_USER, messageList.get(position).getPassName());
                mA.nextFragment(new ChatFragment(), bundle);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 注册广播
        if (mReceiver == null) {
            mReceiver = new MessageBroadCastReceiver();
        }
        newMessageService = DbUtil.getImMessageService();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(800);
        intentFilter.addAction(Constants.XMPP.RECEIVE_MESSAGE);
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 注销广播 关闭数据库
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    public void setAdapter() {
        if (adapter == null) {
            adapter = new MessageAdapter2(mA, messageList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新新消息
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-7 上午11:23:10
     * @version V1.0
     */
    private void refreshNewMessage() {
        // 查询数据并展示
        messageList.clear();
        messageList.addAll(newMessageService.queryNewMessage());
        setAdapter();
    }

    /**
     * 消息接收广播 接收来自服务器的消息提醒
     *
     * @author yeliangliang
     * @ClassName: MessageBroadCastReceiver
     * @date 2015-8-4 上午11:57:37
     */
    private class MessageBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.XMPP.RECEIVE_MESSAGE.equals(intent.getAction())) {
                refreshNewMessage();
            }
        }
    }
}
