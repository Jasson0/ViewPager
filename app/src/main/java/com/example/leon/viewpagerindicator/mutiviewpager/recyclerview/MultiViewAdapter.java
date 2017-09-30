package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.BaseViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.VisitableModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactory;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactoryForList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/14.
 */

public class MultiViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @Override
    public int getItemViewType(int position) {
        return models.get(position).type(typeFactory);
    }

    private List<VisitableModel> models;
    private TypeFactory typeFactory;
    private Context context;
    public BaseViewHolder viewHolder;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(viewType, parent,
                false);
        viewHolder = typeFactory.createViewHolder(viewType, itemView);
        return typeFactory.createViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setUpView(models.get(position), position, this);
    }

    public List<VisitableModel> getDataList() {
        if (models == null)
            models = new ArrayList<>();
        return models;
    }

    public void setDataList(List<VisitableModel> dataList) {
        this.models = dataList;
    }

    public MultiViewAdapter(Context context, List<VisitableModel> dataList) {
        this.models = dataList;
        this.context = context;
        this.typeFactory = new TypeFactoryForList();
    }


    @Override
    public int getItemCount() {
        return models.size();
    }
}
