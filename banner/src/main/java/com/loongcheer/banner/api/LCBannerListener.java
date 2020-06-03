package com.loongcheer.banner.api;

import com.loongcheer.core.api.AdError;

public interface LCBannerListener {

    public void onBannerLoaded();

    public void onBannerFailed(AdError adError);

    public void onBannerClicked();

    public void onBannerShow();

    public void onBannerClose();

    public void onBannerAutoRefreshed();

    public void onBannerAutoRefreshFail(AdError adError);
}
