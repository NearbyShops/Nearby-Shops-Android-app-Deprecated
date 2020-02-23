package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefShopAdminHome {



    public static void saveShop(Shop shop, Context context)
    {
        context = MyApplication.getAppContext();

        if(context == null)
        {
            return;
        }

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

//        if(shop == null)
//        {
//            prefsEditor.putString("admin", "null");
//
//        }
//        else
//        {
//            Gson gson = new Gson();
//            String json = gson.toJson(shop);
//            prefsEditor.putString("shop", json);
//        }



        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(shop);
        prefsEditor.putString("shop_profile_for_shop_admin", json);

        prefsEditor.apply();
    }



    public static Shop getShop(Context context)
    {
        context = MyApplication.getAppContext();


        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString("shop_profile_for_shop_admin", null);


//        if(json.equals("null"))
//        {
//
//            return null;
//
//        }else
//        {
//            return gson.fromJson(json, Shop.class);
//        }


        return gson.fromJson(json, Shop.class);

    }
}
