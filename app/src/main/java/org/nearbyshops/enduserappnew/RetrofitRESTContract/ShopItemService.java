package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelEndPoints.ShopItemEndPoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {


//    @GET("/api/v1/ShopItemParcelable/Deprecated")
//    Call<List<ShopItemParcelable>> getShopItems(
//            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
//            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
//            @Query("deliveryRangeMax")Double deliveryRangeMax,
//            @Query("deliveryRangeMin")Double deliveryRangeMin,
//            @Query("proximity")Double proximity,
//            @Query("EndUserID")Integer endUserID,@Query("IsFilledCart")Boolean isFilledCart
//    );

    

    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID")Integer ItemCategoryID,
            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("EndUserID") Integer endUserID,@Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock,@Query("PriceEqualsZero")Boolean priceEqualsZero,
            @Query("MinPrice")Integer minPrice,@Query("MaxPrice")Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled")Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly,
            @Query("GetExtras")Boolean getExtras
    );



//
//    @POST("/api/v1/ShopItemParcelable")
//    Call<ResponseBody> postShopItem(@Body ShopItemParcelable shopItem);
//
//    @PUT("/api/v1/ShopItemParcelable")
//    Call<ResponseBody> putShopItem(@Body ShopItemParcelable shopItem);
//
//    @DELETE("/api/v1/ShopItemParcelable")
//    Call<ResponseBody> deleteShopItem(@Query("ShopID") int ShopID, @Query("ItemID") int itemID);

}
