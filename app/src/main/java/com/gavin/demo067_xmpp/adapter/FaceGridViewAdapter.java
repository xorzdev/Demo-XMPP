package com.gavin.demo067_xmpp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.model.EmojiBean;

import java.util.ArrayList;

/**
 * 表情选择框的gridview适配器
 * 
 * @ClassName: FaceGridViewAdapter
 * @author yeliangliang
 * @date 2015-8-14 下午5:50:01
 */
public class FaceGridViewAdapter extends BaseAdapter {

	private ArrayList<EmojiBean> mList;
	private LayoutInflater inflater;
	private Context mContext;
	private HolderView holderView;

	public FaceGridViewAdapter(Context c, ArrayList<EmojiBean> list) {
		this.mList = list;
		this.mContext = c;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holderView = new HolderView();
			convertView = inflater.inflate(R.layout.item_face, null);
			holderView.img_face = (ImageView) convertView.findViewById(R.id.img_face);
			convertView.setTag(R.id.tag_view, holderView);
		} else {
			holderView = (HolderView) convertView.getTag(R.id.tag_view);
		}
		EmojiBean emojiBean = mList.get(position);
		convertView.setTag(R.id.tag_emoji, emojiBean);
		holderView.img_face
				.setImageDrawable(mContext.getResources().getDrawable(emojiBean.getId()));
		return convertView;
	}

	private class HolderView {
		private ImageView img_face;
	}
}
