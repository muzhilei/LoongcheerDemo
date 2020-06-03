package com.loongcheer.network.mintegral;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.out.MTGBidRewardVideoHandler;
import com.mintegral.msdk.out.MTGRewardVideoHandler;
import com.mintegral.msdk.out.RewardVideoListener;

import java.util.Map;

public class MintegralLCRewardedVideoAdapter extends CustomRewardVideoAdapter {

    private final String TAG = MintegralLCRewardedVideoAdapter.class.getSimpleName();

    MTGRewardVideoHandler mMvRewardVideoHandler;
    MTGBidRewardVideoHandler mMvBidRewardVideoHandler;
    MintegralLCRewardedVideoAdapter mMintegralMediationSetting;
    String place_id = "";
    String mPayload;
    PlaceStrategy.UnitGroupInfo mUnitGroupInfo;
    CountDownTimer mTimer;

    /***
     * init
     */
    private void init(Context context) {

        RewardVideoListener videoListener = new RewardVideoListener() {
            @Override
            public void onVideoLoadSuccess(String s, String s1) {
                if (mLoadResultListener != null) {
                    DBContext.modify(mUnitGroupInfo.getAdvPlacementId()+mUnitGroupInfo.getPlatformName(),true);
                    mTimer.cancel();
                    mLoadResultListener.onReworkVideoAdLoaded(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onLoadSuccess(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdDataLoaded(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onVideoLoadFail(String s) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(MintegralLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", s));
                }
            }

            @Override
            public void onAdShow() {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoStart(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onAdClose(boolean b, String s, float v) {
                if (mLoadResultListener != null) {
                    if (b) {
//                        mLoadResultListener.onReward(MintegralLCRewardedVideoAdapter.this);
                    }
                    mLoadResultListener.onReworkVideoAdClose(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onShowFail(String s) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoError(MintegralLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", ErrorCode.serverError));
                }
            }

            @Override
            public void onVideoAdClicked(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdClicked(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onVideoComplete(String s, String s1) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdVideoEnd(MintegralLCRewardedVideoAdapter.this);
                }
            }

            @Override
            public void onEndcardShow(String s, String s1) {

            }
        };

        mMvRewardVideoHandler = new MTGRewardVideoHandler(place_id,mUnitGroupInfo.getUnitId());
        mMvRewardVideoHandler.setRewardVideoListener(videoListener);
    }


    @Override
    public void loadRewardVideoAd(final Context activity, String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo, CountDownTimer timer, CustomRewardVideoListener customRewardVideoListener) {
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

        if (unitGroupInfo == null) {
            if (mLoadResultListener != null) {
                mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "This placement's params in server is null!"));
            }
            return;
        } else {

            String appid = unitGroupInfo.getAppId();
            String appkey = unitGroupInfo.getAppKey();
            place_id = placementId;
            String unitId = unitGroupInfo.getUnitId();
            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(appkey) || TextUtils.isEmpty(place_id) || TextUtils.isEmpty(unitId)) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "mintegral appid, appkey or unitid is empty!"));
                }
                return;
            }
        }


        MintegralLCInitManager.getInstance().initSDK(activity.getApplicationContext(), unitGroupInfo, new MintegralLCInitManager.InitCallback() {
            @Override
            public void onSuccess() {
                //init
                init(activity);
                //load ad
                startLoad();
            }

            @Override
            public void onError(Throwable e) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onReworkVideoAdLoadFail(MintegralLCRewardedVideoAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", e.getMessage()));
                }
            }
        });
    }


    /***
     * load ad
     */
    public void startLoad() {
        if (mMvRewardVideoHandler != null) {
            mMvRewardVideoHandler.load();
        }

        if (mMvBidRewardVideoHandler != null) {
            mMvBidRewardVideoHandler.loadFromBid(mPayload);
        }
    }

    @Override
    public void show(Context activity) {
        if (mMvRewardVideoHandler != null) {
            mMvRewardVideoHandler.show("1", mUserId);
        }

        if (mMvBidRewardVideoHandler != null) {
            mMvBidRewardVideoHandler.showFromBid("1", mUserId);
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
        if (mMvRewardVideoHandler != null) {
            return mMvRewardVideoHandler.isReady();
        }

        if (mMvBidRewardVideoHandler != null) {
            return mMvBidRewardVideoHandler.isBidReady();
        }
        return false;
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
