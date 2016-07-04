package org.nearbyshops.enduser.zzDataProvidersRetrofit;

import org.nearbyshops.enduser.zaDeprecatedItemCategories.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderItem;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumeet on 26/5/16.
 */
public class ItemProviderRetrofit implements DataProviderItem {



    List<DataSubscriber<Item>> dataSubscribers = new ArrayList<>();

    @Inject
    Retrofit retrofit;

    ItemService itemService;

    public ItemProviderRetrofit() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .inject(this);

        itemService = retrofit.create(ItemService.class);
    }

    @Override
    public void readMany(
            int itemCategoryID,
            int shopID,
            double latCenter,
            double lonCenter,
            double deliveryRangeMax,
            double deliveryRangeMin,
            double proximity, final DataSubscriber<Item> subscriber
    ) {


        Call<List<Item>> listCall = itemService.getItems(
                itemCategoryID,
                shopID,
                latCenter,
                lonCenter,
                deliveryRangeMax,
                deliveryRangeMin,proximity
        );


        listCall.enqueue(new Callback<List<Item>>() {


            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                subscriber.readManyCallback(false,true,response.code(),response.body());

                for(DataSubscriber<Item> dataSubscriber: dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,response.code(),response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

                subscriber.readManyCallback(false,false,-1,null);

                for(DataSubscriber<Item> dataSubscriber: dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,false,-1,null);
                }
            }
        });





    }

    @Override
    public void read(int ID, DataSubscriber<Item> Subscriber) {

    }

    @Override
    public void delete(int ID, DataSubscriber Subscriber) {

    }

    @Override
    public void insert(Item item, DataSubscriber<Item> Subscriber) {

    }

    @Override
    public void update(Item item, DataSubscriber Subscriber) {

    }

    @Override
    public void subscribe(DataSubscriber<Item> Subscriber) {

    }
}
