package com.panda.xglide.shapeimage;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 图形的形状类型
 *
 * Created by panda on 2018/3/7.
 */
@IntDef({ShapeType.CIRCLE, ShapeType.RECTANGLE, ShapeType.TRIANGLE, ShapeType.HEART})
@Retention(RetentionPolicy.SOURCE)
public @interface ShapeType {
    /**
     * 圆形
     */
    int CIRCLE = 0;
    /**
     * 矩形（带圆角）
     */
    int RECTANGLE = 1;
    /**
     * 三角形
     */
    int TRIANGLE = 2;
    /**
     * 心形 ❤️
     */
    int HEART = 3;
}
