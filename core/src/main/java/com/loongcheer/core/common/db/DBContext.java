package com.loongcheer.core.common.db;

import android.content.Context;
import android.util.Log;

import com.loongcheer.core.common.bean.InfoBean;
import com.loongcheer.core.common.utils.CommonLogUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBContext {

    private static final String TAG = "DBdata upData ...";

    private static Realm realm;

    public DBContext(){

    }


    public static void init(Context context){

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("lcdb.realm")
                .schemaVersion(0)
                .inMemory()
                .build();
        realm = Realm.getInstance(config);

    }

    public static void add(InfoBean model){
        if (model != null){
            for (int i =0; i < model.getFlowGroupAndSourceVoList().size();i++){

                for (int j = 0 ; j< model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().size();j++){

                    String advPlacememtId = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getAdvPlacementId();
                    String gameId =model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getGameId();
                    String placementId = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getPlacementId();
                    String appId = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getAppId();
                    String appKey = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getAppKey();
                    String sdkKey = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getSdkKey();
                    int ecpm = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getEcpm();
                    String platformName = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getPlatformName();
                    boolean status = false;
                    int adType = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getAdvType();
                    int requestNum = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getRequests();
                    String banSize = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getBanSize();
                    String dataId = advPlacememtId+platformName;
                    String unitId = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getUnitId();
//                    String flowGroupId = model.getFlowGroupAndSourceVoList().get(i).getAdSourceVo().get(j).getFlowGroupId();

                    try{

                        realm.beginTransaction();
                        RealmModel realmModel = realm.createObject(RealmModel.class);
                        realmModel.setAdvPlacememtId(advPlacememtId);
                        realmModel.setGameId(gameId);
                        realmModel.setPlacementId(placementId);
                        realmModel.setAppId(appId);
                        realmModel.setAppKey(appKey);
                        realmModel.setSdkKey(sdkKey);
                        realmModel.setEcmp(ecpm);
                        realmModel.setStatus(status);
                        realmModel.setAdvType(adType);
                        realmModel.setPlatformName(platformName);
                        realmModel.setBanSize(banSize);
                        realmModel.setRequestNum(requestNum);
                        realmModel.setDataId(dataId);
                        realmModel.setUnitId(unitId);
//                        realmModel.setFlowGroupId(flowGroupId);
                        realm.commitTransaction();


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static void remove(String advPlacementId,String platformName){
        realm.beginTransaction();
        RealmResults realmResults = realm.where(RealmModel.class).equalTo("advPlacementId",advPlacementId).equalTo("platformName",platformName).findAll();
        realmResults.deleteFromRealm(0);
        realm.commitTransaction();
    }


    public static void removeAll(){
        realm.beginTransaction();
        RealmResults realmResults = realm.where(RealmModel.class).findAll();
        realmResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static void modify(String dataId,boolean status){
        realm.beginTransaction();
        RealmModel realmTM = realm.where(RealmModel.class).equalTo("dataId",dataId).findFirst();
        realmTM.setStatus(status);
        realm.commitTransaction();
    }

    public static List<RealmModel> check(){
        RealmResults<RealmModel> realmTMS = realm.where(RealmModel.class).findAll();
        if (realmTMS.isEmpty()){
            CommonLogUtil.e(TAG,"data is null ");
            return null;
        }
        List<RealmModel> realm = new ArrayList<>();
        for (RealmModel realmTM :realmTMS){
            realm.add(realmTM);
           Log.e(TAG,realmTM.getAdvPlacememtId()+" " +realmTM.getGameId() +" " +realmTM.getPlacementId() + " " +realmTM.getPlatformName()
                    +" " +realmTM.getAppId() + " " + realmTM.getAppKey() + " " + realmTM.getSdkKey()+ " "+realmTM.isStatus()
                   +" " +realmTM.getBanSize() +" " + realmTM.getRequestNum());
        }
        return realm;
    }

    public static void closeDb(){
        if (realm !=  null){
            realm.close();
        }
    }

}
