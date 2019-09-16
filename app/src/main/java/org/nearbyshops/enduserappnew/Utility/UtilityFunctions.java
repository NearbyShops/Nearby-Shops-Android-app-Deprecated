package org.nearbyshops.enduserappnew.Utility;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by sumeet on 10/7/17.
 */

public class UtilityFunctions {

    public static final String TAG_LOG = "app_log";


    /* Utility Functions */

    public static Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }





    public static String refinedString(double number)
    {
        if(number % 1 !=0)
        {
            // contains decimal numbers

            return String.format("%.2f",number);
        }
        else
        {
            return String.format("%.0f",number);
        }
    }




    public static void updateFirebaseSubscriptions()
    {
        // update topic subscriptions for fcm


//        FirebaseApp.getInstance().delete();

        User user = PrefLogin.getUser(getApplicationContext());
        ServiceConfigurationLocal localConfig = PrefServiceConfig.getServiceConfigLocal(getApplicationContext());


        if(user==null || localConfig==null || localConfig.getRt_market_id_for_fcm()==null)
        {
            return;
        }


        FirebaseApp.initializeApp(getApplicationContext());

        String topic_name = localConfig.getRt_market_id_for_fcm()  + "end_user_" + user.getUserID();

        FirebaseMessaging.getInstance().subscribeToTopic(topic_name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + topic_name);

                    }
                });

    }


}
