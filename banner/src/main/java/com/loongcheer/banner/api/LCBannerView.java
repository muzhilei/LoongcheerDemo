package com.loongcheer.banner.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loongcheer.banner.business.AdLoadManager;
import com.loongcheer.banner.business.InnerBannerListener;
import com.loongcheer.banner.custom.api.CustomBannerAdapter;
import com.loongcheer.core.api.AdError;
import com.loongcheer.core.api.ErrorCode;
import com.loongcheer.core.api.LCSDK;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.SDKContext;
import com.loongcheer.core.common.db.DBContext;
import com.loongcheer.core.common.utils.CommonLogUtil;
import com.loongcheer.core.common.utils.SPUtil;

public class LCBannerView extends FrameLayout {

    private final String TAG = LCBannerView.class.getSimpleName();

    private LCBannerListener mListener;
    private String advPlacememtId;

    private AdLoadManager mAdLoadManager;

    boolean hasTouchWindow = false;
    int visibility = 0;

    boolean hasCallbackShow = false;

    CustomBannerAdapter mCustomBannerAd;

    Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            loadAd(true);
        }
    };

    private InnerBannerListener mInnerBannerListener = new InnerBannerListener() {
        @Override
        public void onBannerLoaded(final CustomBannerAdapter adapter) {

            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (mAdLoadManager){
                        //clean previous ad
                        if (mCustomBannerAd != null) {
                            mCustomBannerAd.clean();
                        }
                        if (adapter != null){
                            mCustomBannerAd = adapter;
                            View networkBannerView = adapter.getBannerView();
                            int index = indexOfChild(networkBannerView);

                            if (index < 0) {
                                removeAllViews();
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.gravity = Gravity.BOTTOM;
                                //TODO Temporary processing
                                if (networkBannerView.getParent() != null && networkBannerView.getParent() != LCBannerView.this) {
                                    ((ViewGroup) networkBannerView.getParent()).removeView(networkBannerView);
                                }

                                addView(networkBannerView, params);
                            } else {
                                for (int i = index - 1; i >= 0; i--) {
                                    removeViewAt(i);
                                }
                            }

                            adapter.notfiyShow(getContext().getApplicationContext());
                            if (mListener != null) {
                                mListener.onBannerLoaded();
                                mListener.onBannerShow();
                            }



                            if (mAdLoadManager != null) {
                                CommonLogUtil.i(TAG, "in window load success to countDown refresh!");
                                startAutoRefresh(mRefreshRunnable);
                            }

                        }else {
                            if (mListener != null) {
                                mListener.onBannerFailed(ErrorCode.getErrorCode(ErrorCode.noADError, "", ""));
                            }
                        }
                    }
                }
            });

        }

        @Override
        public void onBannerFailed(final CustomBannerAdapter adapter, final AdError adError) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                             mCustomBannerAd = adapter;
                            mListener.onBannerFailed(adError);
                    }
                }
            });
        }

        @Override
        public void onBannerClicked(final CustomBannerAdapter adapter) {
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mCustomBannerAd = adapter;
                        mListener.onBannerClicked();
                    }
                }
            });
        }

        @Override
        public void onBannerShow(CustomBannerAdapter adapter) {

            mCustomBannerAd = adapter;
        }

        @Override
        public void onBannerClose(CustomBannerAdapter adapter) {
            mCustomBannerAd = adapter;
            SDKContext.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onBannerClose();
                    }
                }
            });
            //Refresh after closed
            loadAd(true);
        }
    };

    public void setUnitId(String advPlacememtId) {
        mAdLoadManager = AdLoadManager.getInstance(getContext(), advPlacememtId);
        this.advPlacememtId = advPlacememtId;
    }

    public LCBannerView(Context context) {
        super(context);
    }

    public LCBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LCBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LCBannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loadAd() {
        DBContext.init(this.getContext());
        LCSDK.apiLog(advPlacememtId, Const.LOGKEY.API_BANNER, Const.LOGKEY.API_LOAD, Const.LOGKEY.START, "");
        loadAd(false);
    }


    boolean mIsRefresh = false;
    private void loadAd(boolean isRefresh) {
        /**Stop timer**/
        mIsRefresh = isRefresh;
        if (mAdLoadManager != null) {
            CommonLogUtil.i(TAG, "start to load to stop countdown refresh!");
            stopAutoRefresh(mRefreshRunnable);
        }

        if (mAdLoadManager != null) {
            mAdLoadManager.startLoadAd(this,  SDKContext.getInstance().getContext(),advPlacememtId, mInnerBannerListener);
        } else {
            mInnerBannerListener.onBannerFailed(mCustomBannerAd,ErrorCode.getErrorCode(ErrorCode.placeStrategyError, "", ""));
        }
    }

    public void setBannerAdListener(LCBannerListener listener) {
        mListener = listener;
    }

    public void clean() {
        if (mCustomBannerAd != null) {
            mCustomBannerAd.clean();
        }
//        AdCacheManager.getInstance().forceCleanCache(mUnitId);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hasTouchWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hasTouchWindow = false;
        stopAutoRefresh(mRefreshRunnable);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }


    private void startAutoRefresh(Runnable runnable) {
        stopAutoRefresh(runnable);
    }

    private void stopAutoRefresh(Runnable runnable) {
        SDKContext.getInstance().removeMainThreadRunnable(runnable);
    }


}
