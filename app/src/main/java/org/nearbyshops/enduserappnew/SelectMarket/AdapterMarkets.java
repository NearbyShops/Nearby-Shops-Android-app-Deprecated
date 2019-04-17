package org.nearbyshops.enduserappnew.SelectMarket;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterMarkets extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> dataset = null;



    private static final int view_type_current_market = 1;
    private static final int view_type_user_profile = 2;
    private static final int view_type_markets_header = 3;
    private static final int VIEW_TYPE_Market = 4;
    private static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;


    @Inject Gson gson;
    private Fragment fragment;
//    private int total_items_count;
    private boolean loadMore;



    AdapterMarkets(List<Object> dataset, Fragment fragment) {

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

            return ViewHolderHeaderMarket.create(parent,fragment.getActivity());

        }
        else if (viewType == VIEW_TYPE_Market) {


//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_market, parent, false);

//            return new ViewHolderMarket(view, parent, fragment.getActivity(), (ViewHolderMarket.VHMarketNotifications) fragment);

            return ViewHolderMarket.create(parent,fragment.getActivity(), (ViewHolderMarket.VHMarketNotifications) fragment);


        } else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);
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
        else if(dataset.get(position) instanceof User)
        {
            return view_type_user_profile;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return view_type_markets_header;
        }
        else if(dataset.get(position) instanceof ServiceConfigurationGlobal){

            return VIEW_TYPE_Market;
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
        else if(holderVH instanceof ViewHolderUserProfile)
        {
            ViewHolderUserProfile viewHolderUserProfile = (ViewHolderUserProfile) holderVH;
            viewHolderUserProfile.setItem((User) dataset.get(position));

        }
        else if (holderVH instanceof ViewHolderMarket) {

//            ViewHolderMarket holder = (ViewHolderMarket) holderVH;
//            holder.setItem((ServiceConfigurationGlobal) dataset.get(position));


            ((ViewHolderMarket)holderVH).setItem((ServiceConfigurationGlobal) dataset.get(position));

        } else if (holderVH instanceof LoadingViewHolder) {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if (fragment instanceof MarketsFragment) {

//                int items_count = ((MarketsFragment) fragment).item_count;

//
//                if (dataset.size()-1 == total_items_count) {
//                    viewHolder.progressBar.setVisibility(View.GONE);
//                } else {
//                    viewHolder.progressBar.setVisibility(View.VISIBLE);
//                    viewHolder.progressBar.setIndeterminate(true);
//
//                }


                if (loadMore) {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);
                }
                else {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }
        }
    }




    void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }




//
//    void setTotalItemsCount(int totalItemsCount)
//    {
//        total_items_count = totalItemsCount;
//    }





    @Override
    public int getItemCount() {
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




}
