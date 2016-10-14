package com.zcy.goodsdetail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by zcy on 16-10-14.
 */
public class PageOneScrollView extends ScrollView implements SlidingDetailsLayout.TopBottomListener {

    private boolean top,bottom;

    public PageOneScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        fullScroll(View.FOCUS_UP);
    }

    @Override
    public void scrollToBottom() {
        fullScroll(View.FOCUS_DOWN);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);


        if(getChildAt(0).getHeight()<getHeight()){
            top = true;
            bottom = true;
        }else{
            top = getScrollY() == 0;

            int diff = (getChildAt(0).getBottom()-(getHeight()+getScrollY()));
            bottom = diff == 0;
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.e("MyScrollView","dispatchTouchEvent ==> "+ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        top = getScrollY() == 0;

        int diff = (getChildAt(0).getBottom()-(getHeight()+getScrollY()));
        bottom = diff == 0;


    }
}
