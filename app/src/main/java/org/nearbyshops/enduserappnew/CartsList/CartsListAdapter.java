package org.nearbyshops.enduserappnew.CartsList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.squareup.picasso.Picasso;
import org.nearbyshops.enduserappnew.CartItemList.CartItemListActivity;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

/**
 * Created by sumeet on 5/6/16.
 */
public class CartsListAdapter extends RecyclerView.Adapter<CartsListAdapter.ViewHolder> {


    private List<CartStats> dataset = null;
    private Context context;


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

        String imagePath = "http://example.com";

        Shop shop = dataset.get(position).getShop();

        holder.itemsInCart.setText(dataset.get(position).getItemsInCart() + " Items in Cart");
        holder.cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(context) + " " + dataset.get(position).getCart_Total());


        if(shop!=null)
        {
            holder.deliveryCharge.setText("Delivery " + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(shop.getDeliveryCharges()) + " Per Order");
            holder.distance.setText(String.format( "%.2f", shop.getRt_distance()) + " Km");

            holder.shopName.setText(shop.getShopName());

            if(shop.getPickFromShopAvailable())
            {
                holder.pickFromShopIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.pickFromShopIndicator.setVisibility(View.GONE);
            }



            if(shop.getHomeDeliveryAvailable())
            {
                holder.homeDeliveryIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.homeDeliveryIndicator.setVisibility(View.GONE);
            }


//            imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                    + dataset.get(position).getShop().getLogoImagePath();

//            imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
//                    + shop.getLogoImagePath() + ".jpg";

            imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

        }


        System.out.println(imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());



        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.shopImage);

    }



    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView shopImage;
        TextView shopName;
        TextView rating;
        TextView distance;
        TextView deliveryCharge;
        TextView itemsInCart;
        TextView cartTotal;
        LinearLayout cartsListItem;
        TextView pickFromShopIndicator;
        TextView homeDeliveryIndicator;



        public ViewHolder(View itemView) {
            super(itemView);

            shopImage = (ImageView) itemView.findViewById(R.id.shopImage);
            shopName = (TextView) itemView.findViewById(R.id.shopName);
            rating = (TextView) itemView.findViewById(R.id.rating);
            distance = (TextView) itemView.findViewById(R.id.distance);
            deliveryCharge = (TextView) itemView.findViewById(R.id.deliveryCharge);
            itemsInCart = (TextView) itemView.findViewById(R.id.itemsInCart);
            cartTotal = (TextView) itemView.findViewById(R.id.cartTotal);

            pickFromShopIndicator = itemView.findViewById(R.id.indicator_pick_from_shop);
            homeDeliveryIndicator = itemView.findViewById(R.id.indicator_home_delivery);


            cartsListItem = (LinearLayout) itemView.findViewById(R.id.carts_list_item);

            cartsListItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId())
            {

                case R.id.carts_list_item:

                    Intent intent = new Intent(context, CartItemListActivity.class);

//                    intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,dataset.get(getLayoutPosition()).getShop());
//                    intent.putExtra(CartItemListActivity.CART_STATS_INTENT_KEY,dataset.get(getLayoutPosition()));


                    String shopJson = UtilityFunctions.provideGson().toJson(dataset.get(getLayoutPosition()).getShop());
                    intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,shopJson);

                    String cartStatsJson = UtilityFunctions.provideGson().toJson(dataset.get(getLayoutPosition()));
                    intent.putExtra(CartItemListActivity.CART_STATS_INTENT_KEY,cartStatsJson);


                    context.startActivity(intent);

                    break;

                default:

                    break;
            }
        }



    }

}
