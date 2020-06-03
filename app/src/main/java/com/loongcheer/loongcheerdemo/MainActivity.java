package com.loongcheer.loongcheerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loongcheer.banner.api.LCBannerListener;
import com.loongcheer.banner.api.LCBannerView;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.LCGDPRAuthCallback;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.api.LCSDKInitListener;
import com.loongcheer.interstitial.api.LCInterstitial;
import com.loongcheer.interstitial.api.LCInterstitialListener;
import com.loongcheer.reworkvideo.api.LCRewardVideoAd;
import com.loongcheer.reworkvideo.api.LCRewardVideoListener;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button initAdLC;
    private Button initGDPR;
    private Button loadInterstitiaAd;
    private Button showInterstitiaAd;
    private Button loadRewarded;
    private Button showRewarded;
    private Button loadBanner;
    private Button showBanner;
    private Button isReady;
    private TextView text_desc;

    LCBannerView mBannerView;
    LCInterstitial lcInterstitial;
    LCRewardVideoAd lcRewardVideoAd;

    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        final FrameLayout frameLayout = findViewById(R.id.adview_container);
        mBannerView = new LCBannerView(this);
        frameLayout.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mBannerView.setUnitId("1262048671847873833");
        mBannerView.setBannerAdListener(new LCBannerListener() {
            @Override
            public void onBannerLoaded() {
                Log.i(TAG, "onBannerLoaded");
                text_desc.setText("onBannerLoaded");
                makeToast("onBannerLoaded");
            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.i(TAG, "onBannerFailed:" + adError.printStackTrace());
                text_desc.setText("onBannerFailed:" + adError.printStackTrace());
                makeToast("onBannerFailed:" + adError.printStackTrace());
            }

            @Override
            public void onBannerClicked() {
                Log.i(TAG, "onBannerClicked");
                text_desc.setText("onBannerClicked");
                makeToast("onBannerClicked");
            }

            @Override
            public void onBannerShow() {
                Log.i(TAG, "onBannerShow");
                text_desc.setText("onBannerShow");
                makeToast("onBannerShow");
            }

            @Override
            public void onBannerClose() {
                Log.i(TAG, "onBannerClose");
                text_desc.setText("onBannerClose");
                makeToast("onBannerClose");
            }

            @Override
            public void onBannerAutoRefreshed() {
                Log.i(TAG, "onBannerAutoRefreshed");
                text_desc.setText("onBannerAutoRefreshed");
                makeToast("onBannerAutoRefreshed");
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.i(TAG, "onBannerAutoRefreshFail:" + adError.printStackTrace());
                text_desc.setText("onBannerAutoRefreshFail:" + adError.printStackTrace());
                makeToast("onBannerAutoRefreshFail:" + adError.printStackTrace());
            }
        });

        initAdLC = this.findViewById(R.id.initAdLC);
        loadInterstitiaAd = this.findViewById(R.id.loadInterstitiaAd);
        showInterstitiaAd = this.findViewById(R.id.showInterstitiaAd);
        loadRewarded = this.findViewById(R.id.loadRewarded);
        showRewarded = this.findViewById(R.id.showRewarded);
        loadBanner = this.findViewById(R.id.loadBanner);
        showBanner = this.findViewById(R.id.showBanner);
        isReady = this.findViewById(R.id.btn_isready);
        text_desc = this.findViewById(R.id.text_desc);
        initGDPR = this.findViewById(R.id.initGDPR);

        initAdLC.setOnClickListener(this);
        loadInterstitiaAd.setOnClickListener(this);
        showInterstitiaAd.setOnClickListener(this);
        loadRewarded.setOnClickListener(this);
        showRewarded.setOnClickListener(this);
        loadBanner.setOnClickListener(this);
        showBanner.setOnClickListener(this);
        isReady.setOnClickListener(this);
        initGDPR.setOnClickListener(this);
    }

    private void init() {

        if (lcInterstitial != null){
            lcInterstitial = null;
        }

        lcInterstitial = new LCInterstitial(this,"1262048671847863833");
        lcInterstitial.setAdListener(new LCInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
                text_desc.setText("onInterstitialAdLoaded");
                Toast.makeText(MainActivity.this, "onInterstitialAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdLoadFail");
                text_desc.setText("onInterstitialAdLoadFail:" +adError.printStackTrace());
                Toast.makeText(MainActivity.this, "onInterstitialAdLoadFail:" + adError.printStackTrace(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdClicked() {
                Log.i(TAG, "onInterstitialAdClicked");
                text_desc.setText("onInterstitialAdClicked");
                Toast.makeText(MainActivity.this, "onInterstitialAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdShow() {
                Log.i(TAG, "onInterstitialAdShow");
                text_desc.setText("onInterstitialAdShow");
                Toast.makeText(MainActivity.this, "onInterstitialAdShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdClose() {
                Log.i(TAG, "onInterstitialAdClose");
                text_desc.setText("onInterstitialAdClose");
                Toast.makeText(MainActivity.this, "onInterstitialAdClose", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoStart() {
                Log.i(TAG, "onInterstitialAdVideoStart");
                text_desc.setText("onInterstitialAdVideoStart");
                Toast.makeText(MainActivity.this, "onInterstitialAdVideoStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoEnd() {
                Log.i(TAG, "onInterstitialAdVideoEnd");
                text_desc.setText("onInterstitialAdVideoEnd");
                Toast.makeText(MainActivity.this, "onInterstitialAdVideoEnd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError");
                text_desc.setText("onInterstitialAdVideoError");
                Toast.makeText(MainActivity.this, "onInterstitialAdVideoError", Toast.LENGTH_SHORT).show();
            }
        });


        if (lcRewardVideoAd != null){
            lcRewardVideoAd =null;
        }

        lcRewardVideoAd = new LCRewardVideoAd(this,"1262048671847873899");
        lcRewardVideoAd.setAdListener(new LCRewardVideoListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
                text_desc.setText("onRewardedVideoAdLoaded");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailed(AdError errorCode) {
                Log.i(TAG, "onRewardedVideoAdFailed");
                text_desc.setText("onRewardedVideoAdFailed:" +errorCode.printStackTrace());
                Toast.makeText(MainActivity.this, "onRewardedVideoAdFailed:" + errorCode.printStackTrace(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdPlayStart() {
                Log.i(TAG, "onRewardedVideoAdPlayStart");
                text_desc.setText("onRewardedVideoAdPlayStart");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdPlayStart", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdPlayEnd() {
                Log.i(TAG, "onRewardedVideoAdPlayEnd");
                text_desc.setText("onRewardedVideoAdPlayEnd");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdPlayEnd", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError errorCode) {
                Log.i(TAG, "onRewardedVideoAdPlayFailed");
                text_desc.setText("onRewardedVideoAdPlayFailed");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdPlayFailed:" + errorCode.printStackTrace(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.i(TAG, "onRewardedVideoAdClosed");
                text_desc.setText("onRewardedVideoAdClosed");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdPlayClicked() {
                Log.i(TAG, "onRewardedVideoAdPlayClicked");
                text_desc.setText("onRewardedVideoAdPlayClicked");
                Toast.makeText(MainActivity.this, "onRewardedVideoAdPlayClicked", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReward() {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.initAdLC:
                LCSDK.init(MainActivity.this, "111111", "111111", new LCSDKInitListener() {
                    @Override
                    public void onSuccess() {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "初始化成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
                break;
            case R.id.loadInterstitiaAd:
                text_desc.setText("Interstitial is loading ...");
                lcInterstitial.load();
                break;
            case R.id.showInterstitiaAd:
                lcInterstitial.show();
                break;
            case R.id.loadRewarded:
                text_desc.setText("RewardedVideo is loading ...");
                lcRewardVideoAd.load();
                break;
            case R.id.showRewarded:
                lcRewardVideoAd.show();
                break;
            case R.id.loadBanner:
                text_desc.setText("Banner is loading ...");
                mBannerView.loadAd();
                break;
            case R.id.showBanner:
                break;
            case R.id.btn_isready:
                boolean isReady = lcInterstitial.isAdReady();
                makeToast(isReady+"");
                break;
            case R.id.initGDPR:
                LCSDK.showGdprAuth(MainActivity.this, new LCGDPRAuthCallback() {
                    @Override
                    public void onAuthResult(int level) {
                        makeToast("GDRP政策 == " + level);
                        LCSDK.setGDPRUploadDataLevel(MainActivity.this,level);
                    }
                });
                break;
        }
    }

    private void makeToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
