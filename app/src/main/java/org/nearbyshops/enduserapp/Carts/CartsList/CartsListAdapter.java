package org.nearbyshops.enduserapp.Carts.CartsList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserapp.Carts.CartItem.CartItemListActivity;
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.ModelStats.CartStats;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;

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
        holder.cartTotal.setText("Cart Total : Rs " + dataset.get(position).getCart_Total());


        if(shop!=null)
        {
            holder.deliveryCharge.setText("Delivery Rs " + shop.getDeliveryCharges() + " Per Order");
            holder.distance.setText(String.format( "%.2f", shop.getRt_distance()) + " Km");

            holder.shopName.setText(shop.getShopName());

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



        Picasso.with(context)
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


        public ViewHolder(View itemView) {
            super(itemView);

            shopImage = (ImageView) itemView.findViewById(R.id.shopImage);
            shopName = (TextView) itemView.findViewById(R.id.shopName);
            rating = (TextView) itemView.findViewById(R.id.rating);
            distance = (TextView) itemView.findViewById(R.id.distance);
            deliveryCharge = (TextView) itemView.findViewById(R.id.deliveryCharge);
            itemsInCart = (TextView) itemView.findViewById(R.id.itemsInCart);
            cartTotal = (TextView) itemView.findViewById(R.id.cartTotal);


            cartsListItem = (LinearLayout) itemView.findViewById(R.id.carts_list_item);

            cartsListItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId())
            {

                case R.id.carts_list_item:

                    Intent intent = new Intent(context,CartItemListActivity.class);

                    intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,dataset.get(getLayoutPosition()).getShop());
                    intent.putExtra(CartItemListActivity.CART_STATS_INTENT_KEY,dataset.get(getLayoutPosition()));

                    context.startActivity(intent);

                    break;

                default:

                    break;
            }
        }



    }

}
