package org.nearbyshops.enduserappnew.RetrofitRESTContractSDS;



import org.nearbyshops.enduserappnew.ModelReviewMarket.FavouriteMarket;
import org.nearbyshops.enduserappnew.ModelReviewMarket.FavouriteMarketEndpoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sumeet on 2/4/16.
 */



public interface FavouriteMarketService {



    @GET("/api/v1/FavouriteMarket")
    Call<FavouriteMarketEndpoint> getFavouriteMarkets(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @POST("/api/v1/FavouriteMarket")
    Call<FavouriteMarket> insertFavouriteItem(
            @Header("Authorization") String headers,
            @Body FavouriteMarket item
    );

    @DELETE("/api/v1/FavouriteMarket")
    Call<ResponseBody> deleteFavouriteItem(@Header("Authorization") String headers,
                                           @Query("ItemID") Integer itemID,
                                           @Query("EndUserID") Integer endUserID);

}
