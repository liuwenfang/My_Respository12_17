package slide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import utils.DensityUtils;
import zzx.zzxbaselibrary.R;

/**
 * Created by zzx on 2017/11/22 0022.
 */

public class SlideLayout extends FrameLayout {
    private int SHADOW_WIDTH = 16;
    // 页面边缘阴影图
    private Drawable mLeftShadow;
    // 页面边缘阴影的宽度
    private int mShadowWidth;
    // 当前activity
    private Activity mActivity;
    // 滑动帮助类
    private Scroller mScroller;
    // 拦截到的X   触摸事件 手指 down/up 的 x
    private int mInterceptDownX;
    // 拦截到的X   触摸事件  X
    private int mLastInterceptX;
    // 拦截到的Y   触摸事件  Y
    private int mLastInterceptY;

    private int mTouchDownX;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed = false;

    /**
     * scrollTo(x,y)  偏移至 x,y(坐标)
     * scrollBy(x,y)  偏移量 x,y (值)
     */

    public SlideLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);
        mLeftShadow = getResources().getDrawable(R.drawable.slide_shadow);
        mShadowWidth = DensityUtils.dip2px(context, SHADOW_WIDTH);
    }

    /**
     * 绑定 Activity
     */
    public void bindActivity(Activity mActivity) {
        this.mActivity = mActivity;
        // 获取顶级视图  包含整个屏幕 包含 状态栏
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        View childView = decorView.getChildAt(0);
        // 把 childView 从顶层布局移除
        decorView.removeView(childView);
        // 把 childView 添加进自定义布局  SlideLayout
        addView(childView);
        // 把 自定义布局 SlideLayout 加入 顶层布局   SlideLayout做处理
        decorView.addView(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mInterceptDownX = x;
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                int moveX = x - mLastInterceptX;
                int moveY = y - mLastInterceptY;
                // 判断边缘  且 横向 移动   拦截事件
                if ((mInterceptDownX < (getWidth() / 10)) && Math.abs(moveX) > Math.abs(moveY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
        }
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = x - mLastTouchX;
                int moveY = y - mLastTouchY;
                // 没有开始滑动  边缘开始  横向滑动
                if (!isConsumed && (mTouchDownX < (getWidth() / 10)) && (Math.abs(moveX) > Math.abs(moveY))) {
                    isConsumed = true;
                }
                //  判断是否可以滑动
                if (isConsumed) {
                    //   左上  为正  右下 为 负
                    int rightMovedX = mLastTouchX - (int) event.getX();
                    // 移动到  x,y
//                    scrollTo(x, y);
                    // 滑动 rightMovedX
                    scrollBy(rightMovedX, 0);
                }
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                isConsumed = false;
                mTouchDownX = mLastTouchX = mLastTouchY = 0;
                // x 滑动 超过 屏幕一半 滑动到右边  ， 否则 滑动回去
                if (-getScrollX() > (getWidth() / 2)) {
                    scrollClose();
                } else {
                    scrollBack();
                }
                break;
        }
        return true;
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();//负值
        int dx = -getScrollX();
        //  startX,startY  (起点坐标)   dx,dy  (偏移量)  左上 正  右下 负
        mScroller.startScroll(startX, 0, dx, 0, 300);
        //重新绘制
        invalidate();
    }

    /**
     * 滑动关闭
     */
    private void scrollClose() {
        int startX = getScrollX();//负值
        // 从  dx 滑动到 最右端
        int dx = -getScrollX() - getWidth();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        //重新绘制
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//判断startScroll()是否结束
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            mActivity.finish();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    /**
     * 绘制边缘的阴影
     */
    private void drawShadow(Canvas canvas) {
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);
        mLeftShadow.draw(canvas);
        canvas.restore();
    }

}
