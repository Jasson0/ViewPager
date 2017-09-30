package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model;

import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactory;

/**
 * Created by leon on 2017/9/30.
 */

public class NormalModel implements VisitableModel {
    public NormalModel(String text) {
        this.text = text;
    }

    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
