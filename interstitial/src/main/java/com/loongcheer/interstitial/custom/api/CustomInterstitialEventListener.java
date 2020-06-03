package com.loongcheer.interstitial.custom.api;

import com.loongcheer.core.api.AdError;

public interface CustomInterstitialEventListener {
    public void onInterstitialAdClicked(String msg);
    public void onInterstitialAdShow(String msg);
    public void onInterstitialAdClose(String msg);
    public void onInterstitialAdVideoStart(String msg);
    public void onInterstitialAdVideoEnd(String msg);
    public void onInterstitialAdVideoError(String msg, AdError adError);
}
