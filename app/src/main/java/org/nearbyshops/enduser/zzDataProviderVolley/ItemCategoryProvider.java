package org.nearbyshops.enduser.zzDataProviderVolley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderItemCategory;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.VolleySingleton;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class ItemCategoryProvider implements DataProviderItemCategory {


    public void readMany(int parentID, int shopID, final DataSubscriber<ItemCategory> Subscriber) {

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/ItemCategory" + "?ParentID=" + parentID + "&ShopID=" + shopID;

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("response",response);

                Gson gson = new GsonBuilder().create();

                Type listType = new TypeToken<List<ItemCategory>>() {}.getType();
                List<ItemCategory> parsedItems = gson.fromJson(response,listType);

                Subscriber.readManyCallback(false,true,-1,parsedItems);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());

                Subscriber.readManyCallback(false,false,-1,null);

            }
        });

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);

    }

    @Override
    public void read(int ID, DataSubscriber<ItemCategory> Subscriber) {

    }

    @Override
    public void delete(int ID, DataSubscriber Subscriber) {

    }

    @Override
    public void insert(ItemCategory itemCategory, DataSubscriber<ItemCategory> Subscriber) {

    }

    @Override
    public void update(ItemCategory itemCategory, DataSubscriber Subscriber) {

    }

    @Override
    public void subscribe(DataSubscriber<ItemCategory> Subscriber) {

    }

    @Override
    public void readMany(int parentID, int shopID, double latCenter, double lonCenter, double deliveryRangeMax, double deliveryRangeMin, double proximity, DataSubscriber<ItemCategory> Subscriber) {

    }



                /*
            JSONArray array = new JSONArray(jsonString);

            for(int i=0;i<array.length();i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ItemCategory itemCategory = new ItemCategory();
                itemCategory.setItemCategoryID(jsonObject.getInt("itemCategoryID"));
                itemCategory.setCategoryName(jsonObject.getString("categoryName"));
                itemCategory.setCategoryDescription(jsonObject.getString("categoryDescription"));
                itemCategory.setIsLeafNode(jsonObject.getBoolean("isLeafNode"));
                itemCategory.setParentCategoryID(jsonObject.getInt("parentCategoryID"));

                if (dataset != null) {


                    dataset.add(itemCategory);
                    Log.d("response","from Json Parsing" + dataset.size());
                }
            }

            */



}
