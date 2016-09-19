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
 * 最近消息列表
 *
 * @author gavin.xiong
 * @date 2016/7/20
 */
public class MessageAdapter2 extends BaseListAdapter<IMMessage> {

    FaceUtil faceUtil;
    int[] PHOTO_TEXT_BACKGROUND_COLORS;

    public MessageAdapter2(Context mContext, List<IMMessage> mList) {
        super(mContext, mList, R.layout.item_message2);
        faceUtil = FaceUtil.getInstance(mContext);
        PHOTO_TEXT_BACKGROUND_COLORS = mContext.getResources().getIntArray(R.array.contacts_text_background_colors);
    }

    @Override
    protected void convert(ViewHolder holder, IMMessage message, int position) {
        holder.setText(R.id.tv_name, message.getName());
        holder.setText(R.id.tv_content, faceUtil.matchingString(message.getContent(), false));
        holder.setText(R.id.tv_time, message.getTime() + "");

        if (message.getUnReadCount() > 0) {
            holder.setText(R.id.tv_count, message.getUnReadCount() + "");
        } else {
            holder.setText(R.id.tv_count, "");
        }

        CircularContactView circularContactView = holder.$(R.id.ccv_head);
        int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[position % PHOTO_TEXT_BACKGROUND_COLORS.length];
        if (TextUtils.isEmpty(message.getPassName()))
            circularContactView.setImageResource(R.drawable.ic_person_white_120dp, backgroundColorToUse);
        else {
            String characterToShow = TextUtils.isEmpty(message.getName()) ? "" : message.getName().substring(0, 1).toUpperCase(Locale.getDefault());
            circularContactView.setTextAndBackgroundColor(characterToShow, backgroundColorToUse);
            circularContactView.setImageResource(R.drawable.ic_person_white_120dp, backgroundColorToUse);
        }
    }
}
