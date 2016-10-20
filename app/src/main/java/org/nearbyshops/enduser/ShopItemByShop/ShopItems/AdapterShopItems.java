package org.nearbyshops.enduser.ShopItemByShop.ShopItems;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopDetail.ShopDetail;
import org.nearbyshops.enduser.ShopHome.ShopHome;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityShopHome;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 25/5/16.
 */
class AdapterShopItems extends RecyclerView.Adapter<AdapterShopItems.ViewHolder> {


    private List<ShopItem> dataset = null;
    private Context context;


    AdapterShopItems(List<ShopItem> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public AdapterShopItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_item_by_shop,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterShopItems.ViewHolder holder, int position) {

        Item item = dataset.get(position).getItem();
        ShopItem shopItem = dataset.get(position);


//        holder.shopName.setText(dataset.get(position).getShopName());
//        holder.rating.setText(String.format( "%.2f", dataset.get(position).getRt_rating_avg()));
//        holder.distance.setText(String.format( "%.2f", dataset.get(position).getDistance() )+ " Km");

        if(shopItem!=null)
        {
            holder.available.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()));

        }


        if(item!=null)
        {
            holder.itemName.setText(item.getItemName());
            holder.itemPrice.setText("Rs. " + String.format("%.2f",shopItem.getItemPrice()) + " per " + item.getQuantityUnit());
        }


//        holder.rating.setText(String.format("%.2f",));




        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + item.getItemImageURL();

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.itemImage);


    }

    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.item_title)
        TextView itemName;

        @Bind(R.id.item_image)
        ImageView itemImage;

        @Bind(R.id.item_price)
        TextView itemPrice;

        @Bind(R.id.available)
        TextView available;

        @Bind(R.id.rating)
        TextView rating;

        @Bind(R.id.rating_count)
        TextView ratinCount;

        @Bind(R.id.increaseQuantity)
        ImageView increaseQuantity;

        @Bind(R.id.itemQuantity)
        EditText itemQuantity;

        @Bind(R.id.reduceQuantity)
        ImageView reduceQuantity;

        @Bind(R.id.total)
        TextView total;

        @Bind(R.id.add_to_cart)
        TextView addToCart;




        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

        }




        /*@Override
        public void onClick(View v) {

            //Toast.makeText(context,"Item Click : " + String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

            switch (v.getId())
            {
                case R.id.shopImage:

                    Intent intent = new Intent(context, ShopDetail.class);
                    intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
                    context.startActivity(intent);

                    break;

                case R.id.list_item_shop:

                    Intent shopHomeIntent = new Intent(context, ShopHome.class);
                    UtilityShopHome.saveShop(dataset.get(getLayoutPosition()),context);
                    context.startActivity(shopHomeIntent);

                    break;

                default:
                    break;
            }
        }*/



    }
}
