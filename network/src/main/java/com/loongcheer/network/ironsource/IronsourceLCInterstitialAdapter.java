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
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;

import java.util.Map;

public class IronsourceLCInterstitialAdapter extends CustomInterstitialAdapter {

    private final String TAG = IronsourceLCInterstitialAdapter.class.getSimpleName();

    String mPlacementId = "";

    String appKey = "";

    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;


    /***
     * init and load
     */
    private void initAndLoad(Activity activity,String appKey) {
        IntegrationHelper.validateIntegration(activity);

        IronsourceLCInitManager.getInstance().initSDK(activity, appKey, false,new IronsourceLCInitManager.InitCallback() {
            @Override
            public void onFinish() {
                if (IronSource.isISDemandOnlyInterstitialReady(mPlacementId)) {
                    mLoadResultListener.onInterstitialAdLoaded(IronsourceLCInterstitialAdapter.this);
                } else {
                    IronsourceLCInitManager.getInstance().loadInterstitial(mPlacementId, IronsourceLCInterstitialAdapter.this);
                }
            }
        });
    }


    @Override
    public void loadInterstitialAd(Context context, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomInterstitialListener customInterstitialListener) {
        mLoadResultListener = customInterstitialListener;
        appKey = unitGroupInfo.getAppKey();
        mPlacementId = placementId;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        Activity activity = SDKContext.getInstance().getActivity();
        DBContext.init(context);
        if (context == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }


        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onInterstitialAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        initAndLoad(activity,appKey);

    }

    @Override
    public void show(Context context) {
        if (isAdReady()) {
            IronsourceLCInitManager.getInstance().putAdapter("inter_" + mPlacementId, this);
            IronSource.showISDemandOnlyInterstitial(mPlacementId);
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
        return IronSource.isISDemandOnlyInterstitialReady(mPlacementId);
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

    protected void onInterstitialAdReady() {
        if (mLoadResultListener != null) {
            DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
            mTimer.cancel();
            mLoadResultListener.onInterstitialAdLoaded(IronsourceLCInterstitialAdapter.this);
        }
    }

    protected void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        if (mLoadResultListener != null) {
            mLoadResultListener.onInterstitialAdLoadFail(IronsourceLCInterstitialAdapter.this
                    , ErrorCode.getErrorCode(ErrorCode.noADError, ironSourceError.getErrorCode() + "", ironSourceError.getErrorMessage()));
        }
    }

    protected void onInterstitialAdOpened() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onInterstitialAdShow(IronsourceLCInterstitialAdapter.this);
        }

    }

    protected void onInterstitialAdClosed() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onInterstitialAdClose(IronsourceLCInterstitialAdapter.this);
        }
    }


    protected void onInterstitialAdClicked() {
        if (mLoadResultListener != null) {
            mLoadResultListener.onInterstitialAdClicked(IronsourceLCInterstitialAdapter.this);
        }
    }
}
