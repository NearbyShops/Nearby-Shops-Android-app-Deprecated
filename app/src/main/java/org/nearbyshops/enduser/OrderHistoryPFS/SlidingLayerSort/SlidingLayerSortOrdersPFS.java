package org.nearbyshops.enduser.OrderHistoryPFS.SlidingLayerSort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.nearbyshops.enduser.ModelPickFromShop.OrderPFS;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopsByCategoryOld.Interfaces.NotifySort;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortOrdersPFS extends Fragment {

    @Bind(R.id.sort_distance) TextView sort_by_distance;
    @Bind(R.id.sort_date_time) TextView sort_by_date_time;
    @Bind(R.id.order_status) TextView sort_by_status;
    @Bind(R.id.pincode) TextView sort_by_pincode;


    @Bind(R.id.sort_ascending) TextView sort_ascending;
    @Bind(R.id.sort_descending) TextView sort_descending;

    String currentSort = SORT_BY_DISTANCE;
    String currentAscending = SORT_DESCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


    public static String SORT_BY_DISTANCE = "distance";
    public static String SORT_BY_DATE_TIME = OrderPFS.TIMESTAMP_PLACED;
    public static String SORT_BY_STATUS = OrderPFS.STATUS_PICK_FROM_SHOP;
    public static String SORT_BY_PINCODE = DeliveryAddress.PINCODE;

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_orders_pfs,container,false);
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

        currentSort = UtilitySortOrdersPFS.getSort(getActivity());
        currentAscending = UtilitySortOrdersPFS.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_DISTANCE))
        {
            sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_DATE_TIME))
        {
            sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_PINCODE))
        {
            sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_STATUS))
        {
            sort_by_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

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



    @OnClick(R.id.sort_distance)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortOrdersPFS.saveSort(getActivity(), SORT_BY_DISTANCE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_date_time)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortOrdersPFS.saveSort(getActivity(), SORT_BY_DATE_TIME);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.order_status)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortOrdersPFS.saveSort(getActivity(), SORT_BY_STATUS);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.pincode)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortOrdersPFS.saveSort(getActivity(), SORT_BY_PINCODE);

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


        UtilitySortOrdersPFS.saveAscending(getActivity(),SORT_ASCENDING);

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


        UtilitySortOrdersPFS.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}
