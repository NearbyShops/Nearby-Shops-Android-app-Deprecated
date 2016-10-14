package org.nearbyshops.enduser.zzDataProvidersRetrofit;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderShop;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumeet on 25/5/16.
 */
public class ShopRetrofitProvider implements DataProviderShop {

    List<DataSubscriber<Shop>> dataSubscribers = new ArrayList<>();

    @Inject
    Retrofit retrofit;

    ShopService shopService;


    public ShopRetrofitProvider() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .inject(this);

        shopService = retrofit.create(ShopService.class);
    }

    @Override
    public void readMany(Integer distributorID,
                         Integer itemCategoryID,
                         Double latCenter,
                         Double lonCenter,
                         Double deliveryRangeMax,
                         Double deliveryRangeMin,
                         Double proximity,
                         final DataSubscriber<Shop> subscriber) {

        Call<List<Shop>> shopCall = shopService.getShops(distributorID,
                itemCategoryID,
                latCenter,
                lonCenter,
                deliveryRangeMax,
                deliveryRangeMin,
                proximity);


        shopCall.enqueue(new Callback<List<Shop>>() {

            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {

                subscriber.readManyCallback(false,true,response.code(),response.body());

                for(DataSubscriber<Shop> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,true,response.code(),response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {

                subscriber.readManyCallback(false,false,-1,null);

                for(DataSubscriber<Shop> dataSubscriber:dataSubscribers)
                {
                    dataSubscriber.readManyCallback(false,false,-1,null);
                }
            }
        });

    }

    @Override
    public void read(int ID, final DataSubscriber<Shop> subscriber) {

        Call<Shop> shopCall = shopService.getShop(ID);

        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                subscriber.readCallback(false,true,response.code(),response.body());

                for(DataSubscriber<Shop> dataSubscriber: dataSubscribers)
                {
                    dataSubscriber.readCallback(false,true,response.code(),response.body());
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

                subscriber.readCallback(false,false,-1,null);

                for(DataSubscriber<Shop> dataSubscriber: dataSubscribers)
                {
                    dataSubscriber.readCallback(false,false,-1,null);
                }
            }
        });

    }

    @Override
    public void delete(int ID, DataSubscriber Subscriber) {

    }

    @Override
    public void insert(Shop shop, DataSubscriber<Shop> Subscriber) {

    }

    @Override
    public void update(Shop shop, DataSubscriber Subscriber) {

    }

    @Override
    public void subscribe(DataSubscriber<Shop> subscriber) {

        dataSubscribers.add(subscriber);
    }
}
