package com.loongcheer.network.vungle;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.vungle.warren.AdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

public class VungleLCBannerAdapter extends CustomBannerAdapter {

    private String unitid = "";

    CustomBannerListener mListener;

    View mBannerView;

    String size = "";

    CountDownTimer mTimer;

    @Override
    public void loadBannerAd(final LCBannerView bannerView, Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomBannerListener customBannerListener) {
        mListener = customBannerListener;
        unitid = placementId;
        size = unitGroupInfo.getBanSize();
        mTimer = timer;
        if (activity == null) {
            if (mListener != null) {
                mListener.onBannerAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        VungleLCInitManager.getInstance().initSDK(activity.getApplicationContext(), unitGroupInfo, new VungleLCInitManager.InitListener() {
            @Override
            public void onSuccess() {
                loadBanner(bannerView);
            }

            @Override
            public void onError(Throwable throwable) {
                if (mListener != null) {
                    mListener.onBannerAdLoadFail(VungleLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", throwable.getMessage()));
                }
            }
        });
    }

    private void loadBanner(LCBannerView bannerView){
        AdConfig.AdSize adSize = null;
        switch (size){
            case "320x50":
                adSize = AdConfig.AdSize.BANNER;
                break;
            case "300x50":
                adSize = AdConfig.AdSize.BANNER_SHORT;
                break;
            case "728x90":
                adSize = AdConfig.AdSize.BANNER_LEADERBOARD;
                break;
        }

        Banners.loadBanner(unitid, adSize, new LoadAdCallback() {
            @Override
            public void onAdLoad(String s) {
                if (mListener != null){
                    if (mTimer != null){
                        mTimer.cancel();
                    }
                    mListener.onBannerAdLoaded(VungleLCBannerAdapter.this);
                }
            }

            @Override
            public void onError(String s, VungleException e) {
                if (mListener != null){
                    mListener.onBannerAdLoadFail(VungleLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, e.getExceptionCode()+"",e.getLocalizedMessage()));
                }
            }
        });

        if (Banners.canPlayAd(unitid, AdConfig.AdSize.BANNER)) {
            VungleBanner vungleBanner = Banners.getBanner(unitid, AdConfig.AdSize.BANNER, new PlayAdCallback() {
                @Override
                public void onAdStart(String placementReferenceId) {
                    if (mListener != null){
                        mListener.onBannerAdShow(VungleLCBannerAdapter.this);
                    }
                }

                @Override
                public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                    if (mListener != null){
                        mListener.onBannerAdClose(VungleLCBannerAdapter.this);
                        if (isCTAClicked){
                            mListener.onBannerAdClicked(VungleLCBannerAdapter.this);
                        }
                    }

                }

                @Override
                public void onError(String placementReferenceId, VungleException e) {
                    // Play ad error occurred - e.getLocalizedMessage() contains error message
                    if ( mListener != null){
                        mListener.onBannerAdLoadFail(VungleLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, e.getExceptionCode()+"",e.getLocalizedMessage()));

                    }
                }
            });
            mBannerView = vungleBanner;
        }

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
        return VungleLCInitManager.getInstance().getNetworkName();
    }
}
