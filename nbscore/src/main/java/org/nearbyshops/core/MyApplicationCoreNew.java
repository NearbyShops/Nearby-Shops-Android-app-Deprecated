package org.nearbyshops.core;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
//import com.onesignal.OSNotification;
//import com.onesignal.OSNotificationOpenResult;
//import com.onesignal.OneSignal;


/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplicationCoreNew extends MultiDexApplication {



    public static Context context;






    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    public void onCreate() {

        super.onCreate();


        MyApplicationCoreNew.context = getApplicationContext();

    }



    public static Context getAppContext() {
        return MyApplicationCoreNew.context;
    }




}
