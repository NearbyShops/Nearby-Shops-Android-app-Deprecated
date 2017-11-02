package org.nearbyshops.enduserapp.Login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.EditProfileEndUser.EditEndUserFragment;
import org.nearbyshops.enduserapp.EditProfileEndUser.EditProfileEndUser;
import org.nearbyshops.enduserapp.ModelRoles.EndUser;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduserapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 12/8/16.
 */

public class LoginDialog extends DialogFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 1;
    private static final String TAG_LOG = "tag";
    ImageView dismiss_dialog_button;
    TextView signup_button;
    TextView login_button;
    EditText username;
    EditText password;

    @Bind(R.id.progress_bar) ProgressBar progressBar;

    @Inject
    EndUserService endUserService;

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    SignInButton signInButton;


    public LoginDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


    }










    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_login, container);
        ButterKnife.bind(this,view);

        dismiss_dialog_button = (ImageView) view.findViewById(R.id.dialog_dismiss_icon);
        signup_button = (TextView) view.findViewById(R.id.sign_up_button);
        login_button = (TextView) view.findViewById(R.id.login_button);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);

        signup_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        dismiss_dialog_button.setOnClickListener(this);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);


        return view;
    }





    @Override
    public void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @OnClick(R.id.sign_in_button)
    void signIn() {

        if(!mGoogleApiClient.isConnected())
        {
            return;
        }


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        progressBar.setVisibility(View.VISIBLE);

        showToastMessage("Sign in Click !");
    }


    @OnClick(R.id.sign_out_button)
    void signOut()
    {
        if(!mGoogleApiClient.isConnected())
        {
            return;
        }



        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                if(status.isSuccess())
                {
                    showToastMessage("Signed Out !");
                }
            }
        });
    }




    @OnClick(R.id.revoke_access_button)
    void revokeAccess()
    {
        if(!mGoogleApiClient.isConnected())
        {
            return;
        }

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {


                        if(status.isSuccess())
                        {
                            showToastMessage("Revoked !");
                        }
                    }
                });
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        return dialog;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.dialog_dismiss_icon:

                dismiss();
                Toast.makeText(getActivity(), R.string.dialog_dismissed,Toast.LENGTH_SHORT).show();

                break;

            case R.id.login_button:

                login_click();

                break;

            case R.id.sign_up_button:

                signUp_click();

                break;

            case R.id.sign_in_button:

//                signIn();

                break;

            default:
                break;
        }

    }


    void login_click()
    {

        String header = UtilityLogin.baseEncoding(username.getText().toString(),password.getText().toString());


        Call<EndUser> endUserCall = endUserService.getEndUserLogin(header);

        endUserCall.enqueue(new Callback<EndUser>() {
            @Override
            public void onResponse(Call<EndUser> call, Response<EndUser> response) {

                if(response.body()!=null && response.code()==200)
                {
                    UtilityLogin.saveEndUser(response.body(),LoginDialog.this.getActivity());

                    UtilityLogin.saveCredentials(getActivity(),username.getText().toString(),password.getText().toString());

                    if(getActivity() instanceof NotifyAboutLogin)
                    {
                        ((NotifyAboutLogin) getActivity()).NotifyLogin();
                    }

                    dismiss();
                }
                else if(response.code() == 401 || response.code() ==403)
                {
                    showToastMessage("Username or password is incorrect. Login not Authorized !");
                }

            }

            @Override
            public void onFailure(Call<EndUser> call, Throwable t) {

                showToastMessage("No Internet. Check your Internet Connection !");
            }
        });


    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }


    void signUp_click()
    {
//        Intent intent = new Intent(getContext(),SignUp.class);
//        startActivity(intent);

        Intent intent = new Intent(getActivity(), EditProfileEndUser.class);
        intent.putExtra(EditEndUserFragment.EDIT_MODE_INTENT_KEY,EditEndUserFragment.MODE_ADD);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        mGoogleApiClient.connect();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    @Bind(R.id.forgot_password) TextView forgotPassword;


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG_LOG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


//            forgotPassword.setText(acct.getDisplayName());

//            showToastMessage(acct.getIdToken());

            makeRequestGoogleSignIn(acct.getIdToken());


//            forgotPassword.setText(acct.getIdToken());
//            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);

            progressBar.setVisibility(View.GONE);
        }
    }



    void makeRequestGoogleSignIn(String idToken)
    {
        Call<EndUser> endUserCall  = endUserService.googleSignIn(idToken);

        endUserCall.enqueue(new Callback<EndUser>() {
            @Override
            public void onResponse(Call<EndUser> call, Response<EndUser> response) {

                if(response.code()==201)
                {
                    showToastMessage("Successful !");

                    progressBar.setVisibility(View.GONE);

                    EndUser endUser = response.body();
                    UtilityLogin.saveCredentials(getContext(),endUser.getUsername(),endUser.getPassword());
                    UtilityLogin.saveEndUser(endUser,getActivity());

                    dismiss();
                }
                else
                {
                    showToastMessage("failed code : " + String.valueOf(response.code()));
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<EndUser> call, Throwable t) {

                showToastMessage("failed");

                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }
}
