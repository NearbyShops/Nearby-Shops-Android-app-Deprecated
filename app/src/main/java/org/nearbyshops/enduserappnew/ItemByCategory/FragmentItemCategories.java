package org.nearbyshops.enduserappnew.ItemByCategory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.ItemByCategory.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemByCategory.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
//import icepick.Icepick;
//import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentItemCategories extends Fragment implements  AdapterItemCategories.requestSubCategory, NotifyBackPressed, SwipeRefreshLayout.OnRefreshListener {

//    @State int currentCategoryID = 1; // the ID of root category is always supposed to be 1
    ItemCategory currentCategory = null;

    boolean isSaved = false;


    ArrayList<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    AdapterItemCategories listAdapter;

    GridLayoutManager layoutManager;

    Shop shop = null;


//    @Inject
//    ItemCategoryDataRouter dataRouter;

    @Inject
    ItemCategoryService itemCategoryService;

//    boolean isRootCategory = true;
//    ArrayList<String> categoryTree = new ArrayList<>();


    // Interface references

    NotifyCategoryChanged notifyCategoryChanged;
    NotifyTitleChanged notifyTitleChanged;

    // References Ends


    public FragmentItemCategories() {
        super();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);

        Log.d("applog","ItemCategory Fragment Constructor");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setRetainInstance(true);
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_item_categories, container, false);

        ButterKnife.bind(this, rootView);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = (RecyclerView) rootView.findViewById(R.id.recyclerViewItemCategories);

        setupSwipeContainer();
        setupRecyclerView();

        if(!isSaved)
        {
            makeRefreshNetworkCall();
            Log.d("applog","ItemCategories : MakeRequest: SavedInstanceState ");

            isSaved = true;
        }


        if (getActivity() instanceof ShopItemSwipeView) {
            ((ShopItemSwipeView) getActivity()).setNotifyBackPressed(this);
        }


        if (getActivity() instanceof NotifyCategoryChanged) {

            notifyCategoryChanged = (NotifyCategoryChanged) getActivity();


//            notifyCategoryChanged.categoryChanged(currentCategory,false);

        }

        if(getActivity() instanceof NotifyTitleChanged)
        {
            notifyTitleChanged = (NotifyTitleChanged) getActivity();
        }

        return rootView;
    }




    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    void setupSwipeContainer() {

        if (swipeContainer != null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }



    private int limit = 30;
    int offset = 0;
    int item_count = 0;


    void setupRecyclerView()
    {
        listAdapter = new AdapterItemCategories(dataset, getActivity(), this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        itemCategoriesList.setLayoutManager(layoutManager);

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        layoutManager.setSpanCount(metrics.widthPixels / 350);



        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkRequest();

                        Log.d("applog","ItemCategories : MakeRequest:RecyclerView ");
                    }

                }
            }
        });

    }




    void makeNetworkRequest() {



        final Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,currentCategory.getItemCategoryID(),
                null,
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity()),
                null,
                null,
                null,
                true, "id",limit,offset,false);

        /*Call<List<ItemCategory>> call = itemCategoryService.getItemCategories(currentCategory.getItemCategoryID(), null,
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY, 0));
*/


        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {


                if(response.body()!=null)
                {
                    ItemCategoryEndPoint endPoint = response.body();

                    item_count = endPoint.getItemCount();

                    dataset.addAll(endPoint.getResults());

                    Log.d("applog","Item Count : ItemCategories " + String.valueOf(item_count) + " : " + endPoint.getResults().size());

                    if(notifyTitleChanged !=null)
                    {
                        notifyTitleChanged.titleChanged(0,dataset.size(),item_count);
                    }


                }


                listAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");
                swipeContainer.setRefreshing(false);


            }
        });
    }


    void notifyDelete() {

        makeRefreshNetworkCall();
    }



    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;

        currentCategory = itemCategory;

//        currentCategoryID = itemCategory.getItemCategoryID();

        currentCategory.setParentCategory(temp);

//        categoryTree.add(currentCategory.getCategoryName());
//

//        if (isRootCategory) {
//
//            isRootCategory = false;
//
//        } else {
//            boolean isFirst = true;
//        }


        if (notifyCategoryChanged != null) {

            notifyCategoryChanged.categoryChanged(currentCategory,false);
        }



        if (!currentCategory.getisAbstractNode()) {

            notifyCategoryChanged.notifySwipeToRight();
        }

        makeRefreshNetworkCall();

    }



    void makeRefreshNetworkCall() {

        // clear the dataset and reload everything

        if(swipeContainer==null)
        {
            return;
        }

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    Log.d("applog","ItemCategories : MakeRequest: RefreshNetworkCall ");

                    // make a network call
                    dataset.clear();
                    offset = 0; // reset the offset
                    makeNetworkRequest();


                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();

                }
            }
        });
    }



    @Override
    public void onRefresh() {

        Log.d("applog","ItemCategories : MakeRequest: onRefresh ");

        dataset.clear();
        offset = 0;
        makeNetworkRequest();
    }




    void showToastMessage(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public boolean backPressed() {


        int currentCategoryID = -5;


        if (currentCategory != null) {

//            if (categoryTree.size() > 0) {
//
//                categoryTree.remove(categoryTree.size() - 1);
//
//            }


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();

                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {


                if (notifyCategoryChanged != null) {
                    notifyCategoryChanged.categoryChanged(currentCategory,true);
                }

                makeRefreshNetworkCall();
            }
        }

        if (currentCategoryID == -1) {

            return true;

        } else {

            return false;
        }
    }





//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Icepick.saveInstanceState(this, outState);
//        outState.putParcelableArrayList("dataset",dataset);
//    }



//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//
//        Icepick.restoreInstanceState(this, savedInstanceState);
//
//        if (savedInstanceState != null) {
//
//            ArrayList<ItemCategory> tempList = savedInstanceState.getParcelableArrayList("dataset");
//
//            dataset.clear();
//            dataset.addAll(tempList);
//
//
//
//            if(notifyTitleChanged !=null)
//            {
//                notifyTitleChanged.titleChanged(0,dataset.size(),item_count);
//            }
//
//
//            listAdapter.notifyDataSetChanged();
//        }
//
//    }



}
