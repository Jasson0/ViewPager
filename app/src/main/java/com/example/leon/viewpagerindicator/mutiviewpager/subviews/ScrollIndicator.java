package com.example.leon.viewpagerindicator.mutiviewpager.subviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.TabFragment;
import com.example.leon.viewpagerindicator.mutiviewpager.utils.TabUtil;
import com.example.leon.viewpagerindicator.mutiviewpager.utils.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;

/**
 * Created by leon on 2017/9/18.
 */

public class ScrollIndicator extends HorizontalScrollView {
    private int currentPosition;

    /**
     * 目前支持的类型，均分，有margin的均分（margin=tabWidth/2，小于4个）,MUTI指的是超过4个的情况
     */
    public enum TabLayoutType {
        AVERAGE,
        AVERAGE_MARGIN,
        MUTI,
    }

    /**
     * 自定义属性
     */
    private int tabNumber;//可见的tab个数
    private int textLayoutHeight;
    private int textSize;
    private int normalColor;
    private int highLightColor;
    /**
     * dynamicLine的属性
     */
    private int dynamicColor, dynamicHeight, xRadius, yRadius;

    public List<String> titles;//tab的内容List
    private int tabViewWidth = 0;//各个tab的长度,当tab数量大于4时，此时已经不是均分，因此tabViewWidth已经无用
    private int marginLeft = 0;//左间距
    private ArrayList<Integer> underlineWidthList = new ArrayList<>();//下划线的长度,每条下划线长度不同，需要用List保存
    private int marginText;//下划线和内部text的间隔

    private ViewPager viewPager;
    private TabFragment[] mFragments;
    private LinearLayout textLayout;
    private LinearLayout contentLayout;
    private LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private DynamicLine dynamicLine;
    private int lastPosition;
    private int[] location = new int[2];

    public void setFragments(TabFragment[] mFragments) {
        this.mFragments = mFragments;
    }

    private ArrayList<TextView> textViews = new ArrayList<>();
    /**
     * 当自定义控件本身将接口使用时，需要提供给用户同样的回调
     */
    private OnPageChangeListener onPageChangeListener;
    private Context context;
    private int screenWidth;

    private TabLayoutType type;
    /**
     * 当点击tab时不增加颜色切换效果，当左右滑动viewpager时增加颜色渐变切换效果。
     */
    private boolean isClick = false;

