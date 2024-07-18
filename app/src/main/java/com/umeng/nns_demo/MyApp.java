package com.umeng.nns_demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.uyumao.nns.zmd.InitCompleteListener;
import com.uyumao.nns.zmd.ZmdManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final Context appContext = this.getApplicationContext();
        UMConfigure.setLogEnabled(true);
        UMConfigure.preInit(appContext, "6673db4e940d5a4c49736345", "UMENG");

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                UMConfigure.init(appContext, "6673db4e940d5a4c49736345", "UMENG", UMConfigure.DEVICE_TYPE_PHONE, null);
                ZmdManager.setLogEnabled(true);
                ZmdManager.init(appContext, "6673db4e940d5a4c49736345", new InitCompleteListener() {
                    @Override
                    public void initComplete() {
                        Log.i("[nns_demo]", "InitCompleteListener: initComplete");
                    }
                });
            }
        }, 3, TimeUnit.SECONDS);
    }

}
