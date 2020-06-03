package com.loongcheer.network.vungle;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;
import com.vungle.warren.AdConfig;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

public class VungleLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private final String TAG = VungleLCRewardedVideoAdapter.class.getSimpleName();
    String mPlacementId;
//    VungleRewardedVideoSetting mSetting;
    AdConfig mAdConfig;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    private final LoadAdCallback loadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(String s) {
            if (mLoadResultListener != null) {
                DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                mTimer.cancel();
                mLoadResultListener.onReworkVideoAdLoaded(VungleLCRewardedVideoAdapter.this);
            }
        }

        @Override
        public void onError(String s, VungleException e) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(VungleLCRewardedVideoAdapter.this
                        , ErrorCode.getErrorCode(ErrorCode.noADError, e.getExceptionCode()+"", e.getLocalizedMessage()));
            }
        }

    };

    private final PlayAdCallback vungleDefaultListener = new PlayAdCallback() {

        @Override
        public void onAdEnd( String placementReferenceId, boolean wasSuccessFulView, boolean wasCallToActionClicked) {
            // Called when user exits the ad and control is returned to your application
            // if wasSuccessfulView is true, the user watched the ad and could be rewarded
            // if wasCallToActionClicked is true, the user clicked the call to action button in the ad.

            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdVideoEnd(VungleLCRewardedVideoAdapter.this);
//                if (wasSuccessFulView) {
//                    mLoadResultListener.onReward(VungleLCRewardedVideoAdapter.this);
//                }
                mLoadResultListener.onReworkVideoAdClose(VungleLCRewardedVideoAdapter.this);

            }

        }

        @Override
        public void onError(String s, VungleException e) {
            // Called after playAd(placementId, adConfig) is unable to play the ad
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(VungleLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.rewardedVideoPlayError, e.getExceptionCode()+"", e.getLocalizedMessage()));
            }
        }

        @Override
        public void onAdStart( String placementReferenceId) {
            // Called before playing an ad
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdVideoStart(VungleLCRewardedVideoAdapter.this);
            }
        }
    };

    @Override
    public void loadRewardVideoAd(Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
        String mAppId = unitGroupInfo.getAppId();
        mPlacementId = placementId;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(activity);
        mLoadResultListener = customRewardVideoListener;

        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mPlacementId)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", " appid & placementId is empty."));
            }
            return;
        }

        mAdConfig = new AdConfig();

//        if (mSetting != null) {
//            mAdConfig.setAutoRotate(mSetting.getOrientation() == 1);
//            mAdConfig.setMuted(mSetting.isSoundEnable());
//            mAdConfig.setBackButtonImmediatelyEnabled(mSetting.isBackButtonImmediatelyEnable());
//        }

        VungleLCInitManager.getInstance().initSDK(activity.getApplicationContext(), unitGroupInfo, new VungleLCInitManager.InitListener() {
            @Override
            public void onSuccess() {
                Vungle.loadAd(mPlacementId, loadAdCallback);
            }

            @Override
            public void onError(Throwable throwable) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(VungleLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", throwable.toString()));
                }
            }
        });
    }

    @Override
    public void show(Context activity) {
        if (Vungle.canPlayAd(mPlacementId)) {
            // Play a Placement ad with Placement ID, you can pass AdConfig to customize your ad
            Vungle.setIncentivizedFields(mUserId, "", "", "", "");
            Vungle.playAd(mPlacementId, mAdConfig, vungleDefaultListener);
        }
    }

    @Override
    public void onResume(Context activity) {

    }

    @Override
    public void onPause(Context activity) {

    }

    @Override
    public boolean isAdReady() {
        return Vungle.canPlayAd(mPlacementId);
    }

    @Override
    public String getSDKVersion() {
        return "";
    }

    @Override
    public void clean() {

    }

    @Override
    public String getNetworkName() {
        return VungleLCInitManager.getInstance().getNetworkName();
    }
}
