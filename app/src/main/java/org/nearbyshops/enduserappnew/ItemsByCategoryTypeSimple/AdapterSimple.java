package org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopItemByItem.ShopsForItemSwipe;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.ShopItemByItemNew.ShopItemByItemNew;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    private Fragment fragment;

    public AdapterSimple(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver,Fragment fragment) {


//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

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
        else if(holder instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;


            Log.d("adapter_item_cat","Hello from LoadingViewHolder");

            if(fragment instanceof ItemCategoriesFragmentSimple)
            {
                int fetched_count  = ((ItemCategoriesFragmentSimple) fragment).fetched_items_count;
                int items_count = ((ItemCategoriesFragmentSimple) fragment).item_count_item;

                Log.d("adapter_item_cat","Fetched Count : "  + String.valueOf(fetched_count) + " Items Count : "  + String.valueOf(items_count));



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

//        else if(holder instanceof LoadingViewHolder)
//        {
//
//            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
//
//            int itemCount = 0;
//
//            if(fragment instanceof ItemCategoriesFragmentSimple)
//            {
//                itemCount = ((ItemCategoriesFragmentSimple) fragment).item_count_item + ((ItemCategoriesFragmentSimple) fragment).item_count_item_category;
//            }
//
//            if(position == 0 || position == itemCount)
//            {
//                viewHolder.progressBar.setVisibility(View.GONE);
//            }
//            else
//            {
//                viewHolder.progressBar.setVisibility(View.VISIBLE);
//                viewHolder.progressBar.setIndeterminate(true);
//
//            }
//        }



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
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @BindView(R.id.header) TextView header;

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



    class ViewHolderItemCategory extends RecyclerView.ViewHolder{


        @BindView(R.id.name) TextView categoryName;
        @BindView(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @BindView(R.id.categoryImage) ImageView categoryImage;
        @BindView(R.id.cardview) CardView cardView;

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

        if(itemStats!=null)
        {
            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(context);

            holder.priceRange.setText("Price Range :\n" + currency + ". " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            holder.priceAverage.setText("Price Average :\n" + currency + ". " + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
            holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
//            System.out.println("Rating : " + itemStats.getRating_avg() + " : Ratings Count " + itemStats.getRatingCount());
        }



//        holder.itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
//        holder.ratingCount.setText("( " + String.valueOf((int)item.getRt_rating_count()) + " Ratings )");





        if(item.getRt_rating_count()==0)
        {
            holder.itemRating.setText(" New ");
            holder.itemRating.setBackgroundColor(ContextCompat.getColor(context,R.color.phonographyBlue));
            holder.ratingCount.setVisibility(View.GONE);
        }
        else
        {


            holder.itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
            holder.ratingCount.setText("( " + String.valueOf((int)item.getRt_rating_count()) + " Ratings )");

            holder.itemRating.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
            holder.ratingCount.setVisibility(View.VISIBLE);

        }




        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

    }






    class ViewHolderItemSimple extends RecyclerView.ViewHolder {


        @BindView(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @BindView(R.id.items_list_item) CardView itemCategoryListItem;
        @BindView(R.id.itemImage) ImageView categoryImage;
        @BindView(R.id.price_range) TextView priceRange;
        @BindView(R.id.price_average) TextView priceAverage;
        @BindView(R.id.shop_count) TextView shopCount;
        @BindView(R.id.item_rating) TextView itemRating;
        @BindView(R.id.rating_count) TextView ratingCount;



        public ViewHolderItemSimple(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



            @OnClick(R.id.items_list_item)
            public void listItemClick()
            {
                if(dataset.get(getLayoutPosition()) instanceof Item)
                {
//                    Intent intent = new Intent(context, ShopsForItemSwipe.class);
//                    intent.putExtra(ShopsForItemSwipe.ITEM_INTENT_KEY,(Item)dataset.get(getLayoutPosition()));
//                    context.startActivity(intent);

                    Intent intent = new Intent(context,ShopItemByItemNew.class);

                    Gson gson = UtilityFunctions.provideGson();
                    String jsonString = gson.toJson((Item)dataset.get(getLayoutPosition()));
                    intent.putExtra("item_json",jsonString);

                    context.startActivity(intent);



                }
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




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}