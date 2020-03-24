package org.nearbyshops.enduserappnew.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

/**
 * Created by sumeet on 19/4/17.
 */

public class LoginLocalUsingOTPFragment extends Fragment {



    public static final String TAG_SERVICE_INDICATOR = "service_indicator";
    private boolean isDestroyed = false;




    @Inject Gson gson;
//    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.progress_bar_login) ProgressBar progressBar;

//    @BindView(R.id.clear) TextView clear;
//    @BindView(R.id.select_service) TextView selectAutomatic;


    @BindView(R.id.login) Button loginButton;
    @BindView(R.id.text_input_password) TextInputLayout textInputPassword;



    public LoginLocalUsingOTPFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login_using_otp, container, false);
        ButterKnife.bind(this,rootView);


        if(getChildFragmentManager().findFragmentByTag(TAG_SERVICE_INDICATOR)==null)
        {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.service_indicator,new ServiceIndicatorFragment(),TAG_SERVICE_INDICATOR)
                    .commit();
        }



        return rootView;
    }






    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&& resultCode==1)
        {
//            Fragment fragment = getChildFragmentManager()
//                    .findFragmentByTag(TAG_SERVICE_INDICATOR);

//            if(fragment instanceof ServiceIndicatorFragment)
//            {
//                ((ServiceIndicatorFragment)fragment).refresh();
//            }
        }
        else if(requestCode==5)
        {
//            Fragment fragment = getChildFragmentManager()
//                    .findFragmentByTag(TAG_SERVICE_INDICATOR);
//
//            if(fragment instanceof ServiceIndicatorFragment)
//            {
////            showToastMessage("Clear Click : Inside If Block");
//                ((ServiceIndicatorFragment)fragment).refresh();
//            }
        }
    }


//    @OnClick(R.id.clear)
//    void clear()
//    {
//        PrefGeneral.saveServiceURL(null);
//        PrefServiceConfig.saveServiceConfig(null,getActivity());
//
//
//        Fragment fragment = getChildFragmentManager()
//                .findFragmentByTag(TAG_SERVICE_INDICATOR);
//
//        if(fragment instanceof ServiceIndicatorFragment)
//        {
//            ((ServiceIndicatorFragment)fragment).refresh();
//        }
//    }








    private boolean validateData()
    {
        boolean isValid = true;
//        boolean phoneValidity = false;
//        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
//        phoneValidity = android.util.Patterns.PHONE.matcher(username.getText().toString()).matches();



        if(password.getText().toString().isEmpty())
        {
            password.requestFocus();
            password.setError("Please Enter OTP !");
            isValid = false;
        }



//        if(!phoneValidity)
//        {
//            username.setError("Not a valid phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }
//
//        if(username.getText().toString().isEmpty())
//        {
//            password.requestFocus();
//            username.setError("Please enter phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }
//        else if(username.getText().toString().length()!=10)
//        {
//            username.setError("Enter a valid phone number !");
//            username.requestFocus();
//
//            isValid = false;
//        }





        // phone and password both needs to be valid
        isValid = validatePhone() && isValid;




        return isValid;
    }






    private boolean validatePhone()
    {
        boolean isValid = true;
        boolean phoneValidity = false;
//        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
        phoneValidity = android.util.Patterns.PHONE.matcher(username.getText().toString()).matches();


        if(username.getText().toString().isEmpty())
        {
//            password.requestFocus();
            username.setError("Please enter phone !");
            username.requestFocus();

            isValid = false;
        }
        else if(username.getText().toString().length()!=10)
        {
            username.setError("Enter a valid phone number !");
            username.requestFocus();

            isValid = false;
        }


        if(!phoneValidity)
        {
            username.setError("Invalid phone number !");
            isValid = false;
        }



        return isValid;
    }






    @Override
    public void onStart() {
        super.onStart();
        isDestroyed= false;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isDestroyed= true;
    }



    @OnClick(R.id.login)
    void loginButtonClick()
    {
        if(textInputPassword.getVisibility()==View.GONE)
        {
            sendOTP();
        }
        else if (textInputPassword.getVisibility()==View.VISIBLE)
        {
            makeRequestLogin();
        }

    }






    private void makeRequestLogin()
    {

        if(!validateData())
        {
            // validation failed return
            return;
        }



        final String phoneWithCode = username.getText().toString();
//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<User> call = retrofit.create(LoginUsingOTPService.class).getProfileWithLogin(
                PrefLogin.baseEncoding(phoneWithCode,password.getText().toString())
        );



        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    // save username and password



