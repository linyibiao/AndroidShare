package com.lyb.besttimer.pluginwidget.utils;

import android.graphics.drawable.GradientDrawable;

public class GradientDrawableHelper {

    public static GradientDrawable getGradientDrawable(GradientDrawableData gradientDrawableData) {
        GradientDrawable gradientDrawable = new GradientDrawable(gradientDrawableData.getOrientation(), gradientDrawableData.getColors());
        gradientDrawable.setCornerRadius(gradientDrawableData.getmRadius());
        GradientDrawableData.StrokeData strokeData = gradientDrawableData.getStrokeData();
        gradientDrawable.setStroke(strokeData.getWidth(), strokeData.getColor(), strokeData.getDashWidth(), strokeData.getDashGap());
        gradientDrawable.setShape(gradientDrawableData.getmShape());
        gradientDrawable.setGradientType(gradientDrawableData.getmGradient());
        gradientDrawable.setGradientCenter(gradientDrawableData.getmCenterX(), gradientDrawableData.getmCenterY());
        gradientDrawable.setGradientRadius(gradientDrawableData.getmGradientRadius());
        gradientDrawable.setColor(gradientDrawableData.getArgb());
        return gradientDrawable;
    }

    public static class GradientDrawableData {

        private GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        private int[] colors = null;
        private float mRadius = 0.0f;
        private StrokeData strokeData = new StrokeData(0, 0);
        private int mShape = GradientDrawable.RECTANGLE;
        private int mGradient = GradientDrawable.LINEAR_GRADIENT;
        private float mCenterX = 0.5f;
        private float mCenterY = 0.5f;
        private float mGradientRadius = 0.5f;
        private int argb = 0;

        public static class StrokeData {
            private int width;
            private int color;
            private float dashWidth;
            private float dashGap;

            public StrokeData(int width, int color) {
                this(width, color, 0, 0);
            }

            public StrokeData(int width, int color, float dashWidth, float dashGap) {
                this.width = width;
                this.color = color;
                this.dashWidth = dashWidth;
                this.dashGap = dashGap;
            }

            public int getWidth() {
                return width;
            }

            public int getColor() {
                return color;
            }

            public float getDashWidth() {
                return dashWidth;
            }

            public float getDashGap() {
                return dashGap;
            }
        }

        public GradientDrawableData(float mRadius, StrokeData strokeData) {
            this.mRadius = mRadius;
            this.strokeData = strokeData;
        }

        public GradientDrawableData(float mRadius, int argb) {
            this.mRadius = mRadius;
            this.argb = argb;
        }

        public GradientDrawableData(GradientDrawable.Orientation orientation, int[] colors, float mRadius, StrokeData strokeData, int mShape, int mGradient, float mCenterX, float mCenterY, float mGradientRadius, int argb) {
            this.orientation = orientation;
            this.colors = colors;
            this.mRadius = mRadius;
            this.strokeData = strokeData;
            this.mShape = mShape;
            this.mGradient = mGradient;
            this.mCenterX = mCenterX;
            this.mCenterY = mCenterY;
            this.mGradientRadius = mGradientRadius;
            this.argb = argb;
        }

        public GradientDrawable.Orientation getOrientation() {
            return orientation;
        }

        public int[] getColors() {
            return colors;
        }

        public float getmRadius() {
            return mRadius;
        }

        public StrokeData getStrokeData() {
            return strokeData;
        }


        public int getmShape() {
            return mShape;
        }

        public int getmGradient() {
            return mGradient;
        }

        public float getmCenterX() {
            return mCenterX;
        }

        public float getmCenterY() {
            return mCenterY;
        }

        public float getmGradientRadius() {
            return mGradientRadius;
        }

        public int getArgb() {
            return argb;
        }
    }

}
