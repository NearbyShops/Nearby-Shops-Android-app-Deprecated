package org.nearbyshops.enduserappnew.ShopsList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ShopsList.ViewHolders.ViewHolderShop;
import org.nearbyshops.enduserappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderEmptyScreenFullScreenNew;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_SHOP = 1;


    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;



    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;


        if(viewType == VIEW_TYPE_SHOP)
        {
            return ViewHolderShop.create(parent,context,fragment,this);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreenNew.create(parent,context);
        }



        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderShop)
        {
            ((ViewHolderShop) holder).setItem((Shop) dataset.get(position));
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreenNew)
        {
            ((ViewHolderEmptyScreenFullScreenNew) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }




    }





    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }




}
