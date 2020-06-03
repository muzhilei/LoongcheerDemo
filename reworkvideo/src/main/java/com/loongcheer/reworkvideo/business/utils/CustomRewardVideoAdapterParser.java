package com.loongcheer.reworkvideo.business.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoListener;


import java.util.Map;

/**
 * Created by Z on 2018/1/9.
 */

public final class CustomRewardVideoAdapterParser {
    private CustomRewardVideoAdapterParser() {
    }

    public static void loadRewardVideoAd(final Context activity, final CustomRewardVideoAdapter customRewardVideoAdapter,
                                         final PlaceStrategy.UnitGroupInfo unitGroupInfo,
                                         final String placementId,
                                         final CountDownTimer timer,
                                         final CustomRewardVideoListener customListener) {

        // Custom event classes can be developed by any third party and may not be tested.
        // We catch all exceptions here to prevent crashes from untested code.

        SDKContext.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    customRewardVideoAdapter.loadRewardVideoAd(
                            activity,
                            placementId,
                            unitGroupInfo,
                            timer,
                            customListener);
                } catch (Throwable e) {
                    e.printStackTrace();
                    AdError adError = ErrorCode.getErrorCode(ErrorCode.adapterInnerError, "", e.getMessage());
                    customListener.onReworkVideoAdLoadFail(customRewardVideoAdapter, adError);
                }
            }
        });

    }


}