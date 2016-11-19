package com.gamila.zm.ThirdPartiesLibsDemo;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.gamila.zm.ThirdPartiesLibsDemo.util.ResourcesUitl;

/**
 * Created by zeinab on 4/29/2016.
 */
public class AppApplication extends Application {

    private static Context mAppApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppApplication = this;
        // initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        // to  keep track of facebook analytics
        AppEventsLogger.activateApp(this);

        // initialize resources
        ResourcesUitl.getInstance().init(getApplicationContext());



    }


    public static Context getInstance() {
        return mAppApplication.getApplicationContext();
    }
}
