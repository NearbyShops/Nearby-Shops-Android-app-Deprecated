package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.ModelRoles.EndUser;


import okhttp3.RequestBody;
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


    @POST("/api/v1/EndUser")
    Call<EndUser> postEndUser(@Body EndUser endUser);


    @POST("/api/v1/EndUser/{IDToken}")
    Call<EndUser> googleSignIn(@Path("IDToken")String idTokenString);



    @PUT ("/api/v1/EndUser/UpdateBySelf/{EndUserID}")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Path("EndUserID")int endUserID,
                                    @Body EndUser endUser);



    @DELETE ("/api/v1/EndUser/{EndUserID}")
    Call<ResponseBody> deleteShopAdmin(@Header("Authorization") String headers,
                                       @Path("EndUserID")int endUserID);



    @GET ("/api/v1/EndUser/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExists(@Path("username")String username);



    @GET ("/api/v1/EndUser/Login")
    Call<EndUser> getEndUserLogin(@Header("Authorization") String headers);





    // Image Calls

    @POST("api/v1/EndUser/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/EndUser/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);
}
