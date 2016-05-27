package org.nearbyshops.enduser.ItemCategories;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.DataRouters.ItemCategoryDataRouter;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataSubscriber;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemCategories extends AppCompatActivity implements  ItemCategoriesAdapter.requestSubCategory,DataSubscriber<ItemCategory>{

    int currentCategoryID = 1; // the ID of root category is always supposed to be 1
    ItemCategory currentCategory = null;


    List<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    ItemCategoriesAdapter listAdapter;

    GridLayoutManager layoutManager;

    Shop shop = null;

    @Bind(R.id.tablayout) TabLayout tabLayout;

    @Inject
    ItemCategoryDataRouter dataRouter;

    boolean isRootCategory = true;
    ArrayList<String> categoryTree = new ArrayList<>();


    public ItemCategories() {
        super();


        DaggerComponentBuilder.getInstance()
                .getDataComponent()
                .Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);

    }

    SlidingLayer slidingLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = (RecyclerView) findViewById(R.id.recyclerViewItemCategories);
        listAdapter = new ItemCategoriesAdapter(dataset,this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(this,1);
        itemCategoriesList.setLayoutManager(layoutManager);

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);



        layoutManager.setSpanCount(metrics.widthPixels/350);


        if (metrics.widthPixels >= 600 && (
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT
        )){

        }


        // setup Sliding Layer

        slidingLayer = (SlidingLayer)findViewById(R.id.slidingLayer);

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);
        //slidingLayer.setOffsetDistanceRes(R.dimen.offset_distance);
        //slidingLayer.setPreviewOffsetDistanceRes(R.dimen.preview_offset_distance);
        //slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer.setChangeStateOnTap(true);
        slidingLayer.setSlidingEnabled(true);
        slidingLayer.setPreviewOffsetDistance(50);
        slidingLayer.setOffsetDistance(20);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);


    }


    void insertTab(String categoryName)
    {
        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText(" // " + categoryName + " "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

    }

    void removeLastTab()
    {

        tabLayout.removeTabAt(tabLayout.getTabCount()-1);
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

        if(tabLayout.getTabCount()==0)
        {
            tabLayout.setVisibility(View.GONE);
        }
    }


    void makeNetworkRequest()
    {
        dataRouter.getDataProvider().readMany(
                currentCategoryID,
                0,
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),
                this);
    }



    void notifyDelete()
    {
        dataset.clear();

        makeNetworkRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO
        // null pointer exception : Error Prone
        dataset.clear();
        makeNetworkRequest();
    }


    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;

        currentCategory = itemCategory;

        currentCategoryID = itemCategory.getItemCategoryID();

        currentCategory.setParentCategory(temp);


        categoryTree.add(new String(currentCategory.getCategoryName()));

        insertTab(currentCategory.getCategoryName());



        if(isRootCategory == true) {

            isRootCategory = false;

        }else
        {
            boolean isFirst = true;
        }

        makeNetworkRequest();
    }



    @Override
    public void onBackPressed() {

        if(currentCategory!=null)
        {

            if(categoryTree.size()>0) {

                categoryTree.remove(categoryTree.size() - 1);
                removeLastTab();
            }


            if(currentCategory.getParentCategory()!= null) {

                currentCategory = currentCategory.getParentCategory();

                currentCategoryID = currentCategory.getItemCategoryID();

            }
            else
            {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if(currentCategoryID!=-1)
            {
                makeNetworkRequest();
            }
        }

        if(currentCategoryID == -1)
        {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @Override
    public void createCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, ItemCategory itemCategory) {

    }

    @Override
    public void readCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, ItemCategory itemCategory) {

    }

    @Override
    public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<ItemCategory> list) {


        if (!isOffline) {

            if (isSuccessful) {
                dataset.clear();

                if (list != null) {

                    dataset.addAll(list);
                }

                listAdapter.notifyDataSetChanged();

            } else {// request failed

                showToastMessage("Network Request Failed !!");

            }

        }
        else {

            if(!isSuccessful)
            {
                showToastMessage("Application is Offline ! No Network !");
            }
        }

    }

    @Override
    public void updateCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

    }

    @Override
    public void deleteShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
