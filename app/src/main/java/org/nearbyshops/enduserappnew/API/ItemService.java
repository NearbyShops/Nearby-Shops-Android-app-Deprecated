package org.nearbyshops.enduserappnew.API;

import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemEndPoint;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 3/4/16.
 */
public interface ItemService
{

    @GET("/api/v1/Item/Deprecated")
    Call<List<Item>> getItems(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter") Double latCenter,
            @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity);

//





    @GET("/api/v1/Item")
    Call<ItemEndPoint> getItemsEndpoint(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ItemSpecValues") String itemSpecValues,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData
    );




    @GET("/api/v1/Item/{id}")
    Call<Item> getItem(@Path("id") int ItemID);

    @POST("/api/v1/Item")
    Call<Item> insertItem(@Body Item item);

    @PUT("/api/v1/Item/{id}")
    Call<ResponseBody> updateItem(@Body Item item, @Path("id") int id);

    @DELETE("/api/v1/Item/{id}")
    Call<ResponseBody> deleteItem(@Path("id") int id);

}
