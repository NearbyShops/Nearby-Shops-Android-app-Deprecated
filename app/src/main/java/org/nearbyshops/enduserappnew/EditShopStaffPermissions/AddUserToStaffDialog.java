package org.nearbyshops.enduserappnew.EditShopStaffPermissions;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.ShopStaffService;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddUserToStaffDialog extends DialogFragment {



    @BindView(R.id.role_shop_staff) TextView roleShopStaff;
    @BindView(R.id.role_delivery) TextView roleDelivery;

    @BindView(R.id.input_user_id) EditText inputUserID;

    @BindView(R.id.add_member) TextView addMember;
    @BindView(R.id.dismiss) TextView dismiss;


    @Inject Gson gson;



    private int selectedRole = User.ROLE_SHOP_STAFF_CODE;
    private int userID = 0;



    @Inject
    ShopStaffService shopStaffService;


    public AddUserToStaffDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




//    Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_add_shop_staff, container);
        ButterKnife.bind(this,view);

        bindRole();
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









    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }







    @OnClick(R.id.role_shop_staff)
    void shopStaffClick()
    {
        selectedRole = User.ROLE_SHOP_STAFF_CODE;
        bindRole();
    }

    @OnClick(R.id.role_delivery)
    void roleDeliveryClick()
    {
        selectedRole = User.ROLE_DELIVERY_GUY_SELF_CODE;
        bindRole();
    }





    private void bindRole()
    {

        if(selectedRole==User.ROLE_SHOP_STAFF_CODE)
        {
            roleShopStaff.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            roleShopStaff.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.buttonColor));

            roleDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
            roleDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        }
        else if(selectedRole==User.ROLE_DELIVERY_GUY_SELF_CODE)
        {
            roleDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            roleDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.buttonColor));

            roleShopStaff.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
            roleShopStaff.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        }

    }





    @OnTextChanged(R.id.input_user_id)
    void editUserID()
    {

        userID = Integer.parseInt(inputUserID.getText().toString());
    }




    @BindView(R.id.progress_bar_add_member) ProgressBar progressAddMember;








    @OnClick(R.id.add_member)
    void addMemberClick()
    {

        if(inputUserID.getText().toString().equals(""))
        {
            inputUserID.setError("Please enter User ID");
            inputUserID.requestFocus();

            return;
        }


        Call<ResponseBody> call = shopStaffService.upgradeUserToShopStaff(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                userID,selectedRole
        );



        progressAddMember.setVisibility(View.VISIBLE);
        addMember.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressAddMember.setVisibility(View.INVISIBLE);
                addMember.setVisibility(View.VISIBLE);


                if(!isVisible())
                {
                    return;
                }

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                progressAddMember.setVisibility(View.INVISIBLE);
                addMember.setVisibility(View.VISIBLE);



                if(!isVisible())
                {
                    return;
                }


                showToastMessage("Failed !");
            }
        });


    }




    @OnClick(R.id.dismiss)
    void dismissClick()
    {
        dismiss();
    }


}
