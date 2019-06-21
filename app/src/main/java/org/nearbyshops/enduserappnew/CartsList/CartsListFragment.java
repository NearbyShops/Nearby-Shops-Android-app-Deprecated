package org.nearbyshops.enduserappnew.CartsList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CartsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<CartStats>> {


    @Inject
    CartStatsService cartStatsService;

    private RecyclerView recyclerView;
    private CartsListAdapter adapter;
    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;



    private List<CartStats> dataset = new ArrayList<>();
    private boolean isDestroyed = false;


    @BindView(R.id.empty_screen) LinearLayout emptyScreen;

    @BindView(R.id.service_name) TextView serviceName;


    public CartsListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_carts_list);
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.activity_carts_list, container, false);
        ButterKnife.bind(this, rootView);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle("Nearby Shops");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//





        if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
        {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }
        else
        {
            serviceName.setVisibility(View.GONE);
        }






//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
////        toolbar.setTitle("TripLogic");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        // findViewByID's
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        setupSwipeContainer();
        setupRecyclerView();


//        ((AppCompatActivity)getActivity())
//                .getSupportActionBar()
//                .setDisplayHomeAsUpEnabled(true);


        return rootView;
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


        adapter = new CartsListAdapter(dataset,getActivity());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(
        //        new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST)
        //);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        layoutManager.setSpanCount(metrics.widthPixels/350);

        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);




    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }



    void makeNetworkCall()
    {


        User endUser = PrefLogin.getUser(getActivity());
        if(endUser==null)
        {
            showLoginDialog();

            swipeContainer.setRefreshing(false);
            return;
        }



        Call<List<CartStats>> call = cartStatsService.getCart(
                endUser.getUserID(),null,null,true,
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity())
        );

//        ,
//        (double) PrefGeneral.getFromSharedPrefFloat(PrefGeneral.LAT_CENTER_KEY),
//                (double) PrefGeneral.getFromSharedPrefFloat(PrefGeneral.LON_CENTER_KEY)

        /*

        UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY)


        */

        emptyScreen.setVisibility(View.GONE);


        call.enqueue(this);

/*

        Log.d("applog",String.valueOf(endUser.getEndUserID()) + " "
        + UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY) + " "
        + UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY));
*/


        /*
        if(UtilityGeneral.isNetworkAvailable(this))
        {


        }
        else
        {

            showToastMessage("No network. Application is Offline !");
            swipeContainer.setRefreshing(false);

        }

        */

    }


    @Override
    public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

        if(isDestroyed)
        {
            return;
        }


        if(response.body()!=null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();

            if(response.body().size()==0)
            {
                emptyScreen.setVisibility(View.VISIBLE);
            }


        }
        else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();

            emptyScreen.setVisibility(View.VISIBLE);
        }




        swipeContainer.setRefreshing(false);

    }




    @Override
    public void onFailure(Call<List<CartStats>> call, Throwable t) {


        if(isDestroyed)
        {
            return;
        }


        emptyScreen.setVisibility(View.VISIBLE);

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);

    }





    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        onRefresh();
    }






    @Override
    public void onResume() {
        super.onResume();


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                adapter.notifyDataSetChanged();
            }
        });


        isDestroyed=false;

    }


    @Override
    public void onStop() {
        super.onStop();
        isDestroyed=true;
    }




    private void showLoginDialog()
    {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        LoginDialog loginDialog = new LoginDialog();
//        loginDialog.show(fm,"serviceUrl");


        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }


}
