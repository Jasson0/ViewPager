package com.example.leon.viewpagerindicator.subviews;

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
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.leon.viewpagerindicator.GoTopShow;
import com.example.leon.viewpagerindicator.R;


/**
 * Created by leon on 2017/9/9.
 */

public class MutiLayout extends LinearLayout {
    private View topView;
    private View indicator;
    private ViewPager viewPager;
    private RecyclerView recyclerView;

    private OverScroller scroller;
    private VelocityTracker mVelocityTracker;//滑动速度跟踪类
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private int distanceFromViewPagerToX;
    private float mLastY;

    private boolean mDragging;
    private boolean isTopHidden = false;
    private GoTopShow goTopShow;

    public void setGoTopShow(GoTopShow goTopShow) {
        this.goTopShow = goTopShow;
    }

    public MutiLayout(Context context, AttributeSet attrs) {
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
        topView = findViewById(R.id.top_item);
        indicator = findViewById(R.id.my_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
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
                    } else {
//                        if (goTopShow != null) {
//                            goTopShow.showTop();
//                        }
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
