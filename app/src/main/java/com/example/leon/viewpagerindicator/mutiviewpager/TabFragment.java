package com.example.leon.viewpagerindicator.mutiviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.MultiViewAdapter;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.OnItemClickListener;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.BaseViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.NormalViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.FootModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.NormalModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.VisitableModel;

public class TabFragment extends Fragment implements OnItemClickListener {
    public static final String TITLE = "title";
    public static final String DATA = "data";
    private String mTitle = "Defaut Value";
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNTER = 24;
    // 已经获取到多少条数据了
    private int mCurrentCounter = 0;
    private RecyclerView mRecyclerView;
    private boolean isLoading;
    // private TextView mTextView;
    private List<VisitableModel> mDatas = new ArrayList<>();
    private Handler handler = new Handler();
    MultiViewAdapter adapter;
    private FootModel.State mState;
    private ImageView goTop;
    private long leon;
    private Animation showAnimation;
    private Animation hiddenAnimation;

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    private List<String> dataList = new ArrayList<>();

    public void requestData() {
        if (dataList == null) {
            dataList = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                if (j < 10) {
                    dataList.add("2");
                } else {
                    dataList.add("222222" + " -> " + j);
                }
            }
            mDatas.addAll(initData(dataList));
            mDatas.add(new FootModel(FootModel.State.Loading));
            displayLayout.inflate();
            initDisplayView();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //fragment可见时调用
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            dataList = (List<String>) getArguments().getSerializable(DATA);
            if (dataList != null) {
                mDatas.addAll(initData(dataList));
                mDatas.add(new FootModel(FootModel.State.Loading));
            }
        }
        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_up);
        hiddenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_down);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("lux","onResume");
    }

    private List<VisitableModel> initData(List<String> datas) {
        List<VisitableModel> dataList = new ArrayList<>();
        for (String data : datas) {
            VisitableModel dataModel;
            dataModel = new NormalModel(data);
            dataList.add(dataModel);
        }
        return dataList;
    }

    private int temp = 0;

    private long lastTime;

    private LinearLayoutManager linearLayoutManager;

    private ViewStub loadingLayout;
    private ViewStub displayLayout;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        loadingLayout = (ViewStub) view.findViewById(R.id.loading_layout);
        displayLayout = (ViewStub) view.findViewById(R.id.display_layout);
        Log.e("leon", "dataList = " + dataList);
        if (dataList != null) {
            displayLayout.inflate();
            initDisplayView();
        } else {
            loadingLayout.inflate();
        }
        return view;
    }

    private void initDisplayView() {
        View display = view.findViewById(R.id.display_layout_after);
        goTop = (ImageView) display.findViewById(R.id.go_top);
        goTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        adapter = new MultiViewAdapter(getContext(), mDatas);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) display.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
                final int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                        Log.d("test", "loading executed");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCurrentCounter < TOTAL_COUNTER) {
                                    getData();
                                    Log.d("test", "load more completed");
                                } else {
                                    FootModel model = (FootModel) mDatas.get(mDatas.size() - 1);
                                    model.setFootState(FootModel.State.NoMore);
                                    setState(model);
//                                    recyclerView.smoothScrollBy(0, -200);
                                }
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
                final int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                View childView = recyclerView.getChildAt(lastVisibleItemPosition - firstVisibleItemPosition);
                if (childView != null) {
                    if (recyclerView.getChildViewHolder(childView) instanceof NormalViewHolder) {
                        NormalViewHolder holder = (NormalViewHolder) recyclerView.getChildViewHolder(childView);
                        holder.t();
                    }
                }
                if (lastVisibleItemPosition > 20) {
                    if (goTop.getVisibility() == View.GONE) {
                        goTop.startAnimation(showAnimation);
                    }
                    goTop.setVisibility(View.VISIBLE);
                } else {
                    if (goTop.getVisibility() == View.VISIBLE) {
                        goTop.startAnimation(hiddenAnimation);
                    }
                    goTop.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void setState(final FootModel footModel) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null && adapter.viewHolder != null) {
                    //找到最后一个item，即foot的viewHolder。
                    int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    View view = mRecyclerView.getChildAt(mDatas.size() - 1 - firstItemPosition);
                    BaseViewHolder viewHolder = (BaseViewHolder) mRecyclerView.getChildViewHolder(view);
                    viewHolder.changeView(footModel);
                }
            }
        });
    }

    /**
     * 获取测试数据
     */
    private void getData() {
        List<String> newData = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newData.add("上拉加载产生" + i);
        }
        mDatas.addAll(mDatas.size() - 2, initData(newData));
        mCurrentCounter += 6;
        adapter.setDataList(mDatas);
        adapter.notifyDataSetChanged();
    }

    public static TabFragment newInstance(String title, ArrayList<String> dataList) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putSerializable(DATA, dataList);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    public static TabFragment newInstance(String title) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Toast.makeText(getContext(), "11111", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }
}
