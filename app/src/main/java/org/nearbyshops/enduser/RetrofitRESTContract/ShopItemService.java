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


    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(
            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("EndUserID")Integer endUserID,@Query("IsFilledCart")Boolean isFilledCart
    );



    @POST("/api/v1/ShopItem")
    Call<ResponseBody> postShopItem(@Body ShopItem shopItem);

    @PUT("/api/v1/ShopItem")
    Call<ResponseBody> putShopItem(@Body ShopItem shopItem);

    @DELETE("/api/v1/ShopItem")
    Call<ResponseBody> deleteShopItem(@Query("ShopID") int ShopID, @Query("ItemID") int itemID);

}
