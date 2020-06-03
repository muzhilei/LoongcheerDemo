package com.loongcheer.network.applovin;

import android.content.Context;
import android.text.TextUtils;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkSettings;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppLovinLCInitManager extends LCInitMediation {

    private static final String TAG = AppLovinLCInitManager.class.getSimpleName();
    private static AppLovinLCInitManager sInstance;

    private String mSdkKey;

    private AppLovinLCInitManager() {

    }

    public static AppLovinLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new AppLovinLCInitManager();
        }
        return sInstance;
    }

    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        String sdkkey = info.getSdkKey();

        if (!TextUtils.isEmpty(sdkkey)) {
            initSDK(context, sdkkey);
        }
    }


    public AppLovinSdk initSDK(Context context, String sdkKey) {

        if (TextUtils.isEmpty(mSdkKey) || !TextUtils.equals(mSdkKey, sdkKey)) {
            mSdkKey = sdkKey;
        }
        AppLovinSdk appLovinSdk = AppLovinSdk.getInstance(sdkKey, new AppLovinSdkSettings(), context);
        appLovinSdk.getSettings().setVerboseLogging(LCSDK.NETWORK_LOG_DEBUG);
        return appLovinSdk;
    }

    @Override
    public String getNetworkName() {
        return "applovin";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.applovin.sdk.AppLovinSdk";
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.applovin.adview.AppLovinInterstitialActivity");
        list.add("com.applovin.sdk.AppLovinWebViewActivity");
        list.add("com.applovin.mediation.MaxDebuggerActivity");
        list.add("com.applovin.mediation.MaxDebuggerDetailActivity");
        return list;
    }

    @Override
    public List getServiceStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.applovin.impl.sdk.utils.AppKilledService");
        return list;
    }
}
