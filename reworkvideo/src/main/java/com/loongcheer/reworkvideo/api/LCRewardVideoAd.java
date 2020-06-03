package com.loongcheer.reworkvideo.api;

import android.app.Activity;
import android.content.Context;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.reworkvideo.business.AdLoadManager;

public class LCRewardVideoAd {

    final String TAG = getClass().getSimpleName();
    String advPlacememtId;
    Context mActivity;


    LCRewardVideoListener mReworkListener;
    AdLoadManager mAdLoadManager;

    private LCRewardVideoListener lcRewardVideoListener = new LCRewardVideoListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdLoaded();
                    }
                }
            });

        }

        @Override
        public void onRewardedVideoAdFailed(final AdError errorCode) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdFailed(errorCode);
                    }
                }
            });
        }

        @Override
        public void onRewardedVideoAdPlayStart() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdPlayStart();
                    }
                }
            });
        }

        @Override
        public void onRewardedVideoAdPlayEnd() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdPlayEnd();
                    }
                }
            });
        }

        @Override
        public void onRewardedVideoAdPlayFailed(final AdError errorCode) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdPlayFailed(errorCode);
                    }
                }
            });
        }

        @Override
        public void onRewardedVideoAdClosed() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdClosed();
                    }
                }
            });

        }

        @Override
        public void onRewardedVideoAdPlayClicked() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onRewardedVideoAdPlayClicked();
                    }
                }
            });
        }

        @Override
        public void onReward() {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mReworkListener != null) {
                        mReworkListener.onReward();
                    }
                }
            });
        }
    };

    public LCRewardVideoAd(Context activity, String advPlacememtId) {
        this.advPlacememtId = advPlacememtId;
        mActivity = activity;
        mAdLoadManager = AdLoadManager.getInstance(activity, advPlacememtId);
    }

    public void setAdListener(LCRewardVideoListener listener) {
        mReworkListener = listener;
    }



    public void load() {
        load(false);
    }

    private void load(final boolean isAutoRefresh) {
        DBContext.init(mActivity);
        LCSDK.apiLog(advPlacememtId, Const.LOGKEY.API_INTERSTITIAL, Const.LOGKEY.API_LOAD, Const.LOGKEY.START, "");
        mAdLoadManager.refreshContext(mActivity);
        SPUtil.putInt(mActivity,Const.SPU_NAME,Const.SPU_IS_READY,0);
        mAdLoadManager.startLoadAd(mActivity,advPlacememtId,lcRewardVideoListener);
    }

    public boolean isAdReady() {
//        if (UploadDataLevelManager.getInstance(SDKContext.getInstance().getContext())
//                .getUploadDataLevel() == ATSDK.FORBIDDEN) {
//            AdError adError = ErrorCode.getErrorCode(ErrorCode.dataLevelLowError, "", "");
//            Log.e(TAG, adError.getDesc());
//            return false; //如果是FORBIDDEN则不去播放
//        }
        int ready = SPUtil.getInt(mActivity,Const.SPU_NAME,Const.SPU_IS_READY,0);
        boolean isAdReady = ready ==1? true:false;
        return isAdReady;
    }

    public void show() {

        if (mAdLoadManager != null){
            mAdLoadManager.show();
        }

    }

}
