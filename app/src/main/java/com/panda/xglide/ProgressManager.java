package com.panda.xglide;

import com.panda.xglide.https.SSLContext;
import com.panda.xglide.https.SafeHostVerifier;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 进度的管理器
 *
 * Created by panda on 2018/3/9.
 */
public class ProgressManager {
    private static List<WeakReference<OnProgressListener>> progressListeners =
            Collections.synchronizedList(new ArrayList<WeakReference<OnProgressListener>>());

    private static OkHttpClient okHttpClient;

    private ProgressManager() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLContext.createSSLSocketFactory())
                    .hostnameVerifier(new SafeHostVerifier(new String[]{".com"}))
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response.newBuilder()
                                    .body(new ProgressResponseBody(request.url().toString(), response.body(), listeners))
                                    .build();
                        }
                    }).build();
        }
        return okHttpClient;
    }

    private static final OnProgressListener listeners = new OnProgressListener() {
        @Override
        public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone) {
            if (progressListeners == null || progressListeners.size() == 0) return;

            for (int i = 0; i < progressListeners.size(); i++) {
                WeakReference<OnProgressListener> listener = progressListeners.get(i);
                OnProgressListener progressListener = listener.get();
                if (progressListener == null) {
                    progressListeners.remove(i);
                } else {
                    progressListener.onProgress(imageUrl, bytesRead, totalBytes, isDone);
                }
            }
        }
    };

    public static void addProgressListener(OnProgressListener progressListener) {
        if (progressListener == null) return;

        if (findProgressListener(progressListener) == null) {
            progressListeners.add(new WeakReference<>(progressListener));
        }
    }

    public static void removeProgressListener(OnProgressListener progressListener) {
        if (progressListener == null) return;

        WeakReference<OnProgressListener> listener = findProgressListener(progressListener);
        if (listener != null) {
            progressListeners.remove(listener);
        }
    }

    private static WeakReference<OnProgressListener> findProgressListener(OnProgressListener listener) {
        if (listener == null) return null;
        if (progressListeners == null || progressListeners.size() == 0) return null;

        for (int i = 0; i < progressListeners.size(); i++) {
            WeakReference<OnProgressListener> progressListener = progressListeners.get(i);
            if (progressListener.get() == listener) {
                return progressListener;
            }
        }
        return null;
    }
}
