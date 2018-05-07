package com.example.leon.viewpagerindicator;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.mutiviewpager.abandoncode.Indicator;
import com.example.leon.viewpagerindicator.mutiviewpager.subviews.IndicatorLayout;
import com.example.leon.viewpagerindicator.mutiviewpager.subviews.MutiLayout;
import com.example.leon.viewpagerindicator.nestedscrolling.StickyNavLayout;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    ImageView goTop;

    StickyNavLayout stickyNavLayout;
    RecyclerView recyclerView;
    IndicatorLayout indicatorLayout;
    MutiLayout mutiLayout;


    private Indicator mIndicator;
    private ViewPager mContainer;
    private List<String> titleList;
    private ArrayList<TextView> mViews = new ArrayList<TextView>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicatorLayout = (IndicatorLayout) findViewById(R.id.my_indicator_layout);
        mutiLayout = (MutiLayout) findViewById(R.id.my_view);
        indicatorLayout.init(this);
//        setContentView(R.layout.activity_main_2);
//        setContentView(R.layout.activity_main_4);
//
//        mIndicator = (Indicator) findViewById(R.id.indicator);
//        titleList = new ArrayList<>();
//        titleList.add("leon");
//        titleList.add("lulu");
//        titleList.add("jasson");
//        titleList.add("jasson");
//        titleList.add("jasson");
//        titleList.add("jasson");
//        titleList.add("jasson");
//        mIndicator.setTabItemTitles(titleList);
//        mContainer = (ViewPager) findViewById(R.id.container);
//
//        initViews();
//        mContainer.setAdapter(new PagerAdapter() {
//            @Override
//            public boolean isViewFromObject(View arg0, Object arg1) {
//                return arg0 == arg1;
//            }
//
//            @Override
//            public int getCount() {
//                return mViews.size();
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                View view = mViews.get(position);
//                container.addView(view);
//                return view;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position,
//                                    Object object) {
//                container.removeView(mViews.get(position));
//            }
//        });
//        mIndicator.setViewPager(mContainer);
    }

    private void initViews() {
        for (int i = 0; i < titleList.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText("hello android" + i);
            mViews.add(tv);
        }
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
