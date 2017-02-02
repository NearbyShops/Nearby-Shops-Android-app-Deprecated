package org.nearbyshops.enduser.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import org.nearbyshops.enduser.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 5/5/16.
 */
public class UtilityGeneral {


    public static final String LAT_CENTER_KEY = "latCenterKey";
    public static final String LON_CENTER_KEY = "lonCenterKey";
    public static final String DELIVERY_RANGE_MAX_KEY = "deliveryRangeMaxKey";
    public static final String DELIVERY_RANGE_MIN_KEY = "deliveryRagneMinKey";
    public static final String PROXIMITY_KEY = "proximityKey";


    public static void saveInSharedPrefFloat(String key,float value)
    {
        Context context = MyApplication.getAppContext();

        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key,value);
        editor.commit();
    }


    public static float getFromSharedPrefFloat(String key,float defaultValue)
    {
        Context context = MyApplication.getAppContext();
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // read from shared preference
        float value = sharedPref.getFloat(key, defaultValue);

        return value;
    }


    public static float getFromSharedPrefFloat(String key)
    {
        Context context = MyApplication.getAppContext();
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // read from shared preference
        float value = sharedPref.getFloat(key,0.0f);

        return value;
    }





    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getImageEndpointURL(Context context)
    {
        return UtilityGeneral.getServiceURL(context) + "/api/Images";
    }


    public static String getConfigImageEndpointURL(Context context)
    {
        return UtilityGeneral.getServiceURL(context) + "/api/ServiceConfigImages";
    }


    public DisplayMetrics getDisplayMetrics(Activity activity)
    {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics;
    }












    public static void saveServiceURL(String service_url)
    {
        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(
                context.getString(R.string.preference_service_url_key),
                service_url);

        editor.commit();
    }

    public static String getServiceURL(Context context) {

        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key), "http://nearbyshops.org");

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
    }





    public static void saveServiceURL_SDS(String service_url,Context context)
    {
//        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_service_url_sds_key), service_url);
        editor.apply();
    }

    public static String getServiceURL_SDS(Context context) {

//        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.preference_service_url_sds_key), "http://sds.nearbyshops.org");
    }








    private static final String TAG_PREF_CONFIG = "configuration";

    public static void saveConfiguration(ServiceConfigurationLocal configuration, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(configuration);
        prefsEditor.putString(TAG_PREF_CONFIG, json);
        prefsEditor.apply();
    }


    public static ServiceConfigurationLocal getConfiguration(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_CONFIG, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ServiceConfigurationLocal.class);
        }
    }





    public static void saveServiceLightStatus(Context context, int status)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("service_light_status",status);
        editor.apply();
    }



    public static int getServiceLightStatus(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt("service_light_status", 3);
    }


}
