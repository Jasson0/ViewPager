package com.example.leon.viewpagerindicator.mutiviewpager;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.leon.viewpagerindicator.MainActivity;
import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.EndLessOnScroll;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.FooterHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.RecyclerViewAdapter;

public class TabFragment extends Fragment {
    public static final String TITLE = "title";
    private String mTitle = "Defaut Value";
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNTER = 24;
    // 已经获取到多少条数据了
    private int mCurrentCounter = 0;
    private RecyclerView mRecyclerView;
    private boolean isLoading;
    // private TextView mTextView;
    private List<String> mDatas = new ArrayList<String>();
    private Handler handler = new Handler();
    RecyclerViewAdapter adapter;
    private FooterHolder.State mState;
    private ImageView goTop;
    private long leon;
    private Animation showAnimation;
    private Animation hiddenAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_up);
        hiddenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_down);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        goTop = (ImageView) view.findViewById(R.id.go_top);
        goTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        for (int i = 0; i < 20; i++) {
            mDatas.add(mTitle + " -> " + i);
        }
        adapter = new RecyclerViewAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
                setState(FooterHolder.State.Loading);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition > 20) {
                    if (goTop.getVisibility() == View.GONE) {
                        goTop.startAnimation(showAnimation);
                        leon = System.currentTimeMillis();
                    }
                    goTop.setVisibility(View.VISIBLE);
                } else {
                    if (goTop.getVisibility() == View.VISIBLE) {
                        goTop.startAnimation(hiddenAnimation);
                    }
                    goTop.setVisibility(View.GONE);
                }
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentCounter < TOTAL_COUNTER) {
                                getData();
                                Log.d("test", "load more completed");
                                setState(FooterHolder.State.Normal);
                            } else {
                                setState(FooterHolder.State.NoMore);
                            }
                        }
                    }, 1000);
                }
            }
        });
        return view;
    }

    protected void setState(FooterHolder.State mState) {
        this.mState = mState;
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeAdaperState();
            }
        });
    }

    //改变底部bottom的样式
    protected void changeAdaperState() {
        if (adapter != null && adapter.footerHolder != null) {
            adapter.footerHolder.changeState(mState);
        }
    }

    /**
     * 获取测试数据
     */
    private void getData() {
        for (int i = 0; i < 6; i++) {
            mDatas.add("上拉加载产生" + i);
        }
        mCurrentCounter += 6;
        adapter.notifyDataSetChanged();
    }

    public static TabFragment newInstance(String title) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

}
