package org.nearbyshops.enduserapp.RetrofitRESTContract;


import org.nearbyshops.enduserapp.Model.Image;
import org.nearbyshops.enduserapp.ModelItemSpecs.EndPoints.ItemSpecValueEndPoint;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationValue;

import java.util.List;

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
 * Created by sumeet on 3/3/17.
 */

public interface ItemSpecValueService {



    @POST("/api/v1/ItemSpecificationValue")
    Call<ItemSpecificationValue> saveItemSpecValue(@Header("Authorization") String headers,
                                                   @Body ItemSpecificationValue itemSpecValue);



    @PUT("/api/v1/ItemSpecificationValue/{ImageID}")
    Call<ResponseBody> updateItemSpec(@Header("Authorization") String headers,
                                      @Body ItemSpecificationValue itemSpecValue,
                                      @Path("ImageID") int imageID);



    @DELETE("/api/v1/ItemSpecificationValue/{ItemSpecNameID}")
    Call<ResponseBody> deleteItemSpecValue(@Header("Authorization") String headers,
                                           @Path("ItemSpecNameID") int itemSpecNameID);





    @GET("/api/v1/ItemSpecificationValue/OuterJoin")
    Call<ItemSpecValueEndPoint> getItemSpecName(
            @Query("ItemSpecID") Integer itemSpecID,
            @Query("ItemCatID") Integer itemCatID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") Boolean getRowCount);




    @GET("/api/v1/ItemSpecificationValue/SpecValuesForFilters")
    Call<List<ItemSpecificationValue>> getItemSpecsForFilters(
            @Query("ItemSpecID") Integer itemSpecID,
            @Query("ItemCatID") Integer itemCatID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("SearchString") String searchString);



    // Image Calls

    @POST("/api/v1/ItemSpecificationValue/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/ItemSpecificationValue/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);


}
