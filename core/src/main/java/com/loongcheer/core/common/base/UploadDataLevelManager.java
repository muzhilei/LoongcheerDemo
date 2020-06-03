package com.loongcheer.core.common.base;

import android.content.Context;
import android.content.Intent;

import com.loongcheer.core.activity.LoongCheerGdprAuthActivity;
import com.loongcheer.core.api.LCGDPRAuthCallback;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.common.utils.SPUtil;


/**
 * Created by Z on 2018/4/27.
 */

public class UploadDataLevelManager {

    Context mContext;
    private static UploadDataLevelManager sInstance;

    int mLevel = LCSDK.PERSONALIZED;

    private UploadDataLevelManager(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
        }
        mLevel = SPUtil.getInt(mContext, Const.SPU_NAME, Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL, LCSDK.PERSONALIZED);

    }

    public static UploadDataLevelManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UploadDataLevelManager(context);
        }
        return sInstance;
    }

    public void setUploadDataLevel(int level) {
        mLevel = level;
        SPUtil.putInt(mContext, Const.SPU_NAME, Const.SPUKEY.SPU_UPLOAD_DATA_LEVEL, level);
    }

    //Return the level User set
    public int getUploadDataLevel() {
        return mLevel;
    }

    /**
     * Switch of Anythink's DeviceInfo
     * @return
     */
    public boolean canUpLoadDeviceData() {
        int level = SPUtil.getInt(mContext,Const.SPU_NAME,Const.SPUKEY.SPU_GDPR_PERMIT,LCSDK.NONPERSONALIZED);

            if (level == LCSDK.NONPERSONALIZED) {
                return true;
            } else {
                return false;
            }
        }



    public void showUploadDataNotifyDialog(Context context, LCGDPRAuthCallback callback) {
        LoongCheerGdprAuthActivity.mCallback = callback;
        Intent intent = new Intent(context, LoongCheerGdprAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
