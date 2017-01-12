package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduser.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduser.Shops.UtilityLocation;

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
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {


    @POST("/api/v1/Shop")
    Call<Shop> postShop(@Body Shop shop);

    @PUT("/api/v1/Shop/{id}")
    Call<ResponseBody> putShop(@Body Shop shop, @Path("id") int id);

    @DELETE("/api/v1/Shop/{id}")
    Call<ResponseBody> deleteShop(@Path("id") int id);



    @GET("/api/v1/Shop/ForShopFilters")
    Call<ShopEndPoint> getShopForFilters(
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("metadata_only")Boolean metaonly
    );



    @GET ("/api/v1/Shop")
    Call<ShopEndPoint> getShops(
            @Query("DistributorID")Integer distributorID,
            @Query("LeafNodeItemCategoryID")Integer itemCategoryID,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly
    );


    @GET("/api/v1/Shop/FilterByItemCat/{ItemCategoryID}")
    Call<ShopEndPoint> filterShopsByItemCategory(
            @Path("ItemCategoryID")Integer itemCategoryID,
            @Query("DistributorID")Integer distributorID,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly);


    @GET("/api/v1/Shop/{id}")
    Call<Shop> getShop(@Path("id") int id,
                       @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter);




    @GET("/api/v1/Shop/QuerySimple")
    Call<ShopEndPoint> getShopListSimple(
            @Query("Enabled")Boolean enabled,
            @Query("Waitlisted") Boolean waitlisted,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );


    // Deprecated
//
//    @GET("/api/v1/Shop/Deprecated")
//    Call<List<Shop>> getShops(@Query("DistributorID") Integer distributorID,
//                              @Query("LeafNodeItemCategoryID")Integer itemCategoryID,
//                              @Query("latCenter")Double latCenter,
//                              @Query("lonCenter")Double lonCenter,
//                              @Query("deliveryRangeMax")Double deliveryRangeMax,
//                              @Query("deliveryRangeMin")Double deliveryRangeMin,
//                              @Query("proximity")Double proximity);
//

}



/*
    Call<ShopEndPoint> callEndpoint = shopService.getShopForFilters(
            UtilityLocationOld.getLatitude(this),
            UtilityLocationOld.getLongitude(this),
            null, null,
            UtilityLocationOld.getProximity(this),
            null
    );

*/
