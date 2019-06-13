package org.nearbyshops.enduserappnew.Backups;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemsInShopByCategoryDeprecated.ItemsInShopByCatFragmentDeprecated;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderItemsList;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.InputFilterMinMax;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemsInShopBackup extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

//    Map<Integer,ShopItemParcelable> shopItemMap = new HashMap<>();
//    Map<Integer,Item> selectedItems = new HashMap<>();

    private Map<Integer, CartItem> cartItemMap = new HashMap<>();
    private Map<Integer, CartStats> cartStatsMap = new HashMap<>();



    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_SHOP_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    private Fragment fragment;



    public AdapterItemsInShopBackup(List<Object> dataset,
                                    Context context,
                                    NotificationsFromAdapter notificationReceiver,
                                    Fragment fragment
    )
    {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;

        getCartStats(false,0,true);
    }



    void getCartStats(final boolean notifyChange, final int position, final boolean notifyDatasetChanged)
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
            return new ViewHolderItemCategory(view);
        }
        else if(viewType == VIEW_TYPE_SHOP_ITEM)
        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_experimental,parent,false);
//            return new ViewHolderShopItems(view);


            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop_item_by_shop,parent,false);

            return new ViewHolderShopItem(view);

        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);

        }


//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderItemCategory)
        {
            bindItemCategory((ViewHolderItemCategory) holder,position);
        }
        else if(holder instanceof ViewHolderShopItem)
        {
            bindShopItems((ViewHolderShopItem) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }
        else if(holder instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
            {
//                int fetched_count  = ((ItemsInShopByCatFragmentDeprecated) fragment).fetched_items_count;
//                int items_count = ((ItemsInShopByCatFragmentDeprecated) fragment).item_count_item;

//                if(fetched_count == items_count)
//                {
//                    viewHolder.progressBar.setVisibility(View.GONE);
//                }
//                else
//                {
//                    viewHolder.progressBar.setVisibility(View.VISIBLE);
//                    viewHolder.progressBar.setIndeterminate(true);
//
//                }
            }
        }


    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }




    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @BindView(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolderShopItem Class declaration ends



    void bindItemCategory(ViewHolderItemCategory holder,int position)
    {

        if(dataset.get(position) instanceof ItemCategory)
        {
            ItemCategory itemCategory = (ItemCategory) dataset.get(position);

            holder.categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";



            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());



            Picasso.get().load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.categoryImage);

        }
    }



    class ViewHolderItemCategory extends RecyclerView.ViewHolder{


        @BindView(R.id.name)
        TextView categoryName;
        @BindView(R.id.itemCategoryListItem)
        ConstraintLayout itemCategoryListItem;
        @BindView(R.id.categoryImage)
        ImageView categoryImage;
        @BindView(R.id.cardview)
        CardView cardView;

        public ViewHolderItemCategory(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.notifyRequestSubCategory(
                    (ItemCategory) dataset.get(getLayoutPosition()));

//            selectedItems.clear();
        }


    }// ViewHolderShopItem Class declaration ends





    void bindShopItems(ViewHolderShopItem holder,int position)
    {
        if(dataset.get(position) instanceof ShopItem)
        {
            ShopItem shopItem = (ShopItem) dataset.get(position);

            Item item = shopItem.getItem();

//        holder.shopName.setText(dataset.get(position).getShopName());
//        holder.rating.setText(String.format( "%.2f", dataset.get(position).getRt_rating_avg()));
//        holder.distance.setText(String.format( "%.2f", dataset.get(position).getDistance() )+ " Km");



            CartItem cartItem = cartItemMap.get(shopItem.getItemID());

            if(cartItem!=null)
            {
                holder.itemQuantity.setText(String.valueOf(cartItem.getItemQuantity()));
                holder.shopItemListItem.setBackgroundResource(R.color.gplus_color_2Alpha);

                double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

                holder.itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
                holder.addToCartText.setText("Update Cart");

            }else
            {

                holder.shopItemListItem.setBackgroundResource(R.color.colorWhite);
                //holder.shopItemListItem.setBackgroundColor(22000000);
                holder.itemQuantity.setText(String.valueOf(0));
                holder.addToCartText.setText("Add to Cart");
            }




            if(shopItem.getAvailableItemQuantity()==0)
            {
                holder.outOfStockIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.outOfStockIndicator.setVisibility(View.GONE);
            }



            holder.available.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()) + " " + item.getQuantityUnit());



            String imagePath = null;

            if(item!=null)
            {
                String currency = "";
                currency = PrefGeneral.getCurrencySymbol(context);

                holder.itemName.setText(item.getItemName());
                holder.itemPrice.setText(currency + " " + String.format("%.2f",shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

                if(item.getRt_rating_count()==0)
                {
                    holder.rating.setText(" - ");
                    holder.ratinCount.setText("(0 Ratings)");

                }
                else
                {
                    holder.rating.setText(String.format("%.1f",item.getRt_rating_avg()));
                    holder.ratinCount.setText("( " +  String.valueOf((int)item.getRt_rating_count()) +  " Ratings )");
                }



//                imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                        + item.getItemImageURL();

                imagePath = PrefGeneral.getServiceURL(context)
                        + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";

            }


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());


            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.itemImage);
        }

    }




    public class ViewHolderShopItem extends RecyclerView.ViewHolder{


        @BindView(R.id.add_to_cart_text) TextView addToCartText;
        @BindView(R.id.progress_bar) ProgressBar progressBar;

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


        @BindView(R.id.out_of_stock_indicator)
        TextView outOfStockIndicator;


        @BindView(R.id.list_item)
        CardView shopItemListItem;


        ShopItem shopItem;
        CartItem cartItem;
        CartStats cartStats;





        @OnClick(R.id.item_image)
        void itemImageClick()
        {
            if(dataset.get(getLayoutPosition()) instanceof ShopItem)
            {
                ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
                Item item = shopItem.getItem();

                if(item!=null)
                {
                    notificationReceiver.notifyItemImageClick(item);
                }
            }

        }








        public ViewHolderShopItem(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    setFilter();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    shopItem = (ShopItem) dataset.get(getLayoutPosition());
                    cartItem = cartItemMap.get(shopItem.getItemID());
                    cartStats = cartStatsMap.get(shopItem.getShopID());

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


                                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                    {
                                        ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                                    }

                                }else
                                {

                                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                    {
                                        ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");
                                        addToCartText.setText("Remove Item");

//                                        addToCartText.setBackgroundColor(ContextCompat.getColor(context,R.color.deepOrange900));
                                    }
                                }

                            }else
                            {
                                if(cartItem==null)
                                {
                                    // no shop exist

                                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                    {
                                        ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                                    }


                                }else
                                {
                                    // shop Exist

                                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                    {
                                        ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                                    }

                                    addToCartText.setText("Update Cart");
                                }
                            }

                        }
                        catch (Exception ex)
                        {
                            //ex.printStackTrace();
                        }

                    }

                    itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                    {
                        ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
        }




        @OnClick(R.id.add_to_cart_text)
        void addToCartClick(View view) {

            ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());

            CartItem cartItem = new CartItem();
            cartItem.setItemID(shopItem.getItemID());

            if (!itemQuantity.getText().toString().equals("")) {

                cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
            }

            if (!cartItemMap.containsKey(shopItem.getItemID()))
            {


                if (itemQuantity.getText().toString().equals("")){

                    showToastMessage("Please select quantity !");
                }
                else if (Integer.parseInt(itemQuantity.getText().toString()) == 0) {
                    showToastMessage("Please select quantity greater than Zero !");

                } else {

                    //showToastMessage("Add to cart! : " + dataset.get(getLayoutPosition()).getShopID());

                    User endUser = PrefLogin.getUser(context);
                    if(endUser==null)
                    {

//                        Toast.makeText(context, "Please LoginUsingOTP to continue ...", Toast.LENGTH_SHORT).show();
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

                    addToCartText.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            if (response.code() == 201) {

                                Toast.makeText(context, "Add to cart successful !", Toast.LENGTH_SHORT).show();

                                getCartStats(true,getLayoutPosition(),false);

                                addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                                itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                            }


                            addToCartText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            addToCartText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }


            }
            else
            {

                if(itemQuantity.getText().toString().equals(""))
                {
                    return;
                }

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
                            shopItem.getShopID()
                    );

                    addToCartText.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    callDelete.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            addToCartText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);


                            if(response.code()==200)
                            {

                                showToastMessage("Item Removed !");

                                addToCartText.setText("Add to Cart");

                                getCartStats(true,getLayoutPosition(),false);

                                //makeNetworkCall();

//                                notifyFilledCart.notifyCartDataChanged();

                                addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                                itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));

                            }else
                            {

                            }

                            progressBar.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {


                            addToCartText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);


                            progressBar.setVisibility(View.INVISIBLE);
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
                        ShopItem shop = (ShopItem) dataset.get(getLayoutPosition());

                        Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                                cartItem,
                                endUser.getUserID(),
                                shop.getShopID()
                        );

                        addToCartText.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        callUpdate.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                                if (response.code() == 200) {

                                    Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();
                                    getCartStats(false,getLayoutPosition(),false);

                                    addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                                    itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                                }

                                addToCartText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                addToCartText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                }
            }
        }




        void setFilter() {

            if (getLayoutPosition() != -1) {

                shopItem = (ShopItem) dataset.get(getLayoutPosition());
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

            shopItem = (ShopItem) dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(shopItem.getItemID());
            cartStats = cartStatsMap.get(shop.getShopID());

            addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_1));
