package com.loongcheer.network.mintegral;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.mintegral.msdk.out.BannerAdListener;
import com.mintegral.msdk.out.BannerSize;
import com.mintegral.msdk.out.MTGBannerView;

public class MintegralLCBannerAdapter extends CustomBannerAdapter {

    MTGBannerView mMTGBannerView;

    String place_id = "";
    String unit_id = "";
    String size;
//    String mPayload;
    CustomBannerListener mCustomBannerListener;
    CountDownTimer mTimer ;

    @Override
    public void loadBannerAd(final LCBannerView bannerView, final Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomBannerListener customBannerListener) {
        mCustomBannerListener = customBannerListener;
        mTimer = timer;
        String appid = unitGroupInfo.getAppId();
        String appkey = unitGroupInfo.getAppKey();
        size = unitGroupInfo.getBanSize();
        unit_id = unitGroupInfo.getUnitId();
        place_id = placementId;
        if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(appkey) || TextUtils.isEmpty(place_id) || TextUtils.isEmpty(unit_id)) {
            if (mCustomBannerListener != null) {
                AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", "appid、appkey or unitid is empty.");
                mCustomBannerListener.onBannerAdLoadFail(this, adError);
            }
            return;
        }

        final Activity activity1 = SDKContext.getInstance().getActivity();

//        if (!(activity instanceof Activity)) {
//            if (mCustomBannerListener != null) {
//                AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", "Context must be activity.");
//                mCustomBannerListener.onBannerAdLoadFail(this, adError);
//            }
//            return;
//        }

        MintegralLCInitManager.getInstance().initSDK(activity, unitGroupInfo, new MintegralLCInitManager.InitCallback() {
            @Override
            public void onSuccess() {
                startLoad(activity1, bannerView);
            }

            @Override
            public void onError(Throwable e) {
                if (mCustomBannerListener != null) {
                    AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", e.getMessage());
                    mCustomBannerListener.onBannerAdLoadFail(MintegralLCBannerAdapter.this, adError);
                }
            }
        });
    }


    private void startLoad(Activity activity, final LCBannerView bannerView) {

        mMTGBannerView = new MTGBannerView(activity.getBaseContext());

        int bannerSize;
        int width;
        int height;
        switch (size) {
            case "320x90":
                bannerSize = BannerSize.LARGE_TYPE;
                width = 320;
                height = 90;
                break;
            case "300x250":
                bannerSize = BannerSize.MEDIUM_TYPE;
                width = 300;
                height = 250;
                break;
            case "smart":
                bannerSize = BannerSize.SMART_TYPE;
                width = FrameLayout.LayoutParams.MATCH_PARENT;
                height = FrameLayout.LayoutParams.MATCH_PARENT;
                break;
            case "320x50":
            default:
                bannerSize = BannerSize.STANDARD_TYPE;
                width = 320;
                height = 50;
                break;
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width > 0 ? dip2px(activity, width) : width, height > 0 ? dip2px(activity, height) : height);
        lp.gravity = Gravity.CENTER;

        mMTGBannerView.init(new BannerSize(bannerSize,0 , 0), place_id,unit_id);

        mMTGBannerView.setBannerAdListener(new BannerAdListener() {
            @Override
            public void onLoadFailed(String s) {
                if (bannerView != null) {
                    bannerView.removeView(mMTGBannerView);
                }
                if (mCustomBannerListener != null) {
                    mCustomBannerListener.onBannerAdLoadFail(MintegralLCBannerAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", s));
                }
            }

            @Override
            public void onLoadSuccessed() {
                if (mCustomBannerListener != null) {
                    if (mTimer != null){
                        mTimer.cancel();
                    }
                    mCustomBannerListener.onBannerAdLoaded(MintegralLCBannerAdapter.this);
                }
            }

            @Override
            public void onLogImpression() {
//                if (mCustomBannerListener != null) {
//                    if (mTimer != null){
//                        mTimer.cancel();
//                    }
//                    mCustomBannerListener.onBannerAdLoaded(MintegralLCBannerAdapter.this);
//                    mCustomBannerListener.onBannerAdShow(MintegralLCBannerAdapter.this);
//                }
            }

            @Override
            public void onClick() {
                if (mCustomBannerListener != null) {
                    mCustomBannerListener.onBannerAdClicked(MintegralLCBannerAdapter.this);
                }
            }

            @Override
            public void onLeaveApp() {

            }

            @Override
            public void showFullScreen() {

            }

            @Override
            public void closeFullScreen() {

            }

            @Override
            public void onCloseBanner() {
                if (mCustomBannerListener != null) {
                    mCustomBannerListener.onBannerAdClose(MintegralLCBannerAdapter.this);
                }
            }

        });

        if (bannerView != null) {
            bannerView.addView(mMTGBannerView,lp);
        }

//        if (!TextUtils.isEmpty(mPayload)) {
//            mMTGBannerView.loadFromBid(mPayload);
//        } else {
            mMTGBannerView.load();
//        }
    }

    @Override
    public View getBannerView() {
        return mMTGBannerView;
    }

    @Override
    public String getSDKVersion() {
        return MintegralLCConst.getNetworkVersion();
    }

    @Override
    public void clean() {
        if (mMTGBannerView != null) {
            mMTGBannerView.release();
            mMTGBannerView = null;
        }
    }

    @Override
    public String getNetworkName() {
        return MintegralLCInitManager.getInstance().getNetworkName();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
