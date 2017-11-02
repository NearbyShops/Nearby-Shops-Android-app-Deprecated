package org.nearbyshops.enduserapp.Notifications.NonStopService;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.nearbyshops.enduserapp.MyApplication;
import org.nearbyshops.enduserapp.Notifications.SSEIntentServiceUser;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;
import org.nearbyshops.enduserapp.Utility.UtilityLogin;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by sumeet on 9/3/17.
 */





public class IntentServiceSSE extends NonStopIntentService {


    public IntentServiceSSE(String name) {
        super(name);
    }


    public IntentServiceSSE() {
        super("name");
    }


    EventSource eventSource;
    WebTarget target;




    @Override
    protected void onHandleIntent(Intent intent) {

//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

//        CountDownTimer countDownTimer = new CountDownTimer(1000*60,1) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };



//        do {
//
//            if(UtilityLogin.getEndUser(getBaseContext())==null)
//            {
//                break;
//            }
//
//            if(eventSource!=null)
//            {
//                eventSource.close();
//            }
//
//            try
//            {
//                handleNotificationTwo();
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//
//                System.out.println("Exception thrown : " + ex.toString());
//            }
//
//
//            try {
//                Thread.sleep(60*1000*5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//        } while (true);



            if(UtilityLogin.getEndUser(getBaseContext())==null)
            {
                return;
            }

//            if(eventSourceThree!=null)
//            {
//                eventSourceThree.close();
//            }

            try
            {
                handleNotificationThree();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();

                System.out.println("Exception thrown : " + ex.toString());
            }




//        do {
//
//
//
//            try {
//
////                reconnectEventSource();
//
//
//                Thread.sleep(60*1000*1);
//
//                System.out.println("Reconnecting Event Source !");
//
//
//
//
//                if(eventSource.isOpen())
//                {
//                    eventSource.close();
//                }
//
//
//
//
//                eventSource.open();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } while (true);


    }




    void handleNotification()
    {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        int endUserID = -1;

//        System.out.println("Inside Before Shop Fetch!");
//        logMessage("Inside Before EndUser Fetch !");
        System.out.println("On Handle Intent : Handle Notification !");

//        if (intent != null) {
//            endUserID = intent.getIntExtra(END_USER_ID,-1);
//        }


        if(UtilityLogin.getEndUser(getBaseContext())!=null)
        {
            endUserID = UtilityLogin.getEndUser(getBaseContext()).getEndUserID();
        }
        else
        {
            return;
        }

        System.out.println("End User Logged In !");



//        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/ShopStaff/Notifications/" + String.valueOf(shopID);

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/v1/EndUser/Notifications/" + String.valueOf(endUserID);


        System.out.println("URL : " + url);
//        logMessage("URL : " + url);


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
                    new NotificationCompat.Builder(IntentServiceSSE.this)
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

            System.out.println("Notification Service Inside While Loop !");
        }


        System.out.println("Notification Service Outside While Loop !");
    }



    void handleNotificationTwo()
    {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        int endUserID = -1;

//        System.out.println("Inside Before Shop Fetch!");
//        logMessage("Inside Before EndUser Fetch !");
        System.out.println("On Handle Intent : Handle Notification !");

//        if (intent != null) {
//            endUserID = intent.getIntExtra(END_USER_ID,-1);
//        }


        if(UtilityLogin.getEndUser(getBaseContext())!=null)
        {
            endUserID = UtilityLogin.getEndUser(getBaseContext()).getEndUserID();
        }
        else
        {
            return;
        }

        System.out.println("End User Logged In !");



//        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/ShopStaff/Notifications/" + String.valueOf(shopID);

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/v1/EndUser/Notifications/" + String.valueOf(endUserID);


        System.out.println("URL : " + url);
//        logMessage("URL : " + url);


        WebTarget target = client.target(url);


//        eventSource.close();

        System.out.println("Notification Service Closed !");
    }



    void eventSourceThree()
    {


        eventSource = new EventSource(target) {
            @Override
            public void onEvent(InboundEvent inboundEvent) {

//                if ("message-to-client".equals(inboundEvent.getName())) {
                System.out.println(inboundEvent.getName() + "; "
                        + inboundEvent.readData(String.class));

                String eventName = inboundEvent.getName();
                String message = inboundEvent.readData(String.class);


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(IntentServiceSSE.this)
                                .setContentTitle(eventName)
                                .setContentText(message)
                                .setContentInfo(message)
                                .setSmallIcon(R.mipmap.shopping_basket_png)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(2, mBuilder.build());
            }


        };
    }



    EventSource eventSourceThree;



    void handleNotificationThree()
    {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();


        int endUserID = -1;

        if(UtilityLogin.getEndUser(getBaseContext())!=null)
        {
            endUserID = UtilityLogin.getEndUser(getBaseContext()).getEndUserID();
        }
        else
        {
            return;
        }


        System.out.println("On Handle Intent : Handle Notification !");

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/v1/EndUser/Notifications/" + String.valueOf(endUserID);

        System.out.println("URL : " + url);
//        logMessage("URL : " + url);


        WebTarget target = client.target(url);

        eventSourceThree = EventSource.target(target)
                .reconnectingEvery(5,TimeUnit.SECONDS)
                .build();



        EventListener listener = new EventListener() {
            @Override
            public void onEvent(InboundEvent inboundEvent) {


                    System.out.println(inboundEvent.getName() + "; "
                            + inboundEvent.readData(String.class));

                    String eventName = inboundEvent.getName();
                    String message = inboundEvent.readData(String.class);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(IntentServiceSSE.this)
                                    .setContentTitle(eventName)
                                    .setContentText(message)
                                    .setContentInfo(message)
                                    .setSmallIcon(R.mipmap.shopping_basket_png)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(2, mBuilder.build());
            }
        };



        eventSourceThree.register(listener);
        eventSourceThree.open();


    }



    void reconnectEventSource()
    {
        eventSourceThree.close();
        eventSourceThree.open();
    }


}
