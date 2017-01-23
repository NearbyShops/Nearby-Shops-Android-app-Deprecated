package org.nearbyshops.enduser.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.CartService;
import org.nearbyshops.enduser.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduser.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduser.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduser.RetrofitRESTContract.FavouriteItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.FavouriteShopService;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemReviewService;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.OrderItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.OrderService;
import org.nearbyshops.enduser.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewThanksService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduser.RetrofitRESTContractSDS.ServiceConfigService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 14/5/16.
 */

        /*
        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

@Module
public class NetModule {

    String serviceURL;

    // Constructor needs one parameter to instantiate.
    public NetModule() {

    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    /*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    */



    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        //Cache cache

        return client;
    }



    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        Log.d("applog","Retrofit: " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(okHttpClient)
                .build();

        return retrofit;
    }


    @Provides @Named("sds")
    Retrofit provideRetrofitGIDB(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL_SDS(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL_SDS(MyApplication.getAppContext()));


        return retrofit;
    }




    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);

        Log.d("applog","ShopItemService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return shopItemService;
    }


    @Provides
    CartService provideCartService(Retrofit retrofit)
    {
        CartService cartService = retrofit.create(CartService.class);

        Log.d("applog","CartService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return cartService;
    }


    @Provides
    CartItemService provideCartItemService(Retrofit retrofit)
    {
        CartItemService cartItemService = retrofit.create(CartItemService.class);

        Log.d("applog","CartItemService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return  cartItemService;
    }


    @Provides
    CartStatsService provideCartStatsService(Retrofit retrofit)
    {
        CartStatsService service = retrofit.create(CartStatsService.class);

        Log.d("applog","CartStatsService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }

    @Provides
    DeliveryAddressService provideDeliveryAddressService(Retrofit retrofit)
    {

        DeliveryAddressService service = retrofit.create(DeliveryAddressService.class);

        Log.d("applog","DeliveryAddressService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
        OrderService service = retrofit.create(OrderService.class);

        Log.d("applog","OrderService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }


    @Provides
    OrderItemService orderItemService(Retrofit retrofit)
    {
        return retrofit.create(OrderItemService.class);
    }



    @Provides
    ItemCategoryService itemCategoryService(Retrofit retrofit)
    {
        ItemCategoryService service = retrofit.create(ItemCategoryService.class);

        Log.d("applog","ItemCategoryService : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }


    @Provides
    ServiceConfigurationService configurationService(Retrofit retrofit)
    {
        ServiceConfigurationService configurationService = retrofit.create(ServiceConfigurationService.class);
        return configurationService;
    }


    @Provides
    ItemService itemService(Retrofit retrofit)
    {

        ItemService itemService = retrofit.create(ItemService.class);
        return itemService;
    }



    @Provides
    ShopService shopService(Retrofit retrofit)
    {

        ShopService shopService = retrofit.create(ShopService.class);
        return shopService;
    }


    @Provides
    EndUserService endUserService(Retrofit retrofit)
    {

        EndUserService endUserService = retrofit.create(EndUserService.class);
        return endUserService;
    }



    @Provides
    ShopReviewService shopReviewService(Retrofit retrofit)
    {

//        EndUserService endUserService = ;
        return retrofit.create(ShopReviewService.class);
    }


    @Provides
    ItemReviewService itemReviewService(Retrofit retrofit)
    {
        return retrofit.create(ItemReviewService.class);
    }



    @Provides
    FavouriteShopService favouriteShopService(Retrofit retrofit)
    {

//        EndUserService endUserService = ;
        return retrofit.create(FavouriteShopService.class);
    }


    @Provides
    FavouriteItemService favouriteItemService(Retrofit retrofit)
    {
        return retrofit.create(FavouriteItemService.class);
    }



    @Provides
    ShopReviewThanksService shopReviewThanksService(Retrofit retrofit)
    {

//        EndUserService endUserService = ;
        return retrofit.create(ShopReviewThanksService.class);
    }




    @Provides
    ServiceConfigService provideServiceConfig(@Named("sds")Retrofit retrofit)
    {
        return retrofit.create(ServiceConfigService.class);
    }


}
