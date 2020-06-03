package com.loongcheer.interstitial.custom.api;

import android.content.Context;
import android.os.CountDownTimer;

import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.util.Map;

public abstract class CustomInterstitialAdapter extends LoongCheerBaseAdapter {

    protected CustomInterstitialListener mLoadResultListener;
    protected CustomInterstitialEventListener mImpressListener;

    public abstract void loadInterstitialAd(final Context context,
                                            final String placementId,
                                            final PlaceStrategy.UnitGroupInfo unitGroupInfo,
                                            final CountDownTimer timer,
                                            final CustomInterstitialListener customInterstitialListener);

    public abstract void show(Context context);

    public abstract void onResume();

    public abstract void onPause();

    public void setCustomInterstitialEventListener(CustomInterstitialEventListener listener) {
        mImpressListener = listener;
    }

    public void clearLoadListener() {
        mLoadResultListener = null;
    }

    public void clearImpressionListener() {
        mImpressListener = null;
    }

}
