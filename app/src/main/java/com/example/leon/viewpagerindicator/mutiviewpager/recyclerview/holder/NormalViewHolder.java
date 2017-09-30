package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder;

import android.view.View;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.MultiViewAdapter;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.NormalModel;

/**
 * Created by leon on 2017/9/30.
 */

public class NormalViewHolder extends BaseViewHolder<NormalModel> {
    public NormalViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(NormalModel model, int position, MultiViewAdapter adapter) {
        final TextView textView = (TextView) getView(R.id.id_info);
        textView.setText(model.getText());
    }

    @Override
    public void changeView(NormalModel normalModel) {

    }
}
