package org.nearbyshops.enduser.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;

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


    public static float getFromSharedPrefFloat(String key)
    {
        Context context = MyApplication.getAppContext();
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // read from shared preference
        float value = sharedPref.getFloat(key, 0.0f);

        return value;
    }



    public static void saveEndUserID(int endUserID)
    {
        Context context = MyApplication.getAppContext();
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.preference_end_user_id_key),endUserID);
        editor.commit();

    }

    public static int getEndUserID(Context context) {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // read from shared preference
        int endUserID = sharedPref.getInt(context.getString(R.string.preference_end_user_id_key), 0);
        return endUserID;
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
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key), "");

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
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




    /*



    public String  getServiceURL()
    {
        // Get a handle to the shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
    }

     */


    /*
    public int getDistributorID() {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key), 0);

        return distributorID;
    }


    public String getServiceURL() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key), "default");

        return service_url;
    }
     */

}
