package com.panda.xglide.shapeimage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * 矩形的，圆角自定义
 *
 * Created by panda on 2018/3/7.
 */
public class RectangleImageShape implements ImageShape {
    private RectF mRectF;

    public RectangleImageShape() {
        this.mRectF = new RectF();
    }

    @Override
    public void drawBorder(ShapeImageView shapeImageView, Canvas canvas, Paint paint) {
        mRectF.set(0, 0, shapeImageView.getMeasuredWidth(), shapeImageView.getMeasuredHeight());
        if (shapeImageView.getRadius() <= 0) {
            canvas.drawRect(mRectF, paint);
        } else {
            canvas.drawRoundRect(mRectF, shapeImageView.getRadius(), shapeImageView.getRadius(), paint);
        }
    }

    @Override
    public void drawShape(ShapeImageView shapeImageView, Canvas canvas, Path path) {
        mRectF.set(0, 0, shapeImageView.getMeasuredWidth(), shapeImageView.getMeasuredHeight());
        if (shapeImageView.getRadius() <= 0) {
            path.addRect(mRectF, Path.Direction.CW);
        } else {
            path.addRoundRect(mRectF, new float[] {
                    shapeImageView.getRadius(), shapeImageView.getRadius(),
                    shapeImageView.getRadius(), shapeImageView.getRadius(),
                    shapeImageView.getRadius(), shapeImageView.getRadius(),
                    shapeImageView.getRadius(), shapeImageView.getRadius()}, Path.Direction.CW);
        }
    }
}
