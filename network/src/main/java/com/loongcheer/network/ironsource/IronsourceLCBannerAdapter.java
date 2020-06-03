package com.loongcheer.network.ironsource;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;


public class IronsourceLCBannerAdapter extends CustomBannerAdapter {

    private static final String TAG = IronsourceLCBannerAdapter.class.getSimpleName();


    String appKey = "";

    String mPlacementId = "";

    String size = "";

    CustomBannerListener mListener;

    IronSourceBannerLayout mBannerView;

    CountDownTimer mTimer;



    /***
     * init and load
     *
     */
    private void initAndLoad(final Activity activity) {

        IronSourceBannerLayout bannerLayout = null;
        switch (size){
            case "320x50":
                bannerLayout = IronSource.createBanner(activity, ISBannerSize.BANNER);
                break;
            case "320x90":
                bannerLayout = IronSource.createBanner(activity,ISBannerSize.LARGE);
                break;
            case "300x250":
                bannerLayout = IronSource.createBanner(activity,ISBannerSize.RECTANGLE);
                break;
            case "728x90":
                bannerLayout = IronSource.createBanner(activity,ISBannerSize.SMART);
                break;
        }

        bannerLayout.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                if (mListener != null) {
                    if (mTimer != null){
                        mTimer.cancel();
                    }
                    mListener.onBannerAdLoaded(IronsourceLCBannerAdapter.this);

                }
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(IronsourceLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, ironSourceError.getErrorCode() + "", ironSourceError.getErrorMessage()));

                }
            }

            @Override
            public void onBannerAdClicked() {
                if (mListener != null) {
                    mListener.onBannerAdClicked(IronsourceLCBannerAdapter.this);

                }
            }

            @Override
            public void onBannerAdScreenPresented() {
                if (mListener != null) {
                    mListener.onBannerAdShow(IronsourceLCBannerAdapter.this);

                }
            }

            @Override
            public void onBannerAdScreenDismissed() {
                if (mListener != null) {
                    mListener.onBannerAdClose(IronsourceLCBannerAdapter.this);

                }
            }

            @Override
            public void onBannerAdLeftApplication() {
                if (mListener != null) {
                    mListener.onBannerAdShow(IronsourceLCBannerAdapter.this);

                }
            }
        });
        mBannerView = bannerLayout;

       IronsourceLCInitManager.getInstance().initSDK(activity, appKey, true,new IronsourceLCInitManager.InitCallback() {
           @Override
           public void onFinish() {
               IronSource.loadBanner(mBannerView,mPlacementId);
           }
       });


    }


    @Override
    public void loadBannerAd(LCBannerView bannerView, Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        mPlacementId = placementId;
        size = unitGroupInfo.getBanSize();
        mTimer = timer;
        appKey = unitGroupInfo.getAppKey();

        Activity activity = SDKContext.getInstance().getActivity();
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
    public String getSDKVersion() {
        return " ";
    }

    @Override
    public void clean() {
        if (mBannerView != null) {
            IronSource.destroyBanner(mBannerView);
        }
    }

    @Override
    public String getNetworkName() {
        return IronsourceLCInitManager.getInstance().getNetworkName();
    }
}
