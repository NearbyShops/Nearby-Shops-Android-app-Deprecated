package org.nearbyshops.enduser.ItemCategoriesTypeSimple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Carts.CartItemAdapter;
import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ItemCategoriesTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterSimple extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;



    public AdapterSimple(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
            return new ViewHolderItemCategory(view);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
            return new ViewHolderItemSimple(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }

//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderItemCategory)
        {
            bindItemCategory((ViewHolderItemCategory) holder,position);
        }
        else if(holder instanceof ViewHolderItemSimple)
        {
            bindItem((ViewHolderItemSimple) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }

    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @Bind(R.id.header) TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends



    void bindItemCategory(ViewHolderItemCategory holder,int position)
    {

        if(dataset.get(position) instanceof ItemCategory)
        {
            ItemCategory itemCategory = (ItemCategory) dataset.get(position);

            holder.categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.categoryImage);

        }
    }



    class ViewHolderItemCategory extends RecyclerView.ViewHolder{


        @Bind(R.id.name) TextView categoryName;
        @Bind(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;
        @Bind(R.id.cardview) CardView cardView;

        public ViewHolderItemCategory(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.notifyRequestSubCategory(
                    (ItemCategory) dataset.get(getLayoutPosition()));
        }


    }// ViewHolder Class declaration ends




    void bindItem(ViewHolderItemSimple holder,int position)
    {

        Item item = (Item) dataset.get(position);

        holder.categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        holder.priceRange.setText("Price Range :\nRs." + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
        holder.priceAverage.setText("Price Average :\nRs." + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
        holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        holder.itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
        holder.ratingCount.setText("( " + String.valueOf(itemStats.getRatingCount()) + " Ratings )");


        String imagePath = UtilityGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

    }



    class ViewHolderItemSimple extends RecyclerView.ViewHolder {


        @Bind(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @Bind(R.id.items_list_item) CardView itemCategoryListItem;
        @Bind(R.id.itemImage) ImageView categoryImage;
        @Bind(R.id.price_range) TextView priceRange;
        @Bind(R.id.price_average) TextView priceAverage;
        @Bind(R.id.shop_count) TextView shopCount;
        @Bind(R.id.item_rating) TextView itemRating;
        @Bind(R.id.rating_count) TextView ratingCount;



        public ViewHolderItemSimple(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



            @OnClick(R.id.items_list_item)
            public void listItemClick()
            {

            }

    }// ViewHolder Class declaration ends




    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
    }



//    private void showToastMessage(String message)
//    {
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
//    }

}