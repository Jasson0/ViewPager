package com.example.leon.viewpagerindicator.nestedscrolling;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;

/**
 * Created by leon on 2017/9/11.
 */

public class StickyNavLayout extends LinearLayout implements NestedScrollingParent {
    private TextView topView;
    private TextView indicator;
    private MyRecyclerView recyclerView;
    private NestedScrollingParentHelper helper = new NestedScrollingParentHelper(this);

    private OverScroller scroller;
    private VelocityTracker mVelocityTracker;//滑动速度跟踪类
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private int distanceFromViewPagerToX;
    private float mLastY;

    private boolean mDragging;
    private boolean isInControl = false;
    private int mLastOutScroll;


    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        scroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = (TextView) findViewById(R.id.top);
        indicator = (TextView) findViewById(R.id.indicator);
        recyclerView = (MyRecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = getMeasuredHeight() - indicator.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        distanceFromViewPagerToX = topView.getMeasuredHeight();
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
                mLastOutScroll = getScrollY();
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
        scroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, distanceFromViewPagerToX);
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

        super.scrollTo(x,y);

    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
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


    //以下：NestedScrollingParent接口------------------------------
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    //移动edv_content
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (!(target instanceof MyRecyclerView)) {
            return;
        }
        boolean hiddenTop = dy > 0 && getScrollY() < distanceFromViewPagerToX;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

//以下：NestedScrollingParent接口的其他方法

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        helper.onStopNestedScroll(target);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        if (getScrollY() < topView.getMeasuredHeight()) {
//            fling((int) velocityY);
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() < topView.getMeasuredHeight()) {
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
}
