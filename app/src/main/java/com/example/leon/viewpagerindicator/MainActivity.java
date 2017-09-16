package com.example.leon.viewpagerindicator;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.leon.viewpagerindicator.mutiviewpager.subviews.IndicatorLayout;
import com.example.leon.viewpagerindicator.nestedscrolling.StickyNavLayout;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity{

    ImageView goTop;

    StickyNavLayout stickyNavLayout;
    RecyclerView recyclerView;
    IndicatorLayout indicatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicatorLayout = (IndicatorLayout) findViewById(R.id.my_indicator_layout);
        indicatorLayout.init(this);
//        setContentView(R.layout.activity_main_2);
    }

    private void initNestScrolling() {
        stickyNavLayout = (StickyNavLayout) findViewById(R.id.nest_scrolling);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> mDatas = new ArrayList<>();
        String mTitle = "Defaut Value";
        for (int i = 0; i < 50; i++) {
            mDatas.add(mTitle + " -> " + i);
        }
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(this, R.layout.item, mDatas) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.id_info, s);
            }
        };
        recyclerView.setAdapter(commonAdapter);
    }
//用来动态设置layoutparams
//    private boolean isMeasured = false;
//
//    private void setViewpagerHeight(final ViewPager viewPager) {
//        mainView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if (!isMeasured) {
//                    int height = mainView.getHeight() - myIndicator.getHeight();
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                            height);
//                    viewPager.setLayoutParams(params);
//                    isMeasured = true;
//                }
//                return true;
//            }
//        });
//    }
}
