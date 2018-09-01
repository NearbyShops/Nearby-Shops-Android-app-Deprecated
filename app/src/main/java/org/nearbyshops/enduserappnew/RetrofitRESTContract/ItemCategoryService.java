package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelEndPoints.ItemCategoryEndPoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by sumeet on 2/4/16.
 */

public interface ItemCategoryService {

//    @GET("/api/v1/ItemCategory/Deprecated")
//    Call<List<ItemCategory>> getItemCategories(
//            @Query("ParentID") Integer parentID,
//            @Query("ShopID") Integer shopID,
//            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
//            @Query("deliveryRangeMax")Double deliveryRangeMax,
//            @Query("deliveryRangeMin")Double deliveryRangeMin,
//            @Query("proximity")Double proximity
//    );





    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategoriesEndPoint(
            @Query("ShopID")Integer shopID,
            @Query("ParentID")Integer parentID,@Query("IsDetached")Boolean parentIsNull,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("ShopEnabled")Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly
    );




//
//    @GET("/api/v1/ItemCategory/{id}")
//    Call<ItemCategory> getItemCategory(@Path("id") Integer ItemCategoryID);
//
//    @POST("/api/v1/ItemCategory")
//    Call<ItemCategory> insertItemCategory(@Body ItemCategory itemCategory);
//
//    @PUT("/api/v1/ItemCategory/{id}")
//    Call<ResponseBody> updateItemCategory(@Body ItemCategory itemCategory, @Path("id") int id);
//
//    @DELETE("/api/v1/ItemCategory/{id}")
//    Call<ResponseBody> deleteItemCategory(@Path("id") int id);

}
