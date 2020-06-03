package com.loongcheer.network.facebook;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.strategy.PlaceStrategy;

public class FacebookLCBannerAdapter extends CustomBannerAdapter {

    private String unitid = "";

    CustomBannerListener mListener;

    View mBannerView;

    String size = "";

    @Override
    public void loadBannerAd(LCBannerView bannerView, final Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, final CountDownTimer timer , CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        unitid = placementId;
        size = unitGroupInfo.getBanSize();
        if (activity == null) {
            if (mListener != null) {
                mListener.onBannerAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }


        FacebookLCInitManager.getInstance().initSDK(activity.getApplicationContext(),unitGroupInfo);

        final AdListener adListener = new AdListener() {
            @Override
            public void onAdLoaded(Ad ad) {
                mBannerView = (AdView) ad;
                if (mListener != null) {
                    if (timer != null){
                        timer.cancel();
                    }
                    mListener.onBannerAdLoaded(FacebookLCBannerAdapter.this);

                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(FacebookLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, adError.getErrorCode() + "", adError.getErrorMessage()));

                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (mListener != null) {
                    mListener.onBannerAdClicked(FacebookLCBannerAdapter.this);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                if (mListener != null) {
                    mListener.onBannerAdShow(FacebookLCBannerAdapter.this);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                AdView adView = null;
                switch (size) {
                    case "320x50":
                        adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_50);
                        break;
                    case "320x90":
                        adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_90);
                        break;
                    case "320x250":
                        adView = new AdView(activity, unitid, AdSize.RECTANGLE_HEIGHT_250);
                        break;
                    default:
                        adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_50);
                        break;
                }
                adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
            }
        }).start();
    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public void clean() {
        mBannerView = null;
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
