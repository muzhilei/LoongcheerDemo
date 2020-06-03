package com.loongcheer.interstitial.business;

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
import com.loongcheer.interstitial.api.LCInterstitialListener;
import com.loongcheer.interstitial.business.utils.CustomInterstitialAdapterParser;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;

import java.util.ArrayList;
import java.util.List;

public class MediationGroupManager extends CommonMediationManager {

    LCInterstitialListener mCallbackListener;
    private static CustomInterstitialAdapter mAdapter;
    private static List<CustomInterstitialAdapter> adapterList = new ArrayList<>();

    private CustomInterstitialListener customInterstitialListener = new CustomInterstitialListener() {
        @Override
        public void onInterstitialAdDataLoaded(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;

            Log.e("1111","onInterstitialAdDataLoaded");
        }

        @Override
        public void onInterstitialAdLoaded(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            adapterList.add(adapter);
            mCallbackListener.onInterstitialAdLoaded();
            SPUtil.putInt(mApplcationContext, Const.SPU_NAME,Const.SPU_IS_READY,1);
            Log.e("1111","onInterstitialAdLoaded");
        }

        @Override
        public void onInterstitialAdLoadFail(CustomInterstitialAdapter adapter, AdError adError) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdLoadFail(adError);
            Log.e("1111",adError.getPlatformMSG()+adError.getPlatformCode());
        }

        @Override
        public void onInterstitialAdClicked(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdClicked();
        }

        @Override
        public void onInterstitialAdShow(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdShow();
        }

        @Override
        public void onInterstitialAdClose(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdClose();
        }

        @Override
        public void onInterstitialAdVideoStart(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdVideoStart();
        }

        @Override
        public void onInterstitialAdVideoEnd(CustomInterstitialAdapter adapter) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdVideoEnd();
        }

        @Override
        public void onInterstitialAdVideoError(CustomInterstitialAdapter adapter, AdError adError) {
            mAdapter = adapter;
            mCallbackListener.onInterstitialAdVideoError(adError);
        }
    };

    protected MediationGroupManager(Context context) {
        super(context);
    }


    public void setCallbackListener(LCInterstitialListener listener) {
        mCallbackListener = listener;
    }

    protected void loadInterstitialAd(String placementId, RealmModel placeStrategy, PlaceStrategy.UnitGroupInfo list, CountDownTimer timer ) {
        super.loadAd(placementId, placeStrategy, list,timer);
    }

    @Override
    public void onDevelopLoaded() {

    }

    @Override
    public void onDeveloLoadFail(AdError adError) {

    }

    @Override
    public void startLoadAd(LoongCheerBaseAdapter baseAdapter, PlaceStrategy.UnitGroupInfo unitGroupInfo,String placementId, CountDownTimer timer ) {
        if (baseAdapter instanceof CustomInterstitialAdapter) {
            CustomInterstitialAdapterParser.loadInterstitialAd(mActivityRef.get(), (CustomInterstitialAdapter) baseAdapter, unitGroupInfo,placementId, timer,customInterstitialListener);
        }
    }

    public static CustomInterstitialAdapter getmAdapter() {
        return mAdapter;
    }

    public static List<CustomInterstitialAdapter> getmAdapterList(){
        return adapterList;
    }
}
