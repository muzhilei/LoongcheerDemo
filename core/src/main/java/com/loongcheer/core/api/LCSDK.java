package com.loongcheer.core.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.base.UploadDataLevelManager;
import com.loongcheer.core.common.utils.SPUtil;

import org.json.JSONObject;

public class LCSDK {

    /**
     * GDPR LEVEL
     */
    public static final int PERSONALIZED = 0;
    public static final int NONPERSONALIZED = 1;


    /**
     * Mark of SDK init
     */
    private static boolean HAS_INIT = false;

    private LCSDK(){


    }


    /**
     * Debug log switch
     */
    public static boolean NETWORK_LOG_DEBUG = false;


    /**
     * sdk初始化
     *
     * @param context
     * @param appId
     * @param appKey
     */
    public static void init(Context context, String appId, String appKey) {
        init(context, appId, appKey, null);

    }

    /**
     * SDK init
     *
     * @param context
     * @param appId
     * @param appKey
     */
    public static void init(Context context, String appId, String appKey, LCSDKInitListener listener) {
        try {
            if (context == null) {
                if (listener != null) {
                    listener.onFail("init: Context is null!");
                }
                Log.e(Const.RESOURCE_HEAD, "init: Context is null!");
                return;
            }

//
//            if (!HAS_INIT) {
//                HAS_INIT = true;
                SDKContext.getInstance().init(context, appId, appKey,listener);
//            }

//            TaskManager.getInstance().run_proxy(new Runnable() {
//                @Override
//                public void run() {
//                    OffLineTkManager.getInstance().tryToReSendRequest();
//                }
//            });


        } catch (Exception ex) {
            if (Const.DEBUG) {
                ex.printStackTrace();
            }

        } catch (Error e) {

        }
    }


    /**
     * GDPR LEVEL Setting
     */
    public static void setGDPRUploadDataLevel(Context context, int level) {
        if (context == null) {
            Log.e(Const.RESOURCE_HEAD, "setGDPRUploadDataLevel: context should not be null");
            return;
        }

        /**Can't not set without PERSONALIZED and NONPERSONALIZED **/
        if (level == PERSONALIZED || level == NONPERSONALIZED) {
            UploadDataLevelManager.getInstance(context).setUploadDataLevel(level);
        } else {
            Log.e(Const.RESOURCE_HEAD, "GDPR level setting error!!! Level must be PERSONALIZED or NONPERSONALIZED.");
        }

    }


    /**
     * Show GDPR Activity
     *
     * @param context
     */
    public static void showGdprAuth(Activity context) {
        UploadDataLevelManager.getInstance(context).showUploadDataNotifyDialog(context, null);
    }

    /**
     * Show GDPR Activity with callback
     *
     * @param context
     */
    public static void showGdprAuth(Activity context, LCGDPRAuthCallback callback) {
        UploadDataLevelManager.getInstance(context).showUploadDataNotifyDialog(context, callback);
    }

    /**
     * SDK Version
     *
     * @return
     */
    public static String getSDKVersionName() {
        return Const.SDK_VERSION_NAME;
    }

    public static void apiLog(String placementId, String adType, String apiStr, String result, String extra) {
        if (NETWORK_LOG_DEBUG) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("placementId", placementId);
                jsonObject.put("adtype", adType);
                jsonObject.put("api", apiStr);
                jsonObject.put("result", result);
                jsonObject.put("reason", extra);
                Log.i(Const.RESOURCE_HEAD + "_network", jsonObject.toString());
            } catch (Throwable e) {

            }
        }
    }

}
