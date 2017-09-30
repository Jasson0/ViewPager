package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.MultiViewAdapter;

/**
 * Created by leon on 2017/9/30.
 * 使用方式通过以下四个步骤
 * 1.增加一个viewHolder extends GSBaseViewHolder
 * 2.增加对应的model implements Visitable（可见的）
 * 3.在TypeFactory中增加对应的type
 * 4.在TypeFactoryList中增加对应Layout的type类型返回
 *
 * 核心思想：
 * getItemViewType返回的是布局文件R.layout.***的id，TypeFactory中直接通过传入的getItemViewType来
 * 创建相对应的viewHolder。使用instanceof的地方一般都可以使用多态和继承来进行扩展，instanceof是个很不优美的
 * 方法，尽量少用
 * 感谢http://www.jianshu.com/p/1297d2e4d27a 分享的思路
 */


public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        this.mItemView = itemView;
    }

    public View getView(int resID) {
        View view = views.get(resID);

        if (view == null) {
            view = mItemView.findViewById(resID);
            views.put(resID, view);
        }

        return view;
    }

    public abstract void setUpView(T model, int position, MultiViewAdapter adapter);
    public abstract void changeView(T model);
}
