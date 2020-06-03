package com.loongcheer.interstitial.business;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.LCInitMediation;
import com.loongcheer.core.common.CommonAdManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.CommonDeviceUtil;
import com.loongcheer.core.common.utils.CommonLogUtil;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.interstitial.api.LCInterstitial;
import com.loongcheer.interstitial.api.LCInterstitialListener;
import com.loongcheer.interstitial.custom.api.CustomInterstitialAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdLoadManager extends CommonAdManager {

    public static final String TAG = AdLoadManager.class.getSimpleName();

    List<RealmModel> interstitialList;

    CustomInterstitialAdapter customInterstitialAdapter;

    List<CustomInterstitialAdapter> adapterList;

    CountDownTimer timer;

    int count = 0;

    PlaceStrategy.UnitGroupInfo unitGroupInfo;

    public static AdLoadManager getInstance(Context context, String placementId) {

        CommonAdManager adLoadManager = CommonAdManager.getInstance(placementId);
        if (adLoadManager == null || !(adLoadManager instanceof AdLoadManager)) {
            adLoadManager = new AdLoadManager(context, placementId);
            CommonAdManager.addAdManager(placementId, adLoadManager);
        }
        adLoadManager.refreshContext(context);
        return (AdLoadManager) adLoadManager;
    }


    private AdLoadManager(Context context, String placementId) {
        super(context, placementId);
    }



    public void onPause() {

    }

    public void onResume() {
    }

    public void onDestory() {
    }


    /**
     * 广告请求
     *
     * @param listener
     */
    public void startLoadAd(final Context context, final String advPlacememtId , final LCInterstitialListener listener){

        count = 0;
        List<RealmModel> list =  DBContext.check();

        interstitialList = new ArrayList<>();

        if (list != null){

            for (RealmModel realmModel : list){

                if (realmModel.getAdvPlacememtId().equals(advPlacememtId)){
                    interstitialList.add(realmModel);
                }

            }

            if (interstitialList != null){

                final int requestNum = interstitialList.get(0).getRequestNum();

                timer = new CountDownTimer(1000000+500,250000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //每25s去检查数据库中的Onload广告是否满足最低请求数
                        List<RealmModel> booleanList = new ArrayList<>();
                        for (RealmModel model:interstitialList){
                            if (model.isStatus()){
                                booleanList.add(model);
                            }
                        }
                        if ( booleanList == null||booleanList.size()<requestNum){

                            for (int i =count;i<requestNum+count;i++){

                                if ((requestNum+count)>interstitialList.size()){
                                    return;
                                }
                                String platformName = interstitialList.get(i).getPlatformName();
                                String className = getMethod(platformName,2);
                                String placementId = interstitialList.get(i).getPlacementId();
                                String appId = interstitialList.get(i).getAppId();
                                String sdKKey = interstitialList.get(i).getSdkKey();
                                String appKey = interstitialList.get(i).getAppKey();
                                String unitId = interstitialList.get(i).getUnitId();
                                String gameId = interstitialList.get(i).getGameId();
                                unitGroupInfo = new PlaceStrategy.UnitGroupInfo();
                                unitGroupInfo.setAdapterClassName(className);
                                unitGroupInfo.setAdvPlacementId(advPlacememtId);
                                unitGroupInfo.setPlatformName(platformName);
                                unitGroupInfo.setAppId(appId);
                                unitGroupInfo.setSdkKey(sdKKey);
                                unitGroupInfo.setAppKey(appKey);
                                unitGroupInfo.setUnitId(unitId);
                                unitGroupInfo.setGameId(gameId);
                                MediationGroupManager mediationGroupManager = new MediationGroupManager(mApplicationContext);
                                mediationGroupManager.setCallbackListener(listener);
                                mediationGroupManager.loadInterstitialAd(placementId,interstitialList.get(i),unitGroupInfo,timer);

                                Log.e(TAG, "onTick: "+"计数器数值为"+count );
                            }
                            count = requestNum;
                        }else {
                            if (timer!=null){
                                timer.cancel();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        Log.e("onFinish InterstitialAd","is not ad");
                    }
                };
                timer.start();

            }

        }else {
            AdError adError = new AdError("500","LCSDK is not init, try again later","","");
            listener.onInterstitialAdLoadFail(adError);
        }


    }

    private String getMethod(String platform,int advType){
        String method="";
        switch (platform){
            case "facebook":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Facebook",advType);
                break;
            case "admob":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Admob",advType);
                break;
            case "vungle":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Vungle",advType);
                break;
            case "ironsource":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Ironsource",advType);
                break;
            case "mintegral":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Mintegral",advType);
                break;
            case "unity":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("Unity",advType);
                break;
            case "applovin":
                method = Const.CLASS_PACKAGE_NAME+platform+"."+getClassName("AppLovin",advType);
                break;

        }
        return method;
    }


    private String getClassName(String platform ,int advType){
        String name = "";
        switch (advType){
            case 1://激励视频广告
                name = platform+"LCReworkVideoAdapter";
                break;
            case 2://插屏广告
                name = platform+"LCInterstitialAdapter";
                break;
            case 3://横幅广告
                name = platform+"LCBannerAdapter";
                break;
        }
        return name;
    }



    public void show() {
        int isReady = SPUtil.getInt(mApplicationContext,Const.SPU_NAME,Const.SPU_IS_READY,0);
        if (isReady == 1){
            DBContext.init(mApplicationContext);
            customInterstitialAdapter = MediationGroupManager.getmAdapter();
            adapterList = MediationGroupManager.getmAdapterList();
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
//                    customInterstitialAdapter.show(mApplicationContext);

                        for (int i = 0 ; i < interstitialList.size(); i++){
                            for (int j = 0; j < adapterList.size();j++){
                                if (adapterList.get(j).getNetworkName().equals(interstitialList.get(i).getPlatformName())){
                                    adapterList.get(j).show(mApplicationContext);
                                    adapterList.remove(j);
                                    return;
                                }
                            }
                        }

                }
            });
            if (unitGroupInfo != null){
                DBContext.modify(unitGroupInfo.getAdvPlacementId()+unitGroupInfo.getPlatformName(),false);
            }
            if (adapterList.size()<=1){
                SPUtil.putInt(mApplicationContext,Const.SPU_NAME,Const.SPU_IS_READY,0);
            }
        }
    }
}
