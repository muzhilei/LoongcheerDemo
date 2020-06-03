package com.loongcheer.core.api;

import android.content.Context;


import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LCInitMediation {


    public abstract void initSDK(Context context, PlaceStrategy.UnitGroupInfo info);


    public String getNetworkName() {
        return "";
    }

    public String getNetworkSDKClass() {
        return "";
    }

    public Map<String, Boolean> getPluginClassStatus() {
        return null;
    }

    public List getActivityStatus() {
        return null;
    }

    public List getServiceStatus() {
        return null;
    }

    public List getProviderStatus() {
        return null;
    }
}
