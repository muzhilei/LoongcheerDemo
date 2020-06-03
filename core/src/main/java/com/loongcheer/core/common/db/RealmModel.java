package com.loongcheer.core.common.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmModel extends RealmObject {

    private String platformName;
    private String advPlacememtId;
    private String placementId;
    private String gameId;
    private String appId;
    private String sdkKey;
    private String appKey;
    private int ecpm;//价值
    private boolean status;
    private int advType;
    private String banSize;
    private int requestNum;
    private String dataId;
    private String unitId;
//    private String flowGroupId;


    public RealmModel(){

    }

    public RealmModel(String platformName
            ,String advPlacememtId
            , String placementId
            ,String gameId
            ,String appId
            ,String sdkKey
            ,String appKey
            ,String banSize
            ,String dataId
            ,int requestNum
            ,String flowGroupId
            ,String unitId
            ,int ecpm,boolean status,int advType){

        this.advPlacememtId = advPlacememtId;
        this.platformName = platformName;
        this.placementId = placementId;
        this.gameId = gameId;
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.appKey = appKey;
        this.banSize = banSize;
        this.ecpm = ecpm;
        this.status = status;
        this.advType = advType;
        this.requestNum = requestNum;
//        this.flowGroupId = flowGroupId;
        this.dataId = dataId;
        this.unitId = unitId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAdvPlacememtId() {
        return advPlacememtId;
    }

    public void setAdvPlacememtId(String advPlacememtId) {
        this.advPlacememtId = advPlacememtId;
    }

    public String getPlacementId() {
        return placementId;
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public void setSdkKey(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int isEcmp() {
        return ecpm;
    }

    public void setEcmp(int ecpm) {
        this.ecpm = ecpm;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAdvType() {
        return advType;
    }

    public void setAdvType(int advType) {
        this.advType = advType;
    }

    public String getBanSize() {
        return banSize;
    }

    public void setBanSize(String banSize) {
        this.banSize = banSize;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(int requestNum) {
        this.requestNum = requestNum;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    //    public String getFlowGroupId() {
//        return flowGroupId;
//    }
//
//    public void setFlowGroupId(String flowGroupId) {
//        this.flowGroupId = flowGroupId;
//    }
}
