package com.example.leon.viewpagerindicator.mutiviewpager.subviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.MyAdapter;
import com.example.leon.viewpagerindicator.mutiviewpager.TabFragment;
import com.example.leon.viewpagerindicator.mutiviewpager.abandoncode.Indicator;
import com.example.leon.viewpagerindicator.mutiviewpager.utils.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/15.
 */

public class IndicatorLayout extends LinearLayout implements ScrollIndicator.OnPageChangeListener {
    private View rootView;
    private RelativeLayout mainView;
    private ViewPager viewPager;
    private Indicator myIndicator;
    private ScrollIndicator scrollIndicator;
    private Context context;

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.indicator_layout, this);
    }

    private TabFragment[] mFragments;

    public void init(FragmentActivity activity) {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        scrollIndicator = (ScrollIndicator) findViewById(R.id.my_indicator);
        List<String> titleList = new ArrayList<>();
        titleList.add("推荐");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        titleList.add("游记");
        ArrayList<String> mDatas = new ArrayList<>();
        for (int j = 0; j < 20; j++) {
            if (j < 10) {
                mDatas.add("2");
            } else {
                mDatas.add("222222" + " -> " + j);
            }
        }
        mFragments = new TabFragment[titleList.size()];
        mFragments[0] = TabFragment.newInstance(titleList.get(0), mDatas);
        for (int i = 1; i < titleList.size(); i++) {
            mFragments[i] = TabFragment.newInstance(titleList.get(i));
        }
        MyAdapter myAdapter = new MyAdapter(activity.getSupportFragmentManager(), titleList);
        myAdapter.setFragments(mFragments);
        viewPager.setAdapter(myAdapter);
//        viewPager.setPageTransformer(true, new DepthPageTransformer());
        scrollIndicator.setViewPager(viewPager);
        scrollIndicator.setFragments(mFragments);
        scrollIndicator.setTabItemTitles(titleList, ToolUtil.getPxFromDip(getContext(), 16), ToolUtil.getPxFromDip(getContext(), 32), ScrollIndicator.TabLayoutType.MUTI);
        scrollIndicator.setOnChangeListener(this);
    }
    int position = 0;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        scrollIndicator.highlightText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mFragments[position].requestData();
        }
    }
}
