package org.nearbyshops.enduserappnew.SellerModule.StaffListDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.nearbyshops.core.Model.ModelRoles.User;
import org.nearbyshops.core.SignUp.PrefSignUp.PrefrenceSignUp;
import org.nearbyshops.core.SignUp.SignUp;
import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class DeliveryGuyList extends AppCompatActivity {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        setContentView(R.layout.activity_staff_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
//        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);


        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new DeliveryGuyListFragment(),TAG_STEP_ONE)
                    .commitNow();
        }


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }








    @OnClick(R.id.fab)
    void fabClick()
    {
//        showToastMessage("Fab click !");

        PrefrenceSignUp.saveUser(null,this);
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("user_role", User.ROLE_DELIVERY_GUY_SELF_CODE);
        startActivity(intent);
    }






//    @OnClick({R.id.filters})
    void showFilters()
    {
        showToastMessage("Filters Click !");
//        FilterTaxi filterTaxi = new FilterTaxi();
//        filterTaxi.show(getActivity().getSupportFragmentManager(),"filter_taxi");
    }





}
