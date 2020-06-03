package com.loongcheer.core.common;

import android.content.Context;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.task.TaskManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonAdManager {

    private static final String TAG = "CommonAdManager";

    private static HashMap<String, CommonAdManager> sAdMangerMap = new HashMap<>();

    public static CommonAdManager getInstance(String placementId) {
        return sAdMangerMap.get(placementId);
    }

    public static void addAdManager(String placementId, CommonAdManager adManager) {
        sAdMangerMap.put(placementId, adManager);
    }

    protected Context mApplicationContext;
    protected WeakReference<Context> mActivityRef;
    protected String mPlacementId;

    public CommonAdManager(Context context, String placementId) {
        mActivityRef = new WeakReference<>(context);
        mApplicationContext = context.getApplicationContext();
        mPlacementId = placementId;

//        mHistoryMediationManager = new HashMap<>(5);

        if (SDKContext.getInstance().getContext() == null) {
            SDKContext.getInstance().setContext(mApplicationContext);
        }
    }


    public Context getContext() {
        return mActivityRef.get();
    }

    /**
     * Refresh Context
     *
     * @param context
     */
    public void refreshContext(Context context) {
        mActivityRef = new WeakReference<>(context);
    }


    /**
     * Request Placement Setting
     *
     * @param context
     * @param mPlacementId
     * @param placementCallback
     */
    public void loadStragety(final Context context, final String format, final String mPlacementId, final boolean isRefresh, final PlacementCallback placementCallback) {
        TaskManager.getInstance().run_proxy(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
//                    SDKContext.getInstance().checkAppStrategy(context,SDKContext.getInstance().getAppId(),SDKContext.getInstance().getAppKey());

                }

            }
        });
    }

    public interface PlacementCallback {

        void onSuccess(String placementId, String requestId, PlaceStrategy placeStrategy, List<PlaceStrategy.UnitGroupInfo> unitGroupInfoList);

        void onAdLoaded(String placementId, String requestId);

        void onLoadError(String placementId, String requestId, AdError adError);

    }
}
