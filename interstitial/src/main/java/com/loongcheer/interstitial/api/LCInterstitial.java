package com.loongcheer.interstitial.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.interstitial.business.AdLoadManager;

public class LCInterstitial {

    public static String TAG = LCInterstitial.class.getSimpleName();
    public String advPlacememtId;
    public Context mContext;
    public LCInterstitialListener mInterstitialListener;


    AdLoadManager mAdLoadManager;

    private LCInterstitialListener interstitialListener = new LCInterstitialListener() {
        @Override
        public void onInterstitialAdLoaded() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdLoaded();
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdLoadFail(final AdError adError) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdLoadFail(adError);
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdClicked() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdClicked();
                    }
                }
            });

        }

        @Override
        public void onInterstitialAdShow() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdShow();
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdClose() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdClose();
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdVideoStart() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdVideoStart();
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdVideoEnd() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdVideoEnd();
                    }
                }
            });
        }

        @Override
        public void onInterstitialAdVideoError(final AdError adError) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialListener != null) {
                        mInterstitialListener.onInterstitialAdVideoError(adError);
                    }
                }
            });
        }
    };


    /***
     * 创建一个Interstitial 广告帮追
     * @param context 上下文
     * @param advPlacememtId 广告位
     */
    public LCInterstitial(Context context, String advPlacememtId) {

        mContext = context;

        this.advPlacememtId = advPlacememtId;

        mAdLoadManager = AdLoadManager.getInstance(context, advPlacememtId);

    }


    public void setAdListener(LCInterstitialListener listener) {
        mInterstitialListener = listener;
    }


    public void load() {
        load(false);
    }

    private void load(final boolean isAutoRefresh) {
        DBContext.init(mContext);
        LCSDK.apiLog(advPlacememtId, Const.LOGKEY.API_INTERSTITIAL, Const.LOGKEY.API_LOAD, Const.LOGKEY.START, "");
        mAdLoadManager.refreshContext(mContext);
        SPUtil.putInt(mContext,Const.SPU_NAME,Const.SPU_IS_READY,0);
        mAdLoadManager.startLoadAd(mContext,advPlacememtId,interstitialListener);
    }

    public boolean isAdReady() {
//        if (UploadDataLevelManager.getInstance(SDKContext.getInstance().getContext())
//                .getUploadDataLevel() == ATSDK.FORBIDDEN) {
//            AdError adError = ErrorCode.getErrorCode(ErrorCode.dataLevelLowError, "", "");
//            Log.e(TAG, adError.getDesc());
//            return false; //如果是FORBIDDEN则不去播放
//        }
        int ready = SPUtil.getInt(mContext,Const.SPU_NAME,Const.SPU_IS_READY,0);
        boolean isAdReady = ready ==1? true:false;
        return isAdReady;
    }

    public void show() {
        if (mAdLoadManager != null){
            mAdLoadManager.show();
        }
    }

}
