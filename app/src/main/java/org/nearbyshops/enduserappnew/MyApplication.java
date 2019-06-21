package org.nearbyshops.enduserappnew;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.mapbox.mapboxsdk.Mapbox;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.nearbyshops.enduserappnew.Model.ModelUtility.NotificationEvent;
import org.nearbyshops.enduserappnew.OneSignal.PrefOneSignal;
import org.nearbyshops.enduserappnew.Preferences.PrefBadgeCount;



/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends MultiDexApplication {

    private static Context context;



    // end-user notifications
    public static final int ORDER_PLACED = 21;
    public static final int ORDER_CONFIRMED = 22;
    public static final int ORDER_PACKED = 23;
    public static final int ORDER_OUT_FOR_DELIVERY = 24;

    public static final int ORDER_CANCELLED_BY_SHOP = 25;





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



        // Initialize Places.
//        Places.initialize(getApplicationContext(), "AIzaSyAHjmh3U3OVYngo6huNoEpYhscFqcV9CFA");




        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {


//                        Log.d("one_signal",notification.toJSONObject().toString());

                        Log.d("one_signal",notification.payload.additionalData.toString());

                        JSONObject data = notification.payload.additionalData;


//                        int screenToOpen = -1;
                        int notificationType = -1;


                        if (data != null) {

//                            screenToOpen = data.optInt("screen_to_open",-1);
                            notificationType = data.optInt("notification_type",-1);




                            if(notificationType== MyApplication.ORDER_PLACED ||
                                    notificationType== MyApplication.ORDER_PACKED ||
                                    notificationType== MyApplication.ORDER_CONFIRMED||
                                    notificationType== MyApplication.ORDER_OUT_FOR_DELIVERY)
                            {
                                PrefBadgeCount.saveBadgeCountOrders(PrefBadgeCount.getBadgeCountOrders(getApplicationContext())+1,
                                        getApplicationContext());
                            }




//                            if(notificationType==MyApplication.PICKUP_STARTED ||
//                                    notificationType== MyApplication.TRIP_STARTED ||
//                                    notificationType==MyApplication.TRIP_FINISHED ||
//                                    notificationType==MyApplication.TRIP_CANCELLED_BY_DRIVER)
//                            {
//
//                                PrefBadgeCount.saveBadgeCountCarts(PrefBadgeCount.getBadgeCountCarts(getApplicationContext())+1,
//                                        getApplicationContext());
//                            }



                            NotificationEvent event = new NotificationEvent(notificationType);
                            EventBus.getDefault().post(event);

                        }




                    }
                })
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {

                        JSONObject data = result.notification.payload.additionalData;

//                        Gson gson = UtilityFunctions.provideGson();
//                        OneSignalData oneSignalData= gson.fromJson(data.toString(), OneSignalData.class);

                        int screenToOpen = -1;
                        int notificationType = -1;


                        if (data != null) {
                            screenToOpen = data.optInt("screen_to_open",-1);
                            notificationType = data.optInt("notification_type",-1);

                            if (screenToOpen != -1)
                                Log.i("one_signal", "Screen To Open: " + String.valueOf(screenToOpen));
                            Log.i("one_signal", "Notificaion Type : " + String.valueOf(notificationType));
                        }




                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("screen_to_open",screenToOpen);

                        startActivity(intent);

                        Log.d("one_signal","Notification Opened !");



                    }
                })
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();



        Mapbox.getInstance(this,getString(R.string.fake_key));


        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {


                Log.d("one_signal", "User:" + userId);
                Log.d("one_signal", "registrationId:" + registrationId);


                PrefOneSignal.saveLastToken(getApplicationContext(),userId);
                PrefOneSignal.saveToken(getApplicationContext(),userId);



//                if (PrefGeneral.getServiceURL(getApplicationContext())!=null) {
//                    startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
//                }



//                getApplicationContext().startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
            }

        });
    }



    public static Context getAppContext() {
        return MyApplication.context;
    }






}
