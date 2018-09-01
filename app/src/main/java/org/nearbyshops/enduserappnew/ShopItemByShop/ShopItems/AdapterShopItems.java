package org.nearbyshops.enduserappnew.ShopItemByShop.ShopItems;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduserappnew.Utility.InputFilterMinMax;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterShopItems extends RecyclerView.Adapter<AdapterShopItems.ViewHolder> {


    private Map<Integer,CartItem> cartItemMap = new HashMap<>();
    private Map<Integer,CartStats> cartStatsMap = new HashMap<>();


    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;




    private List<ShopItem> dataset = null;
    private Context context;

    private FragmentShopItemsByShop fragment;


    AdapterShopItems(List<ShopItem> dataset, Context context, FragmentShopItemsByShop fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;

        DaggerComponentBuilder.getInstance().getNetComponent().Inject(this);


        makeNetworkCall(false,0,true);
    }


    /*public void setStats(List<CartItem> cartItemList, List<CartStats> cartStatsList)
    {
        cartStatsMap.clear();

        for(CartStats cartStats: cartStatsList)
        {
            cartStatsMap.put(cartStats.getShopID(),cartStats);
        }

        cartItemList.clear();

        for(CartItem cartItem: cartItemList)
        {
            cartItemMap.put(cartItem.getCart().getShopID(),cartItem);
        }


        notifyDataSetChanged();
    }*/




    void makeNetworkCall(final boolean notifyChange, final int position, final boolean notifyDatasetChanged)
    {

        cartItemMap.clear();
        cartStatsMap.clear();

        User endUser = PrefLogin.getUser(context);

        if(endUser == null)
        {
            return;
        }


        Shop shop = PrefShopHome.getShop(context);


        Call<List<CartItem>> cartItemCall = cartItemService.getCartItem(null,null,
                endUser.getUserID(),shop.getShopID(),false);


        cartItemCall.enqueue(new Callback<List<CartItem>>() {

            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

                cartItemMap.clear();

                if(response.body()!=null)
                {
                    for(CartItem cartItem: response.body())
                    {
                        cartItemMap.put(cartItem.getItemID(),cartItem);
                    }
                }

                if(notifyChange)
                {
                    notifyItemChanged(position);
                }

                if(notifyDatasetChanged)
                {
                    notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

                Toast.makeText(context," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });




        Call<List<CartStats>> listCall = cartStatsService
                .getCart(endUser.getUserID(), null,shop.getShopID(),false,null,null);



        listCall.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

                cartStatsMap.clear();

                if(response.body()!=null)
                {
                    for(CartStats cartStats: response.body())
                    {
                        cartStatsMap.put(cartStats.getShopID(),cartStats);
                    }
                }

                if(notifyChange)
                {
                    notifyItemChanged(position);
                }



                if(notifyDatasetChanged)
                {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                Toast.makeText(context," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });


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



        CartItem cartItem = cartItemMap.get(dataset.get(position).getItemID());

        if(cartItem!=null)
        {
            holder.itemQuantity.setText(String.valueOf(cartItem.getItemQuantity()));
            holder.shopItemListItem.setBackgroundResource(R.color.gplus_color_2Alpha);

            double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

            holder.itemTotal.setText("Total : " + String.format( "%.2f", total));
            holder.addToCartText.setText("Update Cart");

        }else
        {

            holder.shopItemListItem.setBackgroundResource(R.color.colorWhite);
            //holder.shopItemListItem.setBackgroundColor(22000000);
            holder.itemQuantity.setText(String.valueOf(0));
            holder.addToCartText.setText("Add to Cart");
        }



        if(shopItem!=null)
        {
            holder.available.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()));

        }

        String imagePath = null;

        if(item!=null)
        {
            holder.itemName.setText(item.getItemName());
            holder.itemPrice.setText("Rs. " + String.format("%.2f",shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

            if(item.getRt_rating_count()==0)
            {
                holder.rating.setText("N/A");
                holder.ratinCount.setText("( Not yet rated )");

            }
            else
            {
                holder.rating.setText(String.format("%.1f",item.getRt_rating_avg()));
                holder.ratinCount.setText("( " +  String.valueOf((int)item.getRt_rating_count()) +  " Ratings )");
            }



            imagePath = PrefGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + item.getItemImageURL();


        }


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.itemImage);

//        holder.rating.setText(String.format("%.2f",));



    }




    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return dataset.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.add_to_cart_text)
        TextView addToCartText;

        @BindView(R.id.item_title)
        TextView itemName;

        @BindView(R.id.item_image)
        ImageView itemImage;

        @BindView(R.id.item_price)
        TextView itemPrice;

        @BindView(R.id.available)
        TextView available;

        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.rating_count)
        TextView ratinCount;

        @BindView(R.id.increaseQuantity)
        ImageView increaseQuantity;

        @BindView(R.id.itemQuantity)
        EditText itemQuantity;

        @BindView(R.id.reduceQuantity)
        ImageView reduceQuantity;

        @BindView(R.id.total)
        TextView itemTotal;

//        @BindView(R.id.add_to_cart_text)
//        TextView addToCart;

        @BindView(R.id.list_item)
        CardView shopItemListItem;


        ShopItem shopItem;
        CartItem cartItem;
        CartStats cartStats;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    setFilter();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    shopItem = dataset.get(getLayoutPosition());
                    cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getItemID());
                    cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());

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

                                    fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                                }else
                                {
                                    fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");
                                    addToCartText.setText("Remove Item");

                                }

                            }else
                            {
                                if(cartItem==null)
                                {
                                    // no shop exist

                                    fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");

                                }else
                                {
                                    // shop Exist

                                    fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

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
                    fragment.cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
        }




        @OnClick(R.id.add_to_cart_text)
        void addToCartClick(View view) {


            CartItem cartItem = new CartItem();
            cartItem.setItemID(dataset.get(getLayoutPosition()).getItemID());

            if (!itemQuantity.getText().toString().equals("")) {

                cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
            }

            if (!cartItemMap.containsKey(dataset.get(getLayoutPosition()).getItemID()))
            {

                if (Integer.parseInt(itemQuantity.getText().toString()) == 0) {
                    showToastMessage("Please select quantity greater than Zero !");

                } else {

                    //showToastMessage("Add to cart! : " + dataset.get(getLayoutPosition()).getShopID());

                    User endUser = PrefLogin.getUser(context);
                    if(endUser==null)
                    {

                        Toast.makeText(context, "Please Login to continue ...", Toast.LENGTH_SHORT).show();
                        showLoginDialog();

                        return;
                    }


                    Shop shop = PrefShopHome.getShop(context);

                    Call<ResponseBody> call = cartItemService.createCartItem(
                            cartItem,
                            endUser.getUserID(),
                            shop.getShopID()
                    );

                    //dataset.get(getLayoutPosition()).getShopID()

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            if (response.code() == 201) {

                                Toast.makeText(context, "Add to cart successful !", Toast.LENGTH_SHORT).show();

                                makeNetworkCall(true,getLayoutPosition(),false);
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
                    User endUser = PrefLogin.getUser(context);
                    if(endUser==null)
                    {
                        return;
                    }

                    Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItem.getItemID(),
                            endUser.getUserID(),
                            dataset.get(getLayoutPosition()).getShopID()
                    );

                    callDelete.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                            if(response.code()==200)
                            {

                                showToastMessage("Item Removed !");

                                addToCartText.setText("Add to Cart");

                                makeNetworkCall(true,getLayoutPosition(),false);

                                //makeNetworkCall();

//                                notifyFilledCart.notifyCartDataChanged();

                            }else
                            {

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
                    User endUser = PrefLogin.getUser(context);

                    if(endUser==null)
                    {
                        return;
                    }

                    if(getLayoutPosition() < dataset.size())
                    {
                        ShopItem shop = dataset.get(getLayoutPosition());

                        Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                                cartItem,
                                endUser.getUserID(),
                                shop.getShopID()
                        );

                        callUpdate.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                                if (response.code() == 200) {

                                    Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();
                                    makeNetworkCall(false,getLayoutPosition(),false);
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




        void setFilter() {

            if (getLayoutPosition() != -1) {

                shopItem = dataset.get(getLayoutPosition());
            }

            if (shopItem != null) {
                int availableItems = shopItem.getAvailableItemQuantity();

                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
            }
        }


        double cartTotalNeutral(){

            double previousTotal = 0;

            if(cartItem!=null && shopItem!=null)
            {
                previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
            }

            double cartTotalValue = 0;

            Shop shop = PrefShopHome.getShop(context);

            CartStats cartStats = cartStatsMap.get(shop.getShopID());

            if(cartStats!=null)
            {
                cartTotalValue = cartStats.getCart_Total();
            }

            return (cartTotalValue - previousTotal);
        }





        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {
            Shop shop = PrefShopHome.getShop(context);

            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getItemID());
            cartStats = cartStatsMap.get(shop.getShopID());



            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){


                try{

                    if(Integer.parseInt(itemQuantity.getText().toString())<=0) {

                        if (cartItem == null) {


                            if(cartStats==null)
                            {
                                fragment.itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
                            }
                            else
                            {
                                fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                            }


                        } else
                        {
                            fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() - 1) + " " + "Items in Cart");

                        }

                        return;
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) - 1));

                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }

                fragment.cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));
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
            Shop shop = PrefShopHome.getShop(context);

            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getItemID());
            cartStats = cartStatsMap.get(shop.getShopID());

            //dataset.get(getLayoutPosition()).getShopID()


            int availableItems = shopItem.getAvailableItemQuantity();
            double total = 0;


            if (!itemQuantity.getText().toString().equals("")) {


                if(cartItem==null)
                {
                    if(Integer.parseInt(itemQuantity.getText().toString())>0 )
                    {

                        if(cartStats==null)
                        {
                            fragment.itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                        }
                        else
                        {
                            fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                        }

                    }

                }
                else
                {

                    fragment.itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
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

                fragment.cartTotal.setText("Cart Total : Rs " + String.valueOf(cartTotalNeutral() + total));


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




    private void showLoginDialog()
    {

        if(context instanceof AppCompatActivity)
        {
//            FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.show(fm,"serviceUrl");
        }



        Intent intent = new Intent(context,Login.class);
        context.startActivity(intent);

    }


}
