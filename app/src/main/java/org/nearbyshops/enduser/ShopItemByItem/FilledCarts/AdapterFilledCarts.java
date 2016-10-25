package org.nearbyshops.enduser.ShopItemByItem.FilledCarts;

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
import org.nearbyshops.enduser.Model.CartItem;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelStats.CartStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduser.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduser.Utility.InputFilterMinMax;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class AdapterFilledCarts extends RecyclerView.Adapter<AdapterFilledCarts.ViewHolder>{


    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;

    private Map<Integer,CartItem> cartItemMap = new HashMap<>();
    private Map<Integer,CartStats> cartStatsMap = new HashMap<>();


    private Item item;

    private List<ShopItem> dataset;

    private Context context;

    private NotifyFilledCart notifyFilledCart;


    public AdapterFilledCarts(List<ShopItem> dataset, Context context,Item item,NotifyFilledCart notifyFilledCart) {
        this.dataset = dataset;
        this.context = context;
        this.item = item;
        this.notifyFilledCart = notifyFilledCart;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        //makeNetworkCall();
    }


    void makeNetworkCall()
    {


        cartItemMap.clear();
        cartStatsMap.clear();

        EndUser endUser = UtilityLogin.getEndUser(context);
        if(endUser == null)
        {
            return;
        }

        Call<List<CartItem>> cartItemCall = cartItemService.getCartItem(0,item.getItemID(),
                endUser.getEndUserID(),0);

        cartItemCall.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {


                //Toast.makeText(context,"Response Code: " + response.code(),Toast.LENGTH_SHORT).show();

                if(response.body()!=null)
                {

                    for(CartItem cartItem: response.body())
                    {
                        cartItemMap.put(cartItem.getCart().getShopID(),cartItem);
                    }

                    notifyDataSetChanged();

                    //showToastMessage("Cart Item Updated !");

                }else
                {
                    cartItemMap.clear();

                    notifyDataSetChanged();

                    //showToastMessage("Cart Item Updated - Null !");
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

                Toast.makeText(context," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<CartStats>> listCall = cartStatsService
                .getCart(endUser.getEndUserID(), 0,
                        UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                        UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY));

        listCall.enqueue(new Callback<List<CartStats>>() {

            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if(response.body()!=null)
                {

                    for(CartStats cartStats: response.body())
                    {
                        cartStatsMap.put(cartStats.getShopID(),cartStats);
                    }

                    //showToastMessage("Cart Stats Updated !");

                    notifyDataSetChanged();
                }else
                {
                    cartStatsMap.clear();
                    notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                Toast.makeText(context," Network Request Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });
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


        //if(cartItemMap.size()>0)
        //{
            CartItem cartItem = cartItemMap.get(dataset.get(position).getShopID());

            if(cartItem!=null)
            {
                holder.itemQuantity.setText(String.valueOf(cartItem.getItemQuantity()));
                holder.shopItemListItem.setBackgroundResource(R.color.backgroundTinted);

                double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

                holder.itemTotal.setText("Total : " + String.format( "%.2f", total));

                holder.addToCartText.setText("Update Cart");

            }else
            {

                holder.shopItemListItem.setBackgroundResource(R.color.shopItemColor);
                //holder.shopItemListItem.setBackgroundColor(22000000);

            }




        //if(cartStatsMap.size()>0)
        //{
            CartStats cartStats = cartStatsMap.get(dataset.get(position).getShopID());

            if(cartStats!=null)
            {
                holder.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                holder.cartTotal.setText("Cart Total : Rs " + String.valueOf(cartStats.getCart_Total()));
            }
        //}


        if(shopItem!=null)
        {
            shop = shopItem.getShop();
        }



        if(shop!=null)
        {

            if(shop.getRt_rating_count()>0)
            {
                holder.rating.setText(String.format("%.1f",shop.getRt_rating_avg()));
            }
            else
            {
                holder.rating.setText("Not yet rated !");
//                holder.rating.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
//                holder.rating.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey);
            }


            holder.ratingCount.setText("( " + String.format("%.0f",shop.getRt_rating_count()) + " Ratings )");



            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + shop.getImagePath();



            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.nature_people)
                    .into(holder.shopImage);

            holder.shopName.setText(shop.getShopName());

            holder.distance.setText(String.format( "%.2f", shop.getDistance()) + " Km");
            holder.deliveryCharge.setText("Delivery : Rs " + String.format( "%.0f", shop.getDeliveryCharges()) + " Per Order");

        }

        if(shopItem !=null)
        {
            holder.itemsAvailable.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()));
            holder.itemPrice.setText("Rs " + String.format( "%.2f", shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.rating)
        TextView rating;

        @Bind(R.id.ratings_count)
        TextView ratingCount;

        @Bind(R.id.textAddToCart)
        TextView addToCartText;

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
        CartItem cartItem;
        CartStats cartStats;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    setFilter();

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    shopItem = dataset.get(getLayoutPosition());
                    cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());

                    cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());


            /*

            if(cartItem==null)
            {

                if(Integer.parseInt(itemQuantity.getText().toString())==0)
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                }else
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                }


            }else
            {
                if(Integer.parseInt(itemQuantity.getText().toString())==0)
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");

                }else
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                }
            }

            */



                    double total = 0;
                    int availableItems = shopItem.getAvailableItemQuantity();

                    if (!itemQuantity.getText().toString().equals(""))
                    {

                        try{

                            if(Integer.parseInt(itemQuantity.getText().toString())>availableItems)
                            {

                                return;
                            }

                            total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());


                            if(Integer.parseInt(itemQuantity.getText().toString())==0)
                            {
                                if(cartItem==null)
                                {

                                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                                }else
                                {
                                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");

                                    addToCartText.setText("Remove Item");

                                }

                            }else
                            {
                                if(cartItem==null)
                                {
                                    // no shop exist

                                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");

                                }else
                                {
                                    // shop Exist

                                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                                    addToCartText.setText("Update Cart");
                                }
                            }

                        }
                        catch (Exception ex)
                        {
                            //ex.printStackTrace();
                        }

                    }


                    itemTotal.setText("Total : " + String.format( "%.2f", total));

                    cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));

                }

                @Override
                public void afterTextChanged(Editable s) {



                }
            });

        }


        void setFilter() {

            if (getLayoutPosition() != -1) {

                shopItem = dataset.get(getLayoutPosition());
            }

            if (shopItem != null) {
                int availableItems = shopItem.getAvailableItemQuantity();

                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
            }

        }


        @OnClick(R.id.addToCart)
        void addToCartClick(View view) {


            CartItem cartItem = new CartItem();
            cartItem.setItemID(dataset.get(getLayoutPosition()).getItemID());

            if (!itemQuantity.getText().toString().equals("")) {

                cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
            }

            if (!cartItemMap.containsKey(dataset.get(getLayoutPosition()).getShopID()))
            {

                if (Integer.parseInt(itemQuantity.getText().toString()) == 0) {
                    showToastMessage("Please select quantity greater than Zero !");

                } else {

                    //showToastMessage("Add to cart! : " + dataset.get(getLayoutPosition()).getShopID());

                    EndUser endUser = UtilityLogin.getEndUser(context);
                    if(endUser==null)
                    {
                        return;
                    }


                    Call<ResponseBody> call = cartItemService.createCartItem(
                            cartItem,
                            endUser.getEndUserID(),
                            dataset.get(getLayoutPosition()).getShopID()
                    );

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            if (response.code() == 201) {
                                Toast.makeText(context, "Add to cart successful !", Toast.LENGTH_SHORT).show();

                                makeNetworkCall();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }


            } else {


                int quantity = Integer.parseInt(itemQuantity.getText().toString());

                if(quantity==0)
                {
                    // Delete from cart

                    //UtilityGeneral.getEndUserID(MyApplication.getAppContext())
                    EndUser endUser = UtilityLogin.getEndUser(context);
                    if(endUser==null)
                    {
                        return;
                    }

                    Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItem.getItemID(),
                            endUser.getEndUserID(),
                            dataset.get(getLayoutPosition()).getShopID()
                    );

                    callDelete.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                            if(response.code()==200)
                            {

                                showToastMessage("Item Removed !");

                                addToCartText.setText("Add to Cart");

                                //makeNetworkCall();

                                notifyFilledCart.notifyCartDataChanged();

                            }else
                            {
                                makeNetworkCall();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });


                }
                else
                {
                    // Update from cart

                    //UtilityGeneral.getEndUserID(MyApplication.getAppContext())
                    EndUser endUser = UtilityLogin.getEndUser(context);

                    if(endUser==null)
                    {
                        return;
                    }

                    if(getLayoutPosition() < dataset.size())
                    {
                        ShopItem shop = dataset.get(getLayoutPosition());

                        Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                                cartItem,
                                endUser.getEndUserID(),
                                shop.getShopID()
                        );

                        callUpdate.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                                if (response.code() == 200) {
                                    Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    }

                }
            }
        }



        double cartTotalNeutral(){

            double previousTotal = 0;

            if(cartItem!=null && shopItem!=null)
            {
                previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
            }

            double cartTotalValue = 0;

            CartStats cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());

            if(cartStats!=null)
            {
                cartTotalValue = cartStats.getCart_Total();
            }

            return (cartTotalValue - previousTotal);
        }


        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {
            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());
            cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());



            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){


                try{

                    if(Integer.parseInt(itemQuantity.getText().toString())<=0) {

                        if (cartItem == null) {

                            itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                        } else
                        {
                            itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() - 1) + " " + "Items in Cart");

                        }

                        return;
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) - 1));

                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }

                cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));
                itemTotal.setText("Total : " + String.format( "%.2f", total));

            }else
            {

                itemQuantity.setText(String.valueOf(0));

                itemTotal.setText("Total : " + String.format( "%.2f", total));
            }

        }

        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {
            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());
            cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());


            int availableItems = shopItem.getAvailableItemQuantity();

            double total = 0;


            if (!itemQuantity.getText().toString().equals("")) {


                if(cartItem==null)
                {
                    if(Integer.parseInt(itemQuantity.getText().toString())>0)
                    {
                        itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");

                    }

                }
                else
                {

                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                }


                try {

                    if (Integer.parseInt(itemQuantity.getText().toString()) >= availableItems) {
                        return;
                    }


                        itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) + 1));


                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }catch (Exception ex)
                {

                }

                itemTotal.setText("Total : " + String.format("%.2f", total));

                cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));


            }else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + String.format( "%.2f", total));
            }
        }



    }// View Holder Ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }



    public interface NotifyFilledCart
    {
        void notifyCartDataChanged();
    }

}
