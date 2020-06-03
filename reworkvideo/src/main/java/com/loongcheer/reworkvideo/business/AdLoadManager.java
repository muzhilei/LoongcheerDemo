package com.loongcheer.reworkvideo.business;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.CommonAdManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.reworkvideo.api.LCRewardVideoListener;
import com.loongcheer.reworkvideo.custom.api.CustomRewardVideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdLoadManager extends CommonAdManager {


    public static final String TAG = AdLoadManager.class.getSimpleName();

    CustomRewardVideoAdapter customInterstitialAdapter;

    List<CustomRewardVideoAdapter> adapterList;
    List<RealmModel> rewardedList;

    CountDownTimer timer;

    int count = 0;
    int num = 0;

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


    public AdLoadManager(Context context, String placementId) {
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
    public void startLoadAd(final Context context, final String advPlacememtId, final LCRewardVideoListener listener){

        count = 0;

        List<RealmModel> list =  DBContext.check();

        rewardedList = new ArrayList<>();

        if (list != null){

            for (RealmModel realmModel : list){
                if (realmModel.getAdvPlacememtId().equals(advPlacememtId)){
                   rewardedList.add(realmModel);
                }
            }

            if (rewardedList != null){

                final int requestNum = rewardedList.get(0).getRequestNum();

                timer = new CountDownTimer(1000000+500,250000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //每25s去检查数据库中的Onload广告是否满足最低请求数
                        List<RealmModel> booleanList = new ArrayList<>();
                        for (RealmModel model:rewardedList){
                            if (model.isStatus()){
                                booleanList.add(model);
                            }
                        }
                        if ( booleanList == null||booleanList.size()<requestNum){

                            for (int i =count;i<requestNum+count;i++){

                                if ((requestNum+count)>rewardedList.size()){
                                    return;
                                }
                                String platformName = rewardedList.get(i).getPlatformName();
                                String className = getMethod(platformName,1);
                                String placementId = rewardedList.get(i).getPlacementId();
                                String appId = rewardedList.get(i).getAppId();
                                String sdkKey = rewardedList.get(i).getSdkKey();
                                String appKey = rewardedList.get(i).getAppKey();
                                String unitId = rewardedList.get(i).getUnitId();
                                String gameId = rewardedList.get(i).getGameId();
                                unitGroupInfo = new PlaceStrategy.UnitGroupInfo();
                                unitGroupInfo.setAdapterClassName(className);
                                unitGroupInfo.setAdvPlacementId(advPlacememtId);
                                unitGroupInfo.setPlatformName(platformName);
                                unitGroupInfo.setAppId(appId);
                                unitGroupInfo.setSdkKey(sdkKey);
                                unitGroupInfo.setAppKey(appKey);
                                unitGroupInfo.setUnitId(unitId);
                                unitGroupInfo.setGameId(gameId);
                                MediationGroupManager mediationGroupManager = new MediationGroupManager(mApplicationContext);
                                mediationGroupManager.setCallbackListener(listener);
                                mediationGroupManager.loadReworkVideoAd(placementId,rewardedList.get(i),unitGroupInfo,timer);

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
                        Log.e("onFinish RewardedAd","is not ad");
                    }
                };

                timer.start();
            }

        }else {
            AdError adError = new AdError("500","LCSDK is not init, try again later","","");
            listener.onRewardedVideoAdFailed(adError);
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
                name = platform+"LCRewardedVideoAdapter";
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
            adapterList = MediationGroupManager.getAdapterList();
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
//                    customInterstitialAdapter.show(mApplicationContext);

                    for (int i = 0 ; i < rewardedList.size(); i++){
                        for (int j = 0; j < adapterList.size();j++){
                            if (adapterList.get(j).getNetworkName().equals(rewardedList.get(i).getPlatformName())){
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
