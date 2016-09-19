package com.gavin.demo067_xmpp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.activity.XXXActivity;
import com.gavin.demo067_xmpp.fragment.ChatFragment;
import com.gavin.demo067_xmpp.widget.listvariants.CircularContactView;
import com.gavin.demo067_xmpp.widget.listvariants.SearchablePinnedHeaderListViewAdapter;
import com.gavin.demo067_xmpp.widget.listvariants.StringArrayAlphabetIndexer;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 通讯录列表适配器
 *
 * @author gavin.xiong
 * @date 2016/7/18
 */
public class FriendAdapter2 extends SearchablePinnedHeaderListViewAdapter<RosterEntry> {

    private Context context;
    private ArrayList<RosterEntry> mContacts;
    private int[] PHOTO_TEXT_BACKGROUND_COLORS;

    @Override
    public CharSequence getSectionTitle(int sectionIndex) {
        return ((StringArrayAlphabetIndexer.AlphaBetSection) getSections()[sectionIndex]).getName();
    }

    public FriendAdapter2(Context context, ArrayList<RosterEntry> contacts) {
        this.context = context;
        this.mContacts = contacts;
        String[] generatedContactNames = generateContactNames(contacts);
        setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames, true));
        PHOTO_TEXT_BACKGROUND_COLORS = context.getResources().getIntArray(R.array.contacts_text_background_colors);
    }

    private String[] generateContactNames(List<RosterEntry> contacts) {
        ArrayList<String> contactNames = new ArrayList<>();
        if (contacts != null)
            for (RosterEntry contactEntity : contacts)
                contactNames.add(contactEntity.getUser());
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View rootView;
        if (convertView == null) {
            holder = new ViewHolder();
            rootView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
            holder.friendProfileCircularContactView = (CircularContactView) rootView
                    .findViewById(R.id.listview_item__friendPhotoImageView);
            holder.friendProfileCircularContactView.getTextView().setTextColor(0xFFffffff);
            holder.friendName = (TextView) rootView
                    .findViewById(R.id.listview_item__friendNameTextView);
            holder.headerView = (TextView) rootView.findViewById(R.id.header_text);
            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }
        final RosterEntry contact = getItem(position);
        String displayName = contact.getUser();
        holder.friendName.setText(displayName);

//        boolean hasPhoto = !TextUtils.isEmpty(contact.photoId);
//        Bitmap cachedBitmap = hasPhoto ? ImageCache.INSTANCE.getBitmapFromMemCache(contact.photoId) : null;
//        if (cachedBitmap != null)
//            holder.friendProfileCircularContactView.setImageBitmap(cachedBitmap);
//        else {
        int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[position % PHOTO_TEXT_BACKGROUND_COLORS.length];
        if (TextUtils.isEmpty(displayName))
            holder.friendProfileCircularContactView.setImageResource(R.drawable.ic_person_white_120dp, backgroundColorToUse);
        else {
            String characterToShow = TextUtils.isEmpty(displayName) ? "" : displayName.substring(0, 1).toUpperCase(Locale.getDefault());
            holder.friendProfileCircularContactView.setTextAndBackgroundColor(characterToShow, backgroundColorToUse);
            holder.friendProfileCircularContactView.setImageResource(R.drawable.ic_person_white_120dp, backgroundColorToUse);
        }
//            if (hasPhoto) {
//
//            }
//        }
        bindSectionHeader(holder.headerView, null, position);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击Item跳转到好友信息页面
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IntentExtra.USER_NAME, contact.getName());
                bundle.putString(Constants.IntentExtra.USER_USER, contact.getUser());
                ((XXXActivity) context).nextFragment(new ChatFragment(), bundle);
            }
        });

        return rootView;
    }

    @Override
    public boolean doFilter(RosterEntry item, CharSequence constraint) {
        if (TextUtils.isEmpty(constraint))
            return true;
        String displayName = item.getUser();
        return !TextUtils.isEmpty(displayName) && displayName.toLowerCase(Locale.getDefault())
                .contains(constraint.toString().toLowerCase(Locale.getDefault()));
    }

    @Override
    public ArrayList<RosterEntry> getOriginalList() {
        return mContacts;
    }

    class ViewHolder {
        public CircularContactView friendProfileCircularContactView;
        public TextView friendName, headerView;
    }

}

