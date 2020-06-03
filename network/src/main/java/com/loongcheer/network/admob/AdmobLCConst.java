package com.loongcheer.network.admob;

import com.google.android.gms.ads.MobileAds;

public class AdmobLCConst {

    public static final int NETWORK_FIRM_ID = 2;

    public static String getNetworkVersion() {
        try {
            return MobileAds.getVersionString();
        } catch (Throwable e) {

        }
        return "";
    }
}
