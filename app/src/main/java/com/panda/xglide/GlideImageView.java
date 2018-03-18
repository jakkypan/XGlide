package com.panda.xglide;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.panda.xglide.shapeimage.ShapeImageView;

/**
 * Created by panda on 2018/3/7.
 */
public class GlideImageView extends ShapeImageView {
    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FILE_RESOURCE = "file://";

    private GlideManager glideManager;

    /**
     * 图片的地址
     */
    private Object mImageUrl;
    /**
     * 占位符
     */
    private int mPlaceHolder;
    /**
     * 出错占位符
     */
    private int mError;
    /**
     * V4新加的
     * 即不是失败，而是本来就没有时的缺省图片
     */
    private int mFallback;
    /**
     * resize图片的大小
     */
    private int mResize;
    /**
     * 图片的类型
     */
    private int mGlideType;
    /**
     * 图片的加载是否经过缓存预取
     */
    private boolean mCache = true;
    /**
     * 仅针对gif，控制gif的播放次数，默认跟随gif的格式设定
     */
    private int mGifCount = -1;
    /**
     * 针对gif播放暂停的回调通知，仅gif
     */
    private GifPlayFinishedListener gifPlayFinishedListener;
    private OnProgressListener progressListener;

    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        glideManager = new GlideManager(this);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.GlideView);
        mImageUrl = attr.getString(R.styleable.GlideView_glUrl);
        if (mImageUrl != null && mImageUrl.toString().startsWith("res/")) {
            int imageResId = attr.getResourceId(R.styleable.GlideView_glUrl, 0);
            mImageUrl = resId2Uri(imageResId).toString();
        }
        mPlaceHolder = attr.getResourceId(R.styleable.GlideView_glPlaceholder, 0);
        mError = attr.getResourceId(R.styleable.GlideView_glError, 0);
        mFallback = attr.getResourceId(R.styleable.GlideView_glFallback, 0);
        mResize = attr.getDimensionPixelSize(R.styleable.GlideView_glResize, 0);
        mGlideType = attr.getInt(R.styleable.GlideView_glGlideType, 0);
        mGifCount = attr.getInt(R.styleable.GlideView_glGifCount, -1);
        mCache = attr.getBoolean(R.styleable.GlideView_glCache, true);

        attr.recycle();
        load();
    }

    private Uri resId2Uri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + "/" + resourceId);
    }

    /**
     * 图片加载
     */
    public void load() {
        glideManager.load();
    }

    public Object getImageUrl() {
        return mImageUrl;
    }

    public GlideImageView setImageUrl(Object imageUrl) {
        this.mImageUrl = imageUrl;
        return this;
    }

    public int getPlaceHolder() {
        return mPlaceHolder;
    }

    public GlideImageView setPlaceHolder(int placeHolder) {
        this.mPlaceHolder = placeHolder;
        return this;
    }

    public int getError() {
        return mError;
    }

    public GlideImageView setError(int error) {
        this.mError = error;
        return this;
    }

    public int getFallback() {
        return mFallback;
    }

    public GlideImageView setFallback(int fallback) {
        this.mFallback = fallback;
        return this;
    }

    public int getResize() {
        return mResize;
    }

    public GlideImageView setResize(int resize) {
        this.mResize = resize;
        return this;
    }

    public int getGlideType() {
        return mGlideType;
    }

    public GlideImageView setGlideType(@GlideType int glideType) {
        this.mGlideType = glideType;
        return this;
    }

    public boolean isCache() {
        return mCache;
    }

    public GlideImageView setCache(boolean cache) {
        this.mCache = cache;
        return this;
    }

    public int getGifCount() {
        return mGifCount;
    }

    public GlideImageView setGifCount(int gifCount) {
        this.mGifCount = gifCount;
        return this;
    }

    public GifPlayFinishedListener getGifPlayFinishedListener() {
        return gifPlayFinishedListener;
    }

    public GlideImageView setGifPlayFinishedListener(GifPlayFinishedListener gifPlayFinishedListener) {
        this.gifPlayFinishedListener = gifPlayFinishedListener;
        return this;
    }

    public OnProgressListener getProgressListener() {
        return progressListener;
    }

    public GlideImageView setProgressListener(OnProgressListener progressListener) {
        this.progressListener = progressListener;
        if (mImageUrl != null && (mImageUrl.toString().startsWith("http://") || mImageUrl.toString().startsWith("https://"))) {
            ProgressManager.addProgressListener(this.progressListener);
        }
        return this;
    }
}
