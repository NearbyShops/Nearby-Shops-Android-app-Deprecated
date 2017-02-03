package org.nearbyshops.enduserapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */




public class UtilityShopHome {

    public static void saveShop(Shop shop, Context context)
    {

        if(context == null)
        {
            return;
        }

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(shop == null)
        {
            prefsEditor.putString("admin", "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(shop);
            prefsEditor.putString("shop", json);
        }

        prefsEditor.apply();
    }


    public static Shop getShop(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("shop", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Shop.class);
        }

    }
}
