package com.loongcheer.core.common.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.base.UploadDataLevelManager;
import com.loongcheer.core.common.utils.CommonBase64Util;
import com.loongcheer.core.common.utils.CommonDeviceUtil;
import com.loongcheer.core.common.utils.CommonLogUtil;
import com.loongcheer.core.common.utils.CommonMD5;
import com.loongcheer.core.common.utils.SPUtil;
import com.loongcheer.core.common.utils.task.TaskManager;
import com.loongcheer.core.common.utils.task.Worker;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public abstract class AbsHttpLoader {

    private static final String TAG = "http.loader";

    public static final String JSON_REQUEST_COMMON_COUNTRY = "country";
    public static final String JSON_REQUEST_COMMON_TIME = "timeZone";
    public static final String JSON_REQUEST_COMMON_APP_ID = "appId";
    public static final String JSON_REQUEST_COMMON_APP_KEY = "appKey";
    public static final String JSON_REQUEST_COMMON_OS_VN = "osVn";
    public static final String JSON_REQUEST_COMMON_OS_VC = "osVc";
    public static final String JSON_REQUEST_COMMON_APP_VC = "appVc";
    public static final String JSON_REQUEST_COMMON_APP_VN = "appVn";
    public static final String JSON_REQUEST_COMMON_MODEL = "model";
    public static final String JSON_REQUEST_COMMON_PACKAGENAME = "packageName";
    //统一网络就叫network_type
    public static final String JSON_REQUEST_COMMON_NETWORK_TYPE = "networkType";
    public static final String JSON_REQUEST_COMMON_SDKVERSION = "sdkVersion";
    public static final String JSON_REQUEST_COMMON_DEVICE_TYPE = "deviceType";
    public static final String JSON_REQUEST_COMMON_DEVICE_BRAND = "deviceBrand";
    public static final String JSON_REQUEST_COMMON_SCREEN_SIZE = "screen";
    public static final String JSON_REQUEST_COMMON_UA = "ua";
    public static final String JSON_REQUEST_COMMON_LANGUAGE = "language";
    public static final String JSON_REQUEST_COMMON_GDPRPREMIT = "gdprPermit";


    public static final String JSON_REQUEST_ANDROID_ID = "androidId";
    public static final String JSON_REQUEST_COMMON_GAID = "gaid";


    public static final int POST = 1;
    public static final int GET = 2;


    protected OnHttpLoaderListener mListener;
    protected boolean mIsStop;


    public AbsHttpLoader() {
        super();
    }

    /**
     * 发起请求
     *
     * @param reqCode
     * @param listener
     */
    public void start(final int reqCode, OnHttpLoaderListener listener) {
        mIsStop = false;

        mListener = listener;

        load(reqCode);

    }


    public void stop(int reqCode) {
        mIsStop = true;
    }


    /**
     * POST or Get
     */
    protected abstract int onPrepareType();

    /**
     * URL
     */
    protected abstract String onPrepareURL();

    /**
     * Header
     */
    protected abstract Map<String, String> onPrepareHeaders();

    /**
     * Content
     */
    protected abstract byte[] onPrepareContent();

    /**
     * Return Code
     */
    protected abstract boolean onParseStatusCode(int code);

    /**
     * appid
     *
     * @return
     */
    protected abstract String getAppId();

    /**
     * Context
     *
     * @return Context
     */
    protected abstract Context getContext();

    /**
     * appkey
     *
     * @return
     */
    protected abstract String getAppKey();

    /**
     * api version
     *
     * @return
     */
    protected abstract String getApiVersion();

    protected abstract Map<String, Object> reqParamEx();

    protected abstract void onErrorAgent(String msg, AdError adError);


    /**
     * Base Info
     *
     * @return
     */
    protected JSONObject getBaseInfoObject() {
        JSONObject deviceJSONObject = new JSONObject();
        Context context = SDKContext.getInstance().getContext();
        try {

            int gdprLevel = SPUtil.getInt(context,Const.SPU_NAME,Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL,0);
            deviceJSONObject.put(JSON_REQUEST_COMMON_OS_VN, CommonDeviceUtil.getOSversionName());
            deviceJSONObject.put(JSON_REQUEST_COMMON_OS_VC, CommonDeviceUtil.getOsVersion());
            deviceJSONObject.put(JSON_REQUEST_COMMON_PACKAGENAME, CommonDeviceUtil.getPackageName(context));
            deviceJSONObject.put(JSON_REQUEST_COMMON_APP_VN, CommonDeviceUtil.getVersionName(context));
            deviceJSONObject.put(JSON_REQUEST_COMMON_APP_VC, CommonDeviceUtil.getVersionCode(context) + "");
            deviceJSONObject.put(JSON_REQUEST_COMMON_MODEL, CommonDeviceUtil.getModel());
            deviceJSONObject.put(JSON_REQUEST_COMMON_NETWORK_TYPE, String.valueOf(CommonDeviceUtil.getNetworkType(context)));
            deviceJSONObject.put(JSON_REQUEST_COMMON_LANGUAGE, CommonDeviceUtil.getLanguage(context));
            deviceJSONObject.put(JSON_REQUEST_COMMON_TIME, CommonDeviceUtil.getTimeZone());
            deviceJSONObject.put(JSON_REQUEST_COMMON_SDKVERSION, Const.SDK_VERSION_NAME);
            deviceJSONObject.put(JSON_REQUEST_COMMON_UA, CommonDeviceUtil.getDefaultUA());
            deviceJSONObject.put(JSON_REQUEST_COMMON_COUNTRY, CommonDeviceUtil.getCountry(context));
            deviceJSONObject.put(JSON_REQUEST_COMMON_GDPRPREMIT, SPUtil.getInt(context, Const.SPU_NAME, Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL,1));

            if (gdprLevel == 0){
                deviceJSONObject.put(JSON_REQUEST_COMMON_DEVICE_TYPE, "gdpr");
                deviceJSONObject.put(JSON_REQUEST_ANDROID_ID, "0000000000000000");
                deviceJSONObject.put(JSON_REQUEST_COMMON_GAID, "0000000000000000");
                deviceJSONObject.put(JSON_REQUEST_COMMON_SCREEN_SIZE, "gdpr");
                deviceJSONObject.put(JSON_REQUEST_COMMON_DEVICE_BRAND, "gdpr");
            }else {
                deviceJSONObject.put(JSON_REQUEST_COMMON_DEVICE_TYPE, 2);
                deviceJSONObject.put(JSON_REQUEST_ANDROID_ID, CommonDeviceUtil.getAndroidID(context));
                deviceJSONObject.put(JSON_REQUEST_COMMON_GAID, CommonDeviceUtil.getGoogleAdId());
                deviceJSONObject.put(JSON_REQUEST_COMMON_SCREEN_SIZE, CommonDeviceUtil.getScreenSize(context));
                deviceJSONObject.put(JSON_REQUEST_COMMON_DEVICE_BRAND, CommonDeviceUtil.getPhoneBrand());
            }

        } catch (Exception e) {
            if (Const.DEBUG) {
                e.printStackTrace();
            }
        }
        return deviceJSONObject;
    }

    /**
     * Extra Info
     *
     * @return
     */
    protected JSONObject getMainInfoObject() {
        Context context = SDKContext.getInstance().getContext();
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put(JSON_REQUEST_ANDROID_ID, CommonDeviceUtil.getAndroidID(context));
//            mainObject.put(JSON_REQUEST_COMMON_GAID, CommonDeviceUtil.getGoogleAdId());

        } catch (Exception e) {
            if (Const.DEBUG) {
                e.printStackTrace();
            }
        }
        return mainObject;
    }


    protected String getReqParam() {
        Map<String, Object> params = new HashMap<String, Object>();
//        String pEncode = CommonBase64Util.base64Encode(getBaseInfoObject().toString());
//        String p2Encode = CommonBase64Util.base64Encode(getMainInfoObject().toString());


        String pEncode = getBaseInfoObject().toString();
        params.put("apiVer", Const.API.APPSTR_APIVERSION);
        params.put("infoSave", pEncode);


        //排序
        List<String> keyList = new ArrayList<>(params.size());
        keyList.addAll(params.keySet());
        Collections.sort(keyList);

        StringBuilder sb = new StringBuilder();
        for (String tmp_key : keyList) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(tmp_key);
            sb.append("=");
            sb.append(params.get(tmp_key));
        }

        CommonLogUtil.d(TAG, " sorted value list:" + sb.toString());

