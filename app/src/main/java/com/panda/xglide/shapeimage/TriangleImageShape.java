package com.panda.xglide.shapeimage;

import android.graphics.Path;

/**
 * 三角形
 *
 * <p/>
 *
 * 三角形绘制算法：是基于当前{@link ShapeImageView}的长宽的矩形作为裁剪的基础，得到"正"三角形，
 * 然后根据定义的角度进行旋转，产生不同旋转角度的三角形
 *
 * Created by panda on 2018/3/7.
 */
public class TriangleImageShape extends SpecialPathImageShape {
    @Override
    public void buildPath(Path path, int width, int height) {
        path.moveTo(width / 2, 0);
        path.lineTo(0,  height);
        path.lineTo(width, height);
        path.close();
    }
}