//                    if(response.body().getRole()!=User.ROLE_END_USER_CODE)
//                    {
//                        showToastMessage("Only an End-User is allowed to login");
//                        return;
//                    }

                    User user = response.body();



                    PrefLogin.saveCredentialsPassword(
                            getActivity(),
                            user.getPhone(),
                            user.getPassword()
                    );





                    // save token and token expiry timestamp
//                    PrefLogin.saveToken(
//                            getActivity(),
//                            response.body().getToken(),
//                            response.body().getTimestampTokenExpires()
//                    );


                    // save user profile information
                    PrefLogin.saveUserProfile(
                            response.body(),
                            getActivity()
                    );









//                    PrefOneSignal.saveToken(getActivity(),PrefOneSignal.getLastToken(getActivity()));
//
//                    if(PrefOneSignal.getToken(getActivity())!=null)
//                    {
//                        // update one signal id if its not updated
//                        getActivity().startService(new Intent(getActivity(), UpdateOneSignalID.class));
//                    }










                    if(getActivity() instanceof NotifyAboutLogin)
                    {
//                        showToastMessage("Notify about login !");
                        ((NotifyAboutLogin) getActivity()).loginSuccess();
                    }



//                        getActivity().finish();


//                    showToastMessage("LoginUsingOTP success : code : " + String.valueOf(response.code()));



                }
                else
                {
                    showToastMessage("Login Failed : Phone or OTP is incorrect !");
//                    System.out.println("Login Failed : Code " + String.valueOf(response.code()));
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }
                showToastMessage("Network connection problem !");
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });

    }






    private void sendOTP()
    {

        if(!validatePhone())
        {
            // validation failed return
            return;
        }



        final String phoneWithCode = username.getText().toString();
//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<ResponseBody> call = retrofit.create(LoginUsingOTPService.class).sendPhoneVerificationCode(
                username.getText().toString()
        );



        textInputPassword.setVisibility(View.VISIBLE);
        loginButton.setText("Login");




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);



                if(response.code()==200) {
                    // save username and password

//                    showToastMessage("OTP Sent !");
                }
                else
                {
                    showToastMessage("Failed to send OTP ... failed Code : " + response.code());
                }


                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Failed ... Please check your network !");

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);



            }
        });




    }











//    @OnClick(R.id.select_service)
//    void getServices()
//    {
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl(PrefServiceConfig.getSDSURL(MyApplicationCoreNew.getAppContext()))
//                .client(new OkHttpClient().newBuilder().build())
//                .build();
//
//
//
//        ServiceConfigService service = retrofit.create(ServiceConfigService.class);
//
//
//
//        Call<ServiceConfigEndpoint> call = service.getServicesListSimple(
//                (double) PrefLocation.getLatitideCurrent(getActivity()),(double)PrefLocation.getLongitudeCurrent(getActivity()),
//                null, null,null,null,null,null,
//                " distance ",1,0
//        );
//
//
//        call.enqueue(new Callback<ServiceConfigEndpoint>() {
//            @Override
//            public void onResponse(Call<ServiceConfigEndpoint> call, Response<ServiceConfigEndpoint> response) {
//
//
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//                if(response.code() == 200 && response.body()!=null) {
//
//
//                    if(response.body().getItemCount()==1)
//                    {
//                        PrefGeneral.saveServiceURL(
//                                response.body().getResults().get(0).getServiceURL(),
//                                getActivity()
//                        );
//
//
//                        PrefServiceConfig.saveServiceConfig(null,getActivity());
//
//                        Fragment fragment = getChildFragmentManager()
//                                .findFragmentByTag(TAG_SERVICE_INDICATOR);
//
//                        if(fragment instanceof ServiceIndicatorFragment)
//                        {
//                            ((ServiceIndicatorFragment)fragment).refresh();
//                        }
//
//                    }
//                }
//                else
//                {
//                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ServiceConfigEndpoint> call, Throwable t) {
//
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//                showToastMessage("Network Connection Failed !");
//
//
//            }
//        });
//
//    }





}
