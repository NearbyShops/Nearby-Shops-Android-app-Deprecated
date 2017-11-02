package org.nearbyshops.enduserapp.Notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.nearbyshops.enduserapp.MyApplication;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by sumeet on 17/11/16.
 */

public class SSEServiceUser extends Service {

    public static final String END_USER_ID = "END_USER_ID";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */




    void logMessage(String message)
    {
        Log.d("notification_log",message);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        logMessage("Inside Notification Intent Service !");
        System.out.println("On Handle Intent !");


        try{

            handleNotification(intent);
        }
        catch (Exception ex)
        {
            System.out.println("Exception : " + ex.toString());
        }

        return null;
    }






    void handleNotification(Intent intent)
    {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        int endUserID = -1;

//        System.out.println("Inside Before Shop Fetch!");
        logMessage("Inside Before EndUser Fetch !");
        System.out.println("On Handle Intent : Handle Notification !");

        if (intent != null) {
            endUserID = intent.getIntExtra(END_USER_ID,-1);
        }

        if(endUserID==-1)
        {
            return;
        }


//        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/ShopStaff/Notifications/" + String.valueOf(shopID);

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/v1/EndUser/Notifications/" + String.valueOf(endUserID);


        System.out.println("URL : " + url);
        logMessage("URL : " + url);


        WebTarget target = client.target(url);

        EventInput eventInput = target.request().get(EventInput.class);


        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                // connection has been closed
                break;
            }

            System.out.println(inboundEvent.getName() + "; "
                    + inboundEvent.readData(String.class));

            String eventName = inboundEvent.getName();
            String message = inboundEvent.readData(String.class);


//            Drawable drawable = VectorDrawableCompat.create(getResources(),R.drawable.ic_shopping_basket_white_24px,getTheme());

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(SSEServiceUser.this)
                            .setContentTitle(eventName)
                            .setContentText(message)
                            .setContentInfo(message)
                            .setSmallIcon(R.mipmap.shopping_basket_png)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

//            .setStyle(new NotificationCompat.BigTextStyle().bigText("Order Received !"))


            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//            Intent notificationIntent = new Intent(getApplicationContext(), OrderHistoryPFS.class);

//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//            PendingIntent intentPending = PendingIntent.getActivity(getApplicationContext(), 0,
//                    notificationIntent, 0);

//            Notification notification = mBuilder.build();

//            notification.set(getApplicationContext(),"Title", message, intent);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // mId allows you to update the notification later on.
            mNotificationManager.notify(2, mBuilder.build());
        }
    }


}
