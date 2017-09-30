package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type;

import android.view.View;

import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.BaseViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.FootModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.NormalModel;

/**
 * Created by leon on 2017/9/30.
 */

public interface TypeFactory {
    int type(NormalModel normalModel);
    int type(FootModel footModel);
    BaseViewHolder createViewHolder(int type, View itemView);
}
