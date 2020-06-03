package com.loongcheer.network.admob;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;

public class AdmobLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private static final String TAG = AdmobLCRewardedVideoAdapter.class.getSimpleName();
    RewardedVideoAd mRewardedVideoAd;
    RewardedAd mRewardedAd;
    String mUnitid;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;
    AdRequest mAdRequest = null;
    Bundle extras = new Bundle();

    boolean isAdReady = false;
    /***
     * load ad
     */
    private void startLoad(final Context activity) {

        boolean exitRewardAD = false;
        try {
            Class.forName("com.google.android.gms.ads.rewarded.RewardedAd");
            exitRewardAD = true;
        } catch (Exception e) {

        }
        if (exitRewardAD) {
            mRewardedAd = new RewardedAd(activity, mUnitid);
        } else {
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    if (mLoadResultListener != null) {
                        DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                        mTimer.cancel();
                        mLoadResultListener.onReworkVideoAdLoaded(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdVideoStart(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdClose(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewarded(RewardItem pRewardItem) {

//                    if (!isPlayComplete) {
//                        isPlayComplete = true;
//                        if (mImpressionListener != null) {
//                            mImpressionListener.onRewardedVideoAdPlayEnd(AdmobATRewardedVideoAdapter.this);
//                        }
//                        if (mImpressionListener != null) {
//                            mImpressionListener.onReward(AdmobATRewardedVideoAdapter.this);
//                        }
//                    }

                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdClicked(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int pErrorCode) {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdLoadFail(AdmobLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "" + pErrorCode));
                    }
                }

                public void onRewardedVideoCompleted() {
                        if (mLoadResultListener != null) {
                            mLoadResultListener.onReworkVideoAdVideoEnd(AdmobLCRewardedVideoAdapter.this);
                        }
//                        if (mLoadResultListener != null) {
//                            mLoadResultListener.(AdmobLCRewardedVideoAdapter.this);
//                        }

                }
            });
        }


        mAdRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();

        if (mRewardedAd != null) {
            mRewardedAd.loadAd(mAdRequest, new RewardedAdLoadCallback() {
                @Override
                public void onRewardedAdLoaded() {
                    if (mLoadResultListener != null) {
                        DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                        mTimer.cancel();
                        mLoadResultListener.onReworkVideoAdLoaded(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewardedAdFailedToLoad(int i) {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdLoadFail(AdmobLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "" + i));
                    }
                }
            });
        } else {
            mRewardedVideoAd.loadAd(mUnitid, mAdRequest);
        }
    }


        @Override
    public void loadRewardVideoAd(Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
        mLoadResultListener = customRewardVideoListener;
        mUnitGroupInfo = unitGroupInfo;
        mTimer = timer;
        DBContext.init(activity);
        if (activity == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "activity is null."));
            }
            return;
        }

        mUnitid = placementId;
        AdmobLCInitManager.getInstance().initSDK(activity,unitGroupInfo);
        extras = AdmobLCInitManager.getInstance().getRequestBundle(activity.getApplicationContext());
        startLoad(activity);

    }

    private static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    @Override
    public void show(Context activity) {
        if (mRewardedAd != null) {
            mRewardedAd.show(findActivity(activity), new RewardedAdCallback() {
                @Override
                public void onRewardedAdClosed() {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdClose(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(int i) {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdLoadFail(AdmobLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "" + i));
                    }
                }

                @Override
                public void onRewardedAdOpened() {
                    if (mLoadResultListener != null) {
                        mLoadResultListener.onReworkVideoAdVideoStart(AdmobLCRewardedVideoAdapter.this);
                    }
                }

                @Override
                public void onUserEarnedReward( com.google.android.gms.ads.rewarded.RewardItem rewardItem) {

//                    if (!isPlayComplete) {
//                        isPlayComplete = true;
//                        if (mImpressionListener != null) {
//                            mImpressionListener.onRewardedVideoAdPlayEnd(AdmobATRewardedVideoAdapter.this);
//                        }
//                    }
//
//                    if (mImpressionListener != null) {
//                        mImpressionListener.onReward(AdmobATRewardedVideoAdapter.this);
//                    }
                }
            });

        }

        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.show();
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
        try {
            if (mRewardedAd != null) {
                return mRewardedAd.isLoaded();
            }

            if (mRewardedVideoAd != null) {
                return mRewardedVideoAd.isLoaded();
            }
        } catch (Throwable e) {

        }
        return isAdReady;
    }

    @Override
    public String getSDKVersion() {
        return "";
    }

    @Override
    public void clean() {

    }

    @Override
    public String getNetworkName() {
        return AdmobLCInitManager.getInstance().getNetworkName();
    }
}
