package com.example.leon.viewpagerindicator;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by leon on 2017/9/7.
 */

public class MyAdapter extends PagerAdapter {

    private List<View> viewList;
    private List<String> titleList;

    public MyAdapter(List<View> listView, List<String> titleList) {
        this.viewList = listView;
        this.titleList = titleList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position), 0);//添加页卡
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
