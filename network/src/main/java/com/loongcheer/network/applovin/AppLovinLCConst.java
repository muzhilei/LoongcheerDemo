package com.loongcheer.network.applovin;

import com.applovin.sdk.AppLovinSdk;

public class AppLovinLCConst {
    public static final int NETWORK_FIRM_ID = 5;

    public static String getNetworkVersion() {
        try {
            return AppLovinSdk.VERSION;
        } catch (Throwable e) {

        }
        return "";
    }
}
