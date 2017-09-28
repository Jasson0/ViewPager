package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leon on 2017/9/26.
 */

public interface OnItemClickListener<T>
{
    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
