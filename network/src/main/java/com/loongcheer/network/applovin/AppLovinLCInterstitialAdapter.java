package com.loongcheer.network.applovin;

import android.content.Context;
import android.os.CountDownTimer;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;

public class AppLovinLCInterstitialAdapter extends CustomInterstitialAdapter {

    private static final String TAG = AppLovinLCInterstitialAdapter.class.getSimpleName();

    String sdkkey = "";
    AppLovinAd mAppLovinAd;
    AppLovinInterstitialAdDialog mInterstitialAd;
    String mPlacementId;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    /***
     * load ad
     */
    private void startLoad(final Context context) {
        AppLovinSdk mApplovinSdk = AppLovinLCInitManager.getInstance().initSDK(context, sdkkey);

        mInterstitialAd = AppLovinInterstitialAd.create(mApplovinSdk, context.getApplicationContext());

        mInterstitialAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdShow(AppLovinLCInterstitialAdapter.this);
                }
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClose(AppLovinLCInterstitialAdapter.this);
                }
            }
        });

        mInterstitialAd.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClicked(AppLovinLCInterstitialAdapter.this);
                }
            }
        });

        mInterstitialAd.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdVideoStart(AppLovinLCInterstitialAdapter.this);
                }
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdVideoEnd(AppLovinLCInterstitialAdapter.this);
                }
            }
        });

        mApplovinSdk.getAdService().loadNextAdForZoneId(mPlacementId, new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd appLovinAd) {
                mAppLovinAd = appLovinAd;
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onInterstitialAdLoaded(AppLovinLCInterstitialAdapter.this);
                }
            }

            @Override
            public void failedToReceiveAd(int i) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(AppLovinLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, i + "", ""));
                }
            }
        });

    }

    @Override
    public void loadInterstitialAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {
        mLoadResultListener = customInterstitialListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        mPlacementId = placementId;
        sdkkey = unitGroupInfo.getSdkKey();
        DBContext.init(context);
        if (context == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        startLoad(context);
    }

    @Override
    public void show(Context context) {
        if (mAppLovinAd != null) {
            mInterstitialAd.showAndRender(mAppLovinAd);
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
        return mAppLovinAd != null;
    }



    @Override
    public void clean() {
        try {
            mAppLovinAd = null;
            if (mInterstitialAd != null) {
                mInterstitialAd.dismiss();
                mInterstitialAd = null;
            }
        } catch (Exception e) {

        }
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
