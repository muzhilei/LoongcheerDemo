package com.loongcheer.network.applovin;

import android.content.Context;
import android.os.CountDownTimer;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;

import java.util.Map;

public class AppLovinLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private static final String TAG = AppLovinLCRewardedVideoAdapter.class.getSimpleName();

    AppLovinIncentivizedInterstitial mInterstitial;

    AppLovinAdRewardListener mAppLovinAdRewardListener;
    AppLovinAdVideoPlaybackListener mAppLovinAdVideoPlaybackListener;
    AppLovinAdDisplayListener mAppLovinAdDisplayListener;
    AppLovinAdClickListener mAppLovinAdClickListener;

    String sdkkey = "";
    String mPlacementId;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    boolean isReward = false;

    /***
     * init
     */
    private void init(Context context) {

        AppLovinSdk mApplovinSdk = AppLovinLCInitManager.getInstance().initSDK(context, sdkkey);

        mInterstitial = AppLovinIncentivizedInterstitial.create(mPlacementId, mApplovinSdk);

        mAppLovinAdRewardListener = new AppLovinAdRewardListener() {
            @Override
            public void userRewardVerified(AppLovinAd pAppLovinAd, Map<String, String> pMap) {
            }

            @Override
            public void userOverQuota(AppLovinAd pAppLovinAd, Map<String, String> pMap) {
            }

            @Override
            public void userRewardRejected(AppLovinAd pAppLovinAd, Map<String, String> pMap) {
            }

            @Override
            public void validationRequestFailed(AppLovinAd pAppLovinAd, int pI) {
            }

            @Override
            public void userDeclinedToViewAd(AppLovinAd pAppLovinAd) {
            }
        };


        mAppLovinAdVideoPlaybackListener = new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoStart(AppLovinLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoEnd(AppLovinLCRewardedVideoAdapter.this);
                }
            }
        };
        mAppLovinAdDisplayListener = new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {

            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdClose(AppLovinLCRewardedVideoAdapter.this);
                }
                isReward = false;
            }
        };
        mAppLovinAdClickListener = new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdClicked(AppLovinLCRewardedVideoAdapter.this);
                }
            }
        };
        mInterstitial.setUserIdentifier(mUserId);

    }

    /***
     * load ad
     */
    public void startload() {

        if (check()) {
            mInterstitial.preload(new AppLovinAdLoadListener() {
                @Override
                public void adReceived(AppLovinAd appLovinAd) {
                    if (mLoadResultListener != null) {
                        DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                        mTimer.cancel();
                        mLoadResultListener.onReworkVideoAdLoaded(AppLovinLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void failedToReceiveAd(int errorCode) {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdLoadFail(AppLovinLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", " " + errorCode));
                    }
                }
            });
        }
    }


    @Override
    public void loadRewardVideoAd(Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
        mLoadResultListener = customRewardVideoListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        mPlacementId = placementId;
        sdkkey = unitGroupInfo.getSdkKey();
        DBContext.init(activity);
        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        init(activity.getApplicationContext());
        startload();
    }

    @Override
    public void show(Context activity) {
        if (check() && mInterstitial.isAdReadyToDisplay()) {
            mInterstitial.show(activity, mAppLovinAdRewardListener, mAppLovinAdVideoPlaybackListener, mAppLovinAdDisplayListener, mAppLovinAdClickListener);
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
        if (check()) {
            return mInterstitial.isAdReadyToDisplay();
        }
        return false;
    }

    private boolean check() {
        return mInterstitial != null;
    }

    @Override
    public void clean() {

    }

    @Override
    public String getSDKVersion() {
        return AppLovinLCConst.getNetworkVersion();
    }

    @Override
    public String getNetworkName() {
        return AppLovinLCInitManager.getInstance().getNetworkName();
    }
}
