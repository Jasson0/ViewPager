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

import static android.graphics.Color.parseColor;

/**
 * Created by leon on 2017/9/18.
 */

public class Indicator extends LinearLayout {

    private Paint mPaint; // 画指示符的paint
    private int mTop; // 指示符的top
    private int mLeft; // 指示符的left
    private int mRight; // 指示符的right
    private int mHeight = 3; // 指示符的高度，固定了
    private int mColor; // 指示符的颜色
    private int mChildCount; // 子item的个数，用于计算指示符的宽度
    private int mVisibleCount;//课件的tab个数
    public List<String> titles;//tab的内容List
    private List<Integer> underLineList;//各个下划线长度的List
    private List<Integer> underLineLeftList;//各个下划线离左边的长度
    private int tabViewWidth = 0;//各个tab的长度

    private ViewPager viewPager;
    /**
     * 当自定义控件本身将接口使用时，需要提供给用户同样的回调
     */
    private OnPageChangeListener onPageChangeListener;

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#FFFFFF"));  // 必须设置背景，否则onDraw不执行
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

    //xml加载完成之后回调，目前由于是动态下发的，不支持用户在xml中定义，如若需要可模仿
    //setTabItemTitles添加
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTop = getMeasuredHeight(); // 测量的高度即指示符的顶部位置
        int width = getMeasuredWidth(); // 获取测量的总宽度
        int height = mTop + mHeight; // 重新定义一下测量的高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //在layout中去执行下划线宽度的计算，因为此时已经measure过，能够获得所有子view的尺寸，并且onLayout的调用频率更低，避免多次计算
        calculateUnderlineWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 圈出一个矩形
        Rect rect = new Rect(mLeft, mTop, mRight, mTop + mHeight);
        canvas.drawRect(rect, mPaint); // 绘制该矩形
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
                highlightText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }

            }
        });
        viewPager.setCurrentItem(0);
    }

    //必须在setViewPager之后调用
    public void setTabItemTitles(List<String> titles) {
        if (titles.size() < mVisibleCount) {
            mVisibleCount = titles.size();
        }
        if (titles != null) {
            this.removeAllViews();
            this.titles = titles;
        }
        tabViewWidth = calculateViewWidth(mVisibleCount, 330);
        mChildCount = titles.size();
        for (int i = 0; i < titles.size(); i++) {
            View view = generateTextView(titles.get(i));
            if (mVisibleCount <= 3) {
                LinearLayout.LayoutParams pm = (LayoutParams) view.getLayoutParams();
                if (i == 0) {
                    pm.leftMargin = tabViewWidth / 2;
                } else if (i == (mVisibleCount - 1)) {
                    pm.rightMargin = tabViewWidth / 2;
                }
                view.setLayoutParams(pm);
            }
            addView(view);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI);
                    highlightText(finalI);
                }
            });
        }
    }

    /**
     * 用来获取屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 用来执行下划线滑动的核心类
     *
     * @param position
     * @param offset
     */
    private void scroll(int position, float offset) {
        int extra;
        if (position >= 0 && position < getChildCount() - 1) {
            extra = underLineLeftList.get(position + 1) - underLineLeftList.get(position);
            mLeft = (int) ((position + offset) * tabViewWidth + underLineLeftList.get(position) + extra * offset);
            mRight = (int) ((position + 1 + offset) * tabViewWidth - underLineLeftList.get(position) - extra * offset);
            if (mVisibleCount <= 3) {
                mLeft += tabViewWidth / 2;
                mRight += tabViewWidth / 2;
            }
        }
        //容器移动,当tab处于移动至最后一个时
        if (position >= mVisibleCount - 2 && offset > 0 && mChildCount > mVisibleCount && position < mChildCount - 1) {
            if (mVisibleCount != 1) {
                this.scrollTo((int) ((position - (mVisibleCount - 2)) * tabViewWidth + tabViewWidth * offset), 0);
            } else {
                this.scrollTo((int) (position * tabViewWidth + tabViewWidth * offset), 0);
            }
        }
        //调用invalidate的时候会去进行重绘，调用onDraw，
        invalidate();
    }

    /**
     * 用来生成tab 目前暂定为TextView
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = tabViewWidth;
        RelativeLayout tvLinearLayout = new RelativeLayout(getContext());
        tvLinearLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams paramtv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramtv.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv.setText(title);
        tv.setId(R.id.tab_name);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(parseColor("#222222"));
        tv.setLayoutParams(paramtv);
        tvLinearLayout.addView(tv);
        return tvLinearLayout;
    }

    /**
     * 用来计算整个view的宽度，规则由设计确定
     *
     * @param visibleCount
     * @param width
     * @return
     */
    private int calculateViewWidth(int visibleCount, int width) {
        if (visibleCount <= 3) {
            return getScreenWidth() / (visibleCount + 1);
        } else if (visibleCount == 4) {
            return getScreenWidth() / visibleCount;
        } else {
            return width;
        }
    }

    /**
     * 用来计算下划线的长度和下划线离view左侧的距离。目前规则是居中
     */
    private boolean isFirst = true;

    private void calculateUnderlineWidth() {
        if (isFirst) {
            underLineList = new ArrayList<>();
            int underLineMarginLeft = 0;
            underLineLeftList = new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) {
                RelativeLayout rl = (RelativeLayout) getChildAt(i);
                int textViewWidth = rl.findViewById(R.id.tab_name).getWidth();
                underLineList.add(textViewWidth + 60);
                underLineMarginLeft = (rl.getWidth() - (textViewWidth + 60)) / 2;
                TextView textView = (TextView) rl.findViewById(R.id.tab_name);
                RelativeLayout.LayoutParams pm = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                pm.height = LinearLayout.LayoutParams.MATCH_PARENT;
                pm.width = textViewWidth + 60;
                textView.setLayoutParams(pm);
                if (i <= 3) {
                    underLineLeftList.add(underLineMarginLeft);
                } else {
                    underLineLeftList.add(underLineMarginLeft);
                }
            }
            isFirst = false;
        }
    }

    /**
     * 用来改变字的颜色
     */
    private void highlightText(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            RelativeLayout rl = (RelativeLayout) getChildAt(i);
            TextView tv = (TextView) rl.findViewById(R.id.tab_name);
            tv.setTextColor(parseColor("#222222"));
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        RelativeLayout rl = (RelativeLayout) getChildAt(position);
        TextView tv = (TextView) rl.findViewById(R.id.tab_name);
        tv.setTextColor(parseColor("#0099F7"));
        tv.setBackgroundColor(Color.parseColor("#F7F7F7"));
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public void setOnChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

}