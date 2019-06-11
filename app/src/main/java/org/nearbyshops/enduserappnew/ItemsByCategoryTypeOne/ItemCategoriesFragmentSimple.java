package org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne;

import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
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
import com.wunderlist.slidinglayer.SlidingLayer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.API.ItemService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.Interfaces.NotifyHeaderChanged;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ModelUtility.HeaderItemsList;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.SlidingLayerSort.SlidingLayerSortItems;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.SlidingLayerSort.UtilitySortItemsByCategory;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.nearbyshops.enduserappnew.ItemsInShopByCategory.ItemsInShopByCat.TAG_SLIDING;

/**
 * Created by sumeet on 2/12/16.
 */





public class ItemCategoriesFragmentSimple extends Fragment implements
        LocationUpdated,
        SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategory.ListItemClick, NotifyBackPressed, NotifySort, NotifySearch {




    boolean isDestroyed = false;

    int item_count_item_category = 0;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item;
    int fetched_items_count = 0;

    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
    ArrayList<Item> datasetItems = new ArrayList<>();


    GridLayoutManager layoutManager;
    AdapterSimple listAdapter;

    @Inject
    ItemCategoryService itemCategoryService;

    @Inject
    ItemService itemService;

    @BindView(R.id.shop_count_indicator) TextView itemHeader;
    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;


    ItemCategory currentCategory = null;


    private static final int REQUEST_CODE_ASK_PERMISSION = 55;

    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.progress_bar_fetching_location) LinearLayout progressBarFetchingLocation;


    @BindView(R.id.service_name) TextView serviceName;




    public ItemCategoriesFragmentSimple() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_categories_simple, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle("Nearby Shops");
//        toolbar.setTitle(getString(R.string.app_name));






        if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
        {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }
        else
        {
            serviceName.setVisibility(View.GONE);
        }








        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();
        notifyItemHeaderChanged();


        setupSlidingLayer();



//        getActivity().startService(new Intent(getActivity(),LocationUpdateServiceLocal.class));
//        requestLocationUpdates();

        return rootView;
    }









    @OnClick(R.id.toolbar)
    void toolbarClicked()
    {
//        showToastMessage("Toolbar Clicked !");


//        if(PrefGeneral.getMultiMarketMode(getActivity()))
//        {
//            if(getActivity().getSupportFragmentManager().findFragmentByTag(TAG_MARKET_FRAGMENT)==null)
//            {
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container,new MarketsFragment(),TAG_MARKET_FRAGMENT)
//                        .commit();
//            }
//
//
////            .addToBackStack("select_market")
//
//        }


//        startActivity(new Intent(getActivity(),ServicesActivity.class));


        if(getActivity() instanceof ShowFragment)
        {
            ((ShowFragment) getActivity()).showProfileFragment();
        }
    }





    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(30);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);



            if(getChildFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
            {
//                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems(),TAG_SLIDING)
                        .commit();
            }
        }

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }




    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }





//    @OnClick({R.id.icon_filter,R.id.text_filter})
    void filterClick()
    {
//        Intent intent = new Intent(getActivity(), FilterItemsActivity.class);
//        intent.putExtra("ItemCatID",currentCategory.getItemCategoryID());
//        startActivityForResult(intent,123);
    }



    void setupSwipeContainer()
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





    void setupRecyclerView()
    {

        listAdapter = new AdapterSimple(dataset,getActivity(),this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, RecyclerView.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {



                if(position == dataset.size())
                {

                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                    final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderItemsList)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        /*final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        return (3/spanCount);*/


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /*if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
*/


                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }



                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {

                    // trigger fetch next page

//
//                    String log = "Dataset Size : " + String.valueOf(dataset.size()) + "\n"
//                            + "Last Visible Item Position : " + layoutManager.findLastVisibleItemPosition() + "\n"
//                            + "Previous Position : " + previous_position + "\n"
//                            + "Offset Item : " + offset_item + "\n"
//                            + "Limit Item : " + limit_item + "\n"
//                            + "Item Count Item : " + item_count_item;

//                    System.out.println(log);
//                    Log.d("log_scroll",log);




//                    if(layoutManager.findLastVisibleItemPosition()== previous_position)
//                    {
//                        return;
//                    }


                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;


//                        Log.d("item_requests","Item Fetched from API");

                        makeRequestItem(false,false);
                    }


//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }
            }

        });

    }


    int previous_position = -1;


    void resetPreviousPosition()
    {
        previous_position = -1;
    }



    @Override
    public void onRefresh() {




        emptyScreen.setVisibility(View.GONE);

        makeRequestItemCategory();
        makeRequestItem(true,true);
    }



    void makeRefreshNetworkCall()
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
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }




    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
        EventBus.getDefault().register(this);
    }




    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }








    boolean isFirst = true;

    void makeRequestItemCategory()
    {



//
//        if(searchQuery!=null)
//        {
//            return;
//        }


//        (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY, 0),
//                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY, 0),




//        showToastMessage("Lat : " + String.valueOf(PrefLocation.getLatitude(getActivity()))  + " : "  + String.valueOf(PrefLocation.getLongitude(getActivity())));




        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,
                currentCategory.getItemCategoryID(),
                null,
                PrefLocation.getLatitude(MyApplication.getAppContext()), PrefLocation.getLongitude(MyApplication.getAppContext()),
                null,null,null,
                true,
                ItemCategory.CATEGORY_ORDER,null,null,false);





        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    ItemCategoryEndPoint endPoint = response.body();
                    item_count_item_category = endPoint.getItemCount();

                    datasetCategory.clear();
                    datasetCategory.addAll(endPoint.getResults());
                }


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }


