package org.nearbyshops.enduserapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.nearbyshops.enduserapp.Application.ApplicationState;

/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends MultiDexApplication{

    private static Context context;


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public void onCreate() {

        super.onCreate();

        MyApplication.context = getApplicationContext();

        //LeakCanary.install(this);

        ApplicationState.getInstance().setMyApplication(this);
    }


    public static Context getAppContext() {

        return MyApplication.context;

    }


}
