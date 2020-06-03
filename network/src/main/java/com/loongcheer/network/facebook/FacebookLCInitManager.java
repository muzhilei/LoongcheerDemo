package com.loongcheer.network.facebook;

import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacebookLCInitManager extends LCInitMediation {

    private static final String TAG = FacebookLCInitManager.class.getSimpleName();
    private boolean mIsInit;
    private static FacebookLCInitManager sInstance;


    public static FacebookLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new FacebookLCInitManager();
        }
        return sInstance;
    }

    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        try {
            if (!mIsInit) {
                AudienceNetworkAds.initialize(context.getApplicationContext());
                mIsInit = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNetworkName() {
        return "facebook";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.facebook.ads.AudienceNetworkAds";
    }

    @Override
    public Map<String, Boolean> getPluginClassStatus() {
        return super.getPluginClassStatus();
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.facebook.ads.AudienceNetworkActivity");
        list.add("com.facebook.ads.internal.ipc.RemoteANActivity");
        return list;
    }

    @Override
    public List getServiceStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.facebook.ads.internal.ipc.AdsProcessPriorityService");
        list.add("com.facebook.ads.internal.ipc.AdsMessengerService");
        return list;
    }

    @Override
    public List getProviderStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.facebook.ads.AudienceNetworkContentProvider");
        return list;
    }
}
