package com.panda.xglide.shapeimage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 自绘制的图形流程都是差不多的，因此中间再抽象一层
 *
 * Created by panda on 2018/3/7.
 */
public abstract class SpecialPathImageShape implements ImageShape {

    @Override
    public void drawBorder(ShapeImageView shapeImageView, Canvas canvas, Paint paint) {
        int width = shapeImageView.getMeasuredWidth();
        int height = shapeImageView.getMeasuredHeight();
        Path borderPath = new Path();
        buildPath(borderPath, width, height);
        canvas.drawPath(borderPath, paint);
    }

    @Override
    public void drawShape(ShapeImageView shapeImageView, Canvas canvas, Path path) {
        int width = shapeImageView.getMeasuredWidth();
        int height = shapeImageView.getMeasuredHeight();
        buildPath(path, width, height);
    }

    public abstract void buildPath(Path path, int width, int height);
}
