package com.loongcheer.core.common.net;

import android.content.Context;
import android.text.TextUtils;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.utils.CommonDeviceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppStrategyLoader extends AbsHttpLoader{

    private static final String TAG = AppStrategyLoader.class.getSimpleName();

    private String appid;
    private String appKey;
    private Context mContext;

    long startTime;

    public AppStrategyLoader(Context mContext, String appId, String appKey) {
        this.appid = appId;
        this.appKey = appKey;
        this.mContext = mContext;
    }

    @Override
    protected int onPrepareType() {
        return AbsHttpLoader.POST;
    }

    @Override
    protected String onPrepareURL() {
        return Const.API.URL_TESTURL;
    }

    @Override
    protected Map<String, String> onPrepareHeaders() {
        Map<String, String> maps = new HashMap<>();
        maps.put("Accept-Encoding", "gzip");
        maps.put("Content-Type", "application/json;charset=utf-8");
        return maps;
    }

    @Override
    protected byte[] onPrepareContent() {
        try {
            return getReqParam().getBytes("utf-8");
        } catch (Exception e) {

        }
        return getReqParam().getBytes();
    }

    @Override
    protected boolean onParseStatusCode(int code) {
        return false;
    }

    @Override
    protected String getAppId() {
        return appid;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    protected String getAppKey() {
        return appKey;
    }

    @Override
    protected String getApiVersion() {
        return Const.API.APPSTR_APIVERSION;
    }

    @Override
    protected Map<String, Object> reqParamEx() {
        return null;
    }

    @Override
    protected JSONObject getBaseInfoObject() {
        JSONObject temp = super.getBaseInfoObject();
        try {
            temp.put("app_id", appid);
//            temp.put(JSON_REQUEST_COMMON_NW_VERSION, CommonDeviceUtil.getAllNetworkVersion());

        } catch (JSONException e) {
            if (Const.DEBUG) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    @Override
    protected JSONObject getMainInfoObject() {
        JSONObject p2Object = super.getMainInfoObject();
        return p2Object;
    }

    @Override
    protected void onErrorAgent(String msg, AdError adError) {

    }

    @Override
    protected Object onParseResponse(Map<String, List<String>> headers, String jsonString) throws IOException {
        jsonString = jsonString.trim();
//        AgentEventManager.sentHostCallbackTime("app", startTime, System.currentTimeMillis());
        return jsonString;
    }

    @Override
    protected void handleSaveHttpRequest(AdError error) {
//        AgentEventManager.sendErrorAgent("app", adError.getPlatformCode(), adError.getPlatformMSG(), null, null, null, "", "");

    }
}
