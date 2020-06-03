package com.loongcheer.banner.business;

import android.content.Context;
import android.os.CountDownTimer;

import com.loongcheer.banner.api.LCBannerListener;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.business.utils.CustomBannerAdapterParser;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.CommonMediationManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;

import java.util.List;

public class MediationGroupManager extends CommonMediationManager  {

    private LCBannerView mBannerView;

    InnerBannerListener mCallbackListener;

    private CustomBannerListener customBannerListener = new CustomBannerListener() {
        @Override
        public void onBannerAdLoaded(CustomBannerAdapter customBannerAd) {
            mCallbackListener.onBannerLoaded(customBannerAd);
        }

        @Override
        public void onBannerAdLoadFail(CustomBannerAdapter customBannerAd, AdError adError) {
            mCallbackListener.onBannerFailed(customBannerAd,adError);
        }

        @Override
        public void onBannerAdClicked(CustomBannerAdapter customBannerAd) {
            mCallbackListener.onBannerClicked(customBannerAd);
        }

        @Override
        public void onBannerAdShow(CustomBannerAdapter customBannerAd) {
            mCallbackListener.onBannerShow(customBannerAd);
        }

        @Override
        public void onBannerAdClose(CustomBannerAdapter customBannerAd) {
            mCallbackListener.onBannerClose(customBannerAd);
        }
    };

    public void setCallbackListener(InnerBannerListener listener) {
        mCallbackListener = listener;
    }

    protected MediationGroupManager(Context context) {
        super(context);
    }



    protected void loadBannerAd(LCBannerView bannerView, String placementId, RealmModel placeStrategy, PlaceStrategy.UnitGroupInfo info, CountDownTimer timer) {
        mBannerView = bannerView;
        super.loadAd(placementId, placeStrategy, info,timer);
    }
    @Override
    public void onDevelopLoaded() {

    }

    @Override
    public void onDeveloLoadFail(AdError adError) {

    }

    @Override
    public void startLoadAd(LoongCheerBaseAdapter baseAdapter, PlaceStrategy.UnitGroupInfo unitGroupInfo, String placementId, CountDownTimer timer ) {
        if (baseAdapter instanceof CustomBannerAdapter) {
            CustomBannerAdapterParser.loadBannerAd(mBannerView, mApplcationContext, (CustomBannerAdapter) baseAdapter, unitGroupInfo, placementId,timer, customBannerListener);
        }

    }

}
