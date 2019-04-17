package org.nearbyshops.enduserappnew.ShopsByCatSimple;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopHome.ShopHome;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterShopsByCat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

//    Map<Integer,ShopItemParcelable> shopItemMap = new HashMap<>();
//    Map<Integer,Item> selectedItems = new HashMap<>();

    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_SHOP = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    private Fragment fragment;





    public AdapterShopsByCat(List<Object> dataset,
                             Context context,
                             NotificationsFromAdapter notificationReceiver,
                             Fragment fragment
    )
    {

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }







    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
            return new ViewHolderItemCategory(view);
        }
        else if(viewType == VIEW_TYPE_SHOP)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop,parent,false);
            return new ViewHolderShop(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);

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
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }
        else if(holder instanceof ViewHolderShop)
        {
            bindShop((ViewHolderShop) holder,position);

        }
        else if(holder instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if(fragment instanceof ShopsByCatFragment)
            {
                int fetched_count  = ((ShopsByCatFragment) fragment).fetched_items_count;
                int items_count = ((ShopsByCatFragment) fragment).item_count_item;

                if(fetched_count == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
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
        else if (dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP;
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




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    private void bindShop(ViewHolderShop holder, int position)
    {

        if(dataset.get(position) instanceof Shop)
        {
            Shop shop = (Shop) dataset.get(position);

            holder.shopName.setText(shop.getShopName());

            if(shop.getShopAddress()!=null)
            {
                holder.shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
            }


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.shopLogo);


            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(context);

            holder.delivery.setText("Delivery : " + currency + ". " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
            holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");



            if(shop.getRt_rating_count()==0)
            {
                holder.rating.setText("N/A");
                holder.rating_count.setText("( Not Yet Rated )");

            }
            else
            {
                holder.rating.setText(String.valueOf(shop.getRt_rating_avg()));
                holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
            }


            if(shop.getShortDescription()!=null)
            {
                holder.description.setText(shop.getShortDescription());
            }
        }

    }


    public class ViewHolderShop extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.shop_name)
        TextView shopName;

        @BindView(R.id.shop_address)
        TextView shopAddress;

        @BindView(R.id.shop_logo)
        ImageView shopLogo;

        @BindView(R.id.delivery)
        TextView delivery;

        @BindView(R.id.distance)
        TextView distance;

        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.rating_count)
        TextView rating_count;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.shop_info_card)
        CardView list_item;

        public ViewHolderShop(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }






        @OnClick(R.id.shop_info_card)
        void listItemClick()
        {

            if (dataset.get(getLayoutPosition()) instanceof Shop)
            {
                Shop shop = (Shop) dataset.get(getLayoutPosition());

                Intent shopHomeIntent = new Intent(context, ShopHome.class);
                PrefShopHome.saveShop(shop,context);
                context.startActivity(shopHomeIntent);
            }

        }


//        @OnClick(R.id.shop_logo)
        void shopLogoClick()
        {
            if (dataset.get(getLayoutPosition()) instanceof Shop) {
//                Shop shop = (Shop) dataset.get(getLayoutPosition());

//                Intent intent = new Intent(context, MarketDetail.class);
//                intent.putExtra(MarketDetail.SHOP_DETAIL_INTENT_KEY, shop);
//                context.startActivity(intent);
            }
        }


        @Override
        public void onClick(View v) {

            //Toast.makeText(context,"Item Click : " + String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

            switch (v.getId())
            {
                case R.id.shopImage:


                    break;

                case R.id.list_item_shop:

                    if (dataset.get(getLayoutPosition()) instanceof Shop)
                    {
                        Shop shop = (Shop) dataset.get(getLayoutPosition());

                        Intent shopHomeIntent = new Intent(context, ShopHome.class);
                        PrefShopHome.saveShop(shop,context);
                        context.startActivity(shopHomeIntent);
                    }


                    break;

                default:
                    break;
            }
        }
    }





    public class ViewHolderHeader extends RecyclerView.ViewHolder{


        @BindView(R.id.header)
        TextView header;

        ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }




    }// ViewHolderShopItem Class declaration ends



    void bindItemCategory(ViewHolderItemCategory holder,int position)
    {

        if(dataset.get(position) instanceof ItemCategory)
        {
            ItemCategory itemCategory = (ItemCategory) dataset.get(position);

            holder.categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.categoryImage);

        }
    }



    public class ViewHolderItemCategory extends RecyclerView.ViewHolder{


        @BindView(R.id.name) TextView categoryName;
        @BindView(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @BindView(R.id.categoryImage) ImageView categoryImage;
        @BindView(R.id.cardview) CardView cardView;

        ViewHolderItemCategory(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.notifyRequestSubCategory(
                    (ItemCategory) dataset.get(getLayoutPosition()));

//            selectedItems.clear();
        }


    }// ViewHolderItem Class declaration ends













    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
//        void notifyItemSelected();
    }



    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

}