package com.loongcheer.interstitial.business.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;


import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;
import com.loongcheer.interstitial.custom.api.CustomInterstitialListener;


import java.util.Map;

/**
 * Created by Z on 2018/1/9.
 * 通过反射拿到Adapter对象来广告加载
 */

public final class CustomInterstitialAdapterParser {

    private CustomInterstitialAdapterParser() {
    }

    public static CustomInterstitialAdapter createInterstitialAdapter(final PlaceStrategy.UnitGroupInfo unitGroupInfo) {
        CustomInterstitialAdapter customInterstitialAdapter;
        try {
            customInterstitialAdapter = CustomInterstitialFactory.create(unitGroupInfo.adapterClassName);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        return customInterstitialAdapter;
    }

    public static void loadInterstitialAd(final Context activity, final CustomInterstitialAdapter customInterstitialAdapter,
                                          final PlaceStrategy.UnitGroupInfo unitGroupInfo,
                                          final String placementid,
                                          final CountDownTimer timer,
                                          final CustomInterstitialListener customListener) {

        // Custom event classes can be developed by any third party and may not be tested.
        // We catch all exceptions here to prevent crashes from untested code.

        SDKContext.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    customInterstitialAdapter.loadInterstitialAd(
                            activity,
                            placementid,
                            unitGroupInfo,
                            timer,
                            customListener);
                } catch (Throwable e) {
                    e.printStackTrace();
                    AdError adError = ErrorCode.getErrorCode(ErrorCode.adapterInnerError, "", e.getMessage());
                    customListener.onInterstitialAdLoadFail(customInterstitialAdapter, adError);
                }
            }
        });

    }


}