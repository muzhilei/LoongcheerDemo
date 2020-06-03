package com.loongcheer.core.common.utils;

import android.util.Log;

import com.loongcheer.core.common.base.Const;
import com.loongcheer.core.common.base.LoongCheerBaseAdapter;
import com.loongcheer.core.common.strategy.PlaceStrategy;

import java.lang.reflect.Constructor;

/**
 * Created by Z on 2018/1/9.
 * Adapter Factory
 */

public class CustomAdapterFactory {

    protected static CustomAdapterFactory instance = new CustomAdapterFactory();

    protected static LoongCheerBaseAdapter create(final String className) throws Exception {
        if (className != null) {
            final Class<? extends LoongCheerBaseAdapter> nativeClass = Class.forName(className)
                    .asSubclass(LoongCheerBaseAdapter.class);
            return instance.internalCreate(nativeClass);
        } else {
            return null;
        }
    }

    protected LoongCheerBaseAdapter internalCreate(
            final Class<? extends LoongCheerBaseAdapter> nativeClass) throws Exception {
        if (nativeClass == null) {
            Log.w(Const.RESOURCE_HEAD, "can not find adapter");
        }

        final Constructor<?> nativeConstructor = nativeClass.getDeclaredConstructor((Class[]) null);
        nativeConstructor.setAccessible(true);
        return (LoongCheerBaseAdapter) nativeConstructor.newInstance();
    }

    public static LoongCheerBaseAdapter createAdapter(final PlaceStrategy.UnitGroupInfo unitGroupInfo) {
        LoongCheerBaseAdapter customRewardVideoAdapter;

        try {
            customRewardVideoAdapter = CustomAdapterFactory.create(unitGroupInfo.adapterClassName);
        } catch (Throwable e) {

            e.printStackTrace();
            return null;
        }
        return customRewardVideoAdapter;
    }
}
