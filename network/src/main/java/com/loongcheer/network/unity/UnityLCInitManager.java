package com.loongcheer.network.unity;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UnityLCInitManager extends LCInitMediation {

    private static final String TAG = UnityLCInitManager.class.getSimpleName();
    private String mGameId;
    private static UnityLCInitManager sIntance;
    private ConcurrentHashMap<String, Object> mLoadResultAdapterMap = new ConcurrentHashMap<>();

    private IUnityAdsListener iUnityAdsListener = new IUnityAdsListener() {
        @Override
        public void onUnityAdsReady(String placementId) {
            Object adapter = mLoadResultAdapterMap.get(placementId);
            try {
                if (adapter instanceof UnityLCInterstitialAdapter) {
                    ((UnityLCInterstitialAdapter) adapter).notifyLoaded(placementId);
                }
            } catch (Throwable e) {

            }

            try {
                if (adapter instanceof UnityLCRewardedVideoAdapter) {
                    ((UnityLCRewardedVideoAdapter) adapter).notifyLoaded(placementId);
                }
            } catch (Throwable e) {

            }
            removeLoadResultAdapter(placementId);
        }

        @Override
        public void onUnityAdsStart(String placementId) {
            Object adapter = mLoadResultAdapterMap.get(placementId);
            try {
                if (adapter instanceof UnityLCInterstitialAdapter) {
                    ((UnityLCInterstitialAdapter) adapter).notifyStart(placementId);
                }
            } catch (Throwable e) {

            }

            try {
                if (adapter instanceof UnityLCRewardedVideoAdapter) {
                    ((UnityLCRewardedVideoAdapter) adapter).notifyStart(placementId);
                }
            } catch (Throwable e) {

            }
            removeLoadResultAdapter(placementId);
        }

        @Override
        public void onUnityAdsFinish(String placementId, UnityAds.FinishState finishState) {
            Object adapter = mLoadResultAdapterMap.get(placementId);
            try {
                if (adapter instanceof UnityLCInterstitialAdapter) {
                    ((UnityLCInterstitialAdapter) adapter).notifyFinish(placementId);
                }
            } catch (Throwable e) {

            }

            try {
                if (adapter instanceof UnityLCRewardedVideoAdapter) {
                    ((UnityLCRewardedVideoAdapter) adapter).notifyFinish(placementId);
                }
            } catch (Throwable e) {

            }
            removeLoadResultAdapter(placementId);
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

            for (Object adapter : mLoadResultAdapterMap.values()) {
                try {
                    if (adapter instanceof UnityLCInterstitialAdapter) {
                        ((UnityLCInterstitialAdapter) adapter).notifyLoadFail(unityAdsError.name(), s);
                    }
                } catch (Throwable e) {

                }
                try {
                    if (adapter instanceof UnityLCRewardedVideoAdapter) {
                        ((UnityLCRewardedVideoAdapter) adapter).notifyLoadFail(unityAdsError.name(), s);
                    }
                } catch (Throwable e) {

                }
            }
            mLoadResultAdapterMap.clear();
        }
    };

    protected synchronized void putLoadResultAdapter(String placementId, final Object adapter) {
        mLoadResultAdapterMap.put(placementId, adapter);
    }

    protected synchronized void removeLoadResultAdapter(String placementId) {
        mLoadResultAdapterMap.remove(placementId);
    }

    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        if (!(context instanceof Activity)) {
            return;
        }

        String game_id = info.gameId;
        if (!TextUtils.isEmpty(game_id)) {
            if (TextUtils.isEmpty(mGameId) || !TextUtils.equals(mGameId, game_id)) {
                UnityAds.initialize((Activity) context,game_id);
//                UnityMonetization.initialize(((Activity) context), game_id, mListener);
                mGameId = game_id;
            }
            UnityAds.addListener(iUnityAdsListener);
//            UnityMonetization.setListener(mListener);
        }
    }

    private UnityLCInitManager() {

    }


    public static UnityLCInitManager getInstance() {
        if (sIntance == null) {
            sIntance = new UnityLCInitManager();
        }
        return sIntance;
    }



    @Override
    public String getNetworkName() {
        return "unity";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.unity3d.services.monetization.UnityMonetization";
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.unity3d.services.ads.adunit.AdUnitActivity");
        list.add("com.unity3d.services.ads.adunit.AdUnitTransparentActivity");
        list.add("com.unity3d.services.ads.adunit.AdUnitTransparentSoftwareActivity");
        list.add("com.unity3d.services.ads.adunit.AdUnitSoftwareActivity");
        return list;
    }

}
