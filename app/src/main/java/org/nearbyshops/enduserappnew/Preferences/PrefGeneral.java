package org.nearbyshops.enduserappnew.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 5/5/16.
 */
public class PrefGeneral {



    //    public static final String DEFAULT_SERVICE_URL = "http://taxireferral.org";
    public static final String DEFAULT_SERVICE_URL = "http://example.com";

    public static final String SERVICE_URL_TEST_HYD = "http://192.168.1.33:5500";
    public static final String SERVICE_URL_LOCAL_HOTSPOT = "http://192.168.43.73:5121";
    public static final String SERVICE_URL_LOCAL = "http://192.168.0.5:5120";
    public static final String SERVICE_URL_NEARBYSHOPS = "http://api.nearbyshops.org";






    private static final String TAG_PREF_CURRENCY = "currency_symbol";
    private static final String TAG_PREF_CONFIG = "configuration";


//    public static final String LAT_CENTER_KEY = "latCenterKey";
//    public static final String LON_CENTER_KEY = "lonCenterKey";
//    public static final String DELIVERY_RANGE_MAX_KEY = "deliveryRangeMaxKey";
//    public static final String DELIVERY_RANGE_MIN_KEY = "deliveryRagneMinKey";
//    public static final String PROXIMITY_KEY = "proximityKey";





//    public static void saveInSharedPrefFloat(String key,float value)
//    {
//        Context context = MyApplication.getAppContext();
//
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putFloat(key,value);
//        editor.apply();
//    }
//
//
//    public static float getFromSharedPrefFloat(String key,float defaultValue)
//    {
//        Context context = MyApplication.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // read from shared preference
//
//        return sharedPref.getFloat(key, defaultValue);
//    }
//
//
//
//
//    public static float getFromSharedPrefFloat(String key)
//    {
//        Context context = MyApplication.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // read from shared preference
//
//        return sharedPref.getFloat(key,0.0f);
//    }





//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }


    public static String getImageEndpointURL(Context context)
    {
        return PrefGeneral.getServiceURL(context) + "/api/Images";
    }


//    public static String getConfigImageEndpointURL(Context context)
//    {
//        return PrefGeneral.getServiceURL(context) + "/api/ServiceConfigImages";
//    }
//
//
//    public DisplayMetrics getDisplayMetrics(Activity activity)
//    {
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        return metrics;
//    }





    public static void saveServiceURL(String service_url, Context context)
    {

//        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(
                context.getString(R.string.preference_service_url_key),
                service_url);

        editor.apply();


        // log out the user
        PrefLogin.saveUserProfile(null,context);
        PrefLogin.saveCredentials(context,null,null);
    }






    public static String getServiceURL(Context context) {

        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return sharedPref.getString(context.getString(R.string.preference_service_url_key), SERVICE_URL_LOCAL_HOTSPOT);
    }





    public static void saveConfiguration(ServiceConfigurationLocal configuration, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(configuration);
        prefsEditor.putString(TAG_PREF_CONFIG, json);
        prefsEditor.apply();
    }






    public static ServiceConfigurationLocal getConfiguration(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);


        String json = sharedPref.getString(TAG_PREF_CONFIG, null);
        Gson gson = UtilityFunctions.provideGson();

        return gson.fromJson(json, ServiceConfigurationLocal.class);

    }











//    public static void saveServiceLightStatus(Context context, int status)
//    {
//
//        // get a handle to shared Preference
//        SharedPreferences sharedPref;
//
//        sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_file_name),
//                MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("service_light_status",status);
//        editor.apply();
//    }
//
//
//
//    public static int getServiceLightStatus(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//        return sharedPref.getInt("service_light_status", 3);
//    }





    public static void saveCurrencySymbol(String symbol, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = UtilityFunctions.provideGson();

        String json = gson.toJson(symbol);
        prefsEditor.putString(TAG_PREF_CURRENCY, json);
        prefsEditor.apply();
    }


    public static String getCurrencySymbol(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        Gson gson = UtilityFunctions.provideGson();

        return sharedPref.getString(TAG_PREF_CURRENCY, context.getString(R.string.rupee_symbol));
    }

}
