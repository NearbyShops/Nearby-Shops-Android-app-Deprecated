package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.ModelRoles.EndUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 12/3/16.
 */
public interface EndUserService {

    @GET("/api/v1/EndUser/Login")
    Call<EndUser> EndUserLogin(@Header("Authorization") String headers);

    @POST("/api/v1/EndUser")
    Call<EndUser> postEndUser(@Body EndUser endUser);

    @PUT("/api/v1/EndUser/{id}")
    Call<ResponseBody> putEndUser(@Body EndUser endUser, @Path("id") int id);

    @DELETE("/api/v1/EndUser/{id}")
    Call<ResponseBody> deleteShop(@Path("id") int id);

}
