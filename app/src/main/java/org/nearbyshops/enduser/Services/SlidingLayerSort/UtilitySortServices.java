package org.nearbyshops.enduser.Services.SlidingLayerSort;

import android.content.Context;
import android.content.SharedPreferences;


import org.nearbyshops.enduser.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 29/9/16.
 */

public class UtilitySortServices {



    public static void saveSort(Context context, String sort_by)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_services", sort_by);
        editor.apply();
    }


    public static String getSort(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String sort_by = sharedPref.getString("sort_services", SlidingLayerSortServices.SORT_BY_CREATED);

        return sort_by;
    }



    public static void saveAscending(Context context, String descending)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_descending_services",descending);
        editor.apply();
    }



    public static String getAscending(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_descending_services", SlidingLayerSortServices.SORT_DESCENDING);
    }





    public static void saveOfficial(Context context, Boolean descending)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("filter_official",descending);
        editor.apply();
    }



    public static boolean getOfficial(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getBoolean("filter_official", false);
    }



    public static void saveVerified(Context context, Boolean descending)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("filter_verified",descending);
        editor.apply();
    }



    public static boolean getVerified(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getBoolean("filter_verified", false);
    }



    public static void saveServiceType(Context context, int serviceType)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("filter_service_type",serviceType);
        editor.apply();
    }



    public static int getServiceType(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt("filter_service_type", -1);
    }



}
