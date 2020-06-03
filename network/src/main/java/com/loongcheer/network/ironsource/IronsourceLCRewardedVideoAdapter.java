package com.loongcheer.network.ironsource;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.CountDownTimer;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;

import java.util.Map;

public class IronsourceLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    String mPlacementId = "";

    String appKey = "";

    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    /***
     * init and load
     */
    private void initAndLoad(final Activity activity,String appKey) {
        IntegrationHelper.validateIntegration(activity);

        IronSource.setUserId(mUserId);
        IronSource.setDynamicUserId(mUserId);

        IronsourceLCInitManager.getInstance().initSDK(activity, appKey, false,new IronsourceLCInitManager.InitCallback() {
            @Override
            public void onFinish() {
                if (IronSource.isISDemandOnlyRewardedVideoAvailable(mPlacementId)) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onReworkVideoAdLoaded(IronsourceLCRewardedVideoAdapter.this);
                } else {
                    IronsourceLCInitManager.getInstance().loadRewardedVideo(mPlacementId, IronsourceLCRewardedVideoAdapter.this);
                }
                try {
                    if (activity != null) {
                        IronSource.onResume(activity);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void loadRewardVideoAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
        mLoadResultListener = customRewardVideoListener;
        mPlacementId = placementId;
        appKey = unitGroupInfo.getAppKey();
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(context);
        Activity activity = SDKContext.getInstance().getActivity();
        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        initAndLoad(activity, appKey);
    }


    @Override
    public void show(Context activity) {
        if (isAdReady()) {
            IronsourceLCInitManager.getInstance().putAdapter("rv_" + mPlacementId, this);
            IronSource.showISDemandOnlyRewardedVideo(mPlacementId);
        }
    }

    @Override
    public void onResume(Context activity) {

    }

    @Override
    public void onPause(Context activity) {

    }

    @Override
    public boolean isAdReady() {
        return IronSource.isISDemandOnlyRewardedVideoAvailable(mPlacementId);
    }

    @Override
    public String getSDKVersion() {
        return "";
    }

    @Override
    public void clean() {
        IronSource.clearRewardedVideoServerParameters();
    }

    @Override
    public String getNetworkName() {
        return IronsourceLCInitManager.getInstance().getNetworkName();
    }

    /**
     * -------------------------------------------callback-------------------------------------------------------
     **/
    public void onRewardedVideoAdOpened() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdVideoStart(IronsourceLCRewardedVideoAdapter.this);
        }
    }

    public void onRewardedVideoAdClosed() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdVideoEnd(IronsourceLCRewardedVideoAdapter.this);
            mLoadResultListener.onReworkVideoAdClose(IronsourceLCRewardedVideoAdapter.this);
        }
        try {
            if (mActivityRef.get() != null) {
                IronSource.onPause(mActivityRef.get());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onRewardedVideoAdLoadSuccess() {
        if (mLoadResultListener != null) {
            DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
            mTimer.cancel();
            mLoadResultListener.onReworkVideoAdLoaded(IronsourceLCRewardedVideoAdapter.this);
        }
    }

    public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError) {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdLoadFail(IronsourceLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, ironSourceError.getErrorCode() + "", ironSourceError.getErrorMessage()));
        }
    }

//    public void onRewardedVideoAdRewarded() {
//        if (mLoadResultListener != null) {
//            mLoadResultListener(IronsourceLCRewardedVideoAdapter.this);
//        }
//    }

    public void onRewardedVideoAdShowFailed(IronSourceError pIronSourceError) {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdVideoError(IronsourceLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "" + pIronSourceError.getErrorCode(), " " + pIronSourceError.getErrorMessage()));
        }
    }

    public void onRewardedVideoAdClicked() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onReworkVideoAdClicked(IronsourceLCRewardedVideoAdapter.this);
        }
    }

}
