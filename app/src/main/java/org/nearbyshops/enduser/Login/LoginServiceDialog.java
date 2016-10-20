package org.nearbyshops.enduser.Login;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 12/8/16.
 */

public class LoginServiceDialog extends DialogFragment implements View.OnClickListener {


    ImageView dismiss_dialog_button;
    TextView signup_button;
    TextView login_button;
    EditText username;
    EditText password;

    @Inject
    EndUserService endUserService;


    public LoginServiceDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_login_service, container);

        dismiss_dialog_button = (ImageView) view.findViewById(R.id.dialog_dismiss_icon);
        signup_button = (TextView) view.findViewById(R.id.sign_up_button);
        login_button = (TextView) view.findViewById(R.id.login_button);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);

        signup_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        dismiss_dialog_button.setOnClickListener(this);

        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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

            default:
                break;
        }

    }


    void login_click()
    {

        String header = UtilityLogin.baseEncoding(username.getText().toString(),password.getText().toString());


        Call<EndUser> endUserCall = endUserService.EndUserLogin(header);

        endUserCall.enqueue(new Callback<EndUser>() {
            @Override
            public void onResponse(Call<EndUser> call, Response<EndUser> response) {

                if(response.body()!=null && response.code()==200)
                {
                    UtilityLogin.saveEndUser(response.body(),LoginServiceDialog.this.getActivity());

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
    }

}
