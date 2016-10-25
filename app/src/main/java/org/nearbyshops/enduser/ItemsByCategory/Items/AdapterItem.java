package org.nearbyshops.enduser.ItemsByCategory.Items;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {


    private List<Item> dataset = null;

    private Context context;


    public AdapterItem(List<Item> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;

//        Log.d("applog","Shop Adapter Constuctor");
    }

    @Override
    public AdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_items_by_item_category_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItem.ViewHolder holder, int position) {

        if(position >= dataset.size())
        {
            return;
        }

        Item item = dataset.get(position);

        String imagePath = "http://example.com";

        if(item!=null)
        {
            holder.itemName.setText(item.getItemName());
            holder.itemDescription.setText(item.getItemDescription());


            if(item.getRt_rating_count()==0)
            {
                holder.itemRating.setText("N/A");
                holder.ratingCount.setText("(Not yet rated)");
            }
            else
            {
                holder.itemRating.setText(String.format("%.1f",item.getRt_rating_avg()));
                holder.ratingCount.setText("( " + String.valueOf(item.getRt_rating_count()) + " ratings )");
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

            holder.shopCount.setText("In " + String.valueOf(itemStats.getShopCount()) + " " + shop);
            holder.priceRange.setText( "Rs: "
                    + String.valueOf(itemStats.getMin_price())
                    + " - "
                    + String.valueOf(itemStats.getMax_price())
                    + " per " + dataset.get(position).getQuantityUnit()
            );


//            Log.d("applog","Item Stats :" + dataset.get(position).getItemStats().getShopCount());
        }


        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemDescription;
        TextView itemName;
        ImageView itemImage;
        TextView priceRange;
        TextView shopCount;

        TextView itemRating;
        TextView ratingCount;

        CardView itemsListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            priceRange = (TextView) itemView.findViewById(R.id.price_range);
            shopCount = (TextView) itemView.findViewById(R.id.shop_count);
            itemsListItem = (CardView) itemView.findViewById(R.id.items_list_item);

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
