package org.nearbyshops.enduserappnew.SignUp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.SignUp.Interfaces.ShowFragmentSignUp;
import org.nearbyshops.enduserappnew.SignUp.PrefSignUp.PrefrenceSignUp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentEnterPassword extends Fragment {


    @BindView(R.id.enter_password)
    TextInputEditText enterPassword;
    @BindView(R.id.confirm_password)
    TextInputEditText confirmPassword;


    @BindView(R.id.progress_bar_button)
    ProgressBar progressBarButton;
    @BindView(R.id.next)
    TextView nextButton;

    @Inject
    UserService userService;

    User user;


    public FragmentEnterPassword() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_enter_password, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefrenceSignUp.getUser(getActivity());

        return rootView;
    }





    boolean validatePasswords(boolean showError)
    {

        boolean isValid = true;

        if(enterPassword.getText().toString().equals(""))
        {
            if(showError)
            {
                enterPassword.setError("Password cannot be empty !");
                enterPassword.requestFocus();
            }

            isValid = false;
        }
        else if(!enterPassword.getText().toString().equals(confirmPassword.getText().toString()))
        {
            if(showError)
            {
                confirmPassword.setError("Passwords do not match !");
                confirmPassword.requestFocus();
            }

            isValid = false;
        }


        return isValid;
    }










    @OnClick(R.id.next)
    void createAccountClick()
    {

        if(!validatePasswords(true))
        {
            return;
        }

        user.setPassword(enterPassword.getText().toString());
        PrefrenceSignUp.saveUser(user,getActivity());


        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            // registering using phone


            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setTitle("You will receive a verification code !")
                    .setMessage("We will send you an SMS having a verification code. You will be asked to " +
                            "enter the verification code !")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            showToastMessage("Next Initiated");

                            sendPhoneVerificationCode();

                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();



        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // registering using email



            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


            dialog.setTitle("You will receive a verification code !")
                    .setMessage("We will send you a verification code on your e-mail. You will be asked to " +
                            "enter the verification code !")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendEmailVerificationCode();
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();
        }
    }








    void sendEmailVerificationCode()
    {


        progressBarButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        Call<ResponseBody> call = userService.sendVerificationEmail(
            user.getEmail()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    if(getActivity() instanceof ShowFragmentSignUp)
                    {
                        ((ShowFragmentSignUp) getActivity()).showVerifyEmail();
                    }

                }
                else if(response.code()==304)
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }
                else
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                showToastMessage("Failed to send verification code. Please try again !");

            }
        });


    }




    void sendPhoneVerificationCode()
    {


        progressBarButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        Call<ResponseBody> call = userService.sendVerificationPhone(
                user.getPhone()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    if(getActivity() instanceof ShowFragmentSignUp)
                    {
                        ((ShowFragmentSignUp) getActivity()).showVerifyEmail();
                    }

                }
                else if(response.code()==304)
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }
                else
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                showToastMessage("Failed to send verification code. Please try again !");

            }
        });


    }








    void logMessage(String message)
    {
        Log.d("sign_up",message);
    }



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }



}
