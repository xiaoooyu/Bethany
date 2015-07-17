package com.xiaoooyu.bethany.sample;

import android.app.Application;
import android.content.Context;


/**
 * Created by xiaoooyu on 4/15/15.
 *
 * implements BootstrapNotifier thus application can be launched on Beacon detection
 */
public class BethanyApplication extends Application {

    private final static String TAG = "BethanyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }
}
