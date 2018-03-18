package com.panda.xglide;

/**
 * 图片加载的进度监听，只针对网络图片的加载
 *
 * Created by panda on 2018/3/9.
 */
public interface OnProgressListener {
    /**
     * 进度
     *
     * @param imageUrl 图片的地址
     * @param bytesRead 已经读的数据量
     * @param totalBytes 总的数据量
     * @param isDone 是否完成
     */
    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone);
}
