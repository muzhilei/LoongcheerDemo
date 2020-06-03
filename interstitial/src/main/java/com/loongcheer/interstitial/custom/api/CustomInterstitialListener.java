package com.loongcheer.interstitial.custom.api;

import com.loongcheer.core.api.AdError;

public interface CustomInterstitialListener {
    public void onInterstitialAdDataLoaded(CustomInterstitialAdapter adapter);
    public void onInterstitialAdLoaded(CustomInterstitialAdapter adapter);
    public void onInterstitialAdLoadFail(CustomInterstitialAdapter adapter, AdError adError);
    public void onInterstitialAdClicked(CustomInterstitialAdapter adapter);
    public void onInterstitialAdShow(CustomInterstitialAdapter adapter);
    public void onInterstitialAdClose(CustomInterstitialAdapter adapter);
    public void onInterstitialAdVideoStart(CustomInterstitialAdapter adapter);
    public void onInterstitialAdVideoEnd(CustomInterstitialAdapter adapter);
    public void onInterstitialAdVideoError(CustomInterstitialAdapter adapter, AdError adError);
}
