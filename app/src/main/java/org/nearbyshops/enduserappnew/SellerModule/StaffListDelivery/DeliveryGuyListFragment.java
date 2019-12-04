package org.nearbyshops.enduserappnew.SellerModule.StaffListDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import org.nearbyshops.core.API.DeliveryGuyLoginService;
import org.nearbyshops.core.API.UserService;
import org.nearbyshops.core.Model.ModelEndPoints.UserEndpoint;
import org.nearbyshops.core.Model.ModelRoles.User;
import org.nearbyshops.core.Preferences.PrefLocation;
import org.nearbyshops.core.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderProfile.ViewHolderDeliveryProfile;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.Models.HeaderTitle;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 14/6/17.
 */

public class DeliveryGuyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderDeliveryProfile.ListItemClick{

    boolean isDestroyed = false;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Inject
    UserService userService;

    @Inject
    DeliveryGuyLoginService service;






    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ArrayList<Object> dataset = new ArrayList<>();


    // flags
    private boolean clearDataset = false;

//    boolean getRowCountVehicle = false;
//    boolean resetOffsetVehicle = false;


    private int limit = 10;
    private int offset = 0;
    public int item_count = 0;


//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;


    public DeliveryGuyListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_delivery_guy_list, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }





    private void setupSwipeContainer()
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





    private void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

                        getDeliveryProfiles();
                    }


                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }






    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }




    @Override
    public void onRefresh() {

        clearDataset = true;
//        getRowCountVehicle = true;
//        resetOffsetVehicle = true;

        getDeliveryProfiles();
    }







    private void getDeliveryProfiles()
    {

        if(clearDataset)
        {
            offset = 0;
        }


        User user = PrefLogin.getUser(getActivity());

        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }



        Call<UserEndpoint> call = service.getDeliveryGuyForShopAdmin(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                (double) PrefLocation.getLatitude(getActivity()),(double)PrefLocation.getLongitude(getActivity()),
                null,null,
                limit, offset,
                clearDataset,false
        );




        call.enqueue(new Callback<UserEndpoint>() {
            @Override
            public void onResponse(Call<UserEndpoint> call, Response<UserEndpoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null) {


                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();

                        if(item_count>0)
                        {
                            dataset.add(new HeaderTitle("Delivery Staff"));
                        }
                    }



                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }



//                    showToastMessage("Item Count : " + item_count);

                    if(item_count==0)
                    {
                        dataset.add(EmptyScreenDataFullScreen.emptyScreenDeliveryStaff());

                    }


                    if(offset + limit >= item_count)
                    {
                        listAdapter.setLoadMore(false);
                    }
                    else
                    {
                        listAdapter.setLoadMore(true);
                    }
                }



                listAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserEndpoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();

            }
        });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }



//    @Override
//    public void taxiFiltersChanged() {
//        makeRefreshNetworkCall();
//    }







    @Override
    public void listItemClick(User user, int position) {


        if(getActivity().getIntent().getBooleanExtra("select_delivery_guy",false))
        {
            Intent data = new Intent();
            data.putExtra("delivery_guy_id",user.getUserID());
            getActivity().setResult(456,data);

            getActivity().finish();
        }
        else
        {
            Gson gson = new Gson();
            String jsonString = gson.toJson(user);



//            Intent intent = new Intent(getActivity(), EditProfileDelivery.class);
//            intent.putExtra("staff_profile",jsonString);
//            intent.putExtra(FragmentEditProfileDelivery.EDIT_MODE_INTENT_KEY, FragmentEditProfileDelivery.MODE_UPDATE);
//            startActivity(intent);

        }







    }





    @Override
    public boolean listItemLongClick(User user, int position) {
        return false;
    }

}
