package com.loongcheer.banner.business;

import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.core.api.AdError;

public interface InnerBannerListener {

    public void onBannerLoaded(CustomBannerAdapter adapter);

    public void onBannerFailed(CustomBannerAdapter adapter ,AdError adError);

    public void onBannerClicked(CustomBannerAdapter adapter);

    public void onBannerShow(CustomBannerAdapter adapter);

    public void onBannerClose(CustomBannerAdapter adapter);

}
