package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.enduserappnew.ModelReviewShop.ShopReview;
import org.nearbyshops.enduserappnew.ModelReviewShop.ShopReviewStatRow;

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

public interface ShopReviewService {

    @GET("/api/v1/ShopReview")
    Call<ShopReviewEndPoint> getReviews(
            @Query("ShopID") Integer bookID,
            @Query("EndUserID") Integer memberID,
            @Query("GetEndUser") Boolean getMember,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/ShopReview/{id}")
    Call<ShopReview> getShopReview(@Path("id") int shopReviewID);

    @POST("/api/v1/ShopReview")
    Call<ShopReview> insertShopReview(@Body ShopReview book);

    @PUT("/api/v1/ShopReview/{id}")
    Call<ResponseBody> updateShopReview(@Body ShopReview shopReview, @Path("id") int id);

    @PUT("/api/v1/ShopReview/")
    Call<ResponseBody> updateShopReviewBulk(@Body List<ShopReview> shopReviewList);

    @DELETE("/api/v1/ShopReview/{id}")
    Call<ResponseBody> deleteShopReview(@Path("id") int id);


    @GET("/api/v1/ShopReview/Stats/{ShopID}")
    Call<List<ShopReviewStatRow>> getStats(@Path("ShopID")int shopID);


}
