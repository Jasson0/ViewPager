package com.example.leon.viewpagerindicator.mutiviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.leon.viewpagerindicator.mutiviewpager.TabFragment;

import java.util.List;

/**
 * Created by leon on 2017/9/7.
 */

public class MyAdapter extends FragmentPagerAdapter {

    private List<String> titleList;

    public MyAdapter(FragmentManager fm, List<String> titleList) {
        super(fm);
        this.titleList = titleList;
    }

    private TabFragment[] fragmentList;

    public void setFragments(TabFragment[] fragmentList) {
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}