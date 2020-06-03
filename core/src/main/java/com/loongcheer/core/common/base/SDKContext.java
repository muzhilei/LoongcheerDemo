package com.loongcheer.core.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.api.LCSDKInitListener;
import com.loongcheer.core.common.strategy.AppStrategyManager;
import com.loongcheer.core.common.utils.CommonDeviceUtil;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.core.common.utils.task.TaskManager;

import java.lang.reflect.Method;

public class SDKContext {

    private final String TAG = "SDK.init";
    private static SDKContext instance;

    private Context mContext;
    private String mAppId;
    private String mAppKey;
    private Handler mHandler;

    private Activity mActivity;

    public static SDKContext getInstance() {
        if (instance == null) {
            synchronized (SDKContext.class) {
                instance = new SDKContext();
            }
        }
        return instance;
    }

    public SDKContext() {
        mHandler = new Handler(Looper.getMainLooper());
    }


    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public String getAppId() {
        if (TextUtils.isEmpty(mAppId)) {
            mAppId = SPUtil.getString(mContext, Const.SPU_NAME, Const.SPU_APPID, "");
        }
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
        SPUtil.putString(mContext, Const.SPU_NAME, Const.SPU_APPID, appId);
    }


    public String getAppKey() {
        if (TextUtils.isEmpty(mAppKey)) {
            mAppKey = SPUtil.getString(mContext, Const.SPU_NAME, Const.SPU_APPKEY, "");
        }
        return mAppKey;
    }

    public void setAppKey(String appKey) {
        this.mAppKey = appKey;
        SPUtil.putString(mContext, Const.SPU_NAME, Const.SPU_APPKEY, appKey);
    }

    /**
     * init
     *
     * @param context
     * @param appId
     * @param appKey
     */
    public void init(final Context context, final String appId, final String appKey, LCSDKInitListener lcsdkInitListener) {
        if (context == null) {
            return;
        }
        try {
            Context applicationContext = context.getApplicationContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                setActivity(activity);
            }
            setContext(applicationContext);
            setAppId(appId);
            setAppKey(appKey);
            SPUtil.putInt(context,Const.SPU_NAME,Const.SPU_IS_READY,0);

            initGlobalCommonPara();

            doInitWork(context, appId, appKey,lcsdkInitListener);

        } catch (Exception e) {

        }

    }


    private void doInitWork(final Context context, final String mAppId, final String appKey,LCSDKInitListener lcsdkInitListener) {

        checkAppStrategy(context,mAppId,mAppKey,lcsdkInitListener);
    }


    public void initGlobalCommonPara() {

        TaskManager.getInstance().run_proxy(new Runnable() {

            @Override
            public void run() {

                try {
                    CommonDeviceUtil.initCommonDeviceInfo(mContext);// Init Device info
                    try {
                        //Get gaid
                        Class clz = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
                        Class clzInfo = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
                        Method m = clz.getMethod("getAdvertisingIdInfo", Context.class);
                        Object o = m.invoke(null, mContext);
//                                Class<? extends Object> infoClass = o.getClass();

                        Method m2 = clzInfo.getMethod("getId");
                        String googleAdvertisingId = (String) m2.invoke(o);
                        CommonDeviceUtil.setGoogleAdId(googleAdvertisingId);

                    } catch (Exception e) {
//                                e.printStackTrace();
                        // try to get from google play app library
                        try {
                            AdvertisingIdClient.AdInfo adInfo = new AdvertisingIdClient().getAdvertisingIdInfo(mContext);
                            CommonDeviceUtil.setGoogleAdId(adInfo.getId());
                        } catch (Exception e1) {
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * Check AppSetting
     *
     * @param context
     * @param mAppId
     * @param appKey
     */
    public void checkAppStrategy(final Context context, final String mAppId, final String appKey, final LCSDKInitListener lcsdkInitListener) {
        //Update the AppSetting which is out of date
        TaskManager.getInstance().run_proxy(new Runnable() {
            @Override
            public void run() {

                AppStrategyManager.getInstance(context).startRequest(mAppId, appKey,lcsdkInitListener);

            }
        });
    }

    public void runOnMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void runOnThreadPool(Runnable runnable) {
        TaskManager.getInstance().run_proxy(runnable);
    }

    public void runOnMainThreadDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public void removeMainThreadRunnable(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public void runOnThreadPoolsDelayed(Runnable runnable, long delayMillis) {
        TaskManager.getInstance().run_proxyDelayed(runnable, delayMillis);
    }



}
