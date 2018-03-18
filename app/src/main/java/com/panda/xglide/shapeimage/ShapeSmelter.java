package com.panda.xglide.shapeimage;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by panda on 2018/3/6.
 */
public class ShapeSmelter {
    private static Canvas mBitmapCanvas;
    private static PorterDuffXfermode mXfermode;
    private static Paint mBitmapPaint;
    private Bitmap mBitmap;

    public ShapeSmelter() {
        mBitmapCanvas = new Canvas();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setColor(Color.BLACK);
    }

    public void doSmelter(ShapeImageView view, ImageShape shape) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (width == 0 || height == 0) {
            return;
        }
        mBitmap = createBitmap(mBitmap, width, height);
        if (mBitmap == null) {
            return;
        }
        mBitmapCanvas.setBitmap(mBitmap);
//        shape.makeShapeByPorterDuff(view, mBitmapCanvas, mBitmapPaint);
    }

    private Bitmap createBitmap(Bitmap bitmap, int width, int height) {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            return createBitmapKitkat(bitmap, width, height);
        }
        if (bitmap == null) {
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                return null;
            }
        } else {
            if (bitmap.getWidth() != width || bitmap.getHeight() != height) {
                bitmap.recycle();
                try {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {
                    return null;
                } finally {
                    System.gc();
                }
            } else {
                bitmap.eraseColor(Color.TRANSPARENT);
            }
        }
        return bitmap;
    }

    @TargetApi(19)
    private Bitmap createBitmapKitkat(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            try {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                return null;
            }
        } else {
            if (bitmap.getWidth() != width || bitmap.getHeight() != height) {
                bitmap.eraseColor(Color.TRANSPARENT);
                try {
                    bitmap.reconfigure(width, height, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {
                    bitmap.recycle();
                    return null;
                } finally {
                    System.gc();
                }
            } else {
                bitmap.eraseColor(Color.TRANSPARENT);
            }
        }
        return bitmap;
    }
}
