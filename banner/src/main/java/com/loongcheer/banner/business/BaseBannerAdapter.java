package com.loongcheer.banner.business;

import android.content.Context;
import android.view.View;

import com.loongcheer.core.common.CommonAdManager;
import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;


public abstract class BaseBannerAdapter extends LoongCheerBaseAdapter {

    public final void notfiyShow(Context context) {

        }

    public abstract View getBannerView();


}
