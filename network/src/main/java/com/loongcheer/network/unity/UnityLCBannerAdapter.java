package com.loongcheer.network.unity;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.network.facebook.FacebookLCBannerAdapter;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBannerSize;
import com.unity3d.services.banners.UnityBanners;
import com.unity3d.services.banners.api.BannerListener;

public class UnityLCBannerAdapter extends CustomBannerAdapter {

    private String unitid = "";

    CustomBannerListener mListener;

    View mBannerView;

    String size = "";

    @Override
    public void loadBannerAd(final LCBannerView bannerView, final Context activity, final String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, final CountDownTimer timer, CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        unitid = placementId;
        size = unitGroupInfo.getBanSize();
        final Activity activity1 = SDKContext.getInstance().getActivity();
        if (activity1 == null) {
            if (mListener != null) {
                mListener.onBannerAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        UnityLCInitManager.getInstance().initSDK(activity,unitGroupInfo);

        final BannerView.IListener listener = new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerView) {
                mBannerView = bannerView;
                if (mListener != null) {
                    if (timer != null){
                        timer.cancel();
                    }
                    mListener.onBannerAdLoaded(UnityLCBannerAdapter.this);

                }
            }

            @Override
            public void onBannerClick(BannerView bannerView) {
                if (mListener != null) {
                    mListener.onBannerAdClicked(UnityLCBannerAdapter.this);
                }
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(UnityLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, bannerErrorInfo.errorCode + "", bannerErrorInfo.errorMessage));

                }
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {
//                if (mListener != null) {
//                    mListener.onBannerAdShow(UnityLCBannerAdapter.this);
//                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                UnityBannerSize bannerSize = null;
                int with;
                int height;
                switch (size) {
                    case "320x50":
                         with = 320;
                         height = 50;
                        bannerSize = new UnityBannerSize(with,height);
                        break;
                    default:
                         with = 320;
                         height = 50;
                        bannerSize = new UnityBannerSize(with,height);
                        break;
                }
                BannerView bannerView1 = new BannerView(activity1,placementId,bannerSize);
                bannerView1.setListener(listener);
                bannerView1.load();
            }
        }).start();


    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public String getSDKVersion() {
       return UnityLCConst.getNetworkVersion();
    }

    @Override
    public void clean() {
        mBannerView = null;
    }

    @Override
    public String getNetworkName() {
        return UnityLCInitManager.getInstance().getNetworkName();
    }
}
