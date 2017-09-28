package com.example.leon.viewpagerindicator.mutiviewpager.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by leon on 2017/9/28.
 */

public class ToolUtil {
    /**
     * 用来获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    //dp转像素
    public static int getPxFromDip(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        return (int) ((dip * SCALE) + 0.5f);
    }

    // 像素转dip
    public static int PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float dips = Pixels / SCALE;
        return (int) dips;
    }
}
