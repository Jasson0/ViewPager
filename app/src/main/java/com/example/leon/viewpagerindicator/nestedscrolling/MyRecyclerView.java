package com.example.leon.viewpagerindicator.nestedscrolling;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by leon on 2017/9/11.
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mCanFreeScroll = true;

    private boolean mCanInterceptTouchEvent;

    private boolean mFlingHaveDone;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (mListener != null){
                mListener.onTouchDown();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private RecycleViewScrollTopListener mListener;

    public void setOnRecycleViewScrollTopListener(RecycleViewScrollTopListener listener){
        mListener = listener;
    }

    public interface RecycleViewScrollTopListener{
        public void onTouchDown();
    }

    public void setCanInterceptTouchEvent(boolean canInterceptTouchEvent){
        mCanInterceptTouchEvent = canInterceptTouchEvent;
    }

    public boolean getCanFreeScroll(){
        return mCanFreeScroll;
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        if (layoutManager == null){
            return 0;
        }
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    public void setFlingHaveDone(boolean moreFling){
        mFlingHaveDone = moreFling;
    }

    public boolean getFlingHaveDone(){
        return mFlingHaveDone;
    }
}
