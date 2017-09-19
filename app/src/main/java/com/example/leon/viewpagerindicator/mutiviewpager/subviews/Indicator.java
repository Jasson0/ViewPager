package com.example.leon.viewpagerindicator.mutiviewpager.subviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/18.
 */

public class Indicator extends LinearLayout {

    private Paint mPaint; // 画指示符的paint
    private int mTop; // 指示符的top
    private int mLeft; // 指示符的left
    private int mRight; // 指示符的right
    private int mWidth; // 指示符的width
    private int mHeight = 5; // 指示符的高度，固定了
    private int mColor; // 指示符的颜色
    private int mChildCount; // 子item的个数，用于计算指示符的宽度
    private int mVisibleCount;//课件的tab个数
    public List<String> titles;//tab的内容

    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;
    private float lastPosition = -1;
    private boolean isLeft = false;
    private boolean isScrolling;//是否滑动中

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.TRANSPARENT);  // 必须设置背景，否则onDraw不执行

        // 获取自定义属性 指示符的颜色
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
        mColor = ta.getColor(R.styleable.Indicator_color, 0X0000FF);
        mVisibleCount = ta.getInteger(R.styleable.Indicator_visiable_count, 4);
        ta.recycle();

        // 初始化paint
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public void setOnChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isScrolling) {
                    if (positionOffset != 0) {
                        if (lastPosition > positionOffset) {
                            //左滑
                            isLeft = true;
                        } else {
                            //右滑
                            isLeft = false;
                        }
                    }
                }
                lastPosition = positionOffset;
                scroll(position, positionOffset);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }

            }
        });
        viewPager.setCurrentItem(0);
    }

    //xml加载完成之后回调
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTop = getMeasuredHeight(); // 测量的高度即指示符的顶部位置
        int width = getMeasuredWidth(); // 获取测量的总宽度
        int height = mTop + mHeight; // 重新定义一下测量的高度
        mWidth = width / mVisibleCount; // 指示符的宽度为总宽度/item的个数
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        calculateUnderlineWidth();
    }

    public void scroll(int position, float offset) {
        int extra = 0;
        if (position >= 0 && position < getChildCount() - 1) {
            extra = underLineLeftList.get(position + 1) - underLineLeftList.get(position);
            mLeft = (int) ((position + offset) * mWidth + underLineLeftList.get(position) + extra * offset);
            mRight = (int) ((position + 1 + offset) * mWidth - underLineLeftList.get(position) - extra * offset);
        }
        //容器移动,当tab处于移动至最后一个时
        if (position >= mVisibleCount - 2 && offset > 0 && mChildCount > mVisibleCount && position < mChildCount - 2) {
            if (mVisibleCount != 1) {
                this.scrollTo((int) ((position - (mVisibleCount - 2)) * mWidth + mWidth * offset), 0);
            } else {
                this.scrollTo((int) (position * mWidth + mWidth * offset), 0);
            }
        }
        invalidate();
    }

    public void setTabItemTitles(List<String> titles, int mVisibleCount) {
        this.mVisibleCount = mVisibleCount;
        if (titles != null) {
            this.removeAllViews();
            this.titles = titles;
        }
        mChildCount = titles.size();
        for (int i = 0; i < titles.size(); i++) {
            addView(generateTextView(titles.get(i)));
        }
        setItemClick();//代码添加时增加view点击
    }

    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = calculateViewWidth(mVisibleCount, 0);
        RelativeLayout tvLinearLayout = new RelativeLayout(getContext());
        tvLinearLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams paramtv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramtv.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv.setText(title);
        tv.setId(R.id.tab_name);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setLayoutParams(paramtv);
        tvLinearLayout.addView(tv);
        return tvLinearLayout;
    }

    private int calculateViewWidth(int visibleCount, int width) {
        if (visibleCount <= 3) {
            return getScreenWidth() / (visibleCount + 1);
        } else if (visibleCount == 4) {
            return getScreenWidth() / visibleCount;
        } else {
            return width;
        }
    }

    private List<Integer> underLineList;
    private List<Integer> underLineLeftList;

    private void calculateUnderlineWidth() {
        underLineList = new ArrayList<>();
        int underLineMarginLeft = 0;
        underLineLeftList = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            RelativeLayout rl = (RelativeLayout) getChildAt(i);
            int textViewWidth = rl.findViewById(R.id.tab_name).getWidth();
            underLineList.add(textViewWidth + 100);
            underLineMarginLeft = (rl.getWidth() - (textViewWidth + 100)) / 2;
            underLineLeftList.add(underLineMarginLeft);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 圈出一个矩形
        Rect rect = new Rect(mLeft, mTop, mRight, mTop + mHeight);
        canvas.drawRect(rect, mPaint); // 绘制该矩形
        super.onDraw(canvas);
    }

    private void setItemClick() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }
    }
}