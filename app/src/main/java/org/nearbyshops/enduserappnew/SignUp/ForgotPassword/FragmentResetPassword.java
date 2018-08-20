package org.nearbyshops.enduserappnew.SignUp.ForgotPassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.SignUp.Interfaces.ShowFragmentForgotPassword;
import org.nearbyshops.enduserappnew.SignUp.PrefSignUp.PrefrenceForgotPassword;

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

public class FragmentResetPassword extends Fragment {


//    @BindView(R.id.check_icon) ImageView checkIcon;
//    @BindView(R.id.cross_icon) ImageView crossIcon;
//    @BindView(R.id.message) TextView textAvailable;


//    @BindView(R.id.progress_bar) ProgressBar progressBar;
//    @BindView(R.id.verification_code) TextInputEditText verificationCode;
//    @BindView(R.id.email_text) TextView emailText;


//    @BindView(R.id.progress_bar_resend) ProgressBar progressBarResend;
//    @BindView(R.id.message_resend) TextView messageResend;


    @BindView(R.id.progress_bar_button)
    ProgressBar progressBarButton;
    @BindView(R.id.reset_button)
    TextView resetButton;
    @BindView(R.id.enter_password)
    EditText enterPassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;


    @Inject
    UserService userService;

    User user;


//    boolean verificationCodeValid = false; // flag to keep record of verification code


    public FragmentResetPassword() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefrenceForgotPassword.getUser(getActivity());


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





    void logMessage(String message)
    {
        Log.d("verify_email",message);
    }



    @OnClick(R.id.reset_button)
    void createAccountClick()
    {
        if(!validatePasswords(true))
        {
            return;
        }

        resetPassword();
    }




    void resetPassword()
    {
            user.setPassword(enterPassword.getText().toString());

            Gson gson = new Gson();
            logMessage(gson.toJson(user));


        progressBarButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);


            Call<ResponseBody> call = userService.resetPassword(user);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    progressBarButton.setVisibility(View.INVISIBLE);
                    resetButton.setVisibility(View.VISIBLE);

                    if(response.code()==200)
                    {
                        if(getActivity() instanceof ShowFragmentForgotPassword)
                        {
                            ((ShowFragmentForgotPassword) getActivity()).showResultSuccess();
                        }

                    }
                    else if(response.code()==304)
                    {

                        showToastMessage("Failed to Reset Password");
                    }
                    else
                    {
                        showToastMessage("Failed code : " + String.valueOf(response.code()));
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                    progressBarButton.setVisibility(View.INVISIBLE);

                    showToastMessage("Network failure !");
                }
            });
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

}
