package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */







public class PrefLocation {

    public static String KEY_LAT_CENTER = "key_lat_center";
    public static String KEY_LON_CENTER = "key_lon_center";

    public static String KEY_PROXIMITY = "key_proximity";
    public static String KEY_DELIVERY_RANGE_MAX = "key_delivery_range_max";
    public static String KEY_DELIVERY_RANGE_MIN = "key_delivery_range_min";



    public static Location getLocation(Context context)
    {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        double longitude = (double) sharedPref.getFloat(KEY_LON_CENTER, 0f);
        double latitude = (double)sharedPref.getFloat(KEY_LAT_CENTER, 0f);


        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }





    public static void saveLatLonCurrent(double lat,double lon, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putFloat(KEY_LAT_CENTER, (float) lat);
        prefsEditor.putFloat(KEY_LON_CENTER, (float) lon);

        prefsEditor.apply();
    }




    public static void saveLatitude(float latitude, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.preference_file_name),
                        MODE_PRIVATE
                );


        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putFloat(KEY_LAT_CENTER, latitude);
        prefsEditor.apply();
    }


    public static double getLatitude(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double)sharedPref.getFloat(KEY_LAT_CENTER, 0f);
    }







    // saving longitude

    public static void saveLongitude(float longitude, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();


        prefsEditor.putFloat(KEY_LON_CENTER, longitude);
        prefsEditor.apply();
    }




    public static double getLongitude(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double) sharedPref.getFloat(KEY_LON_CENTER, 0f);
    }




    // saving longitude

//    public static void saveProximity(Float proximity, Context context)
//    {
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context
//                .getSharedPreferences(
//                        context.getString(R.string.preference_file_name),
//                        MODE_PRIVATE
//                );
//
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        if(proximity == null)
//        {
//            prefsEditor.putFloat(KEY_PROXIMITY, -1);
//        }
//        else
//        {
//            prefsEditor.putFloat(KEY_PROXIMITY, proximity);
//        }
//
//        prefsEditor.apply();
//    }
//
//
//
//
//
//    public static Double getProximity(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        Double proximity = (double) sharedPref.getFloat(KEY_PROXIMITY, -1);
//
//        if( proximity == -1)
//        {
//            return null;
//        }
//        else
//        {
//            return proximity;
//        }
//    }








    // saving longitude
//
//    public static void saveDeliveryRangeMax(Float rangeMax, Context context)
//    {
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context
//                .getSharedPreferences(
//                        context.getString(R.string.preference_file_name),
//                        MODE_PRIVATE
//                );
//
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        if(rangeMax == null)
//        {
//            prefsEditor.putFloat(KEY_DELIVERY_RANGE_MAX, -1);
//        }
//        else
//        {
//            prefsEditor.putFloat(KEY_DELIVERY_RANGE_MAX, rangeMax);
//        }
//
//        prefsEditor.apply();
//    }





//    public static Double getDeliveryRangeMax(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        Double deliveryRangeMax = (double) sharedPref.getFloat(KEY_DELIVERY_RANGE_MAX, -1);
//
//        if( deliveryRangeMax == -1)
//        {
//            return null;
//        }
//        else
//        {
//            return deliveryRangeMax;
//        }
//    }


//
//
//    public static void saveDeliveryRangeMin(Float rangeMax, Context context)
//    {
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context
//                .getSharedPreferences(
//                        context.getString(R.string.preference_file_name),
//                        MODE_PRIVATE
//                );
//
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        if(rangeMax == null)
//        {
//            prefsEditor.putFloat(KEY_DELIVERY_RANGE_MIN, -1);
//        }
//        else
//        {
//            prefsEditor.putFloat(KEY_DELIVERY_RANGE_MIN, rangeMax);
//        }
//
//        prefsEditor.apply();
//    }
//
//
//    public static Double getDeliveryRangeMin(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_file_name),
//                MODE_PRIVATE);
//
//        Double deliveryRangeMax = (double) sharedPref.getFloat(KEY_DELIVERY_RANGE_MIN, -1);
//
//        if( deliveryRangeMax == -1)
//        {
//            return null;
//        }
//        else
//        {
//            return deliveryRangeMax;
//        }
//    }

}
