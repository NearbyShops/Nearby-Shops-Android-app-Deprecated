package org.nearbyshops.enduserappnew.EditProfile.ChangeEmail;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentVerifyEmailChange extends Fragment {


    /* Token renewal variables : BEGIN */

    boolean isDestroyed = false;

    // constants - request codes for token renewal
    public static final int REQUEST_CODE_UPDATE_EMAIL = 1;
    // housekeeping for token renewal
    int token_renewal_attempts = 0;  // variable to keep record of renewal attempts
    int token_renewal_request_code = -1; // variable to store the request code;

    /* Token renewal variables : END */



    @BindView(R.id.check_icon)
    ImageView checkIcon;
    @BindView(R.id.cross_icon)
    ImageView crossIcon;
    @BindView(R.id.message)
    TextView textAvailable;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.verification_code)
    TextInputEditText verificationCode;
    @BindView(R.id.email_text)
    TextView emailText;


    @BindView(R.id.progress_bar_resend)
    ProgressBar progressBarResend;
    @BindView(R.id.message_resend)
    TextView messageResend;


    @Inject
    UserService userService;

    User user;


//    boolean verificationCodeValid = false; // flag to keep record of verification code


    public FragmentVerifyEmailChange() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_verify_email, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefChangeEmail.getUser(getActivity());

        emailText.setText(user.getEmail());

        return rootView;
    }






    @OnTextChanged(R.id.verification_code)
    void verificationCodeChanged()
    {

//        verificationCodeValid = false; // reset the flag

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);


        if(!validateVerificationCode(false))
        {
            return;
        }



//        logMessage("Validated !");


        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        countDownTimer.start();
    }





    boolean validateVerificationCode(boolean showError)
    {

        boolean isValid = true;

        if(verificationCode.getText().toString().length() == 0)
        {
            if(showError)
            {
                verificationCode.requestFocus();
                verificationCode.setError("Verification code cannot be empty !");
            }

            isValid = false;
        }
        else if(verificationCode.getText().toString().length() < 3)
        {
            if(showError)
            {
                verificationCode.requestFocus();
                verificationCode.setError("Verification code not valid !");
            }

            isValid = false;
        }


        return isValid;
    }





    void logMessage(String message)
    {
        Log.d("verify_email",message);
    }



    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

            logMessage("Timer onTick()");
        }

        public void onFinish() {

            logMessage("Timer onFinish() ");

            verifyEmailCode(false);
        }
    };






    void verifyEmailCode(final boolean initiateNext)
    {


        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = userService.checkEmailVerificationCode(
            user.getEmail(),verificationCode.getText().toString()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.INVISIBLE);

//                verificationCodeValid = false;

                if(response.code()==200)
                {
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Verification code is valid !");



                }
                else if(response.code() == 204)
                {
                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Verification code invalid or expired !");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);

                checkIcon.setVisibility(View.INVISIBLE);
                crossIcon.setVisibility(View.VISIBLE);

                textAvailable.setVisibility(View.VISIBLE);
                textAvailable.setText("Unable to check validity !");

            }
        });

    }





    @OnClick(R.id.update_email)
    void createAccountClick()
    {

        if(!validateVerificationCode(true))
        {
            return;
        }


//        checkIcon.setVisibility(View.INVISIBLE);
//        crossIcon.setVisibility(View.INVISIBLE);
//        textAvailable.setVisibility(View.INVISIBLE);

        verifyEmailCode(true);



        updateEmail();
    }


    void updateEmail()
    {


            user.setRt_email_verification_code(verificationCode.getText().toString());


//            Gson gson = new Gson();
//            logMessage(gson.toJson(user));

            user.setPassword(PrefLogin.getPassword(getActivity()));





            Call<ResponseBody> call = userService.updateEmail(PrefLogin.getAuthorizationHeaders(getActivity()),
                    user
            );


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {

                        User userDetails = PrefLogin.getUser(getActivity());
                        userDetails.setEmail(user.getEmail());
                        PrefLogin.saveUserProfile(userDetails,getActivity());
                        PrefLogin.saveUsername(getContext(),user.getEmail());


                        if(getActivity() instanceof ShowFragmentChangeEmail)
                        {
                            ((ShowFragmentChangeEmail) getActivity()).showResultSuccess();
                        }




                    }
                    else if(response.code()==304)
                    {

                        showToastMessage("Failed to change e-mail");
                    }
                    else if(response.code()==401 || response.code() == 403)
                    {
                        showToastMessage("Not Authorized !");
                    }
                    else
                    {
                        showToastMessage("Failed code : " + String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network failure !");
                }
            });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @OnClick(R.id.resend_code)
    void resendVerificationCode()
    {

        progressBarResend.setVisibility(View.VISIBLE);
        messageResend.setVisibility(View.VISIBLE);
        messageResend.setText("Sending verification code ... ");


        Call<ResponseBody> call = userService.sendVerificationEmail(user.getEmail());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarResend.setVisibility(View.INVISIBLE);
                messageResend.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    messageResend.setText("Verification Code Sent !");
                }
                else
                {
                    messageResend.setText("Failed to resend code !");
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBarResend.setVisibility(View.INVISIBLE);
                messageResend.setText("Network failure please check your internet connection!");
            }
        });
    }




}
