package com.example.leon.viewpagerindicator.mutiviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.EndLessOnScroll;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.RecyclerViewAdapter;

public class TabFragment extends Fragment {
    public static final String TITLE = "title";
    private String mTitle = "Defaut Value";
    private RecyclerView mRecyclerView;
    // private TextView mTextView;
    private List<String> mDatas = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 20; i++) {
            mDatas.add(mTitle + " -> " + i);
        }
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mDatas);
        mRecyclerView.addOnScrollListener(new EndLessOnScroll(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                for (int i = 0; i < 20; i++) {
                    List<String> newData = new ArrayList<String>();
                    mDatas.add("嘿，我是“上拉加载”生出来的" + i);
                    newData.add("嘿，我是“上拉加载”生出来的" + i);
//                    adapter.add(newData);
                    adapter.setDataList(mDatas);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        return view;

    }

    public static TabFragment newInstance(String title) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

}
