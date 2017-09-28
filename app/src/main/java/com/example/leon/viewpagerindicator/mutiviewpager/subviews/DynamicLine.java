package com.example.leon.viewpagerindicator.mutiviewpager.subviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.leon.viewpagerindicator.mutiviewpager.utils.ToolUtil;

/**
 * Created by leon on 2017/9/28.
 */

public class DynamicLine extends View {
    private float startX, stopX;//的起始X,终止X坐标。
    private int xRadius, yRadius;//圆角的坐标
    private int lineHeight;//dynamicLine的宽度
    private Paint paint;
    private RectF rectF = new RectF(startX, 0, stopX, 0);//RectF指的是float精度的矩形


    public DynamicLine(Context context) {
        this(context, null);
    }

    public DynamicLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(int color, int lineHeight, int xRadius, int yRadius) {
        paint = new Paint();
        this.lineHeight = ToolUtil.getPxFromDip(getContext(),lineHeight);
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.FILL);//填充
        paint.setColor(color);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//自定义DynamicLine的高度
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(20, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(startX, 0, stopX, lineHeight);
        canvas.drawRoundRect(rectF, xRadius, yRadius, paint);//圆角矩形的圆角的曲率
    }


    /**
     * 根据起始、终止坐标更新黄色圆角，进行重新绘制
     *
     * @param startX
     * @param stopX
     */
    public void updateView(float startX, float stopX) {
        this.startX = startX;
        this.stopX = stopX;
        invalidate();
    }
}
