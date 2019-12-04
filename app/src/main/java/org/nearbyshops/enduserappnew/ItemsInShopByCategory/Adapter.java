package org.nearbyshops.enduserappnew.ItemsInShopByCategory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.core.Model.ItemCategory;
import org.nearbyshops.core.Model.ModelCartOrder.CartItem;
import org.nearbyshops.core.Model.ModelStats.CartStats;
import org.nearbyshops.core.Model.Shop;
import org.nearbyshops.core.Model.ShopItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ItemsByCategory.AdapterItemCatHorizontalList;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemSimplified;

import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShop;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.ViewHolderHeader;
import org.nearbyshops.enduserappnew.ViewHoldersUtility.ViewHolderHorizontalList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

//    Map<Integer,ShopItemParcelable> shopItemMap = new HashMap<>();
//    Map<Integer,Item> selectedItems = new HashMap<>();


    public Map<Integer, CartItem> cartItemMap = new HashMap<>();
//    public Map<Integer, CartStats> cartStatsMap = new HashMap<>();
    public CartStats cartStats = new CartStats();



    private List<Object> dataset;
    private Context context;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 2;
    public static final int VIEW_TYPE_SHOP_ITEM = 3;
    public static final int VIEW_TYPE_SHOP = 4;



    public static final int VIEW_TYPE_HEADER = 10;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 11;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 12;





    private boolean loadMore;


    private Fragment fragment;
//    private ViewHolderShopItemSeller viewHolderShopItem;





    public Adapter(List<Object> dataset,
                   Context context,
                   Fragment fragment
    )
    {

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
        else if(viewType == VIEW_TYPE_SHOP_ITEM)
        {


//            viewHolderShopItem = ;
            return ViewHolderShopItemSimplified.create(parent,context,fragment,this,cartItemMap,cartStats);

        }
        else if(viewType == VIEW_TYPE_SHOP)
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
            return ViewHolderEmptyScreenFullScreen.create(parent,context);
        }



        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if(holder instanceof ViewHolderShop)
        {
            ((ViewHolderShop) holder).setItem((Shop) dataset.get(position));
        }
        else if(holder instanceof ViewHolderItemCategory)
        {
            ((ViewHolderItemCategory) holder).bindItemCategory((ItemCategory) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHorizontalList)
        {

            List<ItemCategory> list = ((ItemCategoriesList)dataset.get(position)).getItemCategories();

            ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCatHorizontalList(list,context,fragment));

        }
        else if(holder instanceof ViewHolderShopItemSimplified)
        {
            ((ViewHolderShopItemSimplified) holder).bindShopItems((ShopItem) dataset.get(position));
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }


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
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;

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




    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }








    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}