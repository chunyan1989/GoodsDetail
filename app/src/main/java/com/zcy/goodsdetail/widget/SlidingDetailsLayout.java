package com.zcy.goodsdetail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zcy on 16-10-14.
 */
public class SlidingDetailsLayout extends ViewGroup {
    private final static String TAG = SlidingDetailsLayout.class.getSimpleName();
    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;

    /**
     * 手机按下时的屏幕坐标
     */
    private float mYDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mYMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mYLastMove;


    //顶部的view 底部的view
    private View topView, bottomView;
    private View promptView;

    //当前所在的索引
    private int position = 0;

    private TopBottomListener topListener, bottomListener;

    private PositionChangListener positionChangListener;


    private int mTouchSlop;

    /*
    * 监听view是否到了顶部或者底部
    * */
    public interface TopBottomListener {
        public boolean isScrollTop();//已经到了顶部

        public boolean isScrollBottom();//已经到了底部

        public void scrollToTop();//滚动到顶部

        public void scrollToBottom();//滚动到底部
    }

    public interface PositionChangListener {
        public void position(int positon);

        public void onBottom();

        public void backBottom();

        public void onTop();

        public void backTop();
    }

    public SlidingDetailsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);


        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setPositionChangListener(PositionChangListener positionChangListener) {
        this.positionChangListener = positionChangListener;
    }


    @Override
    protected void onFinishInflate() {
        //获取 三个view 并计算 promptView 的宽高
        topView = getChildAt(0);
        promptView = getChildAt(1);
        bottomView = getChildAt(2);


        //topView 和 bottomView 必须继承 TopBottomListener 接口
        if (topView instanceof TopBottomListener) {
            topListener = (TopBottomListener) topView;
        }
        if (bottomView instanceof TopBottomListener) {
            bottomListener = (TopBottomListener) bottomView;
        }


        super.onFinishInflate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局 topView和bottomView宽高都是充满父view
        topView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        promptView.layout(0, getMeasuredHeight(), promptView.getMeasuredWidth(), getMeasuredHeight() + promptView
                .getMeasuredHeight());
        bottomView.layout(0, getMeasuredHeight() + promptView.getMeasuredHeight(), getMeasuredWidth(), 2 *
                getMeasuredHeight() + promptView.getMeasuredHeight());


    }


    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    private boolean top, bottom;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = ev.getRawY();
                mYLastMove = mYDown;
                break;

            case MotionEvent.ACTION_MOVE:

                mYMove = ev.getRawY();

                int scrolledY = (int) (mYLastMove - mYMove);
                mYLastMove = mYMove;

                //当前在第一页
                if (position == 0) {

                    if (positionChangListener != null) {
                        if (getScrollY() + getHeight() > getHeight() + promptView.getHeight()) {
                            if (!bottom) {
                                positionChangListener.onBottom();
                                bottom = true;
                            }
                        } else {
                            if (positionChangListener != null) {
                                positionChangListener.backBottom();
                                bottom = false;

                            }
                        }
                    }


                    if (topListener == null || topListener.isScrollBottom()) {

                        if (getScrollY() + scrolledY < topView.getTop()) {//不能移动超出topview的最上方
                            scrollTo(0, topView.getTop());
                        } else {
                            scrollBy(0, scrolledY);
                            if (getScrollY() != 0) {
                                return true;
                            }
                        }
                    }
                } else {//底部view

                    if (positionChangListener != null) {
                        if (getScrollY() < getHeight()) {
                            if (!top) {
                                positionChangListener.onTop();
                                top = true;
                            }

                        } else {
                            if (top) {
                                positionChangListener.backTop();
                                top = false;
                            }
                        }
                    }


                    if (bottomListener == null || bottomListener.isScrollTop()) {//bottomView 已经移动到了顶部


                        if (getScrollY() + getHeight() + scrolledY > bottomView.getBottom()) {
                            scrollTo(0, bottomView.getBottom() - getHeight());
                        } else {
                            scrollBy(0, scrolledY);
                            if (getScrollY() != getHeight() + promptView.getHeight()) {
                                return true;
                            }
                        }


                    }
                }


                break;

            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex;

                if (position == 0) {
                    targetIndex = getScrollY() + getHeight() > getHeight() + promptView.getHeight() ? 1 : 0;
                } else {
                    targetIndex = getScrollY() < getHeight() ? 0 : 1;
                }
                position = targetIndex;

                int dy;
                if (targetIndex == 0) {
                    dy = targetIndex * getHeight() - getScrollY();
                } else {
                    dy = targetIndex * getHeight() + promptView.getHeight() - getScrollY();
                }

                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(0, getScrollY(), 0, dy);
                invalidate();

                if (positionChangListener != null) {
                    positionChangListener.position(position);
                }

                if (getScrollY() != 0 && getScrollY() != getHeight() + promptView.getHeight()) {//如果处在滑动状态 就不要传递事件给子view
                    return true;
                }

                /*if(Math.abs(ev.getRawY()-mYDown)>mTouchSlop){
                    return false;
                }*/

                break;
        }


        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


}
