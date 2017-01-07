package org.nearbyshops.enduser.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.nearbyshops.enduser.R;

/**
 * Created by sumeet on 20/10/16.
 */

public class UtilityLocationOld {


    public static void saveCurrentLocation(Context context, Location location)
    {

        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("lat", (float) location.getLatitude());
        editor.putFloat("lon", (float) location.getLongitude());
        editor.commit();
    }


    public static Location getCurrentLocation(Context context)
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);


        Location location = new Location("provider");

        // read from shared preference
        float lat = sharedPref.getFloat("lat",-1);
        float lon = sharedPref.getFloat("lon",-1);


        if(lat!=-1 && lon!=-1)
        {
            location.setLatitude(lat);
            location.setLongitude(lon);

            return location;

        }else
        {
            return null;
        }
    }
}
