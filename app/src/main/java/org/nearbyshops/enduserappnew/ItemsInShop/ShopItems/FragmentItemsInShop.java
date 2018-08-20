package org.nearbyshops.enduserappnew.ItemsInShop.ShopItems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemDetail.ItemDetail;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Utility.PrefShopHome;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.SlidingLayerSort.UtilitySortItemsInShop;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentItemsInShop extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, NotifySort,AdapterItemsInShop.NotifyItemsInShopFragment{




        Shop shop;

        ArrayList<ShopItem> dataset = new ArrayList<>();

        boolean isSaved;


        @Inject
        ShopItemService shopItemService;

        RecyclerView recyclerView;
        AdapterItemsInShop adapter;
        GridLayoutManager layoutManager;

        SwipeRefreshLayout swipeContainer;


        boolean isbackPressed = false;


        final private int limit = 10;
        int offset = 0;
        int item_count = 0;


        boolean isDestroyed;


        @BindView(R.id.itemsInCart)
        public TextView itemsInCart;

        @BindView(R.id.cartTotal)
        public TextView cartTotal;



        public FragmentItemsInShop() {
            // inject dependencies through dagger
            DaggerComponentBuilder.getInstance()
                    .getNetComponent().Inject(this);

        }


        public static FragmentItemsInShop newInstance() {

            FragmentItemsInShop fragment = new FragmentItemsInShop();
            Bundle args = new Bundle();
//            args.putParcelable("itemCat",itemCategory);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            setRetainInstance(true);

            View rootView = inflater.inflate(R.layout.fragment_items_in_shop, container, false);

            ButterKnife.bind(this,rootView);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);


                if(savedInstanceState==null)
                {

                    // ensure that there is no swipe to right on first fetch
                    isbackPressed = true;
                    makeRefreshNetworkCall();
                    isSaved = true;

                }
//                else
//                {
//                    Log.d("shopsbycategory","saved State");
//                    onViewStateRestored(savedInstanceState);
//                }


            setupRecyclerView();
            setupSwipeContainer();
            notifyTitleChanged();

            if(shop==null)
            {
                shop = PrefShopHome.getShop(getActivity());
            }

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

            adapter = new AdapterItemsInShop(dataset,getActivity(),this,this);

            recyclerView.setAdapter(adapter);

            layoutManager = new GridLayoutManager(getActivity(),1);
            recyclerView.setLayoutManager(layoutManager);

/*
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
            );

            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
            );
*/

            //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//            layoutManager.setSpanCount(metrics.widthPixels/350);


            int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

            if(spanCount==0){
                spanCount = 1;
            }

            layoutManager.setSpanCount(spanCount);


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);


                    if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                    {

//                        if(dataset.size()== previous_position)
//                        {
//                            return;
//                        }


                        if(offset + limit > layoutManager.findLastVisibleItemPosition())
                        {
                            return;
                        }


                        // trigger fetch next page

                        if((offset+limit)<=item_count)
                        {
                            offset = offset + limit;
                            makeNetworkCall(false,false);
                        }

//                        previous_position = dataset.size();

                    }
                }
            });
        }



//    int previous_position = -1;






    private void makeRefreshNetworkCall() {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {

//                        offset = 0; // reset the offset
//                        dataset.clear();2
                        makeNetworkCall(true,true);

                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });

        }



        @Override
        public void onRefresh() {

//            offset = 0; // reset the offset
//            dataset.clear();
            makeNetworkCall(true,true);
        }







        private void makeNetworkCall(final boolean clearDataset, boolean resetOffset)
        {

            if(resetOffset)
            {
                offset = 0;
            }

            String current_sort = "";
            current_sort = UtilitySortItemsInShop.getSort(getContext())
                            + " " + UtilitySortItemsInShop.getAscending(getContext());

            Call<ShopItemEndPoint> shopItemCall = shopItemService.getShopItemEndpoint(
                    null,shop.getShopID(),
                    null,null,null,
                    null,null,null,
                    null,null,null,null,
                    null, null,
                    null,true,current_sort,
                    limit,offset,null,
                    true
            );



            shopItemCall.enqueue(new Callback<ShopItemEndPoint>() {
                @Override
                public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    //                dataset.clear();

                    if(response.body()!=null)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();
                        }
                        dataset.addAll(response.body().getResults());

                        if(response.body().getItemCount()!=null)
                        {
                            item_count = response.body().getItemCount();
                        }

                    }


                    notifyTitleChanged();

                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage(getString(R.string.network_request_failed));
                    swipeContainer.setRefreshing(false);

                }
            });

        }



        void showToastMessage(String message)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }





    // apply ice pack


//        @Override
//        public void onSaveInstanceState(Bundle outState) {
//            super.onSaveInstanceState(outState);
//            Icepick.saveInstanceState(this, outState);
//        }



//        @Override
//        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//            super.onViewStateRestored(savedInstanceState);
//
//            Icepick.restoreInstanceState(this, savedInstanceState);
//            notifyTitleChanged();
//        }


        /*@Override
        public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {
//            dataset.clear();
            offset = 0 ; // reset the offset
            makeNetworkCall(true);
            this.isbackPressed = isBackPressed;
        }
*/


        void notifyTitleChanged()
        {
            String name = "";

            if(getActivity() instanceof NotifyTitleChanged)
            {

                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged( "Displaying "+
                                String.valueOf(dataset.size())
                                + " out of " + String.valueOf(item_count) + " Items",1);
            }

        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }



    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }


    public int getItemCount() {
        return item_count;
    }



    @Override
    public void notifyItemImageClick(Item item) {

        Intent intent = new Intent(getActivity(), ItemDetail.class);
        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);
        getActivity().startActivity(intent);
    }


}
