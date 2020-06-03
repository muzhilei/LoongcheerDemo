package com.loongcheer.banner.business.utils;

import android.content.Context;
import android.os.CountDownTimer;


import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.banner.custom.api.CustomBannerListener;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;


import java.util.Map;

/**
 * Created by Z on 2018/1/9.
 * BannerAdapter parser
 */

public final class CustomBannerAdapterParser {
    private CustomBannerAdapterParser() {
    }

    public static CustomBannerAdapter createBannerAdapter(final PlaceStrategy.UnitGroupInfo unitGroupInfo) {
        CustomBannerAdapter customBannerAdapter;

        try {
            customBannerAdapter = CustomBannerFactory.create(unitGroupInfo.adapterClassName);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        return customBannerAdapter;
    }

    public static void loadBannerAd(final LCBannerView bannerView, final Context activity, final CustomBannerAdapter customBannerAdapter,
                                    final PlaceStrategy.UnitGroupInfo unitGroupInfo,
                                    final String placementId,
                                    final CountDownTimer timer,
                                    final CustomBannerListener customListener) {

        // Custom event classes can be developed by any third party and may not be tested.
        // We catch all exceptions here to prevent crashes from untested code.

        SDKContext.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    customBannerAdapter.loadBannerAd(bannerView,
                            activity,
                            placementId,
                            unitGroupInfo,
                            timer,
                            customListener);
                } catch (Throwable e) {
                    e.printStackTrace();
                    AdError adError = ErrorCode.getErrorCode(ErrorCode.adapterInnerError, "", e.getMessage());
                    customListener.onBannerAdLoadFail(customBannerAdapter, adError);
                }
            }
        });

    }


}