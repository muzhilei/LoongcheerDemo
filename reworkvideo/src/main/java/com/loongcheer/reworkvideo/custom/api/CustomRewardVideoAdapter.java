package com.loongcheer.reworkvideo.custom.api;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;

import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.Map;

public abstract class CustomRewardVideoAdapter extends LoongCheerBaseAdapter {

    protected CustomRewardVideoListener mLoadResultListener;
    protected CustomRewardedVideoEventListener mImpressionListener;
    protected String mUserId = "";
    protected String mUserData = "";

    public abstract void loadRewardVideoAd(final Context activity,
                                           final String placementId,
                                           final PlaceStrategy.UnitGroupInfo unitGroupInfo,
                                           final CountDownTimer timer,
                                           final CustomRewardVideoListener customRewardVideoListener);

    public abstract void show(Context activity);

    public abstract void onResume(Context activity);

    public abstract void onPause(Context activity);

    public void setUserId(String userId) {
        mUserId = userId;
    }

     public void setUserData(String userData) {
        mUserData = userData;
    }

    public void setAdImpressionListener(CustomRewardedVideoEventListener listener) {
        mImpressionListener = listener;
    }

    public void clearLoadListener() {
        mLoadResultListener = null;
    }

    public void clearImpressionListener() {
        mImpressionListener = null;
    }

}
