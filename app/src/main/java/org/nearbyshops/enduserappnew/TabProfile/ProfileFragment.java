package org.nearbyshops.enduserappnew.TabProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;



import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import org.taxireferral.enduserapp.MapzenSDK.MapzenMap;

/**
 * Created by sumeet on 2/4/17.
 */

public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    boolean isDestroyed = false;

    @BindView(R.id.label_login)TextView labelLogin;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;




    @BindView(R.id.user_profile)
    LinearLayout profileBlock;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.user_id)
    TextView userID;


    @BindView(R.id.current_dues)
    TextView currentDues;
    @BindView(R.id.credit_limit)
    TextView creditLimit;


    @Inject
    UserService userService;



    public ProfileFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);




        setupSwipeContainer();


        if(savedInstanceState==null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {

                    swipeContainer.setRefreshing(true);
                    onRefresh();
                }
            });

            bindUserProfile();

        }


        return rootView;
    }






    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }




    @OnClick(R.id.user_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
        startActivity(intent);

    }





    @OnClick(R.id.billing_info)
    void billingInfoClick()
    {
//        Intent intent = new Intent(getActivity(), Transactions.class);
//        startActivity(intent);
    }



    @OnClick(R.id.faqs_block)
    void faqsBlock()
    {
//        Intent intent = new Intent(getActivity(), MapzenMap.class);
//        startActivity(intent);

    }




    @OnClick(R.id.privacy_policy_block)
    void privacyPolicyClick()
    {
        String url = "https://taxireferral.org/privacy-policy/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }



    @OnClick(R.id.tos_block)
    void termsOfServiceClick()
    {
        String url = "https://taxireferral.org/terms-of-service/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




//    @OnClick(R.id.staff_block)
//    void staffClick()
//    {
//        getActivity().startActivity(new Intent(getActivity(), StaffList.class));
//    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }




    @OnClick({R.id.login_block})
    void loginClick()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }




    void logout()
    {
        // log out
        PrefLogin.saveUserProfile(null,getActivity());
        PrefLogin.saveCredentials(getActivity(),null,null);

        // stop location update service
//        stopService();



        
        if(getActivity() instanceof ShowFragment)
        {
            ((ShowFragment) getActivity()).showLoginFragment();
        }
    }




    @Override
    public void onRefresh() {
//        countDownTimer.start();
        getUserProfile();
    }







    void getUserProfile()
    {

        if(getActivity()==null)
        {
            return;
        }


        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null)
        {
            return;
        }





        Call<User> call = userService.getProfile(
                PrefLogin.getAuthorizationHeaders(getActivity())
        );





        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {

                    PrefLogin.saveUserProfile(response.body(),getActivity());

                }
                else if(response.code()==204)
                {
                    // Vehicle not registered so remove the saved vehicle
//                    PrefVehicle.saveVehicle(null,getContext());

                }
                else
                {
                    showToastMessage("Server error code : " + String.valueOf(response.code()));
                }


                bindUserProfile();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                bindUserProfile();
                swipeContainer.setRefreshing(false);

            }
        });

    }







    void bindUserProfile()
    {

        User user = PrefLogin.getUser(getActivity());

        if(user==null)
        {
            profileBlock.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            return;
        }


        userID.setText("User ID : " + String.valueOf(user.getUserID()));

        profileBlock.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);


        Drawable placeholder = ContextCompat.getDrawable(getActivity(),R.drawable.ic_nature_people_white_48px);
        String imagePath = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";

        showLogMessage("Profile Screen : User Image Path : " + imagePath);



        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(placeholder)
                .into(profileImage);


        phone.setText(user.getPhone());
        userName.setText(user.getName());


//        if(user.getCurrentDues()>=0)
//        {
//            currentDues.setText("You Owe : "
//                    + getString(R.string.rupee_symbol)
//                    + " " + String.format("%.2f",user.getCurrentDues())
//            );
//        }
//        else
//        {
//            currentDues.setText("You have a surplus of "
//                    + getString(R.string.rupee_symbol)
//                    + " " + String.format("%.2f",-user.getCurrentDues())
//                    + " in your account."
//            );
//        }

        creditLimit.setText("Your Credit Limit : "
                + getString(R.string.rupee_symbol) + " "
                + String.format("%.2f",user.getExtendedCreditLimit() + 1000)
        );

    }











    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false; // reset flag
    }


    @Override
    public void onStop() {
        super.onStop();
        isDestroyed = true;
    }







    void showLogMessage(String message)
    {
        Log.d("location_service",message);
    }





}