//        String sign = CommonMD5.getLowerMd5(getAppKey() + sb.toString());
        String sign = getAppKey();
        params.put("sign", sign);

        Map<String, Object> paramsEx = reqParamEx();
        if (paramsEx != null) {
            params.putAll(reqParamEx());
        }

        Set<String> keys = params.keySet();
        JSONObject jsonObject = new JSONObject();
        try {
            for (String key : keys) {
                jsonObject.put(key, String.valueOf(params.get(key)));
            }
            return jsonObject.toString();
        } catch (Exception e) {

        } catch (OutOfMemoryError oom) {
            System.gc();
        }
        return null;
    }


    /**
     * Handle Response
     */
    protected abstract Object onParseResponse(Map<String, List<String>> headers, String jsonString) throws IOException;


    private void load(final int reqCode) {

        Worker worker = new Worker() {

            @Override
            public void work() {
                try {

                    if (mListener != null) {
                        mListener.onLoadStart(reqCode);

                    }
                    String urlStr = onPrepareURL();
                    doUrlConnect(urlStr);

                } catch (OutOfMemoryError | StackOverflowError e) {
                    System.gc();
                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    if (mListener != null) {
                        mListener.onLoadError(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                    }
                } catch (Exception e) {
                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    if (mListener != null) {
                        mListener.onLoadError(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                    }

                }
            }

            /**
             * UrlRequest
             * @param urlStr
             */
            private void doUrlConnect(String urlStr) {
                HttpURLConnection httpConn = null;
                try {
                    CommonLogUtil.i(TAG, "REQUEST URL: " + urlStr);

                    URL url = new URL(urlStr);

                    httpConn = (HttpURLConnection) url.openConnection();

                    //GET or POST
                    int type = onPrepareType();
                    if (type != POST && type != GET) {
                        type = GET;
                    }

                    //POST
                    if (type == POST) {
                        httpConn.setDoInput(true);
                        httpConn.setDoOutput(true);
                        httpConn.setRequestMethod("POST");
                        httpConn.setUseCaches(false);
                    }

                    if (type == GET) {
                        httpConn.setInstanceFollowRedirects(false);
                    }

                    //Header
                    Map<String, String> headers = onPrepareHeaders();
                    if (headers != null && headers.size() > 0) {
                        for (String key : headers.keySet()) {
                            httpConn.addRequestProperty(key, headers.get(key));
                        }
                    }

                    if (mIsStop) {

                        onCancelCallback(reqCode);
                        return;
                    }

                    httpConn.setConnectTimeout(20000);
                    httpConn.setReadTimeout(60000);
                    httpConn.connect();

                    //Content
                    if (type == POST) {
                        byte[] data = onPrepareContent();
                        if (data != null) {
                            OutputStream outputStream = httpConn.getOutputStream();
                            outputStream.write(data);
                            outputStream.flush();
                            outputStream.close();
                        }
                    }
                    final int statusCode = httpConn.getResponseCode();            //获得服务器的响应码
                    boolean statusCodeParseResult = onParseStatusCode(statusCode);

                    if (!statusCodeParseResult) {
                        if (statusCode != HttpURLConnection.HTTP_OK) {

                            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {

                                if (!mIsStop) {
                                    //支持302跳转
                                    String location = httpConn.getHeaderField("Location");
                                    if (location != null) {
                                        if (!location.startsWith("http")) {
                                            location = urlStr + location;
                                        }
                                        doUrlConnect(location);
                                    }

                                } else {
                                    onCancelCallback(reqCode);
                                }
                                return;
                            } else {
                                onErrorCallback(reqCode, "Http respond status code is " + statusCode, ErrorCode.getErrorCode(ErrorCode.httpStatuException, statusCode + "", httpConn.getResponseMessage()));
                                return;
                            }

                        }
                    }

                    if (mIsStop) {
                        onCancelCallback(reqCode);

                        return;
                    }

                    //Handle result
                    InputStream inputStream = getGzipInputStream(httpConn);
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader input = new BufferedReader(reader);
                    String s;
                    StringBuilder sb = new StringBuilder();
                    while ((s = input.readLine()) != null) {
                        sb.append(s);
                    }

                    if (input != null) {
                        input.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (urlStr.equals(Const.API.URL_APP_STRATEGY) || urlStr.equals(Const.API.URL_PLACE_STRATEGY)) {
                        String jsonString = sb.toString();
//                        jsonString = CommonBase64Util.base64Decode(sb.toString());
                        Log.e("request response == ","kkkkkk = "+jsonString);
                        jsonString = jsonString.trim();
                        String dataJSON = "";
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int code = jsonObject.optInt(Const.API.JSON_STATUS);
                        if (code == Const.API.JSON_RESPONSE_STATUS_SUCCESS) {
                            JSONObject dataObj = jsonObject.optJSONObject(Const.API.JSON_DATA);
                            if (dataObj == null) {
                                dataObj = new JSONObject();
                            }
                            dataJSON = dataObj.toString();
                            final Object result = onParseResponse(httpConn.getHeaderFields(), dataJSON);
                            onLoadFinishCallback(reqCode, result);

                        } else {
                            Log.e("LCSdk", jsonString);
//                            CommonLogUtil.d(TAG, "data-code:" + code);
                            onErrorCallback(reqCode, jsonString, ErrorCode.getErrorCode(ErrorCode.statuError, "" + code, jsonString));
                        }
                    } else {
                        final Object result = onParseResponse(httpConn.getHeaderFields(), "");
                        onLoadFinishCallback(reqCode, result);
                    }


                } catch (UnknownHostException e) {
                    handleSaveHttpRequest(ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage())); //存入数据库下次发送
                    onErrorCallback(reqCode, "UnknownHostException", ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                    CommonLogUtil.e(TAG, "UnknownHostException " + e.toString());
                } catch (ConnectException e) {
                    handleSaveHttpRequest(ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage())); //存入数据库下次发送
                    onErrorCallback(reqCode, "Connect error.", ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                    CommonLogUtil.e(TAG, "http connect error! " + e.toString());
                } catch (SocketTimeoutException e) {
                    onErrorCallback(reqCode, "Connect timeout.", ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                } catch (ConnectTimeoutException e) {
                    onConnectTimeout(reqCode, e);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    onErrorCallback(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                } catch (StackOverflowError e) {
                    System.gc();

                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    onErrorCallback(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                } catch (Error e) {
                    System.gc();

                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    onErrorCallback(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));
                } catch (Exception e) {

                    String msg = e.getMessage();
                    if (e.getMessage() != null) {
                        msg = e.getMessage();
                    }
                    if (Const.DEBUG) {
                        e.printStackTrace();
                    }

                    onErrorCallback(reqCode, msg, ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, e.getMessage()));


                } finally {
                    if (httpConn != null) {
                        // 释放连接
                        httpConn.disconnect();
                    }
                }

            }

        };

        if (Const.API.URL_TRACKING_STRATEGY.equals(onPrepareURL()) || Const.API.URL_AGENT.equals(onPrepareURL())) {
            //Tracking and Agent use single thread
            TaskManager.getInstance().run(worker, TaskManager.TYPE_SINGLE);
        } else {
            TaskManager.getInstance().run(worker);
        }

    }

    /**
     * Save Requests which is request error
     */
    protected abstract void handleSaveHttpRequest(AdError error);

    /**
     * Timeout Handle
     */
    protected void onConnectTimeout(int reqCode, ConnectTimeoutException e) {
        String msg = e.getMessage();
        AdError adError = ErrorCode.getErrorCode(ErrorCode.exception, ErrorCode.exception, msg);

        if (mListener != null) {
            mListener.onLoadError(reqCode, "Connect timeout.", adError);
        }
        onErrorAgent(msg, adError);
    }

    protected void onErrorCallback(final int reqCode, final String msg, AdError errorBean) {
        if (mListener != null) {
            mListener.onLoadError(reqCode, msg, errorBean);
        }
        onErrorAgent(msg, errorBean);
    }

    protected void onCancelCallback(final int reqCode) {
        if (mListener != null) {
            mListener.onLoadCanceled(reqCode);

        }
    }

    protected void onLoadFinishCallback(final int reqCode, final Object result) {
        if (mListener != null) {
            mListener.onLoadFinish(reqCode, result);

        }
    }

    /**
     * Gzip Handle
     *
     * @param connection
     * @return
     */
    public InputStream getGzipInputStream(HttpURLConnection connection) {
        if (connection == null) {
            return null;
        }

        InputStream connStream = null;
        try {
            connStream = connection.getInputStream();
        } catch (Exception e) {

        }
        String encoding = connection.getHeaderField("Content-Encoding");
        if ("gzip".equalsIgnoreCase(encoding)) {
            InputStream ips = null;
            BufferedInputStream bis = null;
            try {
                byte[] header = new byte[2];
                bis = new BufferedInputStream(connStream);
                bis.mark(2);
                int result = bis.read(header);
                // reset输入流到开始位置
                bis.reset();
                // 判断是否是GZIP格式
                int ss = (header[0] & 0xff) | ((header[1] & 0xff) << 8);
                if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
                    //System.out.println("为数据压缩格式...");
                    ips = new GZIPInputStream(bis);
                } else {
                    // 取前两个字节
                    ips = bis;
                }
            } catch (Exception e) {
                ips = connStream;
            }
            return ips;
        } else {
            return connStream;
        }

    }

    protected byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("utf-8"));
            gzip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


}
