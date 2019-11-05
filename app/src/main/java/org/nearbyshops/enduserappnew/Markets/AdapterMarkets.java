package org.nearbyshops.enduserappnew.Markets;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Markets.Model.MarketsList;
import org.nearbyshops.enduserappnew.Markets.Model.SignInMarker;
import org.nearbyshops.enduserappnew.Markets.ViewHolders.*;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.enduserappnew.Markets.Interfaces.listItemMarketNotifications;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderHeader;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterMarkets extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> dataset = null;



    private static final int view_type_current_market = 1;
    private static final int view_type_saved_markets_list = 2;
    private static final int view_type_user_profile = 3;
    private static final int view_type_markets_header = 4;
    private static final int VIEW_TYPE_Market = 5;
    private static final int view_type_sign_in = 6;
    private static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 7;
    private static final int VIEW_TYPE_create_market = 8;


    @Inject Gson gson;
    private Fragment fragment;
//    private int total_items_count;
    private boolean loadMore;



    public AdapterMarkets(List<Object> dataset, Fragment fragment) {

        this.dataset = dataset;
        this.fragment = fragment;


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == view_type_current_market)
        {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_market_current,parent,false);
//
//            return new ViewHolderCurrentMarket(view,fragment.getActivity());


            return ViewHolderCurrentMarket.create(parent,fragment.getActivity());

        }
        else if(viewType==view_type_saved_markets_list)
        {

            return ViewHolderSavedMarketList.create(parent,fragment.getActivity(), (listItemMarketNotifications) fragment);

        }
        else if(viewType == view_type_user_profile)
        {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_user_profile,parent,false);
//
//            return new ViewHolderUserProfile(view,fragment.getActivity());

            return ViewHolderUserProfile.create(parent,fragment.getActivity());

        }
        else if(viewType == view_type_markets_header)
        {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_header_markets,parent,false);
//
//            return new ViewHolderHeaderMarket(view);


            return ViewHolderHeader.create(parent,fragment.getActivity());

        }
        else if(viewType == view_type_sign_in)
        {
            return ViewHolderSignIn.create(parent,fragment.getActivity(),fragment);
        }
        else if (viewType == VIEW_TYPE_Market) {


//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_market, parent, false);

//            return new ViewHolderMarket(view, parent, fragment.getActivity(), (ViewHolderMarket.listItemMarketNotifications) fragment);

            return ViewHolderMarket.create(parent,fragment.getActivity(), (listItemMarketNotifications) fragment);


        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);
        }
        else if(viewType==VIEW_TYPE_create_market)
        {
            return ViewHolderEmptyScreenListItem.create(parent,fragment.getActivity(),fragment);
        }



        return null;
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {

            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ServiceConfigurationLocal)
        {
            return view_type_current_market;
        }
//        else if(dataset.get(position) instanceof List<?>)
//        {
//            return view_type_saved_markets_list;
//        }

        else if(dataset.get(position) instanceof MarketsList)
        {
            return view_type_saved_markets_list;
        }
        else if(dataset.get(position) instanceof SignInMarker)
        {
            return view_type_sign_in;
        }
        else if(dataset.get(position) instanceof User)
        {
            return view_type_user_profile;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return view_type_markets_header;
        }
        else if(dataset.get(position) instanceof ServiceConfigurationGlobal){

            return VIEW_TYPE_Market;
        }
        else if(dataset.get(position) instanceof EmptyScreenData)
        {

            return VIEW_TYPE_create_market;
        }


        return -1;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolderCurrentMarket)
        {
            ViewHolderCurrentMarket holderCurrentMarket = (ViewHolderCurrentMarket) holderVH;
            holderCurrentMarket.setItem((ServiceConfigurationLocal) dataset.get(position));

        }
//        else if(holderVH instanceof ViewHolderSavedMarketList)
//        {
//
//            ((ViewHolderSavedMarketList) holderVH).setItem((List<ServiceConfigurationGlobal>) dataset.get(position));
//
//        }
        else if(holderVH instanceof ViewHolderSavedMarketList)
        {

            ((ViewHolderSavedMarketList) holderVH).setItem((MarketsList) dataset.get(position));
        }
        else if(holderVH instanceof ViewHolderUserProfile)
        {
            ViewHolderUserProfile viewHolderUserProfile = (ViewHolderUserProfile) holderVH;
            viewHolderUserProfile.setItem((User) dataset.get(position));

        }
        else if (holderVH instanceof ViewHolderMarket) {

//            ViewHolderMarket holder = (ViewHolderMarket) holderVH;
//            holder.setItem((ServiceConfigurationGlobal) dataset.get(position));


            ((ViewHolderMarket)holderVH).setItem((ServiceConfigurationGlobal) dataset.get(position));

        }
        else if(holderVH instanceof ViewHolderEmptyScreenListItem)
        {
            if(dataset.get(position) instanceof EmptyScreenData)
            {
                ((ViewHolderEmptyScreenListItem) holderVH).setItem((EmptyScreenData) dataset.get(position));
            }
        }
        else if(holderVH instanceof ViewHolderHeader)
        {
            ((ViewHolderHeader) holderVH).setItem((HeaderTitle) dataset.get(position));
        }
        else if (holderVH instanceof LoadingViewHolder) {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;


            if (loadMore) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);
            }
            else {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }




    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }





    public void setData(List<Object> data)
    {
        dataset = data;
    }


//
//    void setTotalItemsCount(int totalItemsCount)
//    {
//        total_items_count = totalItemsCount;
//    }





    @Override
    public int getItemCount() {


//
//        if(dataset!=null)
//        {
//            return (dataset.size()+1);
//        }
//        else
//        {
//            return 0;
//        }


        return (dataset.size()+1);
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }




    public class SavedMarketsMarker{

    }


}
