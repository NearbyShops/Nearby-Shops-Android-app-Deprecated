package org.nearbyshops.enduserappnew.ItemsByCatNew;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.enduserappnew.ItemsByCatNew.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ItemsByCatNew.ViewHolders.AdapterItemCategoriesHorizontal;
import org.nearbyshops.enduserappnew.ItemsByCatNew.ViewHolders.ViewHolderHorizontalList;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.ViewHolders.ViewHolderItemSimple;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderEmptyScreenNew;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderHeaderSimple;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */




public class AdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 2;
    public static final int VIEW_TYPE_ITEM = 3;


    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;


    private boolean loadMore;



    private Fragment fragment;

    public AdapterNew(List<Object> dataset, Context context, Fragment fragment) {


        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;



        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            return ViewHolderItemCategory.create(parent,context,fragment,this);
        }
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY_LIST)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {

            return ViewHolderItemSimple.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeaderSimple.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenNew.create(parent,context);
        }


        return null;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if(holder instanceof ViewHolderItemCategory)
        {
            ((ViewHolderItemCategory) holder).bindItemCategory((ItemCategory) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHorizontalList)
        {

            List<ItemCategory> list = ((ItemCategoriesList)dataset.get(position)).getItemCategories();

            ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCategoriesHorizontal(list,context,fragment));

        }
        else if(holder instanceof ViewHolderItemSimple)
        {

            ((ViewHolderItemSimple) holder).bindItem((Item) dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeaderSimple) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeaderSimple) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenNew)
        {
            ((ViewHolderEmptyScreenNew) holder).setItem((EmptyScreenData) dataset.get(position));
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
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof EmptyScreenData)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
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