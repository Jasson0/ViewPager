package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.leon.viewpagerindicator.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/14.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_FOOTER = 1;
    private int footer_state;

    @Override
    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
        return super.getItemViewType(position);
    }

    private List<String> dataList;
    private Context context;

    public List<String> getDataList() {
        if (dataList == null)
            dataList = new ArrayList<>();
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public RecyclerViewAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public void add(List<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            int oldSize = getDataList().size();
            getDataList().addAll(newList);
            notifyItemRangeChanged(oldSize - 1, newList.size() + 1);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
            return MyViewHolder.createViewHolder(context, parent, R.layout.item);
//        }
//        return MyViewHolder.createViewHolder(context, parent, R.layout.foot);
    }

    public void changeState(int state) {
        this.footer_state = state;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setText(R.id.id_info, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}