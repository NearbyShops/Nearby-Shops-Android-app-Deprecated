package org.nearbyshops.enduserappnew.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.DeliveryAddressService;
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
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopImageService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopReviewService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopReviewThanksService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

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

//        Log.d("applog","Retrofit: " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));


        if(PrefGeneral.getServiceURL(MyApplication.getAppContext())!=null)
        {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                    .client(okHttpClient)
                    .build();

        }
        else
        {
            // a dummy method place here only to prevent returning null
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://example.com")
                    .client(okHttpClient)
                    .build();
        }
    }







    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {

        //        Log.d("applog","ShopItemService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return retrofit.create(ShopItemService.class);
    }


    @Provides
    CartService provideCartService(Retrofit retrofit)
    {

//        Log.d("applog","CartService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return retrofit.create(CartService.class);
    }


    @Provides
    CartItemService provideCartItemService(Retrofit retrofit)
    {

//        Log.d("applog","CartItemService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

        return retrofit.create(CartItemService.class);
    }


    @Provides
    CartStatsService provideCartStatsService(Retrofit retrofit)
    {
//        Log.d("applog","CartStatsService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));
        return retrofit.create(CartStatsService.class);
    }

    @Provides
    DeliveryAddressService provideDeliveryAddressService(Retrofit retrofit)
    {
        //        Log.d("applog","DeliveryAddressService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));
        return retrofit.create(DeliveryAddressService.class);
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
//        Log.d("applog","OrderServicePFS : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));
        return retrofit.create(OrderService.class);
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

//        Log.d("applog","ItemCategoryService : " + PrefGeneral.getServiceURL(MyApplication.getAppContext()));

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
        return retrofit.create(ShopService.class);
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
    UserService provideUserService(Retrofit retrofit)
    {
        return retrofit.create(UserService.class);
    }



    @Provides
    ShopImageService provideShopImage(Retrofit retrofit)
    {
        return retrofit.create(ShopImageService.class);
    }


}
