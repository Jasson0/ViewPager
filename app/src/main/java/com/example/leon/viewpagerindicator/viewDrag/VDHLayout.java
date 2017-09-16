package com.example.leon.viewpagerindicator.viewDrag;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leon.viewpagerindicator.R;

/**
 * Created by leon on 2017/9/12.
 */

public class VDHLayout extends LinearLayout {
    private ViewDragHelper mDragger;
    private View mDragView;
    private RelativeLayout mAutoBackView;
    private View mEdgeTrackerView;
    private TextView close,click,move;
    private Context context;

    private Point mAutoBackOriginPos = new Point();

    public VDHLayout(Context context) {
        this(context,null);
    }

    public VDHLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView.getWidth() - leftBound;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getWidth();
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = (RelativeLayout) findViewById(R.id.my_layout);
        close = (TextView) mAutoBackView.findViewById(R.id.close);
        move = (TextView) mDragView.findViewById(R.id.move);
        click = (TextView) mDragView.findViewById(R.id.click);
        move.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoBackView.setVisibility(VISIBLE);
            }
        });
        click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragView.setVisibility(GONE);
            }
        });
        mEdgeTrackerView = getChildAt(2);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoBackView.setVisibility(GONE);
            }
        });
    }
}
