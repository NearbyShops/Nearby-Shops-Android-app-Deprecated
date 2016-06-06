package org.nearbyshops.enduser.ShopsForItems;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Cart;
import org.nearbyshops.enduser.Model.CartItem;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.CartService;
import org.nearbyshops.enduser.Utility.InputFilterMinMax;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/5/16.
 */
public class AdapterNewCarts extends RecyclerView.Adapter<AdapterNewCarts.ViewHolder>{


    List<ShopItem> dataset;

    Context context;

    @Inject
    CartItemService cartItemService;

    NotifyCallbacks notifyCallbacks;


    public AdapterNewCarts(List<ShopItem> dataset, Context context, NotifyCallbacks callbacks) {
        this.dataset = dataset;
        this.context = context;
        this.notifyCallbacks = callbacks;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShopItem shopItem = dataset.get(position);

        Shop shop = null;

        if(shopItem!=null)
        {
            shop = shopItem.getShop();
        }



        if(shop!=null)
        {
            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + shop.getImagePath();

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.nature_people)
                    .into(holder.shopImage);

            holder.shopName.setText(shop.getShopName());

            holder.distance.setText(String.format( "%.2f", shop.getDistance()) + " Km");
            holder.deliveryCharge.setText("Delivery :\nRs " + String.format( "%.0f", shop.getDeliveryCharges()) + "\nPer Order");

        }

        if(shopItem !=null)
        {
            holder.itemsAvailable.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()));
            holder.itemPrice.setText("Price : Rs " + String.format( "%.2f", shopItem.getItemPrice()));

        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder implements Callback<ResponseBody>, TextWatcher {


        @Bind(R.id.distance)
        TextView distance;

        @Bind(R.id.deliveryCharge)
        TextView deliveryCharge;

        @Bind(R.id.shopName)
        TextView shopName;

        @Bind(R.id.itemsAvailable)
        TextView itemsAvailable;

        @Bind(R.id.itemPrice)
        TextView itemPrice;

        @Bind(R.id.itemTotal)
        TextView itemTotal;

        @Bind(R.id.reduceQuantity)
        ImageView reduceQuantity;

        @Bind(R.id.increaseQuantity)
        ImageView increaseQuantity;

        @Bind(R.id.itemQuantity)
        EditText itemQuantity;

        @Bind(R.id.itemsInCart)
        TextView itemsInCart;

        @Bind(R.id.cartTotal)
        TextView cartTotal;

        @Bind(R.id.addToCart)
        LinearLayout addToCart;

        @Bind(R.id.shopImage)
        ImageView shopImage;

        @Bind(R.id.shopItem_list_item)
        LinearLayout shopItemListItem;


        ShopItem shopItem;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemQuantity.addTextChangedListener(this);
        }


        void setFilter() {

            if (getLayoutPosition() != -1) {

                shopItem = dataset.get(getLayoutPosition());
            }

            if (shopItem != null) {
                int availableItems = shopItem.getAvailableItemQuantity();

                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax(0, availableItems)});
            }

        }



        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {
            setFilter();

            shopItem = dataset.get(getLayoutPosition());

            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){


                try{

                    if(Integer.parseInt(itemQuantity.getText().toString())<=0) {


                        itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");

                        return;

                    }else
                    {
                        itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) - 1));

                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }

                cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
                itemTotal.setText("Total : " + String.format( "%.2f", total));

            }else
            {

                itemQuantity.setText(String.valueOf(0));

                itemTotal.setText("Total : " + String.format( "%.2f", total));
                cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
            }

        }

        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {
            setFilter();

            shopItem = dataset.get(getLayoutPosition());


            int availableItems = shopItem.getAvailableItemQuantity();

            double total = 0;


            if (!itemQuantity.getText().toString().equals("")) {


                try {

                    if (Integer.parseInt(itemQuantity.getText().toString()) >= availableItems) {
                        return;
                    }


                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) + 1));

                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());


                }catch (Exception ex)
                {

                }

                itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                itemTotal.setText("Total : " + String.format("%.2f", total));
                cartTotal.setText("Cart Total : Rs " + String.format("%.2f", total));

            }else
            {
                itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + String.format( "%.2f", total));
                cartTotal.setText("Cart Total : Rs " + String.format("%.2f", total));
            }

        }






        @OnClick(R.id.addToCart)
        void addToCartClick(View view){


            CartItem cartItem = new CartItem();

            cartItem.setItemID(dataset.get(getLayoutPosition()).getItemID());
            cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));

            Call<ResponseBody> call = cartItemService.createCartItem(
                    cartItem,
                    UtilityGeneral.getEndUserID(MyApplication.getAppContext()),
                    dataset.get(getLayoutPosition()).getShopID()
            );

            call.enqueue(this);

        }



        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.code()==201)
            {
                Toast.makeText(context,"Add to cart. Successful !",Toast.LENGTH_SHORT).show();

                notifyCallbacks.notifyAddToCart();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            Toast.makeText(context," Unsuccessful !",Toast.LENGTH_SHORT).show();

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            setFilter();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            shopItem = dataset.get(getLayoutPosition());




            double total = 0;
            //int availableItems = shopItem.getAvailableItemQuantity();

            if (!itemQuantity.getText().toString().equals(""))
            {

                try{


                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());


                    if(Integer.parseInt(itemQuantity.getText().toString())==0)
                    {

                            itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");

                    }else
                    {

                            itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                    }

                }
                catch (Exception ex)
                {
                    //ex.printStackTrace();
                }

            }else
            {
                itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
            }


            itemTotal.setText("Total : " + String.format( "%.2f", total));

            cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    public interface NotifyCallbacks {

        void notifyAddToCart();

    }

}
