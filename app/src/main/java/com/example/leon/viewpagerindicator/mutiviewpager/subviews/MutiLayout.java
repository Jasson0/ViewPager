package com.example.leon.viewpagerindicator.mutiviewpager.subviews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.example.leon.viewpagerindicator.R;


/**
 * Created by leon on 2017/9/9.
 */

public class MutiLayout extends ViewGroup {
    private IndicatorLayout indicatorLayout;
    private View indicator;
    private ViewPager viewPager;
    private RecyclerView recyclerView;

    private OverScroller scroller;
    private VelocityTracker mVelocityTracker;//滑动速度跟踪类
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private int distanceFromViewPagerToX = 0;
    private float mLastY;

    private boolean mDragging;
    private boolean isTopHidden = false;
    private boolean isInControl = false;
    private int mLastMotionY;
    private int mActivePointerId;

    private boolean isPinnedView = false;

    public MutiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        indicatorLayout = (IndicatorLayout) findViewById(R.id.my_indicator_layout);
        indicator = findViewById(R.id.my_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (indicatorLayout.getVisibility() == VISIBLE) {
            isPinnedView = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int size = getChildCount();
        final int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int paretnHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams childLp = child.getLayoutParams();
                final boolean childWidthWC = childLp.width == LayoutParams.WRAP_CONTENT;
                final boolean childHeightWC = childLp.height == LayoutParams.WRAP_CONTENT;
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams childMarginLp = (MarginLayoutParams) childLp;
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec
                            .makeMeasureSpec(parentWidthSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight()
                                    + childMarginLp.leftMargin
                                    + childMarginLp.rightMargin,
                            childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec
                            .makeMeasureSpec(paretnHeightSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom()
                                    + childMarginLp.topMargin
                                    + childMarginLp.bottomMargin,
                            childMarginLp.height);
                } else {
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec
                            .makeMeasureSpec(parentWidthSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight(),
                            childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec
                            .makeMeasureSpec(paretnHeightSize,
                                    MeasureSpec.UNSPECIFIED)
                            : getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom(),
                            childLp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        if (isPinnedView) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dy = y - mLastY;
                    getCurrentRecyclerView();
                    View view = recyclerView.getChildAt(0);
                    if (!isInControl && view != null && view.getTop() == 0 && isTopHidden && dy > 0) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished())
                    scroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                //判断是滑动还是点击
                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }

                if (mDragging) {
                    scrollBy(0, (int) -dy);
                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (getScrollY() == distanceFromViewPagerToX && dy < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                    }
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                //初始化
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当滑动速度比较大的时候，实现快速滑动
     *
     * @param velocityY
     */
    public void fling(int velocityY) {
        //
        scroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, distanceFromViewPagerToX);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > distanceFromViewPagerToX) {
            y = distanceFromViewPagerToX;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        isTopHidden = getScrollY() == distanceFromViewPagerToX;

    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        }
    }

    private void getCurrentRecyclerView() {
        int currentItem = viewPager.getCurrentItem();
        PagerAdapter a = viewPager.getAdapter();
        FragmentPagerAdapter fadapter = (FragmentPagerAdapter) a;
        Fragment item = (Fragment) fadapter.instantiateItem(viewPager,
                currentItem);
        recyclerView = (RecyclerView) item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    return true;
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentRecyclerView();
                if (Math.abs(dy) > mTouchSlop) {
                    //滑动
                    mDragging = true;
                    View view = recyclerView.getChildAt(0);
                    // 拦截条件：topView没有隐藏
                    // 或recyclerView在顶部 && topView隐藏 && 下拉
                    if (!isTopHidden || (view != null && view.getTop() == 0 && isTopHidden && dy > 0)) {
                        initVelocityTrackerIfNotExists();
                        mLastY = y;
                        mVelocityTracker.addMovement(ev);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                recycleVelocityTracker();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childStartPosition;
        final int count = getChildCount();
        if (count == 0) {
            return;
        }
        int lastHeight = 0;
        childStartPosition = getPaddingTop();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != View.GONE) {
                LayoutParams lp = child.getLayoutParams();
                final int childHeight = child.getMeasuredHeight();
                int leftMargin = 0;
                int rightMargin = 0;
                int topMargin = 0;
                int bottomMargin = 0;
                if (lp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) lp;
                    leftMargin = mlp.leftMargin;
                    rightMargin = mlp.rightMargin;
                    topMargin = mlp.topMargin;
                    bottomMargin = mlp.bottomMargin;
                }

                childStartPosition += topMargin;
                int startX = (getWidth() - leftMargin - rightMargin - child
                        .getMeasuredWidth()) / 2 + leftMargin;
                child.layout(startX, childStartPosition,
                        startX + child.getMeasuredWidth(), childStartPosition
                                + childHeight);
                childStartPosition += (childHeight + bottomMargin);
                if (i == count - 1) {
                    lastHeight = childHeight + bottomMargin;
                }
            }
        }
        distanceFromViewPagerToX = childStartPosition - lastHeight;
        if (isPinnedView) {
            ViewGroup.LayoutParams params = viewPager.getLayoutParams();
            params.height = getMeasuredHeight() - indicator.getMeasuredHeight();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
