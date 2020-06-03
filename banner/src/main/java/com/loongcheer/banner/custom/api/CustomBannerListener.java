package com.loongcheer.banner.custom.api;


import com.loongcheer.core.api.AdError;

/**
 * Used by Mediation
 */
public interface CustomBannerListener {
    public void onBannerAdLoaded(CustomBannerAdapter customBannerAd); //Callback of Ad success

    public void onBannerAdLoadFail(CustomBannerAdapter customBannerAd, AdError adError);//Callback of Ad fail

    public void onBannerAdClicked(CustomBannerAdapter customBannerAd);//Callback of Ad click

    public void onBannerAdShow(CustomBannerAdapter customBannerAd);////Callback of Ad impression

    public void onBannerAdClose(CustomBannerAdapter customBannerAd);////Callback of Ad close

}
