package com.panda.xglide.shapeimage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 形状的抽象类
 *
 * Created by panda on 2018/3/6.
 */
public interface ImageShape {
    /**
     * 绘制图形的边框
     *
     * @param shapeImageView
     * @param canvas
     * @param paint
     */
    public void drawBorder(ShapeImageView shapeImageView, Canvas canvas, Paint paint);

    /**
     * 绘制形状的图形
     *
     * @param shapeImageView
     * @param canvas
     * @param path
     */
    public void drawShape(ShapeImageView shapeImageView, Canvas canvas, Path path);
}
