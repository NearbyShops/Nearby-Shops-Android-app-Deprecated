package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelReviewItem.ItemReview;
import org.nearbyshops.enduserappnew.ModelReviewItem.ItemReviewEndPoint;

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

public interface ItemReviewService {

    @GET("/api/v1/ItemReview")
    Call<ItemReviewEndPoint> getReviews(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("GetEndUser") Boolean getEndUser,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/ItemReview/{id}")
    Call<ItemReview> getItemReview(@Path("id") int itemReviewID);

    @POST("/api/v1/ItemReview")
    Call<ItemReview> insertItemReview(@Body ItemReview book);

    @PUT("/api/v1/ItemReview/{id}")
    Call<ResponseBody> updateItemReview(@Body ItemReview itemReview, @Path("id") int id);

    @PUT("/api/v1/ItemReview/")
    Call<ResponseBody> updateItemReviewBulk(@Body List<ItemReview> itemReviewList);

    @DELETE("/api/v1/ItemReview/{id}")
    Call<ResponseBody> deleteItemReview(@Path("id") int id);

}
