package com.loongcheer.network.ironsource;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyInterstitialListener;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyRewardedVideoListener;
import com.ironsource.sdk.IronSourceAdInstance;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IronsourceLCInitManager extends LCInitMediation {

    private static final String TAG = IronsourceLCInitManager.class.getSimpleName();
    private String mAppKey;
    private static IronsourceLCInitManager sInstance;
    private Handler mHandler;

    private ConcurrentHashMap<String, LoongCheerBaseAdapter> mAdapterMap;
    private ConcurrentHashMap<String, LoongCheerBaseAdapter> mLoadResultAdapterMap;

    ISDemandOnlyInterstitialListener isDemandOnlyInterstitialListener = new ISDemandOnlyInterstitialListener() {
        @Override
        public void onInterstitialAdReady(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mLoadResultAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceLCInterstitialAdapter) {
                ((IronsourceLCInterstitialAdapter) baseAdapter).onInterstitialAdReady();
            }
            removeLoadResultAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdLoadFailed(String instanceId, IronSourceError ironSourceError) {
            LoongCheerBaseAdapter baseAdapter = mLoadResultAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceLCInterstitialAdapter) {
                ((IronsourceLCInterstitialAdapter) baseAdapter).onInterstitialAdLoadFailed(ironSourceError);
            }
            removeLoadResultAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdOpened(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceLCInterstitialAdapter) {
                ((IronsourceLCInterstitialAdapter) baseAdapter).onInterstitialAdOpened();
            }
        }

        @Override
        public void onInterstitialAdClosed(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceLCInterstitialAdapter) {
                ((IronsourceLCInterstitialAdapter) baseAdapter).onInterstitialAdClosed();
            }
            removeAdapter("inter_" + instanceId);
        }


        @Override
        public void onInterstitialAdShowFailed(String instanceId, IronSourceError ironSourceError) {
            removeAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdClicked(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceLCInterstitialAdapter) {
                ((IronsourceLCInterstitialAdapter) baseAdapter).onInterstitialAdClicked();
            }
        }
    };


    ISDemandOnlyRewardedVideoListener isDemandOnlyRewardedVideoListener = new ISDemandOnlyRewardedVideoListener() {
        @Override
        public void onRewardedVideoAdLoadSuccess(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mLoadResultAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdLoadSuccess();
            }
            removeLoadResultAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdLoadFailed(String instanceId, IronSourceError ironSourceError) {
            LoongCheerBaseAdapter baseAdapter = mLoadResultAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdLoadFailed(ironSourceError);
            }
            removeLoadResultAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdOpened(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdOpened();
            }
        }

        @Override
        public void onRewardedVideoAdClosed(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdClosed();
            }
        }

        @Override
        public void onRewardedVideoAdShowFailed(String instanceId, IronSourceError ironSourceError) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdShowFailed(ironSourceError);
            }
            removeAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdClicked(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdClicked();
            }
        }

        @Override
        public void onRewardedVideoAdRewarded(String instanceId) {
            LoongCheerBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceLCRewardedVideoAdapter) {
//                ((IronsourceLCRewardedVideoAdapter) baseAdapter).onRewardedVideoAdRewarded();
            }
        }
    };



    private IronsourceLCInitManager() {
        mAdapterMap = new ConcurrentHashMap<>();
        mLoadResultAdapterMap = new ConcurrentHashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static IronsourceLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new IronsourceLCInitManager();
        }
        return sInstance;
    }


    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        if (!(context instanceof Activity)) {
            return;
        }
        initSDK(((Activity) context), info.getAppKey(),false, null);
    }

    public void initSDK(Activity activity, String mAppkey,boolean isBanner, final InitCallback initCallback) {

        final String appkey = mAppkey;
        if (TextUtils.isEmpty(appkey)) {
            return;
        }
        if (TextUtils.isEmpty(mAppKey) || !TextUtils.equals(mAppKey, appkey)) {


            IronSource.setISDemandOnlyInterstitialListener(isDemandOnlyInterstitialListener);
            IronSource.setISDemandOnlyRewardedVideoListener(isDemandOnlyRewardedVideoListener);

            if (isBanner){
                IronSource.init(activity, appkey);
            }else {
                IronSource.initISDemandOnly(activity, appkey, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO);
            }


            mAppKey = appkey;

            postDelay(new Runnable() {
                @Override
                public void run() {
                    mAppKey = appkey;
                    if (initCallback != null) {
                        initCallback.onFinish();
                    }
                }
            }, 5000L); //The first initialization takes about 5 seconds

        } else {
            if (isBanner){
                IronSource.init(activity, appkey);
            }
            if (initCallback != null) {
                initCallback.onFinish();
            }
        }

    }

    public void loadInterstitial(final String instanceId, IronsourceLCInterstitialAdapter interstitialAdapter) {
        putLoadResultAdapter("inter_" + instanceId, interstitialAdapter);
        IronSource.loadISDemandOnlyInterstitial(instanceId);

    }

    public void loadRewardedVideo(final String instanceId, IronsourceLCRewardedVideoAdapter rewardedVideoAdapter) {
        putLoadResultAdapter("rv_" + instanceId, rewardedVideoAdapter);
        IronSource.loadISDemandOnlyRewardedVideo(instanceId);
    }

    protected synchronized void putLoadResultAdapter(String instanceId, LoongCheerBaseAdapter baseAdapter) {
        mLoadResultAdapterMap.put(instanceId, baseAdapter);
    }

    private synchronized void removeLoadResultAdapter(String instanceId) {
        mLoadResultAdapterMap.remove(instanceId);
    }

    protected synchronized void putAdapter(String instanceId, LoongCheerBaseAdapter baseAdapter) {
        mAdapterMap.put(instanceId, baseAdapter);
    }

    private synchronized void removeAdapter(String instanceId) {
        mAdapterMap.remove(instanceId);
    }


    protected void postDelay(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    interface InitCallback {
        void onFinish();
    }

    @Override
    public String getNetworkName() {
        return "ironsource";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.ironsource.mediationsdk.IronSource";
    }


    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.ironsource.sdk.controller.ControllerActivity");
        list.add("com.ironsource.sdk.controller.InterstitialActivity");
        list.add("com.ironsource.sdk.controller.OpenUrlActivity");
        return list;
    }
}
