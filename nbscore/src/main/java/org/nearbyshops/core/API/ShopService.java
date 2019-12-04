package org.nearbyshops.core.API;

import org.nearbyshops.core.Model.Image;
import org.nearbyshops.core.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.core.Model.Shop;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {


    @GET("/api/v1/Shop/ForShopFilters")
    Call<ShopEndPoint> getShopForFilters(
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("metadata_only") Boolean metaonly
    );



    @GET ("/api/v1/Shop")
    Call<ShopEndPoint> getShops(
            @Query("DistributorID") Integer distributorID,
            @Query("LeafNodeItemCategoryID") Integer itemCategoryID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );







    @GET("/api/v1/Shop/FilterByItemCat/{ItemCategoryID}")
    Call<ShopEndPoint> filterShopsByItemCategory(
            @Path("ItemCategoryID") Integer itemCategoryID,
            @Query("DistributorID") Integer distributorID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly);


    @GET("/api/v1/Shop/{id}")
    Call<Shop> getShop(@Path("id") int id,
                       @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter);




    @GET("/api/v1/Shop/GetForShopAdmin")
    Call<Shop> getShopForShopAdmin(@Header("Authorization") String headers);




    @PUT("/api/v1/Shop/SetShopOpen")
    Call<ResponseBody> updateShopOpen(@Header("Authorization") String headers);



    @PUT ("/api/v1/Shop/SetShopClosed")
    Call<ResponseBody> updateShopClosed(@Header("Authorization") String headers);




    @POST("/api/v1/Shop")
    Call<Shop> postShop(@Header("Authorization") String headers,
                        @Body Shop shop);


    @PUT("/api/v1/Shop/UpdateBySelf")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Body Shop shop);





    // Image Calls

    @POST("/api/v1/Shop/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Shop/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);



//    @GET("/api/v1/Shop/QuerySimple")
//    Call<ShopEndPoint> getShopListSimple(
//            @Query("Enabled")Boolean enabled,
//            @Query("Waitlisted") Boolean waitlisted,
//            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
//            @Query("deliveryRangeMax")Double deliveryRangeMax,
//            @Query("deliveryRangeMin")Double deliveryRangeMin,
//            @Query("proximity")Double proximity,
//            @Query("SearchString") String searchString,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") Integer limit, @Query("Offset") int offset
//    );


}



