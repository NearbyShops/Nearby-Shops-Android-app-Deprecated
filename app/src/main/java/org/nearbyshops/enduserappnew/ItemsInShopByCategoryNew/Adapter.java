package org.nearbyshops.enduserappnew.ItemsInShopByCategoryNew;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeOne.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeTwo.AdapterItemCatHorizontalList;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeTwo.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ItemsInShopByCategory.ItemsInShopByCatFragment;
import org.nearbyshops.enduserappnew.ItemsInShopByCategory.ViewHolders.ViewHolderShopItem;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.ModelUtility.HeaderItemsList;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderEmptyScreenNew;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderHeaderSimple;
import org.nearbyshops.enduserappnew.ViewHolderCommon.ViewHolderHorizontalList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

//    Map<Integer,ShopItemParcelable> shopItemMap = new HashMap<>();
//    Map<Integer,Item> selectedItems = new HashMap<>();


    private Map<Integer, CartItem> cartItemMap = new HashMap<>();
    private Map<Integer, CartStats> cartStatsMap = new HashMap<>();



    private List<Object> dataset;
    private Context context;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 2;
    public static final int VIEW_TYPE_SHOP_ITEM = 3;


    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;





    private boolean loadMore;


    private Fragment fragment;
    private ViewHolderShopItem viewHolderShopItem;





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
            viewHolderShopItem = ViewHolderShopItem.create(parent,context,fragment,this,cartItemMap,cartStatsMap);
            return viewHolderShopItem;
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeaderSimple.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return org.nearbyshops.enduserappnew.ViewHolderCommon.LoadingViewHolder.create(parent,context);
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

            ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCatHorizontalList(list,context,fragment));

        }
        else if(holder instanceof ViewHolderShopItem)
        {
            ((ViewHolderShopItem) holder).bindShopItems((ShopItem) dataset.get(position));
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
        else if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
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





    public void getCartStats(final boolean notifyChange, final int position, final boolean notifyDatasetChanged)
    {
        if(viewHolderShopItem!=null)
        {
            viewHolderShopItem.getCartStats(notifyChange,position,notifyDatasetChanged);
        }
    }

}