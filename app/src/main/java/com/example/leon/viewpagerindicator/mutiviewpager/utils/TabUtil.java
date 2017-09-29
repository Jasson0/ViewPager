package com.example.leon.viewpagerindicator.mutiviewpager.utils;

import android.content.Context;
import android.graphics.Color;
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
    public static int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

}
