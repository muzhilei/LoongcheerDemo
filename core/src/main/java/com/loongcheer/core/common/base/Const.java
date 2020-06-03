package com.loongcheer.core.common.base;

public class Const {

    public static final boolean DEBUG = false;

    public static String RESOURCE_HEAD = "loongcheer"; //Head of Resource name

    public static final int SYSTEM = 1;

    public static final String SPU_LOCAL_USERAGENT = "local_ua";
    public static final String SPU_LOCAL_OS = "local_os";


    public static final String SPU_IS_READY = "isReady";

    public static final String CLASS_NAME_BANNER = "banner";
    public static final String CLASS_NAME_INTERSTITIAL = "interstitial";
    public static final String CLASS_NAME_REWORKVIDEO = "reworkvideo";
    public static final String CLASS_PACKAGE_NAME= "com.loongcheer.network.";




    public static final String SPU_NAME = RESOURCE_HEAD + "_sdk";
    public static final String SPU_APPID = RESOURCE_HEAD + "_appid";
    public static final String SPU_APPKEY = RESOURCE_HEAD + "_appkey";
    public static final String SPU_SYS_GAID = RESOURCE_HEAD + "_gaid";

    public static final int NET_TYPE_UNKNOW = 1;
    public static final int NET_TYPE_WIFI = 2;
    public static final int NET_TYPE_4G = -4444444;
    public static final int NET_TYPE_3G = -3333333;
    public static final int NET_TYPE_2G = -2222222;

    /**
     * SDK Version
     */
    public static final String SDK_VERSION_NAME = "UA_1.0.0";

    public static class SPUKEY {

        public static final String SPU_UPLOAD_DATA_LEVEL = "UPLOAD_DATA_LEVEL";//GDPR隐私政策

        public static final String SPU_GDPR_PERMIT = "GDPR_PERMIT";//GDPR隐私政策

        public static final String SPU_NETWORK_VERSION_NAME = "NETWORK_VERSION_NAME";

    }

    public static class URL {
        public static final String GDPR_URL = "http://pixel.loongcheer.ga:8080/PrivacyPolicySetting.html";
    }

    /**
     * API Request
     */
    public static class API {
        public static final String APPSTR_APIVERSION = "1.0";
        public static final String JSON_STATUS = "code";
        public static final int JSON_RESPONSE_STATUS_SUCCESS = 200;
        public static String JSON_DATA = "data";

        public static final String URL_APP_STRATEGY = "http://192.168.0.54:7002/userInitLogMess/initSDK";
        public static final String URL_PLACE_STRATEGY = "http://192.168.0.54:7002/userInitLogMess/initSDK";
        public static final String URL_AGENT = "http://192.168.0.54:7002/userInitLogMess/initSDK";
        public static final String URL_TRACKING_STRATEGY = "http://192.168.0.54:7002/userInitLogMess/initSDK";
        public static final String URL_TESTURL = "http://192.168.0.54:7002/userInitLogMess/initSDK";


        /**Test API**/
//        public static final String URL_APP_STRATEGY = "http://test.aa.toponad.com/v1/open/app";
//        public static final String URL_PLACE_STRATEGY = "http://test.aa.toponad.com/v1/open/placement";
//        public static final String URL_AGENT = "http://test.dd.toponad.com/v1/open/da";
//        public static final String URL_TRACKING_STRATEGY = "http://test.tt.toponad.com/v1/open/tk";

    }

    public static class LOGKEY {
        public static String REQUEST = "request";
        public static String REQUEST_RESULT = "request_result";
        public static String IMPRESSION = "impression";
        public static String CLICK = "click";
        public static String CLOSE = "close";

        public static String SUCCESS = "success";
        public static String FAIL = "fail";
        public static String START = "start";


        public static String API_BANNER = "banner";
        public static String API_INTERSTITIAL = "inter";
        public static String API_REWARD = "reward";

        public static String API_LOAD = "load";
        public static String API_SHOW = "show";
        public static String API_ISREADY = "isready";


    }

    public static class FORMAT {
        public static final String REWARDEDVIDEO_FORMAT = "1";//激励
        public static final String BANNER_FORMAT = "2";//banner
        public static final String INTERSTITIAL_FORMAT = "3";//插屏
    }

}
