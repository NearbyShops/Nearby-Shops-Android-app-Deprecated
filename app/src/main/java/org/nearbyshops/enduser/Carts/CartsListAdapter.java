package org.nearbyshops.enduser.Carts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelStats.CartStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 5/6/16.
 */
public class CartsListAdapter extends RecyclerView.Adapter<CartsListAdapter.ViewHolder> {


    List<CartStats> dataset = null;
    Context context;


    public CartsListAdapter(List<CartStats> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_carts,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Shop shop = dataset.get(position).getShop();

        holder.itemsInCart.setText(dataset.get(position).getItemsInCart() + " Items in Cart");
        holder.cartTotal.setText("Cart Total : Rs " + dataset.get(position).getCart_Total());
        holder.deliveryCharge.setText("Delivery\nRs " + shop.getDeliveryCharges() + "\nPer Order");
        holder.distance.setText(String.format( "%.2f", shop.getDistance())
                                    + " Km");

        holder.shopName.setText(shop.getShopName());

        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + dataset.get(position).getShop().getImagePath();

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(holder.shopImage);


    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView shopImage;
        TextView shopName;
        TextView rating;
        TextView distance;
        TextView deliveryCharge;
        TextView itemsInCart;
        TextView cartTotal;


        public ViewHolder(View itemView) {
            super(itemView);

            shopImage = (ImageView) itemView.findViewById(R.id.shopImage);
            shopName = (TextView) itemView.findViewById(R.id.shopName);
            rating = (TextView) itemView.findViewById(R.id.rating);
            distance = (TextView) itemView.findViewById(R.id.distance);
            deliveryCharge = (TextView) itemView.findViewById(R.id.deliveryCharge);
            itemsInCart = (TextView) itemView.findViewById(R.id.itemsInCart);
            cartTotal = (TextView) itemView.findViewById(R.id.cartTotal);

        }
    }

}
