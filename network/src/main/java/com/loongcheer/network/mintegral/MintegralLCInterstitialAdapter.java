package com.loongcheer.network.mintegral;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGBidInterstitialVideoHandler;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.InterstitialListener;
import com.mintegral.msdk.out.MTGInterstitialHandler;

import java.util.HashMap;

public class MintegralLCInterstitialAdapter extends CustomInterstitialAdapter {

    private final String TAG = MintegralLCInterstitialAdapter.class.getSimpleName();

    MTGBidInterstitialVideoHandler mMvBidIntersititialVideoHandler;
    MTGInterstitialHandler mMvInterstitialHandler;
    MTGInterstitialVideoHandler mMvInterstitialVideoHandler;
    String place_id = "";
    boolean isVideo;
    boolean mIsReady;
    String mPayload;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;


    /***
     * init
     */
    private void init(Context context) {

        InterstitialVideoListener interstitialVideoListener = new InterstitialVideoListener() {
            @Override
            public void onLoadSuccess(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdDataLoaded(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onVideoLoadSuccess(String s, String s1) {
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onInterstitialAdLoaded(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onVideoLoadFail(String s) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(MintegralLCInterstitialAdapter.this
                            , ErrorCode.getErrorCode(ErrorCode.noADError, "", s));
                }
            }

            @Override
            public void onAdShow() {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdShow(MintegralLCInterstitialAdapter.this);
                    mLoadResultListener.onInterstitialAdVideoStart(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdClose(boolean b) {
                if (mLoadResultListener != null) {
                    if (b) {
                        mLoadResultListener.onInterstitialAdVideoEnd(MintegralLCInterstitialAdapter.this);
                    }
                    mLoadResultListener.onInterstitialAdClose(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onShowFail(String s) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdVideoError(MintegralLCInterstitialAdapter.this
                            , ErrorCode.getErrorCode(ErrorCode.noADError, "", s));
                }
            }

            @Override
            public void onVideoAdClicked(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdClicked(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onVideoComplete(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdVideoEnd(MintegralLCInterstitialAdapter.this);
                }
            }

            @Override
            public void onAdCloseWithIVReward(boolean b, int i) {

            }

            @Override
            public void onEndcardShow(String s, String s1) {

            }
        };

        mMvInterstitialVideoHandler = new MTGInterstitialVideoHandler( place_id,mUnitGroupInfo.getUnitId());
        // Please use this method"mMtgInterstitalVideoHandler.setRewardVideoListener()" ,if the SDK version is below 9.0.2
        mMvInterstitialVideoHandler.setInterstitialVideoListener(interstitialVideoListener);

//        mMvBidIntersititialVideoHandler = new MTGBidInterstitialVideoHandler(MIntegralConstans.PROPERTIES_UNIT_ID, place_id);
//        mMvBidIntersititialVideoHandler.setInterstitialVideoListener(interstitialVideoListener);
    }


    @Override
    public void loadInterstitialAd(final Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {
        mIsReady = false;
        isVideo = false;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(context);
        mLoadResultListener = customInterstitialListener;
        if (context == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "context is null."));
            }
            return;
        }

        if (unitGroupInfo == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "This placement's params in server is null!"));
            }
            return;
        }else {
            String appkey = unitGroupInfo.getAppKey();
            String appid = unitGroupInfo.getAppId();
            place_id = placementId;
            String unitId = unitGroupInfo.getUnitId();
            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(appkey) || TextUtils.isEmpty(place_id) || TextUtils.isEmpty(unitId)) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "mintegral appid, appkey or unitid is empty!"));
                }
                return;
            }
        }

        MintegralLCInitManager.getInstance().initSDK(context, unitGroupInfo, new MintegralLCInitManager.InitCallback() {
            @Override
            public void onSuccess() {
                //init
                init(context);
                //load ad
                startLoad();
            }

            @Override
            public void onError(Throwable e) {
                if (mLoadResultListener != null) {
                    AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", e.getMessage());
                    mLoadResultListener.onInterstitialAdLoadFail(MintegralLCInterstitialAdapter.this, adError);
                }
            }
        });
    }


    /***
     * load ad
     */
    public void startLoad() {
        if (mMvInterstitialHandler != null) {
            mMvInterstitialHandler.preload();
        }
        if (mMvInterstitialVideoHandler != null) {
            mMvInterstitialVideoHandler.load();
        }
        if (mMvBidIntersititialVideoHandler != null) {
            mMvBidIntersititialVideoHandler.loadFromBid(mPayload);
        }
    }


    @Override
    public void show(Context context) {
        if (mMvInterstitialHandler != null) {
            mMvInterstitialHandler.show();
        }

        if (mMvInterstitialVideoHandler != null) {
            mMvInterstitialVideoHandler.show();
        }

        if (mMvBidIntersititialVideoHandler != null) {
            mMvBidIntersititialVideoHandler.showFromBid();
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
        if (mMvInterstitialVideoHandler != null) {
            return mMvInterstitialVideoHandler.isReady();
        }

        if (mMvBidIntersititialVideoHandler != null) {
            return mMvBidIntersititialVideoHandler.isBidReady();
        }

        return mIsReady;
    }


    @Override
    public void clean() {

    }


    @Override
    public String getSDKVersion() {
        return MintegralLCConst.getNetworkVersion();
    }

    @Override
    public String getNetworkName() {
        return MintegralLCInitManager.getInstance().getNetworkName();
    }
}
