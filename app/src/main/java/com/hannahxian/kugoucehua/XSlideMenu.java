package com.hannahxian.kugoucehua;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * Created by 王成达 on 2017/7/17.
 * Version:1.0
 * Email:wangchengda1990@gamil.com
 * Description:
 */

public class XSlideMenu extends HorizontalScrollView {
    private final String TAG = "XSlidemenu";

    private int mMenuMargin = 62;

    private View mMenuView,mContentView;

    private int mMenuWidth;

    public XSlideMenu(Context context) {
        this(context,null);
    }

    public XSlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XSlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            Log.d(TAG, "onTouchEvent: ->"+ getScrollX() +" menu->"+mMenuWidth);
            if(getScrollX() < mMenuWidth/2 ){
                openMenu();
            }else{
                closeMenu();
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.d(TAG, "onScrollChanged: l->"+l+" t->"+t);

        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void openMenu() {
        smoothScrollTo(0,0);
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
    }

    @Override
    protected void onFinishInflate() {
        ViewGroup parent = (ViewGroup) getChildAt(0);
        if(parent!=null && parent.getChildCount() == 2){
            mMenuView = parent.getChildAt(0);
            ViewGroup.LayoutParams p1 = mMenuView.getLayoutParams();
            mMenuWidth = getScreenWidth(getContext())-dip2px(getContext(),mMenuMargin);
            p1.width = mMenuWidth;
            mMenuView.setLayoutParams(p1);

            mContentView = parent.getChildAt(1);
            ViewGroup.LayoutParams p2 = mContentView.getLayoutParams();
            p2.width = getScreenWidth(getContext());
            mContentView.setLayoutParams(p2);
        }else{
            throw new RuntimeException("please set two child views into the ViewGroup!");
        }
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth,0);
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * Dip into pixels
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
