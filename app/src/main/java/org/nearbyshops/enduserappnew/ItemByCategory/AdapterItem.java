package org.nearbyshops.enduserappnew.ItemByCategory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopItemByItem.ShopsForItemSwipe;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {


    List<Item> dataset = null;

    Context context;


    public AdapterItem(List<Item> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;

//        Log.d("applog","Shop Adapter Constuctor");
    }

    @Override
    public AdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_items_by_item_category,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItem.ViewHolder holder, int position) {

        if(position >= dataset.size())
        {
            return;
        }


        holder.itemName.setText(dataset.get(position).getItemName());

        holder.itemDescription.setText(dataset.get(position).getItemDescription());

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
                    + "\nper " + dataset.get(position).getQuantityUnit()
            );


//            Log.d("applog","Item Stats :" + dataset.get(position).getItemStats().getShopCount());
        }

        String imagePath = PrefGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + dataset.get(position).getItemImageURL();

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

        RelativeLayout itemsListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
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
