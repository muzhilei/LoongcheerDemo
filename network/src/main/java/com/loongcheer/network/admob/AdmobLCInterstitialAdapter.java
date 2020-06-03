package com.loongcheer.network.admob;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;

public class AdmobLCInterstitialAdapter extends CustomInterstitialAdapter {

    private static final String TAG = AdmobLCInterstitialAdapter.class.getSimpleName();

    InterstitialAd mInterstitialAd;
    String mPlacementId;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;
    AdRequest mAdRequest = null;
    Bundle extras = new Bundle();
    boolean isAdReady = false;
    /***
     * load ad
     */
    private void startLoad(final Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(mPlacementId);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onInterstitialAdLoaded(AdmobLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(AdmobLCInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, errorCode + "", ""));
                }

            }

            @Override
            public void onAdOpened() {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdShow(AdmobLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdLeftApplication() {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClicked(AdmobLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdClosed() {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClose(AdmobLCInterstitialAdapter.this);
                }
            }
        });


        mAdRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();

        mInterstitialAd.loadAd(mAdRequest);

    }



    @Override
    public void loadInterstitialAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {

        mLoadResultListener = customInterstitialListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        mPlacementId = placementId;
        DBContext.init(context);
        if (context == null) {
            log(TAG, "activity is null!");
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        //初始化
        AdmobLCInitManager.getInstance().initSDK(context.getApplicationContext(), unitGroupInfo);

        extras = AdmobLCInitManager.getInstance().getRequestBundle(context.getApplicationContext());

        startLoad(context);
    }

    @Override
    public void show(Context context) {
        if (check()) {
            isAdReady = false;
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
        try {
            if (check()) {
                return mInterstitialAd.isLoaded();
            }
        } catch (Throwable e) {

        }
        return isAdReady;
    }

    private boolean check() {
        if (mInterstitialAd == null) {
            return false;
        }
        return true;
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
        return AdmobLCInitManager.getInstance().getNetworkName();
    }
}
