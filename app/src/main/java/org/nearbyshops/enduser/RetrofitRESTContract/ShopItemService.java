package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.ShopItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {

    @GET("/api/ShopItem")
    Call<List<ShopItem>> getShopItems(
            @Query("ShopID")int ShopID, @Query("ItemID") int itemID,
            @Query("latCenter")double latCenter,@Query("lonCenter")double lonCenter,
            @Query("deliveryRangeMax")double deliveryRangeMax,
            @Query("deliveryRangeMin")double deliveryRangeMin,
            @Query("proximity")double proximity,
            @Query("EndUserID") int endUserID,@Query("IsFilledCart") boolean isFilledCart
    );


    @POST("/api/ShopItem")
    Call<ResponseBody> postShopItem(@Body ShopItem shopItem);

    @PUT("/api/ShopItem")
    Call<ResponseBody> putShopItem(@Body ShopItem shopItem);

    @DELETE("/api/ShopItem")
    Call<ResponseBody> deleteShopItem(@Query("ShopID") int ShopID, @Query("ItemID") int itemID);

}
