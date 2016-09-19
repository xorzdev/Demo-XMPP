package com.gavin.demo067_xmpp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.adapter.FriendAdapter2;
import com.gavin.demo067_xmpp.widget.listvariants.PinnedHeaderListView;
import com.gavin.demo067_xmpp.xmpp.NetCallBackListener;
import com.gavin.demo067_xmpp.xmpp.XMPPManager;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

/**
 * 登录页
 * <p/>
 * Created by gavin.xiong on 2016/5/19.
 */
public class FriendFragment2 extends XXXFragment {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(android.R.id.list)
    PinnedHeaderListView listView;

    private MyBroadCastReceiver receiver;

    private FriendAdapter2 mAdapter;
    private ArrayList<RosterEntry> contacts = new ArrayList<>();

    public FriendFragment2() {
        super(R.layout.fragment_friend2);
    }

    @Override
    public void initData() {
        requestRosterList();
    }

    @Override
    public void bindViews() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRosterList();
            }
        });
    }

    private void showData() {
        Collections.sort(contacts, new Comparator<RosterEntry>() {
            @Override
            public int compare(RosterEntry lhs, RosterEntry rhs) {
                char lhsFirstLetter = TextUtils.isEmpty(lhs.getUser()) ? ' ' : lhs.getUser().charAt(0);
                char rhsFirstLetter = TextUtils.isEmpty(rhs.getUser()) ? ' ' : rhs.getUser().charAt(0);
                int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                if (firstLetterComparison == 0)
                    return lhs.getUser().compareTo(rhs.getUser());
                return firstLetterComparison;
            }
        });

        mAdapter = new FriendAdapter2(mA, contacts);

        int pinnedHeaderBackgroundColor = ContextCompat.getColor(mA, getResIdFromAttribute(mA, android.R.attr.colorBackground));
        mAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
        mAdapter.setPinnedHeaderTextColor( ContextCompat.getColor(mA, R.color.pinned_header_text));
        listView.setPinnedHeaderView(LayoutInflater.from(mA).inflate(R.layout.layout_pinned_header, listView, false));
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(mAdapter);
        listView.setEnableHeaderTransparencyChanges(false);
    }

    public void performSearch(String queryText) {
        mAdapter.getFilter().filter(queryText);
        mAdapter.setHeaderViewVisible(TextUtils.isEmpty(queryText));
    }

    public static int getResIdFromAttribute(Activity activity, int attr) {
        if (attr == 0)
            return 0;
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

    /**
     * 请求好友列表
     *
     * @author yeliangliang
     * @date 2015-7-27 下午2:06:43
     * @version V1.0
     * @return void
     */
    private void requestRosterList() {
        swipeRefreshLayout.setRefreshing(true);
        XMPPManager.requestRosterList(new NetCallBackListener() {
            @Override
            public void responseSuccess(int i, Object o) {
                swipeRefreshLayout.setRefreshing(false);
                handler.obtainMessage(i, o).sendToTarget();
            }

            @Override
            public void responseFailed(int i) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(listView, "获取好友列表失败", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void responseError(int i) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(listView, "获取好友列表失败", Snackbar.LENGTH_LONG).show();
            }

        });

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case Constants.XMPP.REQUEST_SUCCESS:
                    contacts.clear();
                    contacts.addAll((List<RosterEntry>) message.obj);
                    showData();
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (receiver == null) {
            receiver = new MyBroadCastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.XMPP.RECEIVE_FRIENDS);
        context.registerReceiver(receiver, intentFilter);
    }

    private class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            requestRosterList();
        }

    }

}
