package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model;

import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactory;

/**
 * Created by leon on 2017/9/30.
 */

public class FootModel implements VisitableModel {
    private State footState;

    public FootModel(State footState) {
        this.footState = footState;
    }

    public enum State {
        Loading,
        NoMore
    }

    public State getFootState() {
        return footState;
    }

    public void setFootState(State footState) {
        this.footState = footState;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
