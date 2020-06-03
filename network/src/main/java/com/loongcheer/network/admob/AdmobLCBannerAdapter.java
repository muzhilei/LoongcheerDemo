package com.loongcheer.network.admob;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.strategy.PlaceStrategy;

public class AdmobLCBannerAdapter extends CustomBannerAdapter {

    private static final String TAG = AdmobLCBannerAdapter.class.getSimpleName();

    AdRequest mAdRequest = null;
    private String unitid = "";


    CustomBannerListener mListener;

    View mBannerView;

    String size = "";



    @Override
    public void loadBannerAd(LCBannerView bannerView, Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, final CountDownTimer timer, CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        unitid = placementId;
        size = unitGroupInfo.getBanSize();
        if (activity == null) {
            log(TAG, "activity is null!");
            if (mListener != null) {
                mListener.onBannerAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        AdmobLCInitManager.getInstance().initSDK(activity.getApplicationContext(), unitGroupInfo);

        Bundle persionalBundle = AdmobLCInitManager.getInstance().getRequestBundle(activity.getApplicationContext());


        final AdView adView = new AdView(activity);
        switch (size) {
            case "320x50":
                adView.setAdSize(AdSize.BANNER);
                break;
            case "320x100":
                adView.setAdSize(AdSize.LARGE_BANNER);
                break;
            case "300x250":
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                break;
            case "468x60":
                adView.setAdSize(AdSize.FULL_BANNER);
                break;
            case "728x90":
                adView.setAdSize(AdSize.LEADERBOARD);
                break;
            default:
                adView.setAdSize(AdSize.SMART_BANNER);
                break;
        }


        adView.setAdUnitId(unitid);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                log(TAG, "onAdLoaded");
                mBannerView = adView;
                if (mListener != null) {
                    if (timer != null){
                        timer.cancel();
                    }
                    mListener.onBannerAdLoaded(AdmobLCBannerAdapter.this);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                log(TAG, "onAdFailedToLoad");
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(AdmobLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, errorCode + "", ""));

                }
            }

            @Override
            public void onAdOpened() {
                log(TAG, "onAdOpened");
                if (mListener != null) {
                    mListener.onBannerAdShow(AdmobLCBannerAdapter.this);
                }
            }

            @Override
            public void onAdLeftApplication() {
                log(TAG, "onAdLeftApplication");
                if (mListener != null) {
                    mListener.onBannerAdClicked(AdmobLCBannerAdapter.this);
                }
            }

            @Override
            public void onAdClosed() {
                log(TAG, "onAdClosed");
            }
        });
        mAdRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, persionalBundle)
                //                .addKeyword("")
                //                .addNetworkExtras("")
                .build();
        adView.loadAd(mAdRequest);

    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public String getSDKVersion() {
        return "";
    }

    @Override
    public void clean() {
        mBannerView = null;
    }

    @Override
    public String getNetworkName() {
        return AdmobLCInitManager.getInstance().getNetworkName();
    }
}
