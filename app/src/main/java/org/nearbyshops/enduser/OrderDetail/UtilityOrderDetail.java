package org.nearbyshops.enduser.OrderDetail;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.enduser.ModelCartOrder.Order;
import org.nearbyshops.enduser.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */




public class UtilityOrderDetail {


    public static final String TAG_ORDER_DETAIL = "ORDER_DETAIL";

    public static void saveOrder(Order order, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(order == null)
        {
            prefsEditor.putString(TAG_ORDER_DETAIL, "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(order);
            prefsEditor.putString(TAG_ORDER_DETAIL, json);
        }

        prefsEditor.apply();
    }


    public static Order getOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_ORDER_DETAIL, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Order.class);
        }

    }
}
