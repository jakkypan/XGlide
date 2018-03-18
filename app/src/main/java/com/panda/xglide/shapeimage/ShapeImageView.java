package com.panda.xglide.shapeimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.panda.xglide.R;

/**
 * 可裁制形状的ImageView
 *
 * Created by panda on 2018/3/6.
 */
@SuppressLint("AppCompatCustomView")
public class ShapeImageView extends ImageView {
    private ImageShape mShape;
    private Path mShapePath;
    private Paint mBorderPaint;

    private int mRadius;
    private int mBorderWidth;
    private int mBorderColor;
    private float[] mBorderDashArr = null;
    private int mRotate;

    public ShapeImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray customShape = context.obtainStyledAttributes(attrs, R.styleable.ImageShapeView);
        int shapeType = customShape.getInt(R.styleable.ImageShapeView_blShape, -1);
        switch (shapeType) {
            case ShapeType.CIRCLE:
                mShape = new CircleImageShape();
                break;
            case ShapeType.RECTANGLE:
                mShape = new RectangleImageShape();
                break;
            case ShapeType.TRIANGLE:
                mShape = new TriangleImageShape();
                break;
            case ShapeType.HEART:
                mShape = new HeartImageShape();
                break;
            default:
//                mShape = new CircleImageShape();
                break;
        }
        mRadius = customShape.getDimensionPixelSize(R.styleable.ImageShapeView_blRadius, 0);
        mBorderWidth = customShape.getDimensionPixelSize(R.styleable.ImageShapeView_blBorderWidth, 0);
        mBorderColor = customShape.getColor(R.styleable.ImageShapeView_blBorderColor, 0);
        int dashArrId = customShape.getResourceId(R.styleable.ImageShapeView_blBorderDashArr, 0);
        if (dashArrId > 0) {
            int[] intArray = getResources().getIntArray(dashArrId);
            if (intArray.length > 0 && intArray.length % 2 == 0) {
                mBorderDashArr = new float[intArray.length];
                for (int i = 0; i < intArray.length; i++) {
                    mBorderDashArr[i] = intArray[i];
                }
            }
        }
        mRotate = customShape.getInt(R.styleable.ImageShapeView_blRotate, 0);
        customShape.recycle();

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        /**
         *  设置绘制的结尾的/开始的地方设置收尾的形状
         *    BUTT：（默认）不在结尾处添加任何一笔
         *    SQUARE：在结尾处加一个方形
         *    ROUND：在结尾处追加一个半圆
         */
        mBorderPaint.setStrokeCap(Paint.Cap.BUTT);
        /**
         *  设置绘制结合处的形状（如果有结合处）
         *  BEVEL：直线
         *  MITER：（默认）直角
         *  ROUND：圆角
         */
        mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        mShapePath = new Path();
    }

    @Override
    public void draw(Canvas canvas) {
        // 无任何形状的操作
        if (mShape == null) {
            super.draw(canvas);
            return;
        }

        // shape的绘制必须在super之前
        mShapePath.rewind();
        mShape.drawShape(this, canvas, mShapePath);
        canvas.clipPath(mShapePath);

        super.draw(canvas);

        // border的绘制必须在super之后
        if (mBorderWidth > 0) {
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            // 决定border是否是dash的方式绘制
            if (mBorderDashArr != null) {
                mBorderPaint.setPathEffect(new DashPathEffect(mBorderDashArr,0));
            }
            mShape.drawBorder(this, canvas, mBorderPaint);
        }

        // 决定是否进行旋转
        if (mRotate > 0) {
//            canvas.rotate(mRotate, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            Matrix matrix = new Matrix();
            matrix.setRotate(mRotate, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            canvas.setMatrix(matrix);
        }
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        if (mRadius != radius) {
            mRadius = radius;
            invalidate();
        }
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (mBorderWidth != borderWidth) {
            mBorderWidth = borderWidth;
            invalidate();
        }
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor;
            invalidate();
        }
    }
}
