package org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ViewHolders.ViewHolderItem;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ViewHolders.ViewHolderItemCategoryAdmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Map<Integer, ItemCategory> selectedItemCategories = new HashMap<>();
    Map<Integer, Item> selectedItems = new HashMap<>();



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;



    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;




    private boolean loadMore;






    public Adapter(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            return ViewHolderItemCategoryAdmin.create(parent,context,fragment,this,selectedItems,selectedItemCategories);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {
            return ViewHolderItem.create(parent,context,fragment,this,selectedItems);

        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }




        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {



        if(holder instanceof ViewHolderItemCategoryAdmin)
        {
            ((ViewHolderItemCategoryAdmin) holder).setItem((ItemCategory) dataset.get(position));
        }
        else if(holder instanceof ViewHolderItem)
        {

            ((ViewHolderItem) holder).setItem((Item) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHeader)
        {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if(holder instanceof LoadingViewHolder)
        {
            ((LoadingViewHolder) holder).setLoading(loadMore);
        }


    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}