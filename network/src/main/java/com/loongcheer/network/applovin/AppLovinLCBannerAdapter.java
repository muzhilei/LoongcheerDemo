package com.loongcheer.network.applovin;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import com.applovin.adview.AppLovinAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.Map;

public class AppLovinLCBannerAdapter extends CustomBannerAdapter {

    private static final String TAG = AppLovinLCBannerAdapter.class.getSimpleName();


    String sdkkey = "";

    String mPlacementId = "";

    String size = "";

    CustomBannerListener mListener;

    AppLovinAdView mBannerView;

    CountDownTimer mTimer;


    /***
     * init and load
     *
     */
    private void initAndLoad(Context activity) {

        AppLovinSdk applovinSdk = AppLovinLCInitManager.getInstance().initSDK(activity, sdkkey);

        AppLovinAdView appLovinAdView = null;
        switch (size) {
            case "320x50":
                appLovinAdView = new AppLovinAdView(applovinSdk, AppLovinAdSize.BANNER, activity);
                break;
            case "300x250":
                appLovinAdView = new AppLovinAdView(applovinSdk, AppLovinAdSize.MREC, activity);
                break;
            default:
                appLovinAdView = new AppLovinAdView(applovinSdk, AppLovinAdSize.BANNER, activity);
                break;
        }

        final AppLovinAdView finalAdView = appLovinAdView;



        finalAdView.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                if (mListener != null) {
                    mListener.onBannerAdShow(AppLovinLCBannerAdapter.this);
                }

            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
            }
        });

        finalAdView.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                if (mListener != null) {
                    mListener.onBannerAdClicked(AppLovinLCBannerAdapter.this);
                }
            }
        });


        AppLovinAdLoadListener adLoadListener = new AppLovinAdLoadListener() {
            public void adReceived(final AppLovinAd ad) {
                finalAdView.renderAd(ad);
                mBannerView = finalAdView;
                if (mListener != null) {
                    if (mTimer != null){
                        mTimer.cancel();
                    }
                    mListener.onBannerAdLoaded(AppLovinLCBannerAdapter.this);
                }
            }

            @Override
            public void failedToReceiveAd(int i) {
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(AppLovinLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, i + "", ""));
                }
            }
        };

        applovinSdk.getAdService().loadNextAdForZoneId(mPlacementId, adLoadListener);

    }


    @Override
    public void loadBannerAd(LCBannerView bannerView, Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        mPlacementId = placementId;
        size = unitGroupInfo.getBanSize();
        mTimer = timer;
        if (activity == null) {
            if (mListener != null) {
                mListener.onBannerAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }
        initAndLoad(activity);

    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public void clean() {
        if (mBannerView != null) {
            mBannerView.destroy();
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
