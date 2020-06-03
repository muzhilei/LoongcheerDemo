package com.loongcheer.network.unity;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.monetization.UnityMonetization;
import com.unity3d.services.monetization.placementcontent.core.PlacementContent;

public class UnityLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private static final String TAG = UnityLCRewardedVideoAdapter.class.getSimpleName();

    UnityLCRewardedVideoAdapter mUnityAdRewardVideoSetting;
    String placement_id = "";
    Activity mActivity;
    CountDownTimer mTimer;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;

    @Override
    public void loadRewardVideoAd(Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
        mLoadResultListener = customRewardVideoListener;
        mTimer = timer;
        mUnitGroupInfo = unitGroupInfo;
        mActivity = SDKContext.getInstance().getActivity();
        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        if (mActivity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        if (unitGroupInfo == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "This placement's params in server is null!"));
            }
            return;
        } else {
            String game_id = unitGroupInfo.gameId;
            placement_id = placementId;

            if (TextUtils.isEmpty(game_id) || TextUtils.isEmpty(placement_id)) {
                log(TAG, "game_id, placement_id is empty!");
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "unityads game_id, placement_id is empty!"));
                }
                return;
            }
        }

        UnityAds.PlacementState placementContent = UnityAds.getPlacementState(placement_id);
        if (placementContent != null && placementContent.equals(UnityAds.PlacementState.READY)) {
            if (mLoadResultListener != null) {
                DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                mTimer.cancel();
                mLoadResultListener.onReworkVideoAdLoaded(UnityLCRewardedVideoAdapter.this);
            }
        } else {
            UnityLCInitManager.getInstance().putLoadResultAdapter(placement_id, this);
            UnityLCInitManager.getInstance().initSDK(activity, unitGroupInfo);
        }

    }

    @Override
    public void show(Context activity) {
        if (UnityAds.isReady (placement_id)) {
            UnityAds.show (mActivity,placement_id);
        }
    }

    @Override
    public void onResume(Context activity) {

    }

    @Override
    public void onPause(Context activity) {

    }


    public void notifyLoaded(String placementId) {
        if (mLoadResultListener != null && placement_id.equals(placementId)) {
            mLoadResultListener.onReworkVideoAdLoaded(UnityLCRewardedVideoAdapter.this);
        }
    }

    public void notifyLoadFail(String code, String msg) {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdLoadFail(UnityLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, code, msg));
        }
    }


    public void notifyStart(String placementId){
        if (placementId.equals(placement_id)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdVideoStart(UnityLCRewardedVideoAdapter.this);
            }
        }
    }

    public void notifyFinish(String placementId){
        if (placementId.equals(placement_id)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdVideoEnd(UnityLCRewardedVideoAdapter.this);
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
