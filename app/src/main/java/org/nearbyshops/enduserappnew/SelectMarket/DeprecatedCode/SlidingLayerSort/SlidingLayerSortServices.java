package org.nearbyshops.enduserappnew.SelectMarket.DeprecatedCode.SlidingLayerSort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortServices extends Fragment {

    @BindView(R.id.sort_service_range) TextView sort_by_service_range;
    @BindView(R.id.sort_created) TextView sort_by_created;
    @BindView(R.id.updated) TextView sort_by_updated;
    @BindView(R.id.service_name) TextView sort_by_service_name;


    @BindView(R.id.sort_ascending) TextView sort_ascending;
    @BindView(R.id.sort_descending) TextView sort_descending;

    @BindView(R.id.filter_official) TextView filterOfficial;
    @BindView(R.id.filter_verified) TextView filterVerified;

    @BindView(R.id.filter_nonprofit) TextView filterNonprofit;
    @BindView(R.id.filter_government) TextView filterGovernment;
    @BindView(R.id.filter_commertial) TextView filterCommertial;

    String currentSort = SORT_BY_CREATED;
    String currentAscending = SORT_DESCENDING;
    int currentServiceType = SERVICE_TYPE_NONPROFIT;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;
    int colorSelectedFilterOfficialVerified = R.color.phonographyBlue;
    int colorSelectedFilterByType = R.color.buttonColorDark;


    public static String SORT_BY_SERVICE_RANGE = ServiceConfigurationGlobal.SERVICE_RANGE;
    public static String SORT_BY_CREATED = ServiceConfigurationGlobal.CREATED;
    public static String SORT_BY_UPDATED = ServiceConfigurationGlobal.UPDATED;
    public static String SORT_BY_SERVICE_NAME = ServiceConfigurationGlobal.SERVICE_NAME;

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";

    public static int SERVICE_TYPE_NONPROFIT = 1;
    public static int SERVICE_TYPE_GOVERNMENT = 2;
    public static int SERVICE_TYPE_COMMERTIAL = 3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_services,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }





    void loadDefaultSort() {
//        String[] sort_options = UtilitySortShops.getSort(getActivity());

//        currentSort = UtilitySortServices.getSort(getActivity());
//        currentAscending = UtilitySortServices.getAscending(getActivity());

        currentSort = UtilitySortServices.getSort(getActivity());
        currentAscending = UtilitySortServices.getAscending(getActivity());
        currentServiceType = UtilitySortServices.getServiceType(getActivity());




        clearSelectionSort();
        clearSelectionAscending();
        clearSelectionFilterByType();


        if (currentSort.equals(SORT_BY_SERVICE_RANGE))
        {
            sort_by_service_range.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_service_range.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_CREATED))
        {
            sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_SERVICE_NAME))
        {
            sort_by_service_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_service_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_UPDATED))
        {
            sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

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



        if(currentServiceType==SERVICE_TYPE_NONPROFIT)
        {
            filterNonprofit.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterNonprofit.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));
        }
        else if(currentServiceType==SERVICE_TYPE_GOVERNMENT)
        {
            filterGovernment.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterGovernment.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));
        }
        else if(currentServiceType==SERVICE_TYPE_COMMERTIAL)
        {
            filterCommertial.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterCommertial.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));
        }


        if(UtilitySortServices.getVerified(getActivity()))
        {
            filterVerified.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterVerified.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));
        }
        else
        {
            filterVerified.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            filterVerified.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        }



        if(UtilitySortServices.getOfficial(getActivity()))
        {
            filterOfficial.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterOfficial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));
        }
        else
        {
            filterOfficial.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            filterOfficial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        }
    }



    @OnClick(R.id.sort_service_range)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_service_range.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_service_range.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortServices.saveSort(getActivity(), SORT_BY_SERVICE_RANGE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_created)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortServices.saveSort(getActivity(), SORT_BY_CREATED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.updated)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortServices.saveSort(getActivity(), SORT_BY_UPDATED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.service_name)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_service_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_service_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortServices.saveSort(getActivity(), SORT_BY_SERVICE_NAME);

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


        UtilitySortServices.saveAscending(getActivity(),SORT_ASCENDING);

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


        UtilitySortServices.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.filter_official)
    void filterOfficialClick(View view)
    {
        if(UtilitySortServices.getOfficial(getActivity()))
        {
            UtilitySortServices.saveOfficial(getActivity(),false);
            filterOfficial.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            filterOfficial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        }
        else
        {
            UtilitySortServices.saveOfficial(getActivity(),true);
            filterOfficial.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterOfficial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));
        }


        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.filter_verified)
    void filterVerifiedClick(View view)
    {
        if(UtilitySortServices.getVerified(getActivity()))
        {
            UtilitySortServices.saveVerified(getActivity(),false);
            filterVerified.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            filterVerified.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        }
        else
        {
            UtilitySortServices.saveVerified(getActivity(),true);
            filterVerified.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            filterVerified.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));
        }


        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.clear_filters)
    void clearFiltersClick()
    {
        UtilitySortServices.saveOfficial(getActivity(),false);
        filterOfficial.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterOfficial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

        UtilitySortServices.saveVerified(getActivity(),false);
        filterVerified.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterVerified.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));


        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }






    // filter by service Type


    @OnClick(R.id.filter_nonprofit)
    void nonprofitClick(View view)
    {
        clearSelectionFilterByType();
        filterNonprofit.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        filterNonprofit.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));


        UtilitySortServices.saveServiceType(getActivity(),SERVICE_TYPE_NONPROFIT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.filter_government)
    void governmentClick(View view)
    {
        clearSelectionFilterByType();
        filterGovernment.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        filterGovernment.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));


        UtilitySortServices.saveServiceType(getActivity(),SERVICE_TYPE_GOVERNMENT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.filter_commertial)
    void commertialClick(View view)
    {
        clearSelectionFilterByType();
        filterCommertial.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        filterCommertial.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedFilterByType));


        UtilitySortServices.saveServiceType(getActivity(),SERVICE_TYPE_COMMERTIAL);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.clear_filters_type)
    void clearFiltersServiceType()
    {
        UtilitySortServices.saveServiceType(getActivity(),-1);
        filterNonprofit.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterNonprofit.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

        filterGovernment.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterGovernment.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

        filterCommertial.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterCommertial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }



    void clearSelectionSort()
    {
        sort_by_service_range.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_service_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_service_range.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_service_name.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }




    void clearSelectionFilterByType()
    {
        filterNonprofit.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterGovernment.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        filterCommertial.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        filterNonprofit.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        filterGovernment.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        filterCommertial.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }














}
