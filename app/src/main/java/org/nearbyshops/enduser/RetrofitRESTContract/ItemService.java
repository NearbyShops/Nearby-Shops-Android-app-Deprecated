package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Item;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 3/4/16.
 */
public interface ItemService
{

    @GET("/api/Item")
    Call<List<Item>> getItems(
            @Query("ItemCategoryID") int itemCategoryID,
            @Query("ShopID") int shopID,
            @Query("latCenter") double latCenter,
            @Query("lonCenter") double lonCenter,
            @Query("deliveryRangeMax")double deliveryRangeMax,
            @Query("deliveryRangeMin")double deliveryRangeMin,
            @Query("proximity")double proximity
    );

    @GET("/api/Item/{id}")
    Call<Item> getItem(@Path("id") int ItemID);

    @POST("/api/Item")
    Call<Item> insertItem(@Body Item item);

    @PUT("/api/Item/{id}")
    Call<ResponseBody> updateItem(@Body Item item, @Path("id") int id);

    @DELETE("/api/Item/{id}")
    Call<ResponseBody> deleteItem(@Path("id") int id);

}
