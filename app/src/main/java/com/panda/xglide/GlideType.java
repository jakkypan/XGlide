package com.panda.xglide;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by panda on 2018/3/8.
 */
@IntDef({GlideType.DRAWABLE, GlideType.BITMAP, GlideType.GIF, GlideType.FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface GlideType {
    /**
     * drawable
     */
    int DRAWABLE = 0;
    /**
     * bitmap
     */
    int BITMAP = 1;
    /**
     * gif图
     */
    int GIF = 2;
    /**
     * 文件对象
     */
    int FILE = 3;
}
