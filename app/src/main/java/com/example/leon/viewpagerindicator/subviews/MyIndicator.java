package com.example.leon.viewpagerindicator.subviews;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * Created by leon on 2017/9/6.
 */

public class MyIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private View mTabLayout;
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
    private int tabCount;
    private ImageView indicator;
    private LinearLayout tabContent;
    private int indicatorWidth;
    private int windowWitdh;


    public MyIndicator(Context context) {
        this(context, null);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTabLayout = LayoutInflater.from(context).inflate(R.layout.tab_layout, this);
        tabContent = (LinearLayout) mTabLayout.findViewById(R.id.tab_content);
        indicator = (ImageView) mTabLayout.findViewById(R.id.img_indicator);
    }

    public void init(ViewPager viewPager, Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWitdh = dm.widthPixels;
        //防止重复定义
        if (mViewPager == viewPager) {
            return;
        }
        pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        indicatorWidth = windowWitdh / pagerAdapter.getCount();
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        params1.width = indicatorWidth;
        indicator.setLayoutParams(params1);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        tabContent.removeAllViews();
        tabCount = pagerAdapter.getCount();
        for (int i = 0; i < tabCount; i++) {
            String title = (String) pagerAdapter.getPageTitle(i);
            TabView tabView = new TabView(getContext(), i);
            TextView textView = (TextView) tabView.findViewById(R.id.tab_name);
            textView.setText(title);
            tabContent.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
            tabView.setOnClickListener(mTabClickListener);
        }
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TabView tabView;
            if (!(view instanceof TabView)) {
                return;
            }
            tabView = (TabView) view;
            changeTabStyle(tabView);
            int selectIndex = tabView.index;
            mViewPager.setCurrentItem(selectIndex, true);
        }
    };

    private void setCurrentTab(int position) {
        for (int i = 0; i < tabCount; i++) {
            final View child = tabContent.getChildAt(i);
            final boolean isSelected = (i == position);
            child.setSelected(isSelected);
            if (isSelected) {
                changeTabStyle((TabView) child);
            }
        }
        movePositionX(position, 0);
    }

    private void changeTabStyle(TabView tabView) {
        for (int i = 0; i < tabContent.getChildCount(); i++) {
            TabView tv = (TabView) tabContent.getChildAt(i);
            TextView tabText = (TextView) tv.findViewById(R.id.tab_name);
            TextPaint tp = tabText.getPaint();
            tp.setFakeBoldText(false);
        }
        TextView tabText = (TextView) tabView.findViewById(R.id.tab_name);
        TextPaint tp = tabText.getPaint();
        tp.setFakeBoldText(true);
    }

    private class TabView extends LinearLayout {
        private int index;

        public TabView(Context context, int index) {
            super(context);
            this.index = index;
            View view = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null);
            addView(view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            movePositionX(position, positionOffsetPixels/tabCount);
    }

    private void movePositionX(int toPosition, float positionOffsetPixels) {
        float curTranslationX = indicator.getTranslationX();
        float toPositionX = indicatorWidth * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "translationX", curTranslationX, toPositionX);
        animator.setDuration(0);
        animator.start();
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
