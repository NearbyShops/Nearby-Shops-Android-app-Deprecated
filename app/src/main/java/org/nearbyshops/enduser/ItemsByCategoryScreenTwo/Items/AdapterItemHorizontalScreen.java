package org.nearbyshops.enduser.ItemsByCategoryScreenTwo.Items;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.ItemDetail.ItemDetail;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopItemByItem.ShopsForItemSwipe;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterItemHorizontalScreen extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Item> dataset = null;
    private Context context;
    private Fragment fragment;


    public AdapterItemHorizontalScreen(List<Item> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;

//        Log.d("applog","Shop Adapter Constuctor");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType==0)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_items_horizontal_screen,parent,false);

            return new ViewHolder(view);
        }
        else
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new AdapterItemHorizontalScreen.LoadingViewHolder(view);

        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_given, int position) {



        if(holder_given instanceof AdapterItemHorizontalScreen.ViewHolder) {

            AdapterItemHorizontalScreen.ViewHolder holder = (AdapterItemHorizontalScreen.ViewHolder) holder_given;

            if(position >= dataset.size())
            {
                return;
            }

            Item item = dataset.get(position);

            String imagePath = "http://example.com";

            if(item!=null)
            {
                holder.itemName.setText(item.getItemName());
//                holder.itemDescription.setText(item.getItemDescription());


                if(item.getRt_rating_count()==0)
                {
                    holder.itemRating.setText(" - ");
                    holder.ratingCount.setText("( not yet rated )");
                }
                else
                {
                    holder.itemRating.setText(String.format("%.1f",item.getRt_rating_avg()));
                    holder.ratingCount.setText("( " + String.valueOf((int)item.getRt_rating_count()) + " ratings )");
                }




                imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                        + dataset.get(position).getItemImageURL();
            }


            if(dataset.get(position).getItemStats()!=null)
            {
                ItemStats itemStats = dataset.get(position).getItemStats();

                String shop = "Shops";

                if(itemStats.getShopCount()==1)
                {
                    shop = "Shop";
                }

                holder.shopCount.setText("Available In " + String.valueOf(itemStats.getShopCount()) + " " + shop);
                holder.priceRange.setText( "Price Range :\nRs. "
                        + String.valueOf(itemStats.getMin_price())
                        + " - "
                        + String.valueOf(itemStats.getMax_price())
                        + " per " + dataset.get(position).getQuantityUnit()
                );

                holder.priceAverage.setText("Price Average : Rs. " + String.format("%.2f",itemStats.getAvg_price()));
            }



            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.nature_people)
                    .into(holder.itemImage);


        }
        else if(holder_given instanceof AdapterItemHorizontalScreen.LoadingViewHolder)
        {
            AdapterItemHorizontalScreen.LoadingViewHolder viewHolder = (AdapterItemHorizontalScreen.LoadingViewHolder) holder_given;

            int itemCount = 0;

            if(fragment instanceof FragmentItemScreenHorizontal)
            {
                itemCount = ((FragmentItemScreenHorizontal) fragment).getItemCount();
            }


            if(position == 0 || position == itemCount)
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

    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return (dataset.size()+1);
    }



    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position==dataset.size())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        TextView itemDescription;
        TextView itemName;
        ImageView itemImage;
        TextView priceRange;
        TextView priceAverage;
        TextView shopCount;

        TextView itemRating;
        TextView ratingCount;


        ConstraintLayout itemsListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

//            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            priceRange = (TextView) itemView.findViewById(R.id.price_range);
            priceAverage = (TextView) itemView.findViewById(R.id.price_average);
            shopCount = (TextView) itemView.findViewById(R.id.shop_count);
            itemsListItem = (ConstraintLayout) itemView.findViewById(R.id.items_list_item);

            itemRating = (TextView) itemView.findViewById(R.id.item_rating);
            ratingCount = (TextView) itemView.findViewById(R.id.rating_count);

            itemsListItem.setOnClickListener(this);
            itemImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

         //   Toast.makeText(context,"Item Click : " + String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

            switch(view.getId())
            {
                case R.id.items_list_item:

                    if(dataset!=null)
                    {
                        Intent intent = new Intent(context, ShopsForItemSwipe.class);
                        intent.putExtra(ShopsForItemSwipe.ITEM_INTENT_KEY,dataset.get(getLayoutPosition()));
                        context.startActivity(intent);
                    }

                    break;


                case R.id.itemImage:

                    Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
                    context.startActivity(intent);

                    break;

                default:

                    break;
            }

        }
    }
}
