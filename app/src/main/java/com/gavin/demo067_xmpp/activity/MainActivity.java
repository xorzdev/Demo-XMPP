package com.gavin.demo067_xmpp.activity;

import android.content.Intent;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.fragment.LoginFragment;
import com.gavin.demo067_xmpp.utils.L;
import com.gavin.demo067_xmpp.xmpp.service.IMService;
import com.gavin.demo067_xmpp.utils.FaceUtil;
import com.gavin.demo067_xmpp.utils.ThreadPoolUtil;
import com.gavin.demo067_xmpp.xmpp.XMPPConnectManager;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.offline.OfflineMessageHeader;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends XXXActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void initData() {
        initFragmentManager(R.id.content);
        replaceFragment(new LoginFragment());
    }

    @Override
    public void bindViews() {

    }

    /**
     * 初始化 xmpp
     */
    public void initIM() {
        initEmojiFace();
//        getOfflineMessage();

        Intent imService = new Intent(this, IMService.class);
        startService(imService);
    }

    /**
     * 初始化emoji表情
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-14 下午5:42:57
     * @version V1.0
     */
    private void initEmojiFace() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FaceUtil f = FaceUtil.getInstance(MainActivity.this);
                f.getEmojiList();
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }

    @Override
    public void onBackPressed() {
        if (list != null && list.size() > 1) {
            finishFragment();
        } else {
            moveTaskToBack(true);
        }
    }
}
