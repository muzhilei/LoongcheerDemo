package com.loongcheer.core.common;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.CommonDeviceUtil;
import com.loongcheer.core.common.utils.CustomAdapterFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonMediationManager {

    protected Context mApplcationContext;
    protected WeakReference<Context> mActivityRef;
    protected HashMap<String, Runnable> mOverTimeRunnableMap; //Timeout Runnable of UnitGroup
    protected HashMap<LoongCheerBaseAdapter, Boolean> mUnitGroupReturnStatus; //Return status of UnitGroup
    protected HashMap<String, Runnable> mAdDataOverTimeRunnableMap; //AdData Timeout Runnable of UnitGroup

    protected HashMap<LoongCheerBaseAdapter, Boolean> mOverLoadMap; //Short Timeout status of UnitGroup
    protected boolean mHasReturnAdStatus; //Return staus
    protected int mLoadCount; //Request Count

    protected boolean mIsRelease; //Release status

    protected HashMap<String, Long> mUnitGroupLoadTimeMap;

    protected boolean mHasFinishLoad; //Finish loading status
    protected boolean mHasCancelCacheOffer; //Cancel Cached offer status

    protected String mUserId = "";
    protected String mCustomData = "";

    protected boolean mIsRefresh;

    private long mStartLoadTime;

    private PlaceStrategy.UnitGroupInfo mInfo;

    protected CommonMediationManager(Context context) {
        mActivityRef = new WeakReference<>(context);
        mApplcationContext = SDKContext.getInstance().getContext();
    }

    protected void loadAd(String placementId, RealmModel realmModel, PlaceStrategy.UnitGroupInfo info, CountDownTimer timer) {

        mInfo = info;

        loadNetworkAd(realmModel,placementId,timer);
    }


    private void loadNetworkAd(final RealmModel strategy, final String mPlacementId,CountDownTimer timer) {

        LoongCheerBaseAdapter adapter = CustomAdapterFactory.createAdapter(mInfo);
        if (adapter == null) {
            AdError adError = ErrorCode.getErrorCode(ErrorCode.adapterNotExistError, "", mInfo.adapterClassName + " does not exit!");
        }

        startLoadAd(adapter,mInfo,mPlacementId,timer);
//        startLoadAd(adapter, unitGroupInfo);
        }



    public abstract void onDevelopLoaded();

    public abstract void onDeveloLoadFail(AdError adError);

    public abstract void startLoadAd(LoongCheerBaseAdapter baseAdapter, PlaceStrategy.UnitGroupInfo unitGroupInfo,String placementId,CountDownTimer timer);

}
