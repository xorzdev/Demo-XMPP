package com.gavin.demo067_xmpp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gavin.demo067_xmpp.R;
import com.gavin.demo067_xmpp.adapter.FaceGridViewAdapter;
import com.gavin.demo067_xmpp.adapter.FaceViewPagerAdapter;
import com.gavin.demo067_xmpp.model.EmojiBean;
import com.gavin.demo067_xmpp.utils.FaceUtil;
import com.gavin.demo067_xmpp.utils.T;

import java.util.ArrayList;

/**
 * 自定义表情选择布局
 *
 * @author yeliangliang
 * @ClassName: FaceSelectRelativity
 * @date 2015-8-12 下午5:46:58
 */
public class FaceSelectRelativeLayout extends RelativeLayout implements OnItemClickListener {
    private Context mContext;
    private ViewPager viewPager;
    private LinearLayout ll_point;
    private ArrayList<ImageView> pointList;
    // ViewPager 内容集合
    private ArrayList<GridView> gridViewList;
    // 表情集合（已分页）
    private ArrayList<ArrayList<EmojiBean>> emojiList;
    // 表情操作类
    private FaceUtil faceUtil;
    // 表情分页-gridview的adapter集合
    private ArrayList<FaceGridViewAdapter> gridViewAdapterList;
    // 输入框
    private ChatEditText edt_chat;

    public FaceSelectRelativeLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public FaceSelectRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public FaceSelectRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        faceUtil = FaceUtil.getInstance(mContext);
        if (faceUtil.mlist.size() == 0) {
            T.s("表情初始化失败");
            return;
        }
        // 获取表情集合-已分页
        emojiList = faceUtil.mlist;
        // 控件 参数 初始化
        gridViewList = new ArrayList<GridView>();
        pointList = new ArrayList<ImageView>();
        gridViewAdapterList = new ArrayList<FaceGridViewAdapter>();
        gridViewList = new ArrayList<GridView>();
        viewPager = (ViewPager) findViewById(R.id.viewpager_face);
        edt_chat = (ChatEditText) findViewById(R.id.edt_input);
        for (ArrayList<EmojiBean> list : emojiList) {
            // 按照分页好的表情来向ViewPager中添加GridView
            GridView gridView = new GridView(mContext);
            gridView.setNumColumns(7);
            gridView.setColumnWidth(80);
            gridView.setOnItemClickListener(this);
            FaceGridViewAdapter mAdapter = new FaceGridViewAdapter(mContext, list);
            gridViewAdapterList.add(mAdapter);
            gridView.setAdapter(mAdapter);
            gridView.setVerticalSpacing(25);
            gridView.setHorizontalSpacing(10);
            gridView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            gridView.setPadding(5, 25, 5, 5);
            gridView.setGravity(Gravity.CENTER);
            gridView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            gridViewList.add(gridView);
            // 添加point标志

        }
        viewPager.setAdapter(new FaceViewPagerAdapter(gridViewList));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EmojiBean emojiBean = (EmojiBean) view.getTag(R.id.tag_emoji);
        if (emojiBean.getName().equals("删除")) {
            faceUtil.deleteFace(edt_chat, emojiBean);
        } else {
            faceUtil.setFaceToEdt(edt_chat, emojiBean);
        }
    }

}
