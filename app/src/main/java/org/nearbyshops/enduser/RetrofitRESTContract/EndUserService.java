package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Cart;
import org.nearbyshops.enduser.Model.EndUser;

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
public interface EndUserService {

    @GET("/api/EndUser/Validate")
    Call<EndUser> getEndUser(@Query("Password")String password,
                             @Query("Username")String userName,
                             @Query("ID")Integer id);

    @GET("/api/EndUser/Validate")
    Call<EndUser> getEndUser(@Query("Password")String password,
                             @Query("Username")String userName);

    @GET("/api/EndUser/{id}")
    Call<EndUser> getEndUser(@Path("id") int end_user_id);


    @POST("/api/EndUser")
    Call<EndUser> postEndUser(@Body EndUser endUser);

    @PUT("/api/EndUser/{id}")
    Call<ResponseBody> putEndUser(@Body EndUser endUser, @Path("id") int end_user_id);

    @DELETE("/api/EndUser/{id}")
    Call<ResponseBody> deleteEndUser(@Path("id") int end_user_id);

}
