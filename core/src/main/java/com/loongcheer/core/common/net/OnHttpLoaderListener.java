package com.loongcheer.core.common.net;

import com.loongcheer.core.api.AdError;

public interface OnHttpLoaderListener {

    void onLoadStart(int reqCode);

    void onLoadFinish(int reqCode, Object result);

    void onLoadError(int reqCode, String msg, AdError errorCode);

    void onLoadCanceled(int reqCode);
}
