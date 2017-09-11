package com.example.leon.viewpagerindicator;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.leon.viewpagerindicator.adapter.MyAdapter;
import com.example.leon.viewpagerindicator.subviews.MyIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements GoTopShow{

    RelativeLayout mainView;
    ViewPager viewPager;
    MyIndicator myIndicator;
    ImageView goTop;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mainView = (RelativeLayout) findViewById(R.id.main_view);
        myIndicator = (MyIndicator) findViewById(R.id.my_indicator);
        goTop = (ImageView) findViewById(R.id.go_top);
        List<String> titleList = new ArrayList<>();
        titleList.add("leon");
        titleList.add("lulu");
        titleList.add("jasson");
        TabFragment[] mFragments = new TabFragment[titleList.size()];

        for (int i = 0; i < titleList.size(); i++) {
            mFragments[i] = TabFragment.newInstance(titleList.get(i));
        }
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), titleList);
        myAdapter.setFragments(mFragments);
        viewPager.setAdapter(myAdapter);
        setViewpagerHeight(viewPager);
        myIndicator.init(viewPager, this);
    }

    private boolean isMeasured = false;

    private void setViewpagerHeight(final ViewPager viewPager) {
        mainView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isMeasured) {
                    int height = mainView.getHeight() - myIndicator.getHeight();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            height);
                    viewPager.setLayoutParams(params);
                    isMeasured = true;
                }
                return true;
            }
        });
    }

    @Override
    public void showTop() {
        goTop.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goTop.setVisibility(View.GONE);
            }
        },1000);
    }

    private void playShowAnimation() {
        mHideAnimation.cancel();
        if (goTop != null) {
            goTop.startAnimation(mShowAnimation);
        }
    }

    private void playHideAnimation() {
        mShowAnimation.cancel();
        if (goTop != null) {
            goTop.startAnimation(mHideAnimation);
        }
    }
}
