package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Cart;
import org.nearbyshops.enduser.ModelStats.CartStats;

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
 * Created by sumeet on 31/5/16.
 */
public interface CartStatsService {

    @GET("api/CartStats/{EndUserID}")
    Call<List<CartStats>> getCart(@Path("EndUserID") int endUserID,
                                  @Query("latCenter")double latCenter, @Query("lonCenter")double lonCenter);
}
