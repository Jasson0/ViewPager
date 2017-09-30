package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder;

import android.view.View;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.MultiViewAdapter;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.FootModel;

/**
 * Created by leon on 2017/9/30.
 */

public class FootViewHolder extends BaseViewHolder<FootModel> {
    private View mLoadingView;
    private View mTheEndView;

    public FootViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(FootModel model, int position, MultiViewAdapter adapter) {
        mLoadingView = getView(R.id.loading_view);
        mLoadingView.setVisibility(View.VISIBLE);
        mTheEndView = getView(R.id.end_views);
    }

    @Override
    public void changeView(FootModel model) {
        changeState(model.getFootState());
    }

    public void changeState(FootModel.State state) {
        setAllGone();
        switch (state) {
            case Loading:
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case NoMore:
                mTheEndView.setVisibility(View.VISIBLE);
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
    }
}
