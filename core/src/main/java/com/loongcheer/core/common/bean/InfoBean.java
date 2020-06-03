package com.loongcheer.core.common.bean;

import com.google.gson.Gson;

import java.util.List;

public class InfoBean {


    private List<FlowGroupAndSourceVoListBean> flowGroupAndSourceVoList;

    public static InfoBean stringFromData(String str){

        return new Gson().fromJson(str, InfoBean.class);
    }

    public List<FlowGroupAndSourceVoListBean> getFlowGroupAndSourceVoList() {
        return flowGroupAndSourceVoList;
    }

    public void setFlowGroupAndSourceVoList(List<FlowGroupAndSourceVoListBean> flowGroupAndSourceVoList) {
        this.flowGroupAndSourceVoList = flowGroupAndSourceVoList;
    }


    public static class FlowGroupAndSourceVoListBean {
        /**
         * advPlacememtId : 1262048671847863833
         * adType : 3
         * requests : 7
         * flowGroupId:111111
         * adSourceVo : [{"advPlacementId":"1262048671847863833","advType":3,"requests":7,"sourceName":"测试默认广告源2","platformName":"测试平台4","placementId":"532412","gameId":"3124","appId":"","sdkKey":"","appKey":"","ecpm":6},{"advPlacementId":"1262048671847863833","advType":3,"requests":7,"sourceName":"测试默认广告源1","platformName":"测试平台3","placementId":"53213","gameId":"123","appId":"","sdkKey":"","appKey":"","ecpm":5}]
         */

        private String advPlacememtId;
        private int adType;
        private int requests;
//        private String flowGroupId;
        private List<AdSourceVoBean> adSourceVo;

        public String getAdvPlacememtId() {
            return advPlacememtId;
        }

        public void setAdvPlacememtId(String advPlacememtId) {
            this.advPlacememtId = advPlacememtId;
        }

//        public String getFlowGroupId() {
//            return flowGroupId;
//        }
//
//        public void setFlowGroupId(String flowGroupId) {
//            this.flowGroupId = flowGroupId;
//        }

        public int getAdType() {
            return adType;
        }

        public void setAdType(int adType) {
            this.adType = adType;
        }

        public int getRequests() {
            return requests;
        }

        public void setRequests(int requests) {
            this.requests = requests;
        }

        public List<AdSourceVoBean> getAdSourceVo() {
            return adSourceVo;
        }

        public void setAdSourceVo(List<AdSourceVoBean> adSourceVo) {
            this.adSourceVo = adSourceVo;
        }

        public static class AdSourceVoBean {
            /**
             * advPlacementId : 1262048671847863833
             * advType : 3
             * requests : 7
             * flowGroupId:111111
             * sourceName : 测试默认广告源2
             * platformName : 测试平台4
             * placementId : 532412
             * gameId : 3124
             * appId :
             * sdkKey :
             * appKey :
             * ecpm : 6
             */

            private String advPlacementId;
            private int advType;
            private int requests;
//            private String flowGroupId;
            private String sourceName;
            private String platformName;
            private String placementId;
            private String gameId;
            private String appId;
            private String sdkKey;
            private String appKey;
            private String banSize;
            private String unitId;
            private int ecpm;

            public String getAdvPlacementId() {
                return advPlacementId;
            }

            public void setAdvPlacementId(String advPlacementId) {
                this.advPlacementId = advPlacementId;
            }

//            public String getFlowGroupId() {
//                return flowGroupId;
//            }
//
//            public void setFlowGroupId(String flowGroupId) {
//                this.flowGroupId = flowGroupId;
//            }

            public int getAdvType() {
                return advType;
            }

            public void setAdvType(int advType) {
                this.advType = advType;
            }

            public int getRequests() {
                return requests;
            }

            public void setRequests(int requests) {
                this.requests = requests;
            }

            public String getSourceName() {
                return sourceName;
            }

            public void setSourceName(String sourceName) {
                this.sourceName = sourceName;
            }

            public String getPlatformName() {
                return platformName;
            }

            public void setPlatformName(String platformName) {
                this.platformName = platformName;
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

            public int getEcpm() {
                return ecpm;
            }

            public void setEcpm(int ecpm) {
                this.ecpm = ecpm;
            }

            public String getBanSize() {
                return banSize;
            }

            public void setBanSize(String banSize) {
                this.banSize = banSize;
            }

            public String getUnitId() {
                return unitId;
            }

            public void setUnitId(String unitId) {
                this.unitId = unitId;
            }
        }
    }
}
