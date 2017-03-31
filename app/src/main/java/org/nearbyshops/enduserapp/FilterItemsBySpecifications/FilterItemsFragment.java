package org.nearbyshops.enduserapp.FilterItemsBySpecifications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.ModelItemSpecs.EndPoints.ItemSpecNameEndPoint;
import org.nearbyshops.enduserapp.ModelItemSpecs.EndPoints.ItemSpecValueEndPoint;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationItem;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationValue;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemSpecItemService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemSpecNameService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemSpecValueService;
import org.nearbyshops.enduserapp.Shops.UtilityLocation;
import org.nearbyshops.enduserapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class FilterItemsFragment extends Fragment implements AdapterItemSpecName.NotificationsFromAdapterName, AdapterItemSpecValue.NotificationsFromAdapter {

    @Inject
    ItemSpecNameService itemSpecNameService;

    @Bind(R.id.recycler_view_names)
    RecyclerView recyclerViewName;

    AdapterItemSpecName adapterName;

    public List<ItemSpecificationName> datasetName = new ArrayList<>();

    GridLayoutManager layoutManagerName;


    Set<Integer> checkboxesList = new HashSet<>();


    @Inject
    ItemSpecItemService itemSpecItemService;



    @Inject
    ItemSpecValueService itemSpecValueService;

    @Bind(R.id.recycler_view_values)
    RecyclerView recyclerViewValues;

    AdapterItemSpecValue adapterValues;

    public List<ItemSpecificationValue> datasetValues = new ArrayList<>();

    GridLayoutManager layoutManagerValues;

    @Bind(R.id.progress_bar) ProgressBar progressBar;



    boolean isDestroyed;

    private int limit_name = 10;
    int offset_name = 0;
    int item_count_name = 0;


    private int limit_values = 50;
    int offset_values = 0;
    int item_count_values = 0;





    public FilterItemsFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_filter_items, container, false);
        ButterKnife.bind(this,rootView);


        setupRecyclerViewName();
        setupRecyclerViewValues();

        if(savedInstanceState==null)
        {

            boolean resetGetRowCountName = false;
            if(item_count_name == 0)
            {
                resetGetRowCountName=true;
            }

            makeNetworkCallName(true,true,resetGetRowCountName);

        }






        return rootView;
    }




    void setupRecyclerViewName()
    {

        adapterName = new AdapterItemSpecName(datasetName,this,getActivity(),this);

        recyclerViewName.setAdapter(adapterName);

        layoutManagerName = new GridLayoutManager(getActivity(),1);
        recyclerViewName.setLayoutManager(layoutManagerName);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL_LIST));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManagerName.setSpanCount(1);


        recyclerViewName.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManagerName.findLastVisibleItemPosition()==datasetName.size())
                {

                    if(offset_name + limit_name > layoutManagerName.findLastVisibleItemPosition())
                    {
                        return;
                    }

                    // trigger fetch next page

                    if((offset_name + limit_name) <= item_count_name)
                    {
                        offset_name = offset_name + limit_name;
                        makeNetworkCallName(false,false,false);
                    }

                }
            }
        });

    }




    void setupRecyclerViewValues()
    {

        adapterValues = new AdapterItemSpecValue(datasetValues,this,getActivity(),this);

        recyclerViewValues.setAdapter(adapterValues);

        layoutManagerValues = new GridLayoutManager(getActivity(),1);
        recyclerViewValues.setLayoutManager(layoutManagerValues);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL_LIST));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);


