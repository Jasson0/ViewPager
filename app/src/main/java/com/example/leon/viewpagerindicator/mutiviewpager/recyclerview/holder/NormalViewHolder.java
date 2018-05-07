package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.MultiViewAdapter;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.NormalModel;

/**
 * Created by leon on 2017/9/30.
 */

public class NormalViewHolder extends BaseViewHolder<NormalModel> implements MultiViewAdapter.Test{
    public NormalViewHolder(View itemView, MultiViewAdapter adapter) {
        super(itemView, adapter);
        adapter.setTest(this);
    }

    @Override
    public void setUpView(NormalModel model, int position, MultiViewAdapter adapter) {
        final TextView textView = (TextView) getView(R.id.id_info);
        textView.setText(model.getText());
    }

    @Override
    public void changeView(NormalModel normalModel) {

    }
    private boolean isFirst = true;

    @Override
    public void t() {
        Rect rect = new Rect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        if (itemView.getLocalVisibleRect(rect) && isFirst) {
            Log.e("leonleon","ttttt");
            isFirst = false;
        }
    }
}
