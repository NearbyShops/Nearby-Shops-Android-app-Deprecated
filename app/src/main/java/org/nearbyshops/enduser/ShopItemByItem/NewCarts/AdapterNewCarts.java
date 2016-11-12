package org.nearbyshops.enduser.ShopItemByItem.NewCarts;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ItemDetail.ItemDetail;
import org.nearbyshops.enduser.ItemsByCategoryScreenTwo.Items.AdapterItemHorizontalScreen;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Model.CartItem;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduser.ShopItemByItem.ShopsForItemSwipe;
import org.nearbyshops.enduser.Shops.ListFragment.AdapterShopTwo;
import org.nearbyshops.enduser.Shops.ListFragment.FragmentShopTwo;
import org.nearbyshops.enduser.Utility.InputFilterMinMax;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

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
public class AdapterNewCarts extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;

    @Inject
    CartItemService cartItemService;
    private NotifyAddToCart notifyAddToCart;
    private Item item;
    private AppCompatActivity activity;

    private Fragment fragment;

    private final int VIEW_TYPE_ITEM_HEADER = 5;
    private final int VIEW_TYPE_SHOP_ITEMS = 6;


    public AdapterNewCarts(List<Object> dataset, Context context, NotifyAddToCart callbacks, Item item, AppCompatActivity activity, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.notifyAddToCart = callbacks;
        this.item = item;
        this.activity = activity;
        this.fragment = fragment;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;

        if(viewType == VIEW_TYPE_SHOP_ITEMS)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop_item_layout,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_ITEM_HEADER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_header,parent,false);

            return new ViewHolderItem(view);
        }
        else if(viewType == 1)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new AdapterNewCarts.LoadingViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new AdapterNewCarts.LoadingViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_given, int position) {


        if(holder_given instanceof ViewHolder) {

            ViewHolder holder = (ViewHolder) holder_given;

            ShopItem shopItem = (ShopItem) dataset.get(position);

            Shop shop = null;

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
                holder.deliveryCharge.setText("Delivery :Rs " + String.format( "%.0f", shop.getDeliveryCharges()) + "Per Order");

            }

            if(shopItem !=null)
            {
                holder.itemsAvailable.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()));
                holder.itemPrice.setText("Rs " + String.format( "%.2f", shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

            }


        }
        else if(holder_given instanceof LoadingViewHolder)
        {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder_given;

            int itemCount = 0;

            if(fragment instanceof NewCartsFragment)
            {
                itemCount = ((NewCartsFragment) fragment).getItemCount();
            }


            if(position == 0 || position == itemCount+1)
            {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);

            }
        }
        else if(holder_given instanceof ViewHolderItem)
        {
            bindItemHolder((ViewHolderItem) holder_given,position);
        }
    }

    @Override
    public int getItemCount() {
        return (dataset.size()+ 1);
    }



    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return 1;
        }
        else if (dataset.get(position) instanceof ShopItem) {

            return VIEW_TYPE_SHOP_ITEMS;
        }
        else if (dataset.get(position) instanceof Item) {

            return VIEW_TYPE_ITEM_HEADER;
        }
        else
        {
            return  -1;
        }

        /*else if(position == dataset.size())
        {
            return 1;
        }*/

    }

    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }




    public class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        //        TextView itemDescription;
        TextView itemName;
        ImageView itemImage;
        TextView priceRange;
        TextView priceAverage;
        TextView shopCount;

        TextView itemRating;
        TextView ratingCount;


        ConstraintLayout itemsListItem;

        public ViewHolderItem(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

//            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            priceRange = (TextView) itemView.findViewById(R.id.price_range);
            priceAverage = (TextView) itemView.findViewById(R.id.price_average);
            shopCount = (TextView) itemView.findViewById(R.id.shop_count);
            itemsListItem = (ConstraintLayout) itemView.findViewById(R.id.items_list_item);

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
//                        Intent intent = new Intent(context, ShopsForItemSwipe.class);
//                        intent.putExtra(ShopsForItemSwipe.ITEM_INTENT_KEY,dataset.get(getLayoutPosition()));
//                        context.startActivity(intent);
                    }

                    break;


                default:

                    break;
            }

        }
    }



    void bindItemHolder(ViewHolderItem holder,int position)
    {

        if(position >= dataset.size())
        {
            return;
        }

        Item item = (Item) dataset.get(position);

        String imagePath = "http://example.com";

        if(item!=null)
        {
            holder.itemName.setText(item.getItemName());
//                holder.itemDescription.setText(item.getItemDescription());


            if(item.getRt_rating_count()==0)
            {
                holder.itemRating.setText(" - ");
                holder.ratingCount.setText("( not yet rated )");
            }
            else
            {
                holder.itemRating.setText(String.format("%.1f",item.getRt_rating_avg()));
                holder.ratingCount.setText("( " + String.valueOf((int)item.getRt_rating_count()) + " ratings )");
            }




            imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + ((Item)dataset.get(position)).getItemImageURL();
        }


        if(((Item)dataset.get(position)).getItemStats()!=null)
        {
            ItemStats itemStats = ((Item)dataset.get(position)).getItemStats();

            String shop = "Shops";

            if(itemStats.getShopCount()==1)
            {
                shop = "Shop";
            }

            holder.shopCount.setText("Available In " + String.valueOf(itemStats.getShopCount()) + " " + shop);
            holder.priceRange.setText( "Price Range :\nRs. "
                    + String.valueOf(itemStats.getMin_price())
                    + " - "
                    + String.valueOf(itemStats.getMax_price())
                    + " per " + ((Item)dataset.get(position)).getQuantityUnit()
            );

            holder.priceAverage.setText("Price Average : Rs. " + String.format("%.2f",itemStats.getAvg_price()));
        }



        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(holder.itemImage);

    }




    class ViewHolder extends RecyclerView.ViewHolder implements Callback<ResponseBody>, TextWatcher {


        @Bind(R.id.rating)
        TextView rating;

        @Bind(R.id.ratings_count)
        TextView ratingCount;


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

                shopItem = (ShopItem) dataset.get(getLayoutPosition());
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

            shopItem = (ShopItem) dataset.get(getLayoutPosition());

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

            shopItem = (ShopItem) dataset.get(getLayoutPosition());


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



            if(UtilityLogin.getEndUser(activity)==null)
            {

                Toast.makeText(context, "Please Login to continue ...", Toast.LENGTH_SHORT).show();
                showLoginDialog();
                return;
            }


            EndUser endUser = UtilityLogin.getEndUser(context);

            CartItem cartItem = new CartItem();

            cartItem.setItemID(((ShopItem)dataset.get(getLayoutPosition())).getItemID());
            cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));

            Call<ResponseBody> call = cartItemService.createCartItem(
                    cartItem,
                    endUser.getEndUserID(),
                    ((ShopItem)dataset.get(getLayoutPosition())).getShopID()
            );

            //UtilityGeneral.getEndUserID(MyApplication.getAppContext())

            call.enqueue(this);

        }



        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.code()==201)
            {
                Toast.makeText(context,"Add to cart. Successful !",Toast.LENGTH_SHORT).show();

                notifyAddToCart.notifyAddToCart();
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

            shopItem = (ShopItem) dataset.get(getLayoutPosition());




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


    public interface NotifyAddToCart {

        void notifyAddToCart();

    }


    private void showLoginDialog()
    {
        FragmentManager fm = activity.getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }



}
