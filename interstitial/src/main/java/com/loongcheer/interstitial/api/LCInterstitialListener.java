package com.loongcheer.interstitial.api;

import com.loongcheer.core.api.AdError;

public interface LCInterstitialListener {

    public void onInterstitialAdLoaded();

    public void onInterstitialAdLoadFail(AdError adError);

    public void onInterstitialAdClicked();

    public void onInterstitialAdShow();

    public void onInterstitialAdClose();

    public void onInterstitialAdVideoStart();


    public void onInterstitialAdVideoEnd();

    public void onInterstitialAdVideoError(AdError adError);
}
