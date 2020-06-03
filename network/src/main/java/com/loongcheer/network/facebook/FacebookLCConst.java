package com.loongcheer.network.facebook;

import com.facebook.ads.internal.api.BuildConfigApi;

public class FacebookLCConst {
    public static final int NETWORK_FIRM_ID = 1;

    public static String getNetworkVersion() {
        try {
            return BuildConfigApi.getVersionName(null);
        } catch (Throwable e) {

        }
        return "";
    }
}
