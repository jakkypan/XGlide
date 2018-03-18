package com.panda.xglide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 实际的glide的图片加载
 *
 * Created by panda on 2018/3/7.
 */
public class GlideManager {
    private WeakReference<GlideImageView> mImageView;
    private GlideRequest<?> glideType;
    private SimpleTarget simpleTarget;
    /**
     * 图片加载过程中的监听
     */
    private RequestListener requestListener;

    public GlideManager(GlideImageView imageView) {
        this.mImageView = new WeakReference<>(imageView);
    }

    public GlideImageView getImageView() {
        if (mImageView != null) {
            return mImageView.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public void load() {
        final GlideImageView glImageView = getImageView();
        if (glImageView == null) {
            return;
        }
        GlideRequests glideRequests = GlideApp.with(getContext());
        switch (glImageView.getGlideType()) {
            case GlideType.DRAWABLE:
                glideType = glideRequests.asDrawable();
                simpleTarget = new DrawableSimpleTarget(glImageView);
                requestListener = new DrawableSimpleTarget.DrawableRequestListener(glImageView);
                break;
            case GlideType.BITMAP:
                glideType = glideRequests.asBitmap();
                simpleTarget = new BitmapSimpleTarget(glImageView);
                requestListener = new BitmapSimpleTarget.BitmapRequestListener(glImageView);
                break;
            case GlideType.GIF:
                glideType = glideRequests.asGif();
                simpleTarget = new GifSimpleTarget(glImageView);
                requestListener = new GifSimpleTarget.GifRequestListener(glImageView.getGifCount(), glImageView);
                break;
            case GlideType.FILE:
                glideType = glideRequests.asFile();
                simpleTarget = new FileSimpleTarget(glImageView);
                requestListener = new FileSimpleTarget.FileRequestListener(glImageView);
                break;
            default:
                glideType = glideRequests.asDrawable();
                simpleTarget = new BitmapSimpleTarget(glImageView);
                break;
        }
        DiskCacheStrategy strategy = DiskCacheStrategy.ALL;
        boolean memoryCache = false;
        if (!glImageView.isCache()) {
            strategy = DiskCacheStrategy.NONE;
            memoryCache = true;
        }
        glideType.load(glImageView.getImageUrl())
                .placeholder(glImageView.getPlaceHolder())
                .error(glImageView.getError())
                .fallback(glImageView.getFallback())
                .override(glImageView.getResize())
                .diskCacheStrategy(strategy)
                .skipMemoryCache(memoryCache)
                .listener(requestListener)
                .into(simpleTarget);
    }

    /**
     * 取消，一般情况下也不需要调用，随context的生命期自动释放
     */
    public void cancel() {
        GlideImageView glImageView = getImageView();
        if (glImageView == null) {
            return;
        }
        Glide.with(getContext()).clear(glImageView);
    }

    private static class BitmapSimpleTarget extends SimpleTarget<Bitmap> {
        private WeakReference<GlideImageView> imageView;

        public BitmapSimpleTarget(GlideImageView imageView) {
            this.imageView = new WeakReference<>(imageView);
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            if (this.imageView.get() != null) {
                imageView.get().setImageBitmap(resource);
            }
        }

        private static class BitmapRequestListener implements RequestListener<Bitmap> {
            private WeakReference<GlideImageView> imageView;

            public BitmapRequestListener(GlideImageView imageView) {
                this.imageView = new WeakReference<>(imageView);
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }
        }
    }

    private static class DrawableSimpleTarget extends SimpleTarget<Drawable> {
        private WeakReference<GlideImageView> imageView;

        public DrawableSimpleTarget(GlideImageView imageView) {
            this.imageView = new WeakReference<>(imageView);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            if (this.imageView.get() != null) {
                imageView.get().setImageDrawable(resource);
            }
        }

        private static class DrawableRequestListener implements RequestListener<Drawable> {
            private WeakReference<GlideImageView> imageView;

            public DrawableRequestListener(GlideImageView imageView) {
                this.imageView = new WeakReference<>(imageView);
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }
        }
    }

    private static class FileSimpleTarget extends SimpleTarget<File> {
        private WeakReference<GlideImageView> imageView;

        public FileSimpleTarget(GlideImageView imageView) {
            this.imageView = new WeakReference<>(imageView);
        }

        @Override
        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {

        }

        private static class FileRequestListener implements RequestListener<File> {
            private WeakReference<GlideImageView> imageView;


            public FileRequestListener(GlideImageView imageView) {
                this.imageView = new WeakReference<>(imageView);
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }
        }
    }

    private static class GifSimpleTarget extends SimpleTarget<GifDrawable> {
        private WeakReference<GlideImageView> imageView;

        public GifSimpleTarget(GlideImageView imageView) {
            this.imageView = new WeakReference<>(imageView);
        }

        @Override
        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
            if (this.imageView.get() != null) {
                imageView.get().setImageDrawable(resource);
                resource.start();
            }
        }

        /**
         * 监听gif的播放和次数的控制
         */
        private static class GifRequestListener implements RequestListener<GifDrawable> {
            private int loopCount = -1;
            private WeakReference<GlideImageView> imageView;

            public GifRequestListener(int loopCount, GlideImageView imageView) {
                this.loopCount = loopCount;
                this.imageView = new WeakReference<>(imageView);
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                if (imageView.get() != null) {
                    ProgressManager.removeProgressListener(imageView.get().getProgressListener());
                }

//                int frameCount = resource.getFrameCount();
                /**
                 * 4.0无法直接获取每帧的时间，暂时也不弄了
                 *
                 * 如果设置了loop的次数，会在loop完之后给出统一的回调
                 */
                if (loopCount >= 0) {
                    resource.setLoopCount(loopCount);

                    if (imageView.get() != null && imageView.get().getGifPlayFinishedListener() != null) {
                        Single.create(new SingleOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(SingleEmitter<Boolean> e) throws Exception {
                                /**
                                 * 阻塞住，这里需要注意，start之后，isrunning不一定为true，表示还没有读到数据帧，继续等待
                                 */
                                while (resource.isRunning() || resource.getFrameIndex() < 0) { }
                                e.onSuccess(true);
                            }
                        }).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Boolean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        imageView.get().getGifPlayFinishedListener().gifStopped();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }

                }
                return false;
            }
        }
    }
}
