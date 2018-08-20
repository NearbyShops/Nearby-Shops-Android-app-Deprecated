package org.nearbyshops.enduserappnew.EditProfile.ChangePassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.Utility.PrefLogin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 15/4/17.
 */

public class FragmentChangePassword extends Fragment {


    /* Token renewal variables : BEGIN */

    // constants - request codes for token renewal
    public static final int REQUEST_CODE_CHANGE_PASSWORD = 1;

    // housekeeping for token renewal
    int token_renewal_attempts = 0;  // variable to keep record of renewal attempts
    int token_renewal_request_code = -1; // variable to store the request code;

    /* Token renewal variables : END */




    @Inject
    UserService userService;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_new)
    EditText passwordNew;
    @BindView(R.id.password_confirm)
    EditText passwordConfirm;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    boolean isDestroyed = false;



    public FragmentChangePassword() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this,rootView);

        setActionBarTitle();


        // check if token expired and renew
//        checkTokenExpired(false);

        return rootView;
    }



    void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar!=null)
            {
                actionBar.setTitle("Change Password");
            }
        }
    }



    boolean validatePassword()
    {
        boolean isValid = true;




        if(passwordConfirm.getText().toString().length()==0)
        {
            passwordConfirm.requestFocus();
            passwordConfirm.setError("Cannot be empty !");
            isValid = false;
        }



        if(passwordNew.getText().toString().length()<6)
        {
            passwordNew.requestFocus();
            passwordNew.setError("Password should not be less than 6 characters.");
            isValid = false;
        }


        if(passwordNew.getText().toString().length()==0)
        {
            passwordNew.requestFocus();
            passwordNew.setError("Cannot be empty !");
            isValid = false;
        }




        if(!password.getText().toString().equals(PrefLogin.getPassword(getContext())))
        {
            password.requestFocus();
            password.setError("Wrong Password !");
            isValid = false;
        }


        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Cannot be empty !");
            isValid = false;
        }






        if(!passwordConfirm.getText().toString().equals(passwordNew.getText().toString()))
        {
            passwordConfirm.requestFocus();
            passwordConfirm.setError("Passwords do not match !");
            isValid = false;
        }


        return isValid;

    }



    @OnClick(R.id.change_password)
    void updatePassword()
    {
        if(!validatePassword())
        {
            return;
        }

        User user = new User();
        user.setUsername(PrefLogin.getUsername(getContext()));
        user.setPhone(PrefLogin.getUsername(getContext()));
        user.setEmail(PrefLogin.getUsername(getContext()));


        // new password required here
        user.setPassword(passwordNew.getText().toString());

        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call  = userService.changePassword(
                PrefLogin.getAuthorizationHeaders(getContext()),user,password.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Password changed Successfully !");

                    // update the new password so the tokens can be renewed without error
//                    UtilityLoginOld.saveCredentials(getContext(),UtilityLoginOld.getUsername(getContext()),passwordNew.getText().toString());

                    PrefLogin.savePassword(getActivity(),passwordNew.getText().toString());

                }
                else if(response.code()==304)
                {
                    showToastMessage("Password Not Changed !");
                }
                else
                {
                    showToastMessage("Failed with error code : " + String.valueOf(response.code()));
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network connection failure !");

                progressBar.setVisibility(View.GONE);
            }
        });


    }


    void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onResume() {
        super.onResume();
        isDestroyed= false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }







    // renew token in case it was expired
//    boolean checkTokenExpired(boolean showMessage)
//    {
//        if(PrefLogin.getUsername(getActivity())==null)
//        {
//            // not logged in
//            showToastMessage("User logged out !");
//            return false;
//        }
//
//
//        if(PrefLogin.getExpires(getActivity())
//                .before(new Timestamp(System.currentTimeMillis())))
//        {
//            // Token expired renew the token
//
//            if(showMessage)
//            {
//                showToastMessage("Please try again !");
//            }
//
//            getActivity().startService(new Intent(getActivity(),RenewToken.class));
//
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//
//    }




}
