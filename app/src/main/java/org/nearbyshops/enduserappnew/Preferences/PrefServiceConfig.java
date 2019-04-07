package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toolbar;

import com.google.gson.Gson;


import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 20/4/17.
 */



public class PrefServiceConfig {

    // simple or advanced at service selection screen
    // role selected at login screen


    // constants
    public static final int SERVICE_SELECT_MODE_SIMPLE = 1;
    public static final int SERVICE_SELECT_MODE_ADVANCED = 2;



    public static final String DEFAULT_SDS_URL = "http://sds.taxireferralservice.com:5600";
    public static final String DEFAULT_SDS_URL_BACKUP = "http://192.168.1.36:5600";

    private static final String TAG_PREF_CONFIG = "configuration";








    public static void saveServiceConfigLocal(ServiceConfigurationLocal currentTrip, Context context)
    {
        //Creating a shared preference

        if(context==null)
        {
            return;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(currentTrip);
        prefsEditor.putString(TAG_PREF_CONFIG, json);

        prefsEditor.apply();
    }



    public static ServiceConfigurationLocal getServiceConfigLocal(Context context)
    {
        if(context==null)
        {
            return null;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString(TAG_PREF_CONFIG, null);

        return gson.fromJson(json, ServiceConfigurationLocal.class);
    }






//    public static void saveConfigurationLocal(ServiceConfigurationLocal configuration, Context context)
//    {
//        if(context==null)
//        {
//            return;
//        }
//
//
//        //Creating a shared preference
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        Gson gson = UtilityFunctions.provideGson();
//        String json = gson.toJson(configuration);
//        prefsEditor.putString(TAG_PREF_CONFIG, json);
//
//        prefsEditor.apply();
//    }
//
//
//
//
//
//
//    public static ServiceConfigurationLocal getConfigurationLocal(Context context)
//    {
//
//        if(context==null)
//        {
//            return null;
//        }
//
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//
//        String json = sharedPref.getString(TAG_PREF_CONFIG, null);
//        Gson gson = UtilityFunctions.provideGson();
//
//        return gson.fromJson(json, ServiceConfigurationLocal.class);
//
//    }






    public static String getServiceName(Context context)
    {
        ServiceConfigurationLocal serviceConfigurationLocal = getServiceConfigLocal(context);


        if(serviceConfigurationLocal==null)
        {
            return null;
        }
        else
        {
            return serviceConfigurationLocal.getServiceName() + " - " + serviceConfigurationLocal.getCity();
        }
    }







}
