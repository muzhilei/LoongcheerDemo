package com.loongcheer.core.api;

public interface LCSDKInitListener {

    public void onSuccess();

    public void onFail(String errorMsg);
}
