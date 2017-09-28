package com.example.leon.viewpagerindicator.mutiviewpager.utils;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by leon on 2017/9/28.
 */

public class TabUtil {
    public static int getTabWidth(Context context, int mVisibleCount) {
        if (mVisibleCount < 4) {
            return ToolUtil.getScreenWidth(context)/(mVisibleCount+1);
        } else {
            return ToolUtil.getScreenWidth(context)/mVisibleCount;
        }
    }

}
