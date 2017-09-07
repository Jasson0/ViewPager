package com.example.leon.viewpagerindicator;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * Created by leon on 2017/9/6.
 */

public class MyIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private View mTabLayout;
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
    private int tabCount;
    private ImageView indicator;
    private LinearLayout tabContent;
    private int currentIndicatorLeft;
    private int indicatorWidth;
    private int scrollX;
    private int windowWitdh;
    private boolean isScrolling;
    private boolean isLeaveScrolling;


    public MyIndicator(Context context) {
        this(context, null);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTabLayout = LayoutInflater.from(context).inflate(R.layout.tab_layout, this);
        tabContent = mTabLayout.findViewById(R.id.tab_content);
        indicator = mTabLayout.findViewById(R.id.img_indicator);
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
            TextView textView = tabView.findViewById(R.id.tab_name);
            textView.setText(title);
            tabContent.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
            tabView.setOnClickListener(mTabClickListener);
        }
    }

    private boolean isClick = false;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            isClick = true;
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
        scrollX = currentIndicatorLeft
                - (getWidth() - indicatorWidth) / tabCount;
        if (isClick) {
            animateToTab(position);
        }
        // this.post(runnable);
    }

    private void animateToTab(final int position) {
        TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft,
                indicatorWidth * position, 0f, 0f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setFillAfter(true);
        // 执行位移动画
        indicator.startAnimation(animation);
        currentIndicatorLeft = indicatorWidth * position;
        isClick = false;
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            smoothScrollTo(scrollX, 0);
        }
    };

    private void changeTabStyle(TabView tabView) {
        for (int i = 0; i < tabContent.getChildCount(); i++) {
            TabView tv = (TabView) tabContent.getChildAt(i);
            TextView tabText = tv.findViewById(R.id.tab_name);
            TextPaint tp = tabText.getPaint();
            tp.setFakeBoldText(false);
        }
        TextView tabText = tabView.findViewById(R.id.tab_name);
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
        if (positionOffsetPixels != 0) {
            indicator.animate()
                    .translationX(positionOffsetPixels / tabCount)
                    .setDuration(0)
                    .start();
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScrolling = true;
                isLeaveScrolling = false;
                break;
            case 2:
                isScrolling = false;
                isLeaveScrolling = true;
                break;
            default:
                isScrolling = false;
                isLeaveScrolling = false;
                break;
        }
    }

}
