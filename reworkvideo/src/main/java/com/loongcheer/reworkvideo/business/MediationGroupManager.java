package com.loongcheer.reworkvideo.business;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.CommonMediationManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.reworkvideo.api.LCRewardVideoListener;
import com.loongcheer.reworkvideo.business.utils.CustomRewardVideoAdapterParser;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;

import java.util.ArrayList;
import java.util.List;

public class MediationGroupManager extends CommonMediationManager {

    LCRewardVideoListener mCallbackListener;
    private static CustomRewardVideoAdapter mAdapter;
    private static List<CustomRewardVideoAdapter> adapterList = new ArrayList<>();

    private CustomRewardVideoListener customRewardVideoListener = new CustomRewardVideoListener() {
        @Override
        public void onReworkVideoAdDataLoaded(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            Log.e("1111","onInterstitialAdDataLoaded");
        }

        @Override
        public void onReworkVideoAdLoaded(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            adapterList.add(adapter);
            mCallbackListener.onRewardedVideoAdLoaded();
            SPUtil.putInt(mApplcationContext, Const.SPU_NAME,Const.SPU_IS_READY,1);
            Log.e("1111","onInterstitialAdLoaded");
        }

        @Override
        public void onReworkVideoAdLoadFail(CustomRewardVideoAdapter adapter, AdError adError) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdFailed(adError);
            Log.e("1111",adError.getPlatformMSG()+adError.getPlatformCode());
        }

        @Override
        public void onReworkVideoAdClicked(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdPlayClicked();
        }

        @Override
        public void onReworkVideoAdShow(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdPlayStart();
        }

        @Override
        public void onReworkVideoAdClose(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdClosed();
        }

        @Override
        public void onReworkVideoAdVideoStart(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdPlayStart();
        }

        @Override
        public void onReworkVideoAdVideoEnd(CustomRewardVideoAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdPlayEnd();
        }

        @Override
        public void onReworkVideoAdVideoError(CustomRewardVideoAdapter adapter, AdError adError) {
            mAdapter = adapter;
            mCallbackListener.onRewardedVideoAdPlayFailed(adError);
        }
    };

    protected MediationGroupManager(Context context) {
        super(context);
    }

    public void setCallbackListener(LCRewardVideoListener listener) {
        mCallbackListener = listener;
    }

    @Override
    public void onDevelopLoaded() {

    }

    @Override
    public void onDeveloLoadFail(AdError adError) {

    }


    protected void loadReworkVideoAd(String placementId, RealmModel placeStrategy, PlaceStrategy.UnitGroupInfo list, CountDownTimer timer ) {
        super.loadAd(placementId, placeStrategy, list,timer);
    }


    @Override
    public void startLoadAd(LoongCheerBaseAdapter baseAdapter, PlaceStrategy.UnitGroupInfo unitGroupInfo, String placementId, CountDownTimer timer ) {
        if (baseAdapter instanceof CustomRewardVideoAdapter) {
            CustomRewardVideoAdapterParser.loadRewardVideoAd(mApplcationContext, (CustomRewardVideoAdapter) baseAdapter, unitGroupInfo,placementId, timer,customRewardVideoListener);
        }
    }

    public static CustomRewardVideoAdapter getmAdapter() {
        return mAdapter;
    }

    public static List<CustomRewardVideoAdapter> getAdapterList(){
        return adapterList;
    }
}
