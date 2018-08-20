package org.nearbyshops.enduserappnew.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.FavouriteItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.FavouriteShopService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemImageService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemReviewService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemSpecItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemSpecNameService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemSpecValueService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.OrderItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.OrderService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopReviewService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopReviewThanksService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.RetrofitRESTContractPFS.OrderServicePFS;
import org.nearbyshops.enduserappnew.RetrofitRESTContractSDS.ServiceConfigService;
import org.nearbyshops.enduserappnew.Utility.PrefGeneral;

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
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        //Cache cache

        return new OkHttpClient()
                .newBuilder()
                .build();
    }



    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        Log.d("applog","Retrofit: " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));


        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(okHttpClient)
                .build();
    }


    @Provides @Named("sds")
    Retrofit provideRetrofitGIDB(Gson gson, OkHttpClient okHttpClient) {

        //        .client(okHttpClient)

//        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL_SDS(MyApplication.getAppContext()));


        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL_SDS(MyApplication.getAppContext()))
                .build();
    }




    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);

        Log.d("applog","ShopItemService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return shopItemService;
    }


    @Provides
    CartService provideCartService(Retrofit retrofit)
    {
        CartService cartService = retrofit.create(CartService.class);

        Log.d("applog","CartService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return cartService;
    }


    @Provides
    CartItemService provideCartItemService(Retrofit retrofit)
    {
        CartItemService cartItemService = retrofit.create(CartItemService.class);

        Log.d("applog","CartItemService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return  cartItemService;
    }


    @Provides
    CartStatsService provideCartStatsService(Retrofit retrofit)
    {
        CartStatsService service = retrofit.create(CartStatsService.class);

        Log.d("applog","CartStatsService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }

    @Provides
    DeliveryAddressService provideDeliveryAddressService(Retrofit retrofit)
    {

        DeliveryAddressService service = retrofit.create(DeliveryAddressService.class);

        Log.d("applog","DeliveryAddressService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return service;
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
        OrderService service = retrofit.create(OrderService.class);

        Log.d("applog","OrderServicePFS : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

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

        Log.d("applog","ItemCategoryService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

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

        return retrofit.create(ItemService.class);
    }


    @Provides
    ItemImageService itemImageService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageService.class);
    }

    @Provides
    ItemSpecNameService itemSpecNameService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecNameService.class);
    }


    @Provides
    ItemSpecValueService itemSpecValueService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecValueService.class);
    }


    @Provides
    ItemSpecItemService itemSpecItemService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecItemService.class);
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
    OrderServicePFS orderServicePFS(Retrofit retrofit)
    {
        return retrofit.create(OrderServicePFS.class);
    }


    @Provides
    ServiceConfigService provideServiceConfig(@Named("sds")Retrofit retrofit)
    {
        return retrofit.create(ServiceConfigService.class);
    }



    @Provides
    UserService provideUserService(Retrofit retrofit)
    {
        return retrofit.create(UserService.class);
    }

}
