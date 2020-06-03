package com.loongcheer.reworkvideo.custom.api;

import com.loongcheer.core.api.AdError;

public interface CustomRewardedVideoEventListener {
    public void onRewardedVideoAdPlayStart(CustomRewardVideoAdapter customRewardVideoAd);

    public void onRewardedVideoAdPlayEnd(CustomRewardVideoAdapter customRewardVideoAd);

    public void onRewardedVideoAdPlayFailed(CustomRewardVideoAdapter customRewardVideoAd, AdError errorCode);

    public void onRewardedVideoAdClosed(CustomRewardVideoAdapter customRewardVideoAd);

    public void onRewardedVideoAdPlayClicked(CustomRewardVideoAdapter customRewardVideoAd);

    public void onReward(CustomRewardVideoAdapter customRewardVideoAdapter);
}
