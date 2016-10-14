package com.zcy.goodsdetail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by zcy on 2016/10/14.
 */
public class PageTwoWebView extends WebView implements SlidingDetailsLayout.TopBottomListener{
    public float oldY;
    private int t;
    private float oldX;
    private boolean top,bottom;

    public PageTwoWebView(Context context) {
        super(context);
        top = false;
        bottom = false;
    }

    public PageTwoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        top = false;
        bottom = false;
    }

    public PageTwoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        top = false;
        bottom = false;
    }
    @Override
    public boolean isScrollTop() {
        return top;
    }

    @Override
    public boolean isScrollBottom() {
        return bottom;
    }

    @Override
    public void scrollToTop() {
        pageUp(top);

    }

    @Override
    public void scrollToBottom() {
        pageDown(bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();
                float gapHorizontal = X - oldX;

                /** 说明:
                 *如果是横向移动,就让父控件重新获得触摸事件
                 */
                if (Math.abs(gapHorizontal) > 120) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);

        if(getContentHeight()<getHeight()){
            top = true;
            bottom = true;
        }else{
            top = getScrollY() == 0;
            int diff = (getBottom()-(getHeight()+getScrollY()));
            bottom = diff == 0;
        }


    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
        top = getScrollY() == 0;

        int diff = (getBottom()-(getHeight()+getScrollY()));
        bottom = diff == 0;
    }

}
