package com.loongcheer.banner.custom.api;

import android.content.Context;
import android.os.CountDownTimer;


import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.banner.business.BaseBannerAdapter;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.Map;

public abstract class CustomBannerAdapter extends BaseBannerAdapter {

    public abstract void loadBannerAd(final LCBannerView bannerView, final Context activity, final String placementId, PlaceStrategy.UnitGroupInfo unitGroupInfo
            , final CountDownTimer timer , final CustomBannerListener customBannerListener);


    @Override
    public boolean isAdReady() {
        return getBannerView() != null;
    }
}
