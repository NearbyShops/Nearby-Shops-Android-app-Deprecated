package org.nearbyshops.enduserappnew.RetrofitRESTContractSDS;

import org.nearbyshops.enduserappnew.ModelReviewItem.ItemReview;
import org.nearbyshops.enduserappnew.ModelReviewItem.ItemReviewEndPoint;
import org.nearbyshops.enduserappnew.ModelReviewMarket.MarketReview;
import org.nearbyshops.enduserappnew.ModelReviewMarket.MarketReviewEndPoint;

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

public interface MarketReviewService {


    @GET("/api/v1/MarketReview")
    Call<MarketReviewEndPoint> getReviews(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("GetEndUser") Boolean getEndUser,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/MarketReview/{id}")
    Call<MarketReview> getItemReview(@Path("id") int itemReviewID);


    @POST("/api/v1/MarketReview")
    Call<MarketReview> insertItemReview(@Body MarketReview book);

    @PUT("/api/v1/MarketReview/{id}")
    Call<ResponseBody> updateItemReview(@Body MarketReview itemReview, @Path("id") int id);

    @PUT("/api/v1/MarketReview/")
    Call<ResponseBody> updateItemReviewBulk(@Body List<MarketReview> itemReviewList);

    @DELETE("/api/v1/MarketReview/{id}")
    Call<ResponseBody> deleteItemReview(@Path("id") int id);

}
