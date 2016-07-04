package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.ModelEndPoints.ItemCategoryEndPoint;

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

    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(
            @Query("ParentID") Integer parentID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity
    );



    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategoriesEndPoint(
            @Query("ShopID")Integer shopID,
            @Query("ParentID")Integer parentID,@Query("IsDetached")Boolean parentIsNull,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly
    );


    @GET("/api/v1/ItemCategory/{id}")
    Call<ItemCategory> getItemCategory(@Path("id") Integer ItemCategoryID);

    @POST("/api/v1/ItemCategory")
    Call<ItemCategory> insertItemCategory(@Body ItemCategory itemCategory);

    @PUT("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> updateItemCategory(@Body ItemCategory itemCategory, @Path("id") int id);

    @DELETE("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> deleteItemCategory(@Path("id") int id);

}
