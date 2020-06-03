package com.loongcheer.network.facebook;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;

public class FacebookLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private static final String TAG = FacebookLCRewardedVideoAdapter.class.getSimpleName();

    RewardedVideoAd rewardedVideoAd;
    String mUnitid;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;
    String mPayload;


    /***
     * load ad
     */
    private void startLoad(final Context activity) {

        final RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(FacebookLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, error.getErrorCode() + "", "" + error.getErrorMessage()));
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onReworkVideoAdLoaded(FacebookLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdClicked(FacebookLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoStart(FacebookLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                // Call method to give reward
                // giveReward();
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoEnd(FacebookLCRewardedVideoAdapter.this);
                }

            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdClose(FacebookLCRewardedVideoAdapter.this);
                }

            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                rewardedVideoAd = new RewardedVideoAd(activity, mUnitid);
                RewardedVideoAd.RewardedVideoAdLoadConfigBuilder adConfig = rewardedVideoAd
                        .buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .withFailOnCacheFailureEnabled(true)
                        .withRVChainEnabled(true);

                rewardedVideoAd.loadAd(adConfig.build());
            }
        }).start();
    }


    @Override
    public void loadRewardVideoAd(Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {

        mLoadResultListener = customRewardVideoListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(activity);
        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }
        mUnitid = placementId;
        FacebookLCInitManager.getInstance().initSDK(activity.getApplicationContext(),unitGroupInfo);
        startLoad(activity);
    }

    @Override
    public void show(Context activity) {
        if (check() && isAdReady()) {
            rewardedVideoAd.show();
        }
    }

    private boolean check() {
        if (rewardedVideoAd == null) {
            return false;
        }
        return true;
    }

    @Override
    public void onResume(Context activity) {

    }

    @Override
    public void onPause(Context activity) {

    }

    @Override
    public boolean isAdReady() {
        if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded()) {
            return false;
        }
        if (rewardedVideoAd.isAdInvalidated()) {
            return false;
        }
        return true;
    }
    @Override
    public void clean() {
        if (check()) {
            rewardedVideoAd.destroy();
        }
    }

    @Override
    public String getSDKVersion() {
        return FacebookLCConst.getNetworkVersion();
    }

    @Override
    public String getNetworkName() {
        return FacebookLCInitManager.getInstance().getNetworkName();
    }
}