//        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

        layoutManagerValues.setSpanCount(1);


        recyclerViewValues.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManagerValues.findLastVisibleItemPosition()==datasetValues.size())
                {

                    if(offset_values + limit_values > layoutManagerValues.findLastVisibleItemPosition())
                    {
                        return;
                    }

                    // trigger fetch next page

                    if((offset_values+limit_values)<=item_count_values)
                    {
                        offset_values = offset_values + limit_values;
                        makeNetworkCallValues(false,false,false);
                    }

                }
            }
        });

    }



    boolean getRowCountName = true;

    int selectedItemNameID = 0;


    void makeNetworkCallName(final boolean clearDataset, boolean resetOffset, final boolean resetGetRowCount)
    {

        if(resetGetRowCount)
        {
            getRowCountName = true;
        }

        if(resetOffset)
        {
            offset_name = 0;
        }


        Call<List<ItemSpecificationName>> call = itemSpecNameService.getItemSpecsForFilters(
                null,null,
                UtilityLocation.getLatitude(getActivity()),
                UtilityLocation.getLongitude(getActivity()),null
        );



        call.enqueue(new Callback<List<ItemSpecificationName>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationName>> call, Response<List<ItemSpecificationName>> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {
                    datasetName.clear();
                }


                if(response.code()==200)
                {
                    datasetName.addAll(response.body());

//                    if(getRowCountName)
//                    {
//                        item_count_name = response.body().getItemCount();
//                    }
//
//                    getRowCountName=false;

                    adapterName.notifyDataSetChanged();
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationName>> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Failed !");
            }
        });



    }










    void initializeValuesList()
    {

        boolean resetGetRowCountValue = false;

//        if(item_count_values == 0)
//        {
//            resetGetRowCountValue=true;
//        }

        resetGetRowCountValue=true;

        makeNetworkCallValues(true,true, true);
    }




    boolean getRowCountValues = true;


    void makeNetworkCallValues(final boolean clearDataset, boolean resetOffset, final boolean resetGetRowCount)
    {

        if(resetGetRowCount)
        {
            getRowCountValues = true;
        }

        if(resetOffset)
        {
            offset_values = 0;
        }

//        itemSpecNameID = getActivity().getIntent().getIntExtra(ITEM_SPEC_NAME_INTENT_KEY,0);

        progressBar.setVisibility(View.VISIBLE);

        Call<List<ItemSpecificationValue>> call = itemSpecValueService.getItemSpecsForFilters(
                selectedItemNameID,null,null,
                UtilityLocation.getLatitude(getActivity()),
                UtilityLocation.getLongitude(getActivity()),
                null
        );


        call.enqueue(new Callback<List<ItemSpecificationValue>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationValue>> call, Response<List<ItemSpecificationValue>> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {
                    datasetValues.clear();
                }


                if(response.code()==200)
                {
                    datasetValues.addAll(response.body());
//
//                    if(getRowCountValues)
//                    {
//                        item_count_values = response.body();
//                    }

                    getRowCountValues=false;
                    adapterValues.notifyDataSetChanged();
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationValue>> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);

                showToastMessage("Failed !");
            }
        });
    }














    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }








    @Override
    public void listItemClick(ItemSpecificationName itemSpecName, int position) {

        selectedItemNameID = itemSpecName.getId();
        initializeValuesList();

        int itemID = getActivity().getIntent().getIntExtra(ITEM_ID_INTENT_KEY,0);

        Call<List<ItemSpecificationItem>> call = itemSpecItemService.getItemSpecName(
        itemSpecName.getId(), itemID
        );

        progressBar.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<List<ItemSpecificationItem>>() {

            @Override
            public void onResponse(Call<List<ItemSpecificationItem>> call, Response<List<ItemSpecificationItem>> response) {

                if(response.code()==200 && response.body()!=null)
                {

                    checkboxesList.clear();

                    for(ItemSpecificationItem item: response.body())
                    {
                        checkboxesList.add(item.getItemSpecValueID());
                    }


                    adapterValues.notifyDataSetChanged();
                }


                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ItemSpecificationItem>> call, Throwable t) {

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
    public void listItemClick(ItemSpecificationValue itemSpecValue, int position) {

    }


    public static final String ITEM_ID_INTENT_KEY = "item_id_intent_key";


    @Override
    public void deleteItemSpecItem(int itemSpecValueID) {

        int itemID = getActivity().getIntent().getIntExtra(ITEM_ID_INTENT_KEY,0);

        Call<ResponseBody> call = itemSpecItemService.deleteItemSpecItem(UtilityLogin.getAuthorizationHeaders(getActivity()),itemSpecValueID,itemID);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200)
                {
                    showToastMessage("Removed !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




    @Override
    public void insertItemSpecItem(int ItemSpecValueID) {

        ItemSpecificationItem itemSpecificationItem = new ItemSpecificationItem();

        itemSpecificationItem.setItemID(getActivity().getIntent().getIntExtra(ITEM_ID_INTENT_KEY,0));
        itemSpecificationItem.setItemSpecValueID(ItemSpecValueID);


        Call<ResponseBody> call = itemSpecItemService.saveItemSpecName(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemSpecificationItem
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==201)
                {
                    showToastMessage("Added !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}
