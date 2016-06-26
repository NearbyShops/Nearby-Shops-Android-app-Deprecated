package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.ItemCategory;

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
 * Created by sumeet on 2/4/16.
 */

public interface ItemCategoryService {

    @GET("/api/ItemCategory")
    Call<List<ItemCategory>> getItemCategories(
            @Query("ParentID") Integer parentID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity
    );

    @GET("/api/ItemCategory/{id}")
    Call<ItemCategory> getItemCategory(@Path("id") Integer ItemCategoryID);

    @POST("/api/ItemCategory")
    Call<ItemCategory> insertItemCategory(@Body ItemCategory itemCategory);

    @PUT("/api/ItemCategory/{id}")
    Call<ResponseBody> updateItemCategory(@Body ItemCategory itemCategory, @Path("id") int id);

    @DELETE("/api/ItemCategory/{id}")
    Call<ResponseBody> deleteItemCategory(@Path("id") int id);

}
