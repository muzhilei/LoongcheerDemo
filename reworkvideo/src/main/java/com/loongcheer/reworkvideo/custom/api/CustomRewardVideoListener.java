package com.loongcheer.reworkvideo.custom.api;

import com.loongcheer.core.api.AdError;

public interface CustomRewardVideoListener {

    public void onReworkVideoAdDataLoaded(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdLoaded(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdLoadFail(CustomRewardVideoAdapter adapter, AdError adError);
    public void onReworkVideoAdClicked(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdShow(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdClose(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdVideoStart(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdVideoEnd(CustomRewardVideoAdapter adapter);
    public void onReworkVideoAdVideoError(CustomRewardVideoAdapter adapter, AdError adError);

}
