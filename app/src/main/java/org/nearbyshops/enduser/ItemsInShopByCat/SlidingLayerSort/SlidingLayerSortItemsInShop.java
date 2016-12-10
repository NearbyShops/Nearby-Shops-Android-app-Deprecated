package org.nearbyshops.enduser.ItemsInShopByCat.SlidingLayerSort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortItemsInShop extends Fragment {

    @Bind(R.id.sort_rating)
    TextView sort_by_rating;

    @Bind(R.id.sort_popularity)
    TextView sort_by_popularity;

    @Bind(R.id.a_to_z)
    TextView sort_by_shop_count;

    @Bind(R.id.item_price)
    TextView sort_by_price_avg;




    @Bind(R.id.sort_ascending)
    TextView sort_ascending;

    @Bind(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_ITEM_RATING;
    String currentAscending = SORT_DESCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


    public static String SORT_BY_ITEM_RATING = "avg_rating";
    public static String SORT_BY_POPULARITY = "popularity";
    public static String SORT_BY_SHOP_COUNT = Item.ITEM_NAME;
    public static String SORT_BY_AVG_PRICE = ShopItem.ITEM_PRICE;

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_shop_items_by_shop,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    void loadDefaultSort() {
//        String[] sort_options = UtilitySortShops.getSort(getActivity());

        currentSort = UtilitySortItemsInShop.getSort(getActivity());
        currentAscending = UtilitySortItemsInShop.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_ITEM_RATING))
        {
            sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_POPULARITY))
        {
            sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_AVG_PRICE))
        {
            sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_SHOP_COUNT))
        {
            sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }



        if(currentAscending.equals(SORT_ASCENDING))
        {
            sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
        else if(currentAscending.equals(SORT_DESCENDING))
        {
            sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
    }



    @OnClick(R.id.sort_rating)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInShop.saveSort(getActivity(),SORT_BY_ITEM_RATING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_popularity)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInShop.saveSort(getActivity(),SORT_BY_POPULARITY);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.a_to_z)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInShop.saveSort(getActivity(),SORT_BY_SHOP_COUNT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.item_price)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInShop.saveSort(getActivity(),SORT_BY_AVG_PRICE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }






    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortItemsInShop.saveAscending(getActivity(),SORT_ASCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortItemsInShop.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}
