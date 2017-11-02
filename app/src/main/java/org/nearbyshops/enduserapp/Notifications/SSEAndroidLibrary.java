package org.nearbyshops.enduserapp.Notifications;

import android.content.Intent;

import org.nearbyshops.enduserapp.Notifications.NonStopService.NonStopIntentService;

/**
 * Created by sumeet on 17/4/17.
 */

public class SSEAndroidLibrary extends NonStopIntentService{

    public SSEAndroidLibrary(String name) {
        super(name);
    }


    public SSEAndroidLibrary() {
        super("");
    }






    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
