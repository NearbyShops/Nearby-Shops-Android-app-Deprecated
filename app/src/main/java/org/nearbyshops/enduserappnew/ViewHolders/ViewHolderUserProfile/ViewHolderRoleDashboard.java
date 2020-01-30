package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;
import org.nearbyshops.enduserappnew.SellerModule.DeliveryGuyHome.DeliveryHome;
import org.nearbyshops.enduserappnew.SellerModule.ShopAdminHome.ShopAdminHome;
import org.nearbyshops.enduserappnew.adminModule.AdminDashboard.AdminDashboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderRoleDashboard extends RecyclerView.ViewHolder{






    @BindView(R.id.dashboard_name) TextView dashboardName;
    @BindView(R.id.dashboard_description) TextView dashboardDescription;




    private Context context;
    private Fragment fragment;
    private ViewModelShop viewModelShop;


    private ProgressDialog progressDialog;





    public static ViewHolderRoleDashboard create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_role_dashboard,parent,false);
        return new ViewHolderRoleDashboard(view,context,fragment);
    }





    public ViewHolderRoleDashboard(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        bindDashboard();



        viewModelShop = ViewModelProviders.of(fragment).get(ViewModelShop.class);



        viewModelShop.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }



                if(integer == ViewModelShop.EVENT_BECOME_A_SELLER_SUCCESSFUL)
                {
                    bindDashboard();
                }

            }
        });




        viewModelShop.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
            }
        });


    }






    @OnClick(R.id.dashboard_by_role)
    public void dashboardClick()
    {

        User user = PrefLogin.getUser(context);

        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {

            Intent intent = new Intent(context, ShopAdminHome.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            Intent intent = new Intent(context, AdminDashboard.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SELF_CODE)
        {
            Intent intent = new Intent(context, DeliveryHome.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {

            viewModelShop.becomeASeller();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait ... converting you to a seller !");
            progressDialog.show();

        }

    }





    private void bindDashboard()
    {
        User user = PrefLogin.getUser(context);

        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            dashboardName.setText("Shop Dashboard");
            dashboardDescription.setText("Press here to access the shop dashboard !");
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SELF_CODE)
        {

            dashboardName.setText("Delivery Dashboard");
            dashboardDescription.setText("Press here to access the Delivery dashboard !");
        }
        else if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            dashboardName.setText("Admin Dashboard");
            dashboardDescription.setText("Press here to access the admin dashboard !");

        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {
            dashboardName.setText("Become a Seller");
            dashboardDescription.setText("Press here to create a shop and become a seller on currently selected market !");
        }


    }








    void listItemClick()
    {
        ((ListItemClick)fragment).listItemClick();
    }







    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }







    public interface ListItemClick
    {
        void listItemClick();
    }



}

