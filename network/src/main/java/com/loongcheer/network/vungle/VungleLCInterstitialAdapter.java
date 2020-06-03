package com.loongcheer.network.vungle;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;
import com.vungle.warren.AdConfig;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

public class VungleLCInterstitialAdapter extends CustomInterstitialAdapter {

    private final String TAG = VungleLCInterstitialAdapter.class.getSimpleName();
    String mPlacementId;
    AdConfig mAdConfig;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    private final LoadAdCallback loadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(String s) {
            if (mLoadResultListener != null) {
                DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                mTimer.cancel();
                mLoadResultListener.onInterstitialAdLoaded(VungleLCInterstitialAdapter.this);
            }
        }

        @Override
        public void onError(String s, VungleException e) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(VungleLCInterstitialAdapter.this
                        , ErrorCode.getErrorCode(ErrorCode.noADError, e.getExceptionCode()+"", e.getLocalizedMessage()));
            }
        }

    };


    private final PlayAdCallback vungleDefaultListener = new PlayAdCallback() {

        @Override
        public void onAdEnd(String placementReferenceId, boolean wasSuccessFulView, boolean wasCallToActionClicked) {
            // Called when user exits the ad and control is returned to your application
            // if wasSuccessfulView is true, the user watched the ad and could be rewarded
            // if wasCallToActionClicked is true, the user clicked the call to action button in the ad.
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdVideoEnd(VungleLCInterstitialAdapter.this);
                mLoadResultListener.onInterstitialAdClose(VungleLCInterstitialAdapter.this);
            }
        }

        @Override
        public void onError(String s, VungleException e) {
            // Called after playAd(placementId, adConfig) is unable to play the ad
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdVideoError(VungleLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.rewardedVideoPlayError, e.getExceptionCode()+"", e.getLocalizedMessage()));
            }
        }

        @Override
        public void onAdStart(String placementReferenceId) {
            // Called before playing an ad
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdShow(VungleLCInterstitialAdapter.this);
                mLoadResultListener.onInterstitialAdVideoStart(VungleLCInterstitialAdapter.this);
            }
        }

    };


    @Override
    public void loadInterstitialAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {
        mLoadResultListener = customInterstitialListener;

        String mAppId = unitGroupInfo.getAppId();
        mPlacementId = placementId;

        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(context);
        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mPlacementId)) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "vungle appid & placementId is empty."));
            }
            return;
        }

        mAdConfig = new AdConfig();
        VungleLCInitManager.getInstance().initSDK(context.getApplicationContext(), unitGroupInfo, new VungleLCInitManager.InitListener() {
            @Override
            public void onSuccess() {
                Vungle.loadAd(mPlacementId, loadAdCallback);
            }

            @Override
            public void onError(Throwable throwable) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(VungleLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", throwable.getMessage()));
                }
            }
        });

    }

    @Override
    public void show(Context context) {
        if (Vungle.canPlayAd(mPlacementId)) {
            // Play a Placement ad with Placement ID, you can pass AdConfig to customize your ad
            Vungle.playAd(mPlacementId, mAdConfig, vungleDefaultListener);
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

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
