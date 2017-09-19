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
        myIndicator = (Indicator) findViewById(R.id.my_indicator);
        List<String> titleList = new ArrayList<>();
        titleList.add("leon");
        titleList.add("lulu");
        titleList.add("jasson");
        titleList.add("jasson");
        titleList.add("jasson");
        titleList.add("jasson");
        titleList.add("jasson");
        TabFragment[] mFragments = new TabFragment[titleList.size()];

        for (int i = 0; i < titleList.size(); i++) {
            mFragments[i] = TabFragment.newInstance(titleList.get(i));
        }
        MyAdapter myAdapter = new MyAdapter(activity.getSupportFragmentManager(), titleList);
        myAdapter.setFragments(mFragments);
        viewPager.setAdapter(myAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        myIndicator.setTabItemTitles(titleList,4);
        myIndicator.setViewPager(viewPager);
    }
}
