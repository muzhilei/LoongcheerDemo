package com.loongcheer.reworkvideo.api;

import com.loongcheer.core.api.AdError;

public interface LCRewardVideoListener {

    public void onRewardedVideoAdLoaded();

    public void onRewardedVideoAdFailed(AdError errorCode);

    public void onRewardedVideoAdPlayStart();

    public void onRewardedVideoAdPlayEnd();

    public void onRewardedVideoAdPlayFailed(AdError errorCode);

    public void onRewardedVideoAdClosed();

    public void onRewardedVideoAdPlayClicked();

    public void onReward();
}
