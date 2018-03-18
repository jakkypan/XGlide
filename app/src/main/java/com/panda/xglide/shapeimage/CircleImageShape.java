package com.panda.xglide.shapeimage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 圆形
 *
 * Created by panda on 2018/3/6.
 */
public class CircleImageShape implements ImageShape {
    @Override
    public void drawBorder(ShapeImageView shapeImageView, Canvas canvas, Paint paint) {
        float xPoint = shapeImageView.getMeasuredWidth() * 0.5f;
        float yPoint = shapeImageView.getMeasuredHeight() * 0.5f;
        // 边框需要往外延扩一些
        float radius = xPoint > yPoint ? yPoint : xPoint;
//        float borderWidth = shapeImageView.getBorderWidth() * 0.5f;
        canvas.drawCircle(xPoint, yPoint, radius, paint);
    }

    @Override
    public void drawShape(ShapeImageView shapeImageView, Canvas canvas, Path path) {
        float xPoint = shapeImageView.getMeasuredWidth() * 0.5f;
        float yPoint = shapeImageView.getMeasuredHeight() * 0.5f;
        float radius = xPoint > yPoint ? yPoint : xPoint;
        path.addCircle(xPoint, yPoint, radius, Path.Direction.CW);
    }
}
