package com.loongcheer.network.unity;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.monetization.placementcontent.core.PlacementContent;

public class UnityLCInterstitialAdapter extends CustomInterstitialAdapter {

    private static final String TAG = UnityLCInterstitialAdapter.class.getSimpleName();

    String placement_id = "";
    Activity activity;
    CountDownTimer mTimer;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    @Override
    public void loadInterstitialAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {
        mLoadResultListener = customInterstitialListener;
        mTimer = timer;
        activity = SDKContext.getInstance().getActivity();
        mUnitGroupInfo = unitGroupInfo;
        if (context == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "context is null."));
            }
            return;
        }

        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "context is null."));
            }
            return;
        }

        if (unitGroupInfo == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "This placement's params in server is null!"));
            }
            return;
        } else {
            String game_id = unitGroupInfo.gameId;
            placement_id = placementId;

            if (TextUtils.isEmpty(game_id) || TextUtils.isEmpty(placement_id)) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "unityads game_id, placement_id is empty!"));
                }
                return;
            }
        }


        UnityAds.PlacementState placementContent = UnityAds.getPlacementState(placement_id);
        if (placementContent != null && placementContent.equals(UnityAds.PlacementState.READY)) {
            if (mLoadResultListener != null) {
                DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                mTimer.cancel();
                mLoadResultListener.onInterstitialAdLoaded(this);
            }
        } else {
            UnityLCInitManager.getInstance().putLoadResultAdapter(placement_id, this);
            UnityLCInitManager.getInstance().initSDK(activity, unitGroupInfo);
        }

    }

    @Override
    public void show(Context context) {
        if (UnityAds.isReady(placement_id)){
            UnityAds.show(activity,placement_id);
        }

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public void notifyLoaded(String placementId) {
        if (placementId.equals(placement_id)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoaded(UnityLCInterstitialAdapter.this);
            }
        }
    }

    public void notifyLoadFail(String code, String msg) {
        if (mLoadResultListener != null) {
            mLoadResultListener.onInterstitialAdLoadFail(UnityLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, code, msg));
        }
    }

    public void notifyStart(String placementId){
        if (placementId.equals(placement_id)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdVideoStart(UnityLCInterstitialAdapter.this);
            }
        }
    }

    public void notifyFinish(String placementId){
        if (placementId.equals(placement_id)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdVideoEnd(UnityLCInterstitialAdapter.this);
            }
        }
    }

    @Override
    public boolean isAdReady() {
        UnityAds.PlacementState placementState = UnityAds.getPlacementState(placement_id);
        return placementState != null && placementState.equals(UnityAds.PlacementState.READY);
    }

    @Override
    public String getSDKVersion() {
        return UnityLCConst.getNetworkVersion();
    }

    @Override
    public void clean() {

    }

    @Override
    public String getNetworkName() {
        return UnityLCInitManager.getInstance().getNetworkName();
    }
}
