package com.loongcheer.core.common.base;

import android.app.Activity;
import android.content.Context;

import com.loongcheer.core.common.strategy.PlaceStrategy;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

public abstract class LoongCheerBaseAdapter {

    private PlaceStrategy.UnitGroupInfo mUnitgroupInfo;
    boolean isRefresh;
    protected WeakReference<Activity> mActivityRef;


    public PlaceStrategy.UnitGroupInfo getmUnitgroupInfo() {
        return mUnitgroupInfo;
    }

    public void setmUnitgroupInfo(PlaceStrategy.UnitGroupInfo mUnitgroupInfo) {
        this.mUnitgroupInfo = mUnitgroupInfo;
    }

    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return this.isRefresh;
    }

    public void refreshActivityContext(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    @Deprecated
    protected void log(String tag, String msg) {
    }


    public abstract boolean isAdReady();

    public abstract String getSDKVersion();

    public abstract void clean();

    public abstract String getNetworkName();
}
