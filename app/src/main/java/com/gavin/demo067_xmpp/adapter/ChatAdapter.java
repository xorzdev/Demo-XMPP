package com.gavin.demo067_xmpp.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.model.IMMessage;
import com.gavin.demo067_xmpp.utils.FaceUtil;
import com.gavin.demo067_xmpp.widget.listvariants.CircularContactView;

import java.util.List;
import java.util.Locale;

/**
 * 聊天消息列表
 *
 * @author gavin.xiong
 * @date 2016/7/20
 */
public class ChatAdapter extends BaseListAdapter<IMMessage> {

    private FaceUtil faceUtil;
    private int color;

    public ChatAdapter(Context mContext, List<IMMessage> mList) {
        super(mContext, mList, R.layout.item_chat);
        faceUtil = FaceUtil.getInstance(mContext);
        color = mContext.getResources().getIntArray(R.array.contacts_text_background_colors)[0];
    }

    @Override
    protected void convert(ViewHolder holder, IMMessage message, int position) {

        if (message.getSource() == 0) {
            holder.setVisibility(R.id.rl_chat_my, false);
            holder.setVisibility(R.id.rl_chat_you, true);
            holder.setText(R.id.tv_chat_you, faceUtil.matchingString(message.getContent(), true));

            CircularContactView circularContactView = holder.$(R.id.ccv_head_you);
            if (TextUtils.isEmpty(message.getPassName()))
                circularContactView.setImageResource(R.drawable.ic_person_white_120dp, color);
            else {
                String characterToShow = TextUtils.isEmpty(message.getName()) ? "" : message.getName().substring(0, 1).toUpperCase(Locale.getDefault());
                circularContactView.setTextAndBackgroundColor(characterToShow, color);
                circularContactView.setImageResource(R.drawable.ic_person_white_120dp, color);
            }
        } else {
            holder.setVisibility(R.id.rl_chat_you, false);
            holder.setVisibility(R.id.rl_chat_my, true);
            holder.setText(R.id.tv_chat_my, faceUtil.matchingString(message.getContent(), true));

            CircularContactView circularContactView = holder.$(R.id.ccv_head_my);
            if (TextUtils.isEmpty(message.getPassName()))
                circularContactView.setImageResource(R.drawable.ic_person_white_120dp, color);
            else {
                String characterToShow = TextUtils.isEmpty(message.getName()) ? "" : message.getName().substring(0, 1).toUpperCase(Locale.getDefault());
                circularContactView.setTextAndBackgroundColor(characterToShow, color);
                circularContactView.setImageResource(R.drawable.ic_person_white_120dp, color);
            }
        }
    }
}
