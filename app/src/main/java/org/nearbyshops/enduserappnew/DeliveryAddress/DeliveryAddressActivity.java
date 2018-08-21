package org.nearbyshops.enduserappnew.DeliveryAddress;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DeliveryAddress.EditAddress.EditAddressFragment;
import org.nearbyshops.enduserappnew.DeliveryAddress.EditAddress.EditDeliveryAddress;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, DeliveryAddressAdapter.NotifyDeliveryAddress, View.OnClickListener {

    @Inject
    DeliveryAddressService deliveryAddressService;

    RecyclerView recyclerView;
    DeliveryAddressAdapter adapter;
    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    List<DeliveryAddress> dataset = new ArrayList<>();

//    EndUser endUser = null;

    public final static String SHOP_INTENT_KEY = "shop_cart_item";
    public final static String CART_STATS_INTENT_KEY = "cart_stats";


    TextView addNewAddress;


    public DeliveryAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // findView By id'// STOPSHIP: 11/6/16

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addNewAddress = (TextView) findViewById(R.id.addNewAddress);

        addNewAddress.setOnClickListener(this);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupSwipeContainer();
        setupRecyclerView();
    }



    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }


    void setupRecyclerView()
    {


        adapter = new DeliveryAddressAdapter(dataset,this,this);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);


        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }


    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }



    void makeRefreshNetworkCall()
    {


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();
                adapter.notifyDataSetChanged();
            }
        });
    }


    void makeNetworkCall()
    {
        User endUser = PrefLogin.getUser(this);

        if(endUser == null)
        {
            swipeContainer.setRefreshing(false);
            showLoginDialog();
            return;
        }

        Call<List<DeliveryAddress>> call = deliveryAddressService.getAddresses(
                endUser.getUserID());

        call.enqueue(new Callback<List<DeliveryAddress>>() {
            @Override
            public void onResponse(Call<List<DeliveryAddress>> call, Response<List<DeliveryAddress>> response) {


                if(response.body()!=null)
                {
                    dataset.clear();
                    dataset.addAll(response.body());

                    adapter.notifyDataSetChanged();



                }else
                {
                    dataset.clear();
                    adapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<DeliveryAddress>> call, Throwable t) {


                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

            }
        });
    }



    private void showLoginDialog()
    {
//        FragmentManager fm = getSupportFragmentManager();
//        LoginDialog loginDialog = new LoginDialog();
//        loginDialog.show(fm,"serviceUrl");

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

//        ButterKnife.unbind(this);

    }



    @Override
    public void notifyEdit(DeliveryAddress deliveryAddress) {


//        Intent intent = new Intent(this,EditAddressActivity.class);
//        intent.putExtra(EditAddressActivity.DELIVERY_ADDRESS_INTENT_KEY,deliveryAddress);
//        startActivity(intent);

        Intent intent = new Intent(this,EditDeliveryAddress.class);
        intent.putExtra(EditAddressFragment.EDIT_MODE_INTENT_KEY,EditAddressFragment.MODE_UPDATE);
        intent.putExtra(EditAddressFragment.DELIVERY_ADDRESS_INTENT_KEY,deliveryAddress);
        startActivity(intent);

    }

    @Override
    public void notifyRemove(final DeliveryAddress deliveryAddress, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Delete Address !")
                .setMessage("Are you sure you want to delete this address !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteAddress(deliveryAddress,position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Deleted !");
                    }
                })
                .show();



    }


    void deleteAddress(DeliveryAddress deliveryAddress, final int position)
    {

        Call<ResponseBody> call = deliveryAddressService.deleteAddress(deliveryAddress.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
                    dataset.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Failed !");
            }
        });
    }



    @Override
    public void notifyListItemClick(DeliveryAddress deliveryAddress) {

        Intent output = new Intent();
        output.putExtra("output",deliveryAddress);
        setResult(2,output);
        finish();
    }




    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.addNewAddress:

                addNewAddressClick(v);

                break;

            default:
                break;
        }

    }




    void addNewAddressClick(View view)
    {
//        Intent intent = new Intent(this,AddAddressActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this,EditDeliveryAddress.class);
        intent.putExtra(EditAddressFragment.EDIT_MODE_INTENT_KEY,EditAddressFragment.MODE_ADD);
        startActivity(intent);

    }


}
