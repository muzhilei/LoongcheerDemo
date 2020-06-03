package com.loongcheer.core.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.loongcheer.core.activity.component.PrivacyPolicyView;
import com.loongcheer.core.api.LCGDPRAuthCallback;
import com.loongcheer.core.common.base.Const;

/**
 * Created by Z on 2018/5/18.
 * GDPR Activity
 */

public class LoongCheerGdprAuthActivity extends Activity {

    String mCurrentUrl;
    PrivacyPolicyView mPrivacyPolicyView;

    public static LCGDPRAuthCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUrl = Const.URL.GDPR_URL;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        try {
            mPrivacyPolicyView = new PrivacyPolicyView(this);
            mPrivacyPolicyView.setClickCallbackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resultLevel = (int) (v.getTag());
                    if (mCallback != null) {
                        mCallback.onAuthResult(resultLevel);
                        mCallback = null;
                    }
                    finish();
                }
            });
            setContentView(mPrivacyPolicyView);
            mPrivacyPolicyView.loadPolicyUrl(mCurrentUrl);

        } catch (Exception e) {
            if (Const.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        if (mPrivacyPolicyView != null) {
            mPrivacyPolicyView.destory();
        }
        mCallback = null;
        super.onDestroy();

    }
}
