package org.nearbyshops.enduserappnew.SignUp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentEmailOrPhone extends Fragment {


    @BindView(R.id.select_email)
    TextView selectPhone;
    @BindView(R.id.select_phone)
    TextView selectEmail;

    @BindView(R.id.text_input_phone)
    TextInputLayout phoneLayout;
    @BindView(R.id.text_input_email)
    TextInputLayout emailLayout;


//    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.phone)
TextInputEditText phone;
    @BindView(R.id.email)
    TextInputEditText email;

    @BindView(R.id.check_icon)
    ImageView checkIcon;
    @BindView(R.id.cross_icon)
    ImageView crossIcon;
    @BindView(R.id.message)
    TextView textAvailable;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


//    int phoneOrEmail = 1; // flag for indicating the input mode 1 for phone and 2 for email


    boolean phoneIsAvailable = false;
    boolean emailIsAvailable = false;

    User user;


    @Inject
    UserService userService;



    public FragmentEmailOrPhone() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_email_or_phone, container, false);
        ButterKnife.bind(this, rootView);



        user = PrefrenceSignUp.getUser(getActivity());

        if (user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL) {

            selectEmailClick();
        }
        else if (user.getRt_registration_mode() == User.REGISTRATION_MODE_PHONE) {

            selectPhoneClick();
        }
        else
        {
            selectPhoneClick();
        }


//
//        if(PrefServiceConfig.getServiceConfig(getActivity())!=null)
//        {
//            ccp.setCountryForNameCode(PrefServiceConfig.getServiceConfig(getActivity()).getISOCountryCode());
//        }
//
//
//
//        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//                textInputChanged();
//            }
//        });
//
//
//        ccp.registerCarrierNumberEditText(phone);
//        ccp.setNumberAutoFormattingEnabled(false);



        phone.requestFocus();
        bindViews();

        return rootView;
    }





    @OnClick(R.id.select_email)
    void selectEmailClick()
    {

        user.setRt_registration_mode(User.REGISTRATION_MODE_EMAIL);
        email.requestFocus();


        selectPhone.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));

        selectEmail.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));



        phoneLayout.setVisibility(View.INVISIBLE);
        emailLayout.setVisibility(View.VISIBLE);

//        phoneOrEmail = 2;  // set flag


        bindViews();

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        textInputChanged();


    }



    @OnClick(R.id.select_phone)
    void selectPhoneClick()
    {

        user.setRt_registration_mode(User.REGISTRATION_MODE_PHONE);

        phone.requestFocus();


        selectEmail.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));

        selectPhone.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));


        phoneLayout.setVisibility(View.VISIBLE);
        emailLayout.setVisibility(View.INVISIBLE);


//        phoneOrEmail = 1; // set flag

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        textInputChanged();


    }



    void bindViews()
    {
            phone.setText(user.getPhone());
            email.setText(user.getEmail());
    }











    void saveDataFromViews()
    {

//            user.setPhone(ccp.getSelectedCountryCode()+phone.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setEmail(email.getText().toString());

//            showToastMessage("Phone : " + user.getPhone());

    }



    @OnTextChanged({R.id.email,R.id.phone})
    void textInputChanged()
    {
        // reset flags
        phoneIsAvailable = false;
        emailIsAvailable = false;


        textAvailable.setVisibility(View.INVISIBLE);
        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);

        if(!isDataValid(false))
        {
            return;
        }

        saveDataFromViews();


//        checkUsernameExist();


        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();  // restart the timer
        countDownTimer.start();
    }



    boolean isDataValid(boolean showError)
    {
        boolean isValid = true;

        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            // validate phone

            if(phone.getText().toString().equals(""))
            {
                if(showError)
                {
                    phone.requestFocus();
                    phone.setError("Phone cannot be empty !");
                }

                isValid= false;

            }
            else if(phone.getText().toString().length()!=10)
            {
                if(showError)
                {
                    phone.requestFocus();
                    phone.setError("Phone should have 10 digits");
                }

                isValid=false;
            }

        }
        else if (user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
        {
            if(email.getText().toString().equals(""))
            {
                if(showError)
                {
                    email.requestFocus();
                    email.setError("E-mail cannot be empty !");
                }

                isValid = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
            {
                if(showError)
                {
                    email.requestFocus();
                    email.setError("Not a valid e-mail !");
                }

                isValid = false;
            }

        }


        return isValid;
    }









    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

           checkUsernameExist();
        }
    };






    void checkUsernameExist()
    {
        String inputName = "";

        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            // check for phone

            inputName =  phone.getText().toString();
//            inputName = ccp.getSelectedCountryCode() + phone.getText().toString();


        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // check for email

            inputName = email.getText().toString();
        }

        Call<ResponseBody> call = userService.checkUsernameExists(inputName);


        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.INVISIBLE);

                if(response.code()==200)
                {
                    // username is not unique and already exist

                    if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
                    {
                        phone.setError("Somebody has already registered using that phone !");
                    }
                    else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
                    {
                        email.setError("Somebody has already registered using that E-mail !");
                    }


                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Not Available for Registration !");


                    if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = false;
                    }
                    else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = false;
                    }

                }
                else if(response.code()==204)
                {
                    // username is unique and available for registration
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Available for Registration !");


                    if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = true;
                    }
                    else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = true;
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }








    @OnClick(R.id.next)
    void nextClick()
    {
        if(!isDataValid(true))
        {
            return;
        }


        PrefrenceSignUp.saveUser(user,getActivity());



        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            // registering using phone

            if(!phoneIsAvailable)
            {
                // phone is not available for registration so return
                return;
            }


//            showToastMessage("Next Initiated");


            if(getActivity() instanceof ShowFragmentSignUp)
            {
                ((ShowFragmentSignUp) getActivity()).showEnterPassword();
            }

        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // registering using email

            if(!emailIsAvailable)
            {
                // e-mail is not available for registration so return
                return;
            }


            if(getActivity() instanceof ShowFragmentSignUp)
            {
                ((ShowFragmentSignUp) getActivity()).showEnterPassword();
            }
        }

    }







    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

}
