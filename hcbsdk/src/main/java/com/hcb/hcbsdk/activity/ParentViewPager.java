package com.hcb.hcbsdk.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Administrator
 * @time 2015/11/13 0013 16:45
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ParentViewPager extends ViewPager {
    public ParentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private boolean scroll = true;
    public ParentViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
		/*return false;//super.onTouchEvent(arg0);*/
        if(scroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(scroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
