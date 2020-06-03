package com.loongcheer.network.facebook;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;

public class FacebookLCInterstitialAdapter extends CustomInterstitialAdapter {

    InterstitialAd mInterstitialAd;
    String mPlacementId;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    /***
     * load ad
     */
    private void startLoad(final Context context) {

        final InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(FacebookLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, adError.getErrorCode() + "", "" + adError.getErrorMessage()));
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onInterstitialAdLoaded(FacebookLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClicked(FacebookLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdVideoEnd(FacebookLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClose(FacebookLCInterstitialAdapter.this);
                }
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd = new InterstitialAd(context, mPlacementId);
                // Load a new interstitial.
                final InterstitialAd.InterstitialAdLoadConfigBuilder adConfig = mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener);


                mInterstitialAd.loadAd(adConfig.build());
            }
        }).start();
    }

    @Override
    public void loadInterstitialAd(Context context, String placementId , PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer,CustomInterstitialListener customInterstitialListener) {
        mLoadResultListener = customInterstitialListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(context);
        if (context == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(FacebookLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        if (!placementId.equals("")){
            mPlacementId = placementId;
        }
        FacebookLCInitManager.getInstance().initSDK(context.getApplicationContext(),unitGroupInfo);
        startLoad(context);

    }

    @Override
    public void show(Context context) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd == null || !mInterstitialAd.isAdLoaded()) {
            return false;
        }

        if (mInterstitialAd.isAdInvalidated()) {
            return false;
        }

        return true;
    }

    @Override
    public void clean() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
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
