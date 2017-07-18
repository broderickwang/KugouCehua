package com.hannahxian.kugoucehua;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * Created by 王成达 on 2017/7/18.
 * Version:1.0
 * Email:wangchengda1990@gamil.com
 * Description:
 */

public class QQSlideMenu extends HorizontalScrollView {
    private final String TAG = "XSlidemenu";

    private int mMenuMargin = 62;

    private View mMenuView,mContentView;

    private int mMenuWidth;

    private boolean mMenuIsOpen;

    private boolean mIsIntercept;

    private GestureDetector mGestureDetector;

    private GestureDetector.SimpleOnGestureListener mListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(mMenuIsOpen){
                if(velocityX < 0 ) {
                    closeMenu();
                    return true;
                }
            }else{
                if(velocityX > 0) {
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };
    public QQSlideMenu(Context context) {
        this(context,null);
    }

    public QQSlideMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQSlideMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context,mListener);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        if(mMenuIsOpen){
            if(ev.getX() > mMenuWidth){
                closeMenu();
                mIsIntercept = true;
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        mGestureDetector.onTouchEvent(ev);

        if(mGestureDetector.onTouchEvent(ev)){
            return true;
        }

        if(mIsIntercept){
            return true;
        }

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
        //l mMenuWidth - > 0
        /*float scale = 1f * l / mMenuWidth;// scale 变化是 1 - 0
        float rightScale = 0.7f+ scale*0.3f;
        float rightScale2 = 0.7f+ (l/mMenuWidth)*0.3f;
        ViewCompat.setPivotX(mContentView,0);
        ViewCompat.setPivotY(mContentView,mContentView.getHeight()/2);
        ViewCompat.setScaleX(mContentView,rightScale);
        ViewCompat.setScaleY(mContentView,rightScale);

        float alpha = 0.5f+(1-scale)*0.5f;
        ViewCompat.setAlpha(mMenuView,alpha);
        float leftScale = 0.6f+(1-scale)*0.4f;
        Log.d(TAG, "onScrollChanged: "+leftScale);
        ViewCompat.setScaleX(mMenuView,leftScale);
        ViewCompat.setScaleY(mMenuView,leftScale);*/

        ViewCompat.setTranslationX(mMenuView,l*0.6f);

        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void openMenu() {
        smoothScrollTo(0,0);
        mMenuIsOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        mMenuIsOpen = false;
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
