package org.nearbyshops.enduser.ShopItemsByItemCategory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopsForItems.FilledCartsFragment;
import org.nearbyshops.enduser.ShopsForItems.ShopsForItemSwipe;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    List<Item> dataset = null;

    Context context;


    public ItemAdapter(List<Item> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;

        Log.d("applog","Shop Adapter Constuctor");
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_items_by_item_category,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {

        holder.itemName.setText(dataset.get(position).getItemName());

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
                    + String.valueOf(itemStats.getMax_price()));


            Log.d("applog","Item Stats :" + dataset.get(position).getItemStats().getShopCount());
        }

        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + dataset.get(position).getItemImageURL();

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(holder.itemImage);


    }

    @Override
    public int getItemCount() {

        Log.d("applog",String.valueOf(dataset.size()));

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName;
        ImageView itemImage;
        TextView priceRange;
        TextView shopCount;
        RelativeLayout itemsListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            priceRange = (TextView) itemView.findViewById(R.id.price_range);
            shopCount = (TextView) itemView.findViewById(R.id.shop_count);
            itemsListItem = (RelativeLayout) itemView.findViewById(R.id.items_list_item);
            itemsListItem.setOnClickListener(this);
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

                default:

                    break;
            }

        }
    }
}
