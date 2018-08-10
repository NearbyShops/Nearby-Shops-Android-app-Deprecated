package org.nearbyshops.enduserapp.LoginNew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import org.nearbyshops.enduserapp.R;


public class Login extends AppCompatActivity implements ShowFragmentSelectService, NotifyAboutLogin {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";
    public static final String TAG_STEP_THREE = "tag_step_three";
    public static final String TAG_STEP_FOUR = "tag_step_four";

    public static final String TAG_SELECT_SERVICE = "select_service";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);

        setContentView(R.layout.activity_login_new);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
//        toolbar.setTitle("Login");
//        setSupportActionBar(toolbar);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new LoginFragment(),TAG_STEP_ONE)
                    .commitNow();
        }

    }




    @Override
    public void showSelectServiceFragment() {


//        if(getSupportFragmentManager().findFragmentByTag(TAG_SELECT_SERVICE)==null)
//        {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
//                    .replace(R.id.fragment_container,new SelectServiceFragment(),TAG_SELECT_SERVICE)
//                    .addToBackStack("select_service")
//                    .commit();
//        }

    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }

    public static final int RESULT_CODE_LOGIN_SUCCESS  = 1;






    @Override
    public void loginSuccess() {

        setResult(RESULT_OK);
        finish();
    }
}
