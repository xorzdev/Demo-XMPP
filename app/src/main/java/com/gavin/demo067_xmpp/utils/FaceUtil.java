package com.gavin.demo067_xmpp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.model.EmojiBean;
import com.gavin.demo067_xmpp.widget.ChatEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 表情操作工具
 * 
 * @ClassName: FaceUtil
 * @author yeliangliang
 * @date 2015-8-14 下午4:23:52
 */
public class FaceUtil {
	private static FaceUtil mInstance;
	// 表情集合
	private ArrayList<EmojiBean> emojiList;
	// 表情集合
	private HashMap<String, Integer> emojiMap;
	// 表情分页集合
	public ArrayList<ArrayList<EmojiBean>> mlist;
	private static Context mContext;

	public static FaceUtil getInstance(Context c) {
		if (mInstance == null) {
			mInstance = new FaceUtil();
		}
		mContext = c;
		return mInstance;
	}

	/**
	 * 从资源读取表情到内存
	 * 
	 * @author yeliangliang
	 * @date 2015-8-14 下午4:50:41
	 * @version V1.0
	 * @return void
	 * @throws IOException
	 */
	private void readEmojiFromResource() {
		InputStream in;
		BufferedReader bin = null;
		try {
			in = mContext.getAssets().open("emoji");

			bin = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			emojiList = new ArrayList<>();
			emojiMap = new HashMap<>();
			while ((line = bin.readLine()) != null) {
				EmojiBean emojiBean = new EmojiBean();
				// 资源名称
				String name = line.split(",")[0];
				name = name.substring(0, name.lastIndexOf("."));
				String content = line.split(",")[1];
				int id = mContext.getResources().getIdentifier(name, "drawable",
						mContext.getPackageName());
				emojiBean.setName(name);
				emojiBean.setContent(content);
				emojiBean.setId(id);
				emojiMap.put(content, id);
				emojiList.add(emojiBean);
			}
		} catch (IOException e) {
			emojiList.clear();
			e.printStackTrace();
		} finally {
			if (bin != null) {
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取分页表情
	 * 
	 * @return
	 * @author yeliangliang
	 * @date 2015-8-14 下午4:47:09
	 * @version V1.0
	 * @return ArrayList<ArrayList<EmojiBean>>
	 */
	public void getEmojiList() {
		// 读取资源文件
		mlist = new ArrayList<ArrayList<EmojiBean>>();
		readEmojiFromResource();
		// 将表情分页 每页20个
		int size = emojiList.size();
		int count = (int) Math.ceil(size / 20.00);
		for (int i = 0; i < count; i++) {
			ArrayList<EmojiBean> list2 = null;
			if (i == count - 1) {
				list2 = new ArrayList<EmojiBean>(emojiList.subList(20 * i,
						20 * i + (emojiList.size() - (20 * i))));
			} else {
				list2 = new ArrayList<EmojiBean>(emojiList.subList(20 * i, 20 * (i + 1)));
			}
			EmojiBean emojiBean = new EmojiBean();
			emojiBean.setName("删除");
			emojiBean.setContent("[1120282删除1120282]");
			emojiBean.setId(R.drawable.selector_face_delete);
			list2.add(emojiBean);
			mlist.add(list2);
		}

	}

	/**
	 * 将表情展示到输入框
	 * 
	 * @author yeliangliang
	 * @date 2015-8-17 下午4:52:25
	 * @version V1.0
	 * @return void
	 */
	public void setFaceToEdt(ChatEditText edt, EmojiBean emojiBean) {
		SpannableString sp = getSpannableString(edt, emojiBean);
		Editable edit = edt.getEditableText();// 获取EditText的文字
		edit.insert(edt.getSelectionStart(), sp);// 光标所在位置插入
	}

	/**
	 * 获取Spannable
	 * 
	 * @return
	 * @author yeliangliang
	 * @date 2015-8-17 下午5:01:46
	 * @version V1.0
	 * @return SpannableString
	 */
	private SpannableString getSpannableString(ChatEditText edt, EmojiBean emojiBean) {
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), emojiBean.getId());
		bitmap = Bitmap.createScaledBitmap(bitmap, 45, 45, true);
		ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
		SpannableString spannable = new SpannableString(emojiBean.getContent());
		spannable.setSpan(imageSpan, 0, emojiBean.getContent().length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 删除表情
	 * 
	 * @author yeliangliang
	 * @date 2015-8-17 下午5:13:57
	 * @version V1.0
	 * @return void
	 */
	public void deleteFace(ChatEditText edt, EmojiBean emojiBean) {
		String content = edt.getText().toString();
		int curosr = edt.getSelectionStart();// 当前光标

		if (curosr > 0) {
			// 当前光标前8位字符
			String s = content.substring(curosr - 8, curosr);
			if (s.equals("1120282]")) {// 低级加密。。。自创
				// 将要删除的是一个表情
				// 从0开始到当前光标的字符串
				String s2 = content.substring(0, curosr);
				// 内容中最后一个"[1120282"
				int i = s2.lastIndexOf("[1120282");
				edt.getText().delete(i, curosr);
			} else {
				// 将要删除的不是一个表情
				edt.getText().delete(curosr, curosr - 1);
			}
		}
	}

	/**
	 * 匹配
	 * 
	 * @param content
	 * @author yeliangliang
	 * @date 2015-8-17 下午9:34:16
	 * @version V1.0
	 * @return void
	 */
	public SpannableString matchingString(String content,boolean isChat ) {
		// \[1120282[\u4e00-\u9fa5]{1,3}1120282]
		// 匹配字符中被[11202821120282]中间包裹的1-3个汉字的内容
		SpannableString spannableString = new SpannableString(content);
		String zhengze = "\\[1120282[\u4e00-\u9fa5]{1,3}1120282]";
		Pattern patten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		// 检测替换
		replaceFace(patten, spannableString, 0,isChat);
		return spannableString;
	}

	/**
	 * 替换表情
	 * 
	 * @author yeliangliang
	 * @date 2015-8-17 下午10:28:13
	 * @version V1.0
	 * @return void
	 */
	private void replaceFace(Pattern pattern, SpannableString spannableString, int start,boolean isChat) {
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String string = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			if (emojiMap.get(string) == null) {
				return;
			}
			int id = emojiMap.get(string);
			Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
			if (isChat) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 45, 45, true);
			}else {
				bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
			}
			ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
			int length = string.length();
			spannableString.setSpan(imageSpan, matcher.start(), matcher.start() + length,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
	}
}
