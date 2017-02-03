package org.nearbyshops.enduserapp;

import android.app.Application;
import android.content.Context;

import org.nearbyshops.enduserapp.Application.ApplicationState;

/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends Application{

    private static Context context;

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
