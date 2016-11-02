package com.lyb.besttimer.pluginwidget.view.tablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;

/**
 * base TabLayout
 * Created by linyibiao on 2016/10/28.
 */

public class BaseTabLayout extends TabLayout {

    public BaseTabLayout(Context context) {
        this(context, null);
    }

    public BaseTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewGroup mTabStrip;

    private void init() {
        try {

            Field mTabStripField = getClass().getSuperclass().getDeclaredField("mTabStrip");
            mTabStripField.setAccessible(true);
            mTabStrip = (ViewGroup) mTabStripField.get(this);

            initIndicator();
            initBackground();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTabStrip.getViewTreeObserver().addOnPreDrawListener(mTabStripOnPreDrawListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTabStrip.getViewTreeObserver().removeOnPreDrawListener(mTabStripOnPreDrawListener);
    }

    private ViewTreeObserver.OnPreDrawListener mTabStripOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (indicatorShape != SHAPE_INDICATOR.Normal) {
                try {
                    int mIndicatorLeft = mIndicatorLeftField.getInt(mTabStrip);
                    int mIndicatorRight = mIndicatorRightField.getInt(mTabStrip);
                    if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                        if (mCustomIndicatorLeft != mIndicatorLeft || mCustomIndicatorRight != mIndicatorRight) {
                            mCustomIndicatorLeft = mIndicatorLeft;
                            mCustomIndicatorRight = mIndicatorRight;
                            ViewCompat.postInvalidateOnAnimation(BaseTabLayout.this);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };

    private void initIndicator() throws NoSuchFieldException, IllegalAccessException {

        mIndicatorLeftField = mTabStrip.getClass().getDeclaredField("mIndicatorLeft");
        mIndicatorLeftField.setAccessible(true);

        mIndicatorRightField = mTabStrip.getClass().getDeclaredField("mIndicatorRight");
        mIndicatorRightField.setAccessible(true);

        Field mSelectedIndicatorHeightField = mTabStrip.getClass().getDeclaredField("mSelectedIndicatorHeight");
        mSelectedIndicatorHeightField.setAccessible(true);
        mLastSelectedIndicatorHeight = (int) mSelectedIndicatorHeightField.get(mTabStrip);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        mIndicatorPath = new Path();

    }

    //super data
    private Field mIndicatorLeftField;
    private Field mIndicatorRightField;

    private int mLastSelectedIndicatorHeight;
    private int mCustomIndicatorLeft = -1;
    private int mCustomIndicatorRight = -1;
    private int indicatorPaddingVertical = 0;
    private Paint mIndicatorPaint;
    private Path mIndicatorPath;
    private SHAPE_INDICATOR indicatorShape = SHAPE_INDICATOR.Normal;

    //indicator shape,normal for default
    public enum SHAPE_INDICATOR {
        Normal, Circle, Triangle
    }

    public void setIndicatorPaddingVertical(int indicatorPaddingVertical) {
        this.indicatorPaddingVertical = indicatorPaddingVertical;
    }

    public void setIndicatorColor(int indicatorColor) {
        mIndicatorPaint.setColor(indicatorColor);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setIndicatorShape(SHAPE_INDICATOR indicatorShape) {
        this.indicatorShape = indicatorShape;
        if (indicatorShape == SHAPE_INDICATOR.Normal) {
            setSelectedTabIndicatorHeight(mLastSelectedIndicatorHeight);
        } else {
            super.setSelectedTabIndicatorHeight(0);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void setSelectedTabIndicatorHeight(int height) {
        mLastSelectedIndicatorHeight = height;
        if (indicatorShape == SHAPE_INDICATOR.Normal) {
            super.setSelectedTabIndicatorHeight(height);
        }
    }

    private void initBackground() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private Paint mBackgroundPaint;
    private Paint mEdgePaint;
    private SHAPE_BACKGROUND backgroundShape = SHAPE_BACKGROUND.Normal;

    //background shape,normal for default
    public enum SHAPE_BACKGROUND {
        Normal, Circle
    }

    public void setBGColor(int bgColor) {
        mBackgroundPaint.setColor(bgColor);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setBackgroundShape(SHAPE_BACKGROUND backgroundShape) {
        this.backgroundShape = backgroundShape;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private Bitmap bitmap_BGCircle;

    private Bitmap getBitmapInstance_BGCircle(int width, int height) {

        if (bitmap_BGCircle == null) {

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);

            float d = height;
            float startX = d / 2;
            float startY = height / 2;
            float stopX = width - d / 2;
            float stopY = height / 2;

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(null);
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStrokeWidth(d);
            bitmapCanvas.drawLine(startX, startY, stopX, stopY, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            paint.setStrokeCap(Paint.Cap.ROUND);
            bitmapCanvas.drawLine(startX, startY, stopX, stopY, paint);
            bitmap_BGCircle = bitmap;
        }

        return bitmap_BGCircle;
    }

    @Override
    public void draw(Canvas canvas) {

        int layerId_BGCircle = 0;

        //handle background
        if (backgroundShape == SHAPE_BACKGROUND.Normal) {
            canvas.drawColor(mBackgroundPaint.getColor());
        } else if (backgroundShape == SHAPE_BACKGROUND.Circle) {
            Rect bounds = canvas.getClipBounds();
            bounds.set(bounds.left, bounds.top + indicatorPaddingVertical, bounds.right, bounds.bottom - indicatorPaddingVertical);
            layerId_BGCircle = canvas.saveLayer(new RectF(bounds), null, Canvas.ALL_SAVE_FLAG);
            float d = bounds.height();
            float startX = bounds.left + d / 2;
            float startY = (bounds.top + bounds.bottom) / 2;
            float stopX = bounds.right - d / 2;
            float stopY = (bounds.top + bounds.bottom) / 2;
            mBackgroundPaint.setStrokeWidth(d);
            canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);
        }

        //handle indicator
        if (indicatorShape == SHAPE_INDICATOR.Normal) {
        } else if (indicatorShape == SHAPE_INDICATOR.Circle) {
            float actualWidth = mCustomIndicatorRight - mCustomIndicatorLeft;
            float actualHeight = getHeight() - indicatorPaddingVertical * 2;
            if (actualWidth > actualHeight) {
                float d = actualHeight;
                float startX = mCustomIndicatorLeft + d / 2;
                float startY = getHeight() / 2;
                float stopX = mCustomIndicatorRight - d / 2;
                float stopY = getHeight() / 2;
                mIndicatorPaint.setStrokeWidth(d);
                canvas.drawLine(startX, startY, stopX, stopY, mIndicatorPaint);
            } else if (actualWidth < actualHeight) {
                float d = actualWidth;
                float startX = (mCustomIndicatorLeft + mCustomIndicatorRight) / 2;
                float startY = indicatorPaddingVertical + d / 2;
                float stopX = (mCustomIndicatorLeft + mCustomIndicatorRight) / 2;
                float stopY = getHeight() - (indicatorPaddingVertical + d / 2);
                mIndicatorPaint.setStrokeWidth(d);
                canvas.drawLine(startX, startY, stopX, stopY, mIndicatorPaint);
            } else {
                float cx = (mCustomIndicatorLeft + mCustomIndicatorRight) / 2;
                float cy = getHeight() / 2;
                float radius = (actualWidth + actualHeight) / 2 / 2;
                canvas.drawCircle(cx, cy, radius, mIndicatorPaint);
            }
        } else if (indicatorShape == SHAPE_INDICATOR.Triangle) {
            mIndicatorPath.reset();
            PointF left = new PointF(-20, 0);
            PointF right = new PointF(20, 0);
            PointF top = new PointF(0, -10);
            float dx = (mCustomIndicatorLeft + mCustomIndicatorRight) / 2;
            float dy = getHeight() - indicatorPaddingVertical;
            left.offset(dx, dy);
            right.offset(dx, dy);
            top.offset(dx, dy);
            mIndicatorPath.moveTo(top.x, top.y);
            mIndicatorPath.lineTo(right.x, right.y);
            mIndicatorPath.lineTo(left.x, left.y);
            mIndicatorPath.close();
            canvas.drawPath(mIndicatorPath, mIndicatorPaint);
        }

        super.draw(canvas);

        //handle edge
        if (backgroundShape == SHAPE_BACKGROUND.Normal) {
        } else if (backgroundShape == SHAPE_BACKGROUND.Circle) {

            Rect bounds = canvas.getClipBounds();
            bounds.set(bounds.left, bounds.top + indicatorPaddingVertical, bounds.right, bounds.bottom - indicatorPaddingVertical);

            Bitmap bitmap_BGCircle = getBitmapInstance_BGCircle(bounds.width(), bounds.height());
            mEdgePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(bitmap_BGCircle, bounds.left, bounds.top, mEdgePaint);
            mEdgePaint.setXfermode(null);

            canvas.restoreToCount(layerId_BGCircle);

        }

    }

    public static abstract class BaseTabAdapter<VH extends ViewHolder> {

        private TabLayout tabLayout;

        public BaseTabAdapter(TabLayout tabLayout) {
            this.tabLayout = tabLayout;
        }

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(VH holder, int position);

        public abstract int getItemCount();

        private SparseArray<VH> vhList = new SparseArray<>();

        private VH getVH(int position) {
            VH vh = vhList.get(position);
            if (vh == null) {
                vh = onCreateViewHolder(tabLayout);
                vhList.put(position, vh);
            }
            if (tabLayout.getTabAt(position) == null) {
                tabLayout.addTab(tabLayout.newTab(), position);
            }
            if (tabLayout.getTabAt(position).getCustomView() == null) {
                tabLayout.getTabAt(position).setCustomView(vh.itemView);
            }
            return vh;
        }

        private void removeAllTabs() {
            tabLayout.removeAllTabs();
            vhList.clear();
        }

        public void notifyDataSetChanged() {
            for (int position = 0; position < getItemCount(); position++) {
                onBindViewHolder(getVH(position), position);
            }
        }

    }

    public static abstract class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

    }

}
