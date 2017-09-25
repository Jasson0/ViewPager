package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leon.viewpagerindicator.R;

/**
 * Created by leon on 2017/9/25.
 */

public class FooterHolder extends MyViewHolder {
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;

    public enum State {
        Normal,
        NoMore/**加载到最底了*/
        , Loading/**加载中..*/
        , NetWorkError/**网络异常*/
    }

    public FooterHolder(Context context, View itemView) {
        super(context, itemView);
    }

    public static FooterHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        FooterHolder footerHolder = new FooterHolder(context, itemView);
        return footerHolder;
    }

    public void initFootView(int loadingId, int endId, int networkErrorId) {
        mLoadingView = getView(loadingId);
        mNetworkErrorView = getView(networkErrorId);
        mTheEndView = getView(endId);
    }

    public void changeState(State state) {
        setAllGone();
        switch (state) {
            case Loading:
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case NoMore:
                mTheEndView.setVisibility(View.VISIBLE);
                break;
            case NetWorkError:
                mNetworkErrorView.setVisibility(View.VISIBLE);
                break;
        }
    }

    void setAllGone() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mTheEndView != null) {
            mTheEndView.setVisibility(View.GONE);
        }
        if (mNetworkErrorView != null) {
            mNetworkErrorView.setVisibility(View.GONE);
        }
    }
}