    public ScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        screenWidth = ToolUtil.getScreenWidth(context);
        initAttr(attrs);
        createDynamicLine();
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        setHorizontalScrollBarEnabled(false);//去除滑动条
        contentLayout = new LinearLayout(context);
        textLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(contentParams);
        textLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentLayout, contentParams);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
        textLayoutHeight = ToolUtil.getPxFromDip(context, ta.getInteger(R.styleable.Indicator_text_view_height, 0));
        textSize = ta.getInteger(R.styleable.Indicator_text_size, 16);
        normalColor = ta.getColor(R.styleable.Indicator_text_normal_color, Color.parseColor("#222222"));
        highLightColor = ta.getColor(R.styleable.Indicator_text_hl_color, Color.parseColor("#0099F7"));
        dynamicColor = ta.getColor(R.styleable.Indicator_dynamic_line_color, Color.parseColor("#0099F7"));
        dynamicHeight = ta.getInteger(R.styleable.Indicator_dynamic_line_height, 5);
        xRadius = ta.getInteger(R.styleable.Indicator_dynamic_line_xRadius, 5);
        yRadius = ta.getInteger(R.styleable.Indicator_dynamic_line_yRadius, 5);
        ta.recycle();
    }

    /**
     * 初始化下划线
     */
    private void createDynamicLine() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dynamicLine = new DynamicLine(context);
        dynamicLine.init(dynamicColor, dynamicHeight, xRadius, yRadius);
        dynamicLine.setLayoutParams(params);
    }

    /**
     * 设置联动的viewpager
     *
     * @param viewPager
     */
    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, int positionOffsetPixels) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
                int extra;
                if (underlineWidthList.size() > 1 && position >= 0 && position < textLayout.getChildCount() - 1) {
                    int start = 0;
                    int stop = 0;
                    if (titles.size() < 5) {
                        int extraLeft = (tabViewWidth - underlineWidthList.get(position)) / 2;//下划线和tabContent的间距
                        extra = (underlineWidthList.get(position + 1) - underlineWidthList.get(position)) / 2;
                        start = (int) ((position + positionOffset) * tabViewWidth + marginLeft + extraLeft - extra * positionOffset);
                        stop = (int) ((position + 1 + positionOffset) * tabViewWidth + marginLeft - extraLeft + extra * positionOffset);
                    } else {
                        int margin = 0;
                        for (int i = 0; i < position; i++) {
                            margin += underlineWidthList.get(i);
                        }
                        start = (int) (margin + underlineWidthList.get(position) * positionOffset + marginLeft);
                        stop = (int) (margin + underlineWidthList.get(position) + underlineWidthList.get(position + 1) * positionOffset + marginLeft);
                    }
                    dynamicLine.updateView(start, stop);
                }
                //用来实现颜色变化
                if (!isClick) {
                    if (position < lastPosition) {
                        gradientTextColor(position, (1 - positionOffset), normalColor, highLightColor);
                        gradientTextColor(lastPosition, (1 - positionOffset), highLightColor, normalColor);
                    } else {
                        if (position < titles.size() - 1 && positionOffset > 0) {
                            gradientTextColor(position, positionOffset, highLightColor, normalColor);
                            gradientTextColor(position + 1, positionOffset, normalColor, highLightColor);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("leon","onPageSelected");
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("leon","onPageScrollStateChanged = "+state);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    boolean scrollRight = lastPosition < viewPager.getCurrentItem();
                    if (lastPosition + 1 < textViews.size() && lastPosition - 1 >= 0) {
                        textViews.get(viewPager.getCurrentItem()).getLocationOnScreen(location);
                        /**
                         * 下面几行代码，解决页面滑到的TAB页时对应的TextView对应，TextView处于屏幕外面，
                         * 这个时候就需要将HorizontalScrollView滑动到屏幕中间。
                         */
                        if (location[0] > screenWidth / 2 && scrollRight) {
                            ScrollIndicator.this.smoothScrollBy(location[0] - screenWidth / 2, 0);
                        } else if (location[0] < screenWidth / 2 && !scrollRight) {
                            ScrollIndicator.this.smoothScrollBy(location[0] - screenWidth / 2, 0);
                        }
                    }
                    lastPosition = viewPager.getCurrentItem();
                }
            }
        });
        viewPager.setCurrentItem(0);
    }

    /**
     * 设置tab的一些参数
     *
     * @param titles
     * @param marginLeft 最左边的tab离左侧的间距
     * @param marginText text和下划线宽度的差值
     * @param type       tab分布的类型，目前支持3种
     */
    public void setTabItemTitles(List<String> titles, int marginLeft, int marginText, TabLayoutType type) {
        this.marginLeft = marginLeft;
        this.marginText = marginText;
        this.type = type;
        tabNumber = titles.size();
        if (titles != null) {
            textLayout.removeAllViews();
            this.titles = titles;
        }
        tabViewWidth = getTabWidth(tabNumber);
        for (int i = 0; i < titles.size(); i++) {
            View view = generateTextView(titles.get(i), i);
            textLayout.addView(view);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastPosition == finalI) {
                        mFragments[finalI].getmRecyclerView().smoothScrollToPosition(0);
                    }
                    viewPager.setCurrentItem(finalI);
                    highlightText(finalI);
                    isClick = true;
                }
            });
        }

        if (textLayoutHeight != 0) {
            textViewParams.height = textLayoutHeight;
        }
        contentLayout.addView(textLayout, textViewParams);
        contentLayout.addView(dynamicLine);
    }

    /**
     * 获得tab的宽度
     *
     * @param visibleCount
     * @return
     */
    private int getTabWidth(int visibleCount) {
        if (type == TabLayoutType.AVERAGE) {
            return screenWidth / (visibleCount);
        } else if (type == TabLayoutType.AVERAGE_MARGIN) {
            return screenWidth / (visibleCount + 1);
        } else {
            return screenWidth / visibleCount;
        }
    }

    /**
     * 用来动态生成tab 目前暂定为TextView,决定它的layoutParams
     *
     * @param title
     * @return
     */
    private View generateTextView(String title, final int position) {
        final TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = tabViewWidth;
        if (type == TabLayoutType.AVERAGE_MARGIN) {
            marginLeft = tabViewWidth / 2;
        }
        //处理margin
        if (position == 0) {
            params.leftMargin = marginLeft;
        } else if (position == titles.size() - 1) {
            params.rightMargin = marginLeft;
        }
        RelativeLayout tvRl = new RelativeLayout(context);
        tvRl.setLayoutParams(params);

        RelativeLayout.LayoutParams paramtv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramtv.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv.setText(title);
        tv.setId(R.id.tab_name);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        tv.setTextColor(normalColor);
        tv.setLayoutParams(paramtv);
        textViews.add(tv);
        tvRl.addView(tv);
        observeText(tv, position);
        return tvRl;
    }

    /**
     * 给text设置全局观察者，需要当textView被measure之后才能动态确定每条underline的长度
     *
     * @param tv
     * @param position
     */
    private void observeText(final TextView tv, final int position) {
        ViewTreeObserver observer = tv.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int size = tv.getMeasuredWidth() + marginText;
                if (type == TabLayoutType.MUTI) {
                    underlineWidthList.add(size);
                } else {
                    underlineWidthList.add(size > tabViewWidth ? tabViewWidth : size);
                }
                //当为muti的时候需要重新layout rl
                if (type == TabLayoutType.MUTI) {
                    RelativeLayout rl = (RelativeLayout) tv.getParent();
                    LinearLayout.LayoutParams pm = (LinearLayout.LayoutParams) rl.getLayoutParams();
                    pm.width = size;
                    rl.setLayoutParams(pm);
                }
                if (position == 0) {
                    int extra = 0;
                    if (titles.size() < 5) {
                        extra = (tabViewWidth - underlineWidthList.get(0)) / 2;
                    }
                    int start = marginLeft + extra;
                    int stop = underlineWidthList.get(0) + start;
                    dynamicLine.updateView(start, stop);
                    highlightText(0);
                }
            }
        });
    }


    /**
     * 用来改变字的颜色
     */
    public void highlightText(int position) {
        currentPosition = position;
        for (int i = 0; i < textLayout.getChildCount(); i++) {
            RelativeLayout rl = (RelativeLayout) textLayout.getChildAt(i);
            TextView tv = (TextView) rl.findViewById(R.id.tab_name);
            tv.setTextColor(normalColor);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(false);
        }
        RelativeLayout rl = (RelativeLayout) textLayout.getChildAt(position);
        TextView tv = (TextView) rl.findViewById(R.id.tab_name);
        tv.setTextColor(highLightColor);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 用来渐变text颜色
     *
     * @param position
     * @param fraction
     * @param startColor
     * @param endColor
     */
    private void gradientTextColor(int position, float fraction, int startColor, int endColor) {
        RelativeLayout rl1 = (RelativeLayout) textLayout.getChildAt(position);
        TextView tv1 = (TextView) rl1.findViewById(R.id.tab_name);
        tv1.getCurrentTextColor();
        tv1.setTextColor(TabUtil.getCurrentColor(fraction, startColor, endColor));
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