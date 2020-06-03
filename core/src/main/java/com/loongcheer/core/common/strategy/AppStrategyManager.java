package com.loongcheer.core.common.strategy;

import android.content.Context;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.LCSDKInitListener;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.bean.InfoBean;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.net.AppStrategyLoader;
import com.loongcheer.core.common.net.OnHttpLoaderListener;
import com.loongcheer.core.common.utils.CommonLogUtil;

public class AppStrategyManager {

    public static final String TAG = AppStrategyManager.class.getSimpleName();
    private static AppStrategyManager mInstance = null;
    private Context mContext;
    private boolean isLoading;


    private AppStrategyManager(Context context) {
        mContext = context;
        isLoading = false;
    }

    public static AppStrategyManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppStrategyManager.class) {
                if (mInstance == null) {
                    mInstance = new AppStrategyManager(context);
                }
            }
        }
        return mInstance;
    }

    private Context getContext() {
        return mContext;
    }

    private void setContext(Context context) {
        mContext = context;
    }



    public void startRequest(String appid, String appkey, OnHttpLoaderListener listener) {
        if (isLoading) {
            return;
        }
        AppStrategyLoader appStrategyLoader = new AppStrategyLoader(mContext, appid, appkey);
        appStrategyLoader.start(0, listener);
    }

    /***
     * Appsetting request
     */
    public void startRequest(final String appid, final String appkey, final LCSDKInitListener lcsdkInitListener) {
        startRequest(appid, appkey, new OnHttpLoaderListener() {
            @Override
            public void onLoadStart(int reqCode) {
                isLoading = true;
            }

            @Override
            public void onLoadFinish(int reqCode, Object result) {

                try {
                DBContext.init(SDKContext.getInstance().getContext());
                isLoading = false;
                if (result != null) {
                    String json = String.valueOf(result);
                    Log.e("111" ,json);
                    InfoBean bean = InfoBean.stringFromData(json);
                    DBContext.add(bean);
                    lcsdkInitListener.onSuccess();
                } else {
                    CommonLogUtil.e(TAG, "app strg f!");
                }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadError(int reqCode, String msg, AdError errorBean) {
                isLoading = false;
                CommonLogUtil.e(TAG, "app strg f!" + msg);
                lcsdkInitListener.onFail(msg);
            }

            @Override
            public void onLoadCanceled(int reqCode) {
                isLoading = false;
            }
        });
    }

}
