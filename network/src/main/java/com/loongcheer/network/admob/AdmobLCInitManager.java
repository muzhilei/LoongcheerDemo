package com.loongcheer.network.admob;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.internal.ClientApi;
import com.google.android.gms.ads.internal.zzl;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.gass.AdShieldVm;
import com.google.android.gms.internal.ads.zzdpt;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.base.AdvertisingIdClient;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmobLCInitManager extends LCInitMediation {


    private static final String TAG = AdmobLCInitManager.class.getSimpleName();
    private boolean mIsInit;
    private static AdmobLCInitManager sInstance;

    private Map<String, Object> mOfferMap;

    private AdmobLCInitManager() {

    }

    public static AdmobLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new AdmobLCInitManager();
        }
        return sInstance;
    }


    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        try {
            if (!mIsInit) {
                MobileAds.initialize(context,info.getAppId());
                mIsInit = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Bundle getRequestBundle(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "0");

        ConsentStatus consentStatus = ConsentInformation.getInstance(context).getConsentStatus();
        switch (consentStatus) {
            case UNKNOWN:
                break;
            case PERSONALIZED:
                break;
            case NON_PERSONALIZED:
                bundle.putString("npa", "1");
                break;
        }
        return bundle;
    }

    @Override
    public String getNetworkName() {
        return "admob";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.google.android.gms.ads.MobileAds";
    }

    @Override
    public Map<String, Boolean> getPluginClassStatus() {
        HashMap<String, Boolean> pluginMap = new HashMap<>();
        pluginMap.put("conscent-*.jar", false);

        pluginMap.put("play-services-ads-*.aar", false);
        pluginMap.put("play-services-ads-base-*.aar", false);
        pluginMap.put("play-services-ads-identifier-*.aar", false);
        pluginMap.put("play-services-basement-*.aar", false);
        pluginMap.put("play-services-gass-*.aar", false);
        pluginMap.put("play-services-measurement-base-*.aar", false);
        pluginMap.put("play-services-measurement-sdk-api-*.aar", false);

        Class clazz;
        try {
            clazz = ConsentInformation.class;
            pluginMap.put("conscent-*.jar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = ClientApi.class;
            pluginMap.put("play-services-ads-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = zzdpt.class;
            pluginMap.put("play-services-ads-base-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = AdvertisingIdClient.class;
            pluginMap.put("play-services-ads-identifier-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = GoogleSignatureVerifier.class;
            pluginMap.put("play-services-basement-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = AdShieldVm.class;
            pluginMap.put("play-services-gass-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = zzl.class;
            pluginMap.put("play-services-measurement-base-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = AppMeasurementSdk.class;
            pluginMap.put("play-services-measurement-sdk-api-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return pluginMap;
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.google.android.gms.ads.AdActivity");
        return list;
    }

    @Override
    public List getProviderStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.google.android.gms.ads.MobileAdsInitProvider");
        return list;
    }

}
