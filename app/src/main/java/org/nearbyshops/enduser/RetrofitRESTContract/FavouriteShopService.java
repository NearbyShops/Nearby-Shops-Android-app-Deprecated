package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduser.ModelReviewShop.FavouriteShop;

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

public interface FavouriteShopService {

    @GET("/api/v1/FavouriteShop")
    Call<FavouriteShopEndpoint> getFavouriteBooks(
            @Query("ShopID") Integer bookID,
            @Query("EndUserID") Integer memberID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @POST("/api/v1/FavouriteShop")
    Call<FavouriteShop> insertFavouriteBook(@Body FavouriteShop book);

    @DELETE("/api/v1/FavouriteShop")
    Call<ResponseBody> deleteFavouriteBook(@Query("ShopID") Integer bookID,
                                           @Query("EndUserID") Integer memberID);

}
