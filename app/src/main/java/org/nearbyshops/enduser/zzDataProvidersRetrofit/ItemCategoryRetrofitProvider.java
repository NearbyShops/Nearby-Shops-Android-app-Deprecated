package org.nearbyshops.enduser.zzDataProvidersRetrofit;


import org.nearbyshops.enduser.zaDeprecatedItemCategories.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderItemCategory;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumeet on 19/5/16.
 */
public class ItemCategoryRetrofitProvider implements DataProviderItemCategory {


    //public static final String ITEM_CATEGORY_PARENT_ID_PARAM = "parentID";
    //public static final String ITEM_CATEGORY_SHOP_ID_PARAM = "shopID";


    //public final String PARENT_ID_PARAM = "ParentID";
    //public final String SHOP_ID = "ShopID";

    List<DataSubscriber<ItemCategory>> dataSubscribers = new ArrayList<>();

    @Inject Retrofit retrofit;

    ItemCategoryService itemCategoryService;

    public ItemCategoryRetrofitProvider() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .inject(this);

        itemCategoryService = retrofit.create(ItemCategoryService.class);

    }

    @Override
    public void read(int ID,
                     final DataSubscriber<ItemCategory> subscriber) {

        Call<ItemCategory> call = itemCategoryService.getItemCategory(ID);


        //dataSubscribers.add(subscriber);

        call.enqueue(new Callback<ItemCategory>() {
            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                subscriber.readCallback(false,true,response.code(),response.body());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readCallback(false,true,response.code(),response.body());
                }

            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                subscriber.readCallback(false,true,-1,null);

                for(DataSubscriber<ItemCategory> dataSubscriber: dataSubscribers)
                {
                    dataSubscriber.readCallback(false,true,-1,null);
                }

            }
        });


    }

    /*
    public void readMany(Map<String, String> stringParams,
                         Map<String, Integer> intParams,
                         Map<String, Boolean> booleanParams,
                         final DataSubscriber<ItemCategory> Subscriber) {

        Call<List<ItemCategory>> call = itemCategoryService.getItemCategories(
                intParams.get(ITEM_CATEGORY_PARENT_ID_PARAM),
                intParams.get(ITEM_CATEGORY_SHOP_ID_PARAM)
        );

        call.enqueue(new Callback<List<ItemCategory>>() {

            @Override
            public void onResponse(Call<List<ItemCategory>> call, Response<List<ItemCategory>> response) {

                Subscriber.readManyCallback(false,true,response.code(),response.body());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,response.code(),response.body());
                }


            }

            @Override
            public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                Subscriber.readManyCallback(false,false,-1,null);

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,-1,null);
                }

            }
        });

    }*/


    @Override
    public void delete(int ID, final DataSubscriber Subscriber) {


        Call<ResponseBody> call = itemCategoryService.deleteItemCategory(ID);

        //dataSubscribers.add(Subscriber);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Subscriber.deleteShopCallback(false,true,response.code());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.deleteShopCallback(false,true,response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Subscriber.deleteShopCallback(false,false,-1);

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.deleteShopCallback(false,false,-1);
                }

            }
        });

    }


    @Override
    public void insert(ItemCategory itemCategory, final DataSubscriber<ItemCategory> Subscriber) {

        Call<ItemCategory> call = itemCategoryService.insertItemCategory(itemCategory);

        call.enqueue(new Callback<ItemCategory>() {

            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                Subscriber.createCallback(false,true,response.code(),response.body());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.createCallback(false,true,response.code(),response.body());
                }
            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                Subscriber.createCallback(false,false,-1,null);

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.createCallback(false,false,-1,null);
                }
            }
        });

    }



    @Override
    public void update(ItemCategory itemCategory, final DataSubscriber Subscriber) {

        Call<ResponseBody> call = itemCategoryService.updateItemCategory(itemCategory,
                itemCategory.getItemCategoryID());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Subscriber.updateCallback(false,true,response.code());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.updateCallback(false,true,response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Subscriber.updateCallback(false,false,-1);

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.updateCallback(false,false,-1);
                }
            }
        });

    }


    @Override
    public void subscribe(DataSubscriber<ItemCategory> subscriber) {

        dataSubscribers.add(subscriber);
    }

    @Override
    public void readMany(
            int parentID,
            int shopID,
            double latCenter,
            double lonCenter,
            double deliveryRangeMax,
            double deliveryRangeMin,
            double proximity,
            final DataSubscriber<ItemCategory> Subscriber) {


        Call<List<ItemCategory>> call = itemCategoryService.getItemCategories(
                parentID,
                shopID,
                latCenter,
                lonCenter,
                deliveryRangeMax,
                deliveryRangeMin,
                proximity
        );

        //dataSubscribers.add(Subscriber);


        call.enqueue(new Callback<List<ItemCategory>>() {

            @Override
            public void onResponse(Call<List<ItemCategory>> call, Response<List<ItemCategory>> response) {


                Subscriber.readManyCallback(false,true,response.code(),response.body());

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,response.code(),response.body());


                }

                //dataSubscribers.remove(Subscriber);
            }

            @Override
            public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                Subscriber.readManyCallback(false,true,-1,null);

                for(DataSubscriber<ItemCategory> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,-1,null);
                }

                //dataSubscribers.remove(Subscriber);

            }
        });


    }
}
