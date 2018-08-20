package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelReviewItem.FavouriteItem;
import org.nearbyshops.enduserappnew.ModelReviewItem.FavouriteItemEndpoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sumeet on 2/4/16.
 */

public interface FavouriteItemService {

    @GET("/api/v1/FavouriteItem")
    Call<FavouriteItemEndpoint> getFavouriteBooks(
            @Query("ItemID")Integer itemID,
            @Query("EndUserID")Integer endUserID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("metadata_only")Boolean metaonly
    );


    @POST("/api/v1/FavouriteItem")
    Call<FavouriteItem> insertFavouriteItem(@Body FavouriteItem item);

    @DELETE("/api/v1/FavouriteItem")
    Call<ResponseBody> deleteFavouriteItem(@Query("ItemID") Integer itemID,
                                           @Query("EndUserID") Integer endUserID);

}
