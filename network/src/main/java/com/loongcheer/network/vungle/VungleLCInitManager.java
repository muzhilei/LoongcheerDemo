package com.loongcheer.network.vungle;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.gson.Gson;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.moat.analytics.mobile.vng.MoatAdEvent;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VungleLCInitManager extends LCInitMediation {

    private static final String TAG = VungleLCInitManager.class.getSimpleName();
    private String mAppId;
    private static VungleLCInitManager sInstance;

    private VungleLCInitManager() {

    }

    public static VungleLCInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new VungleLCInitManager();
        }
        return sInstance;
    }

    protected void initSDK(Context context, PlaceStrategy.UnitGroupInfo unitGroupInfo, final InitListener listener) {
        final String appId = unitGroupInfo.getAppId();

        if (TextUtils.isEmpty(mAppId) || !TextUtils.equals(mAppId, appId)) {

            try {

                    //GDPR Consent
                    int gdp_consent = SPUtil.getInt(context, Const.SPU_NAME,Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL,0);

                    if (gdp_consent == 1) {
                        Vungle.updateConsentStatus(gdp_consent==1? Vungle.Consent.OPTED_IN : Vungle.Consent.OPTED_OUT, "1.0.0");
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }

            Vungle.init(appId, context, new InitCallback() {
                @Override
                public void onSuccess() {
                    mAppId = appId;
                    if (listener != null) {
                        listener.onSuccess();
                    }
                }

                @Override
                public void onError(VungleException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }


                @Override
                public void onAutoCacheAdAvailable(String s) {

                }
            });
        } else {
            if (listener != null) {
                listener.onSuccess();
            }
        }
    }

    @Override
    public void initSDK(Context context, PlaceStrategy.UnitGroupInfo info) {
        initSDK(context, info, null);
    }

    public interface InitListener {
        void onSuccess();

        void onError(Throwable throwable);
    }

    @Override
    public String getNetworkName() {
        return "vungle";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.vungle.warren.Vungle";
    }

    @Override
    public Map<String, Boolean> getPluginClassStatus() {
        HashMap<String, Boolean> pluginMap = new HashMap<>();
        pluginMap.put("play-services-ads-identifier-*.aar", false);
        pluginMap.put("play-services-basement-*.aar", false);

        pluginMap.put("converter-gson-*.aar", false);
        pluginMap.put("gson-*.aar", false);
        pluginMap.put("logging-interceptor-*.aar", false);
        pluginMap.put("okhttp-*.jar", false);
        pluginMap.put("okio-*.jar", false);
        pluginMap.put("retrofit-*.jar", false);
        pluginMap.put("vng-moat-mobile-app-kit-*.jar", false);


        Class clazz;
        try {
            clazz = AdvertisingIdClient.class;
            pluginMap.put("play-services-ads-identifier-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = GoogleSignatureVerifier.class;
            pluginMap.put("play-services-basement-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = GsonConverterFactory.class;
            pluginMap.put("converter-gson-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = Gson.class;
            pluginMap.put("gson-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = HttpLoggingInterceptor.Logger.class;
            pluginMap.put("logging-interceptor-*.aar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = OkHttpClient.class;
            pluginMap.put("okhttp-*.jar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = Okio.class;
            pluginMap.put("okio-*.jar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = Retrofit.class;
            pluginMap.put("retrofit-*.jar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            clazz = MoatAdEvent.class;
            pluginMap.put("vng-moat-mobile-app-kit-*.jar", true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return pluginMap;
    }

    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.vungle.warren.ui.VungleActivity");
        list.add("com.vungle.warren.ui.VungleFlexViewActivity");
        list.add("com.vungle.warren.ui.VungleWebViewActivity");
        return list;
    }

}
