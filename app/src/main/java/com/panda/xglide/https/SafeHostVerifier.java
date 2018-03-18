package com.panda.xglide.https;

import android.util.Log;

import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https的host校验
 *
 * Created by panda on 2018/3/8.
 */
public class SafeHostVerifier implements HostnameVerifier {

    private String[] safeHosts;

    public SafeHostVerifier(String[] safeHosts) {
        this.safeHosts = safeHosts;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        if (safeHosts == null || safeHosts.length < 0) {
            return true;
        }

        for (String safeHost : safeHosts) {
            if (hostname.contains(safeHost)) {
                return true;
            }
        }

        Log.w("111", hostname + " not in safeHosts: " + toString() + " ");
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(safeHosts);
    }
}
