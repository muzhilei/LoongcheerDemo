package com.loongcheer.network.mintegral;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.interstitial.view.MTGInterstitialActivity;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.mtgbanner.view.MTGBannerWebView;
import com.mintegral.msdk.mtgbid.out.BidManager;
import com.mintegral.msdk.mtgjscommon.base.BaseWebView;
import com.mintegral.msdk.mtgnative.a.b;
import com.mintegral.msdk.nativex.view.MTGMediaView;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.playercommon.PlayerView;
import com.mintegral.msdk.reward.player.MTGRewardVideoActivity;
import com.mintegral.msdk.video.js.bridge.RewardJs;
import com.mintegral.msdk.videofeeds.vfplayer.VideoFeedsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MintegralLCInitManager extends LCInitMediation {

    public static final String TAG = MintegralLCInitManager.class.getSimpleName();

    private String mAppId;
    private String mAppKey;
    private final Handler mHandler;
    private static MintegralLCInitManager sInstance;

    private MintegralLCInitManager() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static MintegralLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new MintegralLCInitManager();
        }
        return sInstance;
    }


    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        initSDK(context, info, null);
    }


    public void initSDK(final Context context, final PlaceStrategy.UnitGroupInfo info, final InitCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String appid = info.getAppId();
                String appkey = info.getAppKey();

                if (!TextUtils.isEmpty(appid) && !TextUtils.isEmpty(appkey)) {
                    try {
                        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppKey) || !TextUtils.equals(mAppId, appid) || !TextUtils.equals(mAppKey, appkey)) {
                            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
                            Map<String, String> map = sdk.getMTGConfigurationMap(appid, appkey);

                            int gdp_consent = SPUtil.getInt(context, Const.SPU_NAME,Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL,0);
                            int open = gdp_consent == 1 ? MIntegralConstans.IS_SWITCH_ON : MIntegralConstans.IS_SWITCH_OFF;
                            String level = MIntegralConstans.AUTHORITY_ALL_INFO;
                            sdk.setUserPrivateInfoType(context, level, open);

                            sdk.init(map, context.getApplicationContext());
                            mAppId = appid;
                            mAppKey = appkey;

                            if (callback != null) {
                                callback.onSuccess();
                            }
                        } else {
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();

                        if (callback != null) {
                            callback.onError(e);
                        }
                    }
                }
            }
        });
    }


    public interface InitCallback {
        void onSuccess();

        void onError(Throwable e);
    }

    @Override
    public String getNetworkName() {
        return "mintegral";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.mintegral.msdk.MIntegralSDK";
    }

    @Override
    public Map<String, Boolean> getPluginClassStatus() {
        HashMap<String, Boolean> pluginMap = new HashMap<>();
        pluginMap.put("mintegral_alphab.aar", false);
        pluginMap.put("mintegral_interstitial.aar", false);
        pluginMap.put("mintegral_interstitialvideo.aar", false);
        pluginMap.put("mintegral_mtgbanner.aar", false);
        pluginMap.put("mintegral_mtgbid.aar", false);
        pluginMap.put("mintegral_mtgjscommon.aar", false);
        pluginMap.put("mintegral_mtgnative.aar", false);
        pluginMap.put("mintegral_nativeex.aar", false);
        pluginMap.put("mintegral_playercommon.aar", false);
        pluginMap.put("mintegral_reward.aar", false);
        pluginMap.put("mintegral_videocommon.aar", false);
        pluginMap.put("mintegral_videofeeds.aar", false);
        pluginMap.put("mintegral_videojs.aar", false);

        if(MintegralLCConst.isChinaSdk()) {//国内版
            pluginMap.put("mintegral_mtgdownloads.aar", false);
        }

        Class clazz;
//        try {
//            clazz = AlphabReceiver.class;
//            pluginMap.put("mintegral_alphab.aar", true);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

        try {
            clazz = MTGInterstitialActivity.class;
            pluginMap.put("mintegral_interstitial.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = MTGInterstitialVideoHandler.class;
            pluginMap.put("mintegral_interstitialvideo.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = MTGBannerWebView.class;
            pluginMap.put("mintegral_mtgbanner.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = BidManager.class;
            pluginMap.put("mintegral_mtgbid.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = BaseWebView.class;
            pluginMap.put("mintegral_mtgjscommon.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = b.class;
            pluginMap.put("mintegral_mtgnative.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = MTGMediaView.class;
            pluginMap.put("mintegral_nativeex.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = PlayerView.class;
            pluginMap.put("mintegral_playercommon.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = MTGRewardVideoActivity.class;
            pluginMap.put("mintegral_reward.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = RewardJs.class;
            pluginMap.put("mintegral_videocommon.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = VideoFeedsActivity.class;
            pluginMap.put("mintegral_videofeeds.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
//
//        try {
//            clazz = VideoWebViewActivity.class;
//            pluginMap.put("mintegral_videojs.aar", true);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

        try {
            if(MintegralLCConst.isChinaSdk()) {
                //国内版
                Class.forName("com.mintegral.msdk.pluginFramework.PluginService");
                pluginMap.put("mintegral_mtgdownloads.aar", true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return pluginMap;
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.mintegral.msdk.activity.MTGCommonActivity");
        list.add("com.mintegral.msdk.reward.player.MTGRewardVideoActivity");
        list.add("com.mintegral.msdk.interstitial.view.MTGInterstitialActivity");
        return list;
    }

    @Override
    public List getServiceStatus() {
        if(MintegralLCConst.isChinaSdk()) {
            ArrayList<String> list = new ArrayList<>();
            list.add("com.mintegral.msdk.shell.MTGService");
            return list;
        }
        return null;
    }

    @Override
    public List getProviderStatus() {
        if(MintegralLCConst.isChinaSdk()) {
            //国内版
            ArrayList<String> list = new ArrayList<>();
            list.add("com.mintegral.msdk.base.utils.MTGFileProvider");
            return list;
        }
        return null;

    }
}
