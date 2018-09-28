package org.nearbyshops.enduserappnew.SignUp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;


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
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentVerifyEmailSignUp extends Fragment {


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
    @BindView(R.id.header_title)
    TextView message;
    @BindView(R.id.header)
    TextView header;


    @BindView(R.id.progress_bar_resend)
    ProgressBar progressBarResend;
    @BindView(R.id.message_resend)
    TextView messageResend;

    SmsVerifyCatcher smsVerifyCatcher;


    @Inject
    UserService userService;

    User user;


//    boolean verificationCodeValid = false; // flag to keep record of verification code


    public FragmentVerifyEmailSignUp() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_verify_email_signup, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefrenceSignUp.getUser(getActivity());


        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
        {
            header.setText("Step 4 : Verify your E-mail");
            message.setText("We have sent you a verification code on your e-mail ID : ");
            emailText.setText(user.getEmail());
        }
        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            header.setText("Step 4 : Verify your phone");
            message.setText("We have sent you a one time password (OTP) on your phone : ");
            emailText.setText(user.getPhone());
        }



        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {

                String code = parseCode(message);//Parse verification code
                verificationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });


        return rootView;
    }







    private String parseCode(String message) {
        return message.replaceAll("[^0-9]", "");
    }




    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                verificationCode.setError("Verification code cannot be empty !");
            }

            isValid = false;
        }
        else if(verificationCode.getText().toString().length() < 3)
        {
            if(showError)
            {
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


            if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
            {
                verifyEmailCode(false);
            }
            else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
            {
                verifyPhoneCode();
            }


        }
    };





    void verifyPhoneCode()
    {



        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = userService.checkPhoneVerificationCode(
                user.getPhone(),verificationCode.getText().toString()
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

//                    verificationCodeValid = false;
                }
                else
                {
                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Failed code : " + String.valueOf(response.code()));
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                verificationCodeValid = false;

                progressBar.setVisibility(View.INVISIBLE);

                checkIcon.setVisibility(View.INVISIBLE);
                crossIcon.setVisibility(View.VISIBLE);

                textAvailable.setVisibility(View.VISIBLE);
                textAvailable.setText("Unable to check validity !");

            }
        });

    }






    void verifyEmailCode(final boolean initiateNext)
    {

//        User user = null;

//        if(getActivity() instanceof ReadWriteUser)
//        {
//            user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//        }

//        if(user==null)
//        {
//            return;
//        }



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

//                    verificationCodeValid = true;

//                    if(initiateNext) {

//                        showToastMessage("Next Initiated !");


                        // save Verification code
//                        if(getActivity() instanceof ReadWriteUser)
//                        {
//                            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//                            user.setRt_email_verification_code(verificationCode.getText().toString());
//                            ((ReadWriteUser) getActivity()).setSignUpProfile(user);
//                        }




//                        user.setRt_email_verification_code(verificationCode.getText().toString());
//
//                        if (getActivity() instanceof ShowFragmentSignUp)
//                        {
//                            ((ShowFragmentSignUp) getActivity()).showEnterPassword();
//                        }


//                    }

                }
                else if(response.code() == 204)
                {


                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Verification code invalid or expired !");

//                    verificationCodeValid = false;
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                verificationCodeValid = false;

                progressBar.setVisibility(View.INVISIBLE);

                checkIcon.setVisibility(View.INVISIBLE);
                crossIcon.setVisibility(View.VISIBLE);

                textAvailable.setVisibility(View.VISIBLE);
                textAvailable.setText("Unable to check validity !");

            }
        });

    }







//    @OnClick(R.id.next)
    void nextClick()
    {

//        if(!verificationCodeValid)
//        {
//            return;
//        }
    }


    @OnClick(R.id.create_account)
    void createAccountClick()
    {

        if(!validateVerificationCode(true))
        {
            return;
        }


//        checkIcon.setVisibility(View.INVISIBLE);
//        crossIcon.setVisibility(View.INVISIBLE);
//        textAvailable.setVisibility(View.INVISIBLE);


        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
        {
            verifyEmailCode(true);
        }
        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            verifyPhoneCode();
        }


        createAccount();
    }


    void createAccount()
    {

//        if(getActivity() instanceof ReadWriteUser)
//        {

//            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//            user.setPassword(enterPassword.getText().toString());

            if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
            {
                user.setRt_email_verification_code(verificationCode.getText().toString());
            }
            else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
            {
                user.setRt_phone_verification_code(verificationCode.getText().toString());
            }



//            Gson gson = new Gson();
//            logMessage(gson.toJson(user));


            Call<User> call = userService.endUserRegistration(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if(response.code()==201)
                    {
//                        showToastMessage("Next Initiated !");

                        // save user
//                        ((ReadWriteUser) getActivity()).setSignUpProfile(response.body());

                        if(getActivity() instanceof ShowFragmentSignUp)
                        {
                            ((ShowFragmentSignUp) getActivity()).showResultSuccess();
                        }

                    }
                    else if(response.code()==304)
                    {

                        showToastMessage("Failed to create account");
                    }
                    else
                    {
                        showToastMessage("Failed code : " + String.valueOf(response.code()));
                    }


                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

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

//        User user = null;

//        if(getActivity() instanceof ReadWriteUser)
//        {
//            user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//        }
//
//        if(user==null)
//        {
//            return;
//        }



        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            resendCodePhone();
        }
        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
        {
            resendCodeEmail();
        }
    }



    void resendCodeEmail()
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



    void resendCodePhone()
    {


        progressBarResend.setVisibility(View.VISIBLE);
        messageResend.setVisibility(View.VISIBLE);
        messageResend.setText("Sending verification code ... ");


        Call<ResponseBody> call = userService.sendVerificationPhone(user.getPhone());

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
