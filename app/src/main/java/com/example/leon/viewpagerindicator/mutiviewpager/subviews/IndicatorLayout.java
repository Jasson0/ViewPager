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
import com.example.leon.viewpagerindicator.mutiviewpager.anim.DepthPageTransformer;
import com.example.leon.viewpagerindicator.mutiviewpager.utils.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/15.
 */

public class IndicatorLayout extends LinearLayout {
    private View rootView;
    RelativeLayout mainView;
    ViewPager viewPager;
    Indicator myIndicator;
    ScrollIndicator scrollIndicator;
    private Context context;
    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.indicator_layout, this);
    }

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
            mDatas.add("222" + " -> " + 1);
        }
        TabFragment[] mFragments = new TabFragment[titleList.size()];
        for (int i = 0; i < titleList.size(); i++) {
            ArrayList<String> leon = new ArrayList<>();
            for (int j = 0; j < mDatas.size(); j++) {
                leon.add(mDatas.get(i));
            }
            mFragments[i] = TabFragment.newInstance(titleList.get(i),leon);
        }
        MyAdapter myAdapter = new MyAdapter(activity.getSupportFragmentManager(), titleList);
        myAdapter.setFragments(mFragments);
        viewPager.setAdapter(myAdapter);
//        viewPager.setPageTransformer(true, new DepthPageTransformer());
        scrollIndicator.setViewPager(viewPager);
        scrollIndicator.setFragments(mFragments);
        scrollIndicator.setTabItemTitles(titleList, ToolUtil.getPxFromDip(getContext(),16), ToolUtil.getPxFromDip(getContext(),32), ScrollIndicator.TabLayoutType.MUTI);
    }
}