//            itemTotal.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){


                try{

                    if(Integer.parseInt(itemQuantity.getText().toString())<=0) {

                        if (cartItem == null) {


                            if(cartStats==null)
                            {


                                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                {
                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
                                }
                            }
                            else
                            {

                                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                                {
                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");;
                                }


                            }


                        } else
                        {
                            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                            {
                                ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() - 1) + " " + "Items in Cart");
                            }
                        }

                        return;
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) - 1));

                    total = shopItem.getItemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }




                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                {
                    ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
                }



                itemTotal.setText("Total : " + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

            }else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
            }

        }




        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {
            Shop shop = PrefShopHome.getShop(context);

            shopItem = (ShopItem) dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(shopItem.getItemID());
            cartStats = cartStatsMap.get(shop.getShopID());

            //dataset.get(getLayoutPosition()).getShopID()

            addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_1));
//            itemTotal.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


            int availableItems = shopItem.getAvailableItemQuantity();
            double total = 0;


            if (!itemQuantity.getText().toString().equals("")) {


                if(cartItem==null)
                {
                    if(Integer.parseInt(itemQuantity.getText().toString())>0 )
                    {

                        if(cartStats==null)
                        {
                            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                            {
                                ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                            }
                        }
                        else
                        {

                            if(fragment instanceof ItemsInShopByCatFragmentDeprecated) {

                                ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                            }



                        }

                    }

                }
                else
                {


                    if(fragment instanceof ItemsInShopByCatFragmentDeprecated) {

                        ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                    }
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

                itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f", total));


                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
                {
                    ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
                }



            }else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
            }
        }


    }// View Holder Ends











    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }




    private void showLoginDialog()
    {

//        if(context instanceof AppCompatActivity)
//        {
//            FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.show(fm,"serviceUrl");
//        }

        
        notificationReceiver.showLogin();
    }




    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemImageClick(Item item);
        void showLogin();
    }



}