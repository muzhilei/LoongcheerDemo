package com.loongcheer.banner.business;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.loongcheer.banner.api.LCBannerListener;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.CommonAdManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.db.RealmModel;
import com.loongcheer.core.common.strategy.PlaceStrategy;
import com.loongcheer.core.common.utils.CommonLogUtil;
import com.loongcheer.core.common.utils.CommonUtil;
import com.loongcheer.core.common.utils.CustomAdapterFactory;

import java.util.ArrayList;
import java.util.List;

public class AdLoadManager extends CommonAdManager {

    public static final String TAG = "Banner" + AdLoadManager.class.getSimpleName();

    int count = 0 ;

    CountDownTimer timer;

    public AdLoadManager(Context context, String placementId) {
        super(context, placementId);
    }


    public static AdLoadManager getInstance(Context context, String placementId) {

        CommonAdManager adLoadManager = CommonAdManager.getInstance(placementId);
        if (adLoadManager == null || !(adLoadManager instanceof AdLoadManager)) {
            adLoadManager = new AdLoadManager(context, placementId);
            CommonAdManager.addAdManager(placementId, adLoadManager);
        }
        adLoadManager.refreshContext(context);
        return (AdLoadManager) adLoadManager;
    }

    /**
     * 广告请求
     *
     * @param listener
     */
    public void startLoadAd(final LCBannerView lcBannerView, final Context context, String advPlacememtId, final InnerBannerListener listener){

        count = 0;
        List<RealmModel> list =  DBContext.check();

        final List<RealmModel> bannerList = new ArrayList<>();

        if (list != null){

            for (RealmModel realmModel : list){

                if (realmModel.getAdvPlacememtId().equals(advPlacememtId)){

                    bannerList.add(realmModel);

                }

            }

            if (bannerList != null){

                timer = new CountDownTimer(60*1000+500,10*1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //定时任务去执行Onload请求
                        if (count < bannerList.size()){
                            String platformName = bannerList.get(count).getPlatformName();
                            String className = getMethod(platformName,3);
                            String placementId = bannerList.get(count).getPlacementId();
                            String appId = bannerList.get(count).getAppId();
                            String sdkKey = bannerList.get(count).getSdkKey();
                            String appKey = bannerList.get(count).getAppKey();
                            String unitId = bannerList.get(count).getUnitId();
                            String gameId = bannerList.get(count).getGameId();
                            PlaceStrategy.UnitGroupInfo unitGroupInfo = new PlaceStrategy.UnitGroupInfo();
                            unitGroupInfo.setAdapterClassName(className);
                            unitGroupInfo.setBanSize(bannerList.get(count).getBanSize());
                            unitGroupInfo.setAppId(appId);
                            unitGroupInfo.setSdkKey(sdkKey);
                            unitGroupInfo.setAppKey(appKey);
                            unitGroupInfo.setUnitId(unitId);
                            unitGroupInfo.setGameId(gameId);
                            MediationGroupManager mediationGroupManager = new MediationGroupManager(mApplicationContext);
                            mediationGroupManager.setCallbackListener(listener);
                            mediationGroupManager.loadBannerAd(lcBannerView,placementId,bannerList.get(count),unitGroupInfo,timer);
                            Log.e("计时器数值为：",count+"ms");
                            count++;
                        }else {
                            count = 0 ;
                            String platformName = bannerList.get(count).getPlatformName();
                            String className = getMethod(platformName,3);
                            String placementId = bannerList.get(count).getPlacementId();
                            String appId = bannerList.get(count).getAppId();
                            String sdkKey = bannerList.get(count).getSdkKey();
                            String appKey = bannerList.get(count).getAppKey();
                            String unitId = bannerList.get(count).getUnitId();
                            String gameId = bannerList.get(count).getGameId();
                            PlaceStrategy.UnitGroupInfo unitGroupInfo = new PlaceStrategy.UnitGroupInfo();
                            unitGroupInfo.setAdapterClassName(className);
                            unitGroupInfo.setBanSize(bannerList.get(count).getBanSize());
                            unitGroupInfo.setAppId(appId);
                            unitGroupInfo.setSdkKey(sdkKey);
                            unitGroupInfo.setAppKey(appKey);
                            unitGroupInfo.setUnitId(unitId);
                            unitGroupInfo.setGameId(gameId);
                            MediationGroupManager mediationGroupManager = new MediationGroupManager(mApplicationContext);
                            mediationGroupManager.setCallbackListener(listener);
                            mediationGroupManager.loadBannerAd(lcBannerView,placementId,bannerList.get(count),unitGroupInfo,timer);
                            Log.e("循坏计时器数值为：",count+"ms");
                            count++;
                        }
                    }

                    @Override
                    public void onFinish() {
                        Log.e("onFinish 计时器数值为：",count+"ms");
                        Log.e("bannerOnload","is not ad");
                        cancel();
                    }
                };
                timer.start();
            }else {
                CommonLogUtil.e("errer","LCSDK is not Ad");
            }

        }else {
            CommonLogUtil.e("errer","LCSDK is not init, try again later");
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

}