//                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }



//                if(swipeContainer!=null)
//                {
//                    swipeContainer.setRefreshing(false);
//                }

            }
        });
    }









    void refreshAdapter() {
        dataset.clear();



        HeaderTitle headerItemCategory = new HeaderTitle();


        if(datasetCategory.size()>0)
        {

            if (searchQuery == null) {


                if (currentCategory.getParentCategoryID() == -1) {
                    headerItemCategory.setHeading("Item Categories");
                } else {
                    headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
                }

                dataset.add(headerItemCategory);

                dataset.addAll(datasetCategory);
            }


//            dataset.addAll(datasetCategory);
        }





            HeaderTitle headerItem = new HeaderTitle();



//            if(currentCategory.getParentCategoryID()!=-1)
//            {
//
//            }


            if(searchQuery==null)
            {


                if(datasetItems.size()>0)
                {
                    headerItem.setHeading(currentCategory.getCategoryName() + " Items");
                }
                else
                {
                    headerItem.setHeading("No Items in this category");
                }

            }
            else
            {
                if(datasetItems.size()>0)
                {
                    headerItem.setHeading("Search Results");
                }
                else
                {
                    headerItem.setHeading("No items for the given search !");
                }

            }




        if(datasetCategory.size()==0 && datasetItems.size()==0)
        {
            emptyScreen.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyScreen.setVisibility(View.GONE);

            dataset.add(headerItem);
        }


        dataset.addAll(datasetItems);




        listAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }




    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }




    void makeRequestItem(final boolean clearDatasetLocal, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }



//        (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
//                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),



        String current_sort = "";

        current_sort = UtilitySortItemsByCategory.getSort(getActivity()) + " " + UtilitySortItemsByCategory.getAscending(getActivity());

        Call<ItemEndPoint> endPointCall = null;



        if(searchQuery==null)
        {
            endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
                    null,false,
                    PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
                    null,
                    null,null, null, searchQuery,
                    current_sort, limit_item,offset_item,clearDatasetLocal,false);

        }
        else
        {

            endPointCall = itemService.getItemsEndpoint(null,
                    null,false,
                    PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
                    null,
                    null,null, null, searchQuery,
                    current_sort, limit_item,offset_item,clearDatasetLocal,false);

        }






        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(clearDatasetLocal)
                {


                    if(response.body()!=null)
                    {

                        datasetItems.clear();
                        datasetItems.addAll(response.body().getResults());

//                        fetched_items_count = fetched_items_count + response.body().getResults().size();
//                        item_count_item = response.body().getItemCount();

                        item_count_item = response.body().getItemCount();
                        fetched_items_count = datasetItems.size();

//                        if(response.body().getItemCount()!=null)
//                        {
//
//                        }
                    }


                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }

                }
                else
                {
                    if(response.body()!=null)
                    {

                        dataset.addAll(response.body().getResults());
                        fetched_items_count = fetched_items_count + response.body().getResults().size();
//                        item_count_item = response.body().getItemCount();
                        listAdapter.notifyDataSetChanged();
                    }

                    swipeContainer.setRefreshing(false);
                }




                if(offset_item+limit_item >= item_count_item)
                {
                    listAdapter.setLoadMore(false);
                }
                else
                {
                    listAdapter.setLoadMore(true);
                }



                notifyItemHeaderChanged();

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }



                if(clearDatasetLocal)
                {

                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }
                }
                else
                {
                    swipeContainer.setRefreshing(false);
                }


                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }





    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        resetPreviousPosition();
    }




    boolean backPressed = false;

    @Override
    public boolean backPressed() {

        // previous position is a variable used for tracking scrolling
        resetPreviousPosition();

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
                makeRefreshNetworkCall();
            }
        }



        return currentCategoryID == -1;
    }









    void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyHeaderChanged)
        {
            ((NotifyHeaderChanged) getActivity()).notifyItemHeaderChanged(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Items");
        }




        if(currentCategory.getItemCategoryID()==1)
        {
            itemHeader.setText(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " Items");
        }
        else
        {
            itemHeader.setText(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName());
        }
//        + " Items"

    }




    @Override
    public void notifySortChanged() {

        makeRefreshNetworkCall();
    }







    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }







    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(Location location) {

        showToastMessage("Location Updated !");
        makeRefreshNetworkCall();
    }






//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void permissionGranted(LocationPermissionGranted granted) {
//
////        showToastMessage("Granted event bus !");
////        requestLocationUpdates();
//    }















    @Override
    public void permissionGranted() {
//        showToastMessage("Granted interface !");
//        requestLocationUpdates();
    }






    @Override
    public void locationUpdated() {
        makeRefreshNetworkCall();
    }





    String searchQuery = null;

    @Override
    public void search(final String searchString) {

//        showToastMessage("Query : " + searchString);
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {

//        showToastMessage("Search Collapsed !");
        searchQuery = null;
        makeRefreshNetworkCall();
    }
}
