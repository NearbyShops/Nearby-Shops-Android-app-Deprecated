package org.nearbyshops.enduserappnew.API;


import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 3/4/17.
 */

public interface LoginUsingOTPService {



    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingPhoneOTP")
    Call<User> getProfileWithLogin(@Header("Authorization") String headers);



    @PUT("/api/v1/User/LoginUsingOTP/SendPhoneVerificationCode/{phone}")
    Call<ResponseBody> sendPhoneVerificationCode(@Path("phone") String phone);



    @GET ("/api/v1/User/LoginUsingOTP/CheckPhoneVerificationCode/{phone}")
    Call<ResponseBody> checkPhoneVerificationCode(@Path("phone") String phone,
                                                  @Query("VerificationCode") String verificationCode);




    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingGlobalCredentials")
    Call<User> loginWithGlobalCredentials(
            @Header("Authorization") String headerParam,
            @Query("ServiceURLSDS") String serviceURLForSDS,
            @Query("MarketID") int marketID

    );






    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingGlobalCredentials")
    Call<User> loginWithGlobalCredentials(
            @Header("Authorization") String headerParam,
            @Query("ServiceURLSDS") String serviceURLForSDS,
            @Query("MarketID") int marketID,
            @Query("GetServiceConfiguration") boolean getServiceConfig,
            @Query("GetUserProfileGlobal") boolean getUserProfileGlobal
    );


}
