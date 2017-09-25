package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by leon on 2017/9/14.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private TextView textView;
    private View mConvertView;
    private Context mContext;

    public MyViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static MyViewHolder createViewHolder(Context context, View itemView) {
        MyViewHolder holder = new MyViewHolder(context, itemView);
        return holder;
    }

    public static MyViewHolder createViewHolder(Context context,
                                                ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        MyViewHolder holder = new MyViewHolder(context, itemView);
        return holder;
    }

    protected  <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /****以下为辅助方法，可以根据需要自行添加，不要把view相关的代码卸载adapter中*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public MyViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
}
