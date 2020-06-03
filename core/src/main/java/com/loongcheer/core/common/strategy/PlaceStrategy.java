package com.loongcheer.core.common.strategy;

public class PlaceStrategy {


    public static class UnitGroupInfo {

        /***
         * Adapter Class Name
         */
        public String adapterClassName;

        public boolean isReady;

        public String banSize;

        public String platformName;

        public String advPlacementId;

        public String appId;

        public String sdkKey;

        public String appKey;

        public String unitId;

        public String gameId;

        public String getBanSize() {
            return banSize;
        }

        public void setBanSize(String banSize) {
            this.banSize = banSize;
        }

        public boolean isReady() {
            return isReady;
        }

        public void setReady(boolean ready) {
            isReady = ready;
        }

        public String getAdapterClassName() {
            return adapterClassName;
        }

        public void setAdapterClassName(String adapterClassName) {
            this.adapterClassName = adapterClassName;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getAdvPlacementId() {
            return advPlacementId;
        }

        public void setAdvPlacementId(String advPlacementId) {
            this.advPlacementId = advPlacementId;
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

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }
    }

}
