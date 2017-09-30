package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type;

import android.view.View;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.BaseViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.FootViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.NormalViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.FootModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.NormalModel;

/**
 * Created by leon on 2017/9/30.
 */

public class TypeFactoryForList implements TypeFactory {
    private static final int TYPE_RESOURCE_NORMAL = R.layout.item;
    private static final int TYPE_RESOURCE_FOOT = R.layout.foot_layout;

    @Override
    public int type(NormalModel normalModel) {
        return TYPE_RESOURCE_NORMAL;
    }

    @Override
    public int type(FootModel footModel) {
        return TYPE_RESOURCE_FOOT;
    }

    @Override
    public BaseViewHolder createViewHolder(int type, View itemView) {
        if (type == TYPE_RESOURCE_NORMAL) {
            return new NormalViewHolder(itemView);
        } else if (type == TYPE_RESOURCE_FOOT) {
            return new FootViewHolder(itemView);
        }
        return null;
    }
}
