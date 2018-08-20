package org.nearbyshops.enduserappnew.Carts.CartItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduserappnew.Preferences.InputFilterMinMax;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 6/6/16.
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{


    List<CartItem> dataset = null;


    Context context;
    NotifyCartItem notifyCartItem;

    CartStats cartStats;

    double cartTotal = 0;


    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;


    public CartItemAdapter() {

    }




    public CartItemAdapter(List<CartItem> dataset, Context context, NotifyCartItem notifyCartItem) {

        this.dataset = dataset;
        this.context = context;
        this.notifyCartItem = notifyCartItem;
//        this.cartStats = cartStats;


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    void setCartStats(CartStats cartStats)
    {
        this.cartStats = cartStats;
        cartTotal = cartStats.getCart_Total();
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart_item_new,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CartItem cartItem = dataset.get(position);

//        String imagePath = null;

        Item item = null;

        if(cartItem!=null)
        {
            item = cartItem.getItem();
        }

        if(item != null)
        {
            holder.itemName.setText(item.getItemName());

            holder.itemQuantity.setText(String.valueOf(cartItem.getItemQuantity()));
            holder.itemPrice.setText("Price : " + String.format( "%.2f", cartItem.getRt_itemPrice()) + " per " + item.getQuantityUnit());

            //holder.itemTotal.setText(" x " + cartItem.getRt_availableItemQuantity() + " (Unit Price) = "
            //        + "Rs:" +  String.format( "%.2f", cartItem.getRt_itemPrice()*cartItem.getItemQuantity()));


            holder.itemTotal.setText("Total "
                    + String.format( "%.2f", cartItem.getRt_itemPrice()*cartItem.getItemQuantity()));

            holder.itemsAvailable.setText("Available : " + String.valueOf(cartItem.getRt_availableItemQuantity()));



//            imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                    + item.getItemImageURL();

            String imagePath = PrefGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());



            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.shopImage);
        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {

        ImageView shopImage;
        TextView rating;
        TextView itemName;
        TextView itemsAvailable;
        TextView itemPrice;
        TextView itemTotal;
        ImageView increaseQuantity;
        ImageView reduceQuantity;
        EditText itemQuantity;
        TextView updateButton;
        TextView removeButton;
        ProgressBar progressBarRemove;
        ProgressBar progressBarUpdate;


        public ViewHolder(View itemView) {
            super(itemView);

            updateButton = (TextView) itemView.findViewById(R.id.textUpdate);
            removeButton = (TextView) itemView.findViewById(R.id.textRemove);
            reduceQuantity = (ImageView) itemView.findViewById(R.id.reduceQuantity);
            increaseQuantity = (ImageView) itemView.findViewById(R.id.increaseQuantity);
            itemTotal = (TextView) itemView.findViewById(R.id.itemTotal);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemsAvailable = (TextView) itemView.findViewById(R.id.itemsAvailable);
            rating = (TextView) itemView.findViewById(R.id.rating);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            shopImage = (ImageView) itemView.findViewById(R.id.itemImage);

            itemQuantity = (EditText) itemView.findViewById(R.id.itemQuantity);

            itemQuantity.addTextChangedListener(this);
            reduceQuantity.setOnClickListener(this);
            increaseQuantity.setOnClickListener(this);
            updateButton.setOnClickListener(this);
            removeButton.setOnClickListener(this);

            progressBarRemove = (ProgressBar) itemView.findViewById(R.id.progress_bar_remove);
            progressBarUpdate = (ProgressBar) itemView.findViewById(R.id.progress_bar_update);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.textRemove:

                    removeClick();

                    break;

                case R.id.textUpdate:

                    updateClick();

                    break;

                case R.id.reduceQuantity:

                    reduceQuantityClick();

                    break;

                case R.id.increaseQuantity:

                    increaseQuantityClick();

                    break;


                default:
                    break;
            }

        }



        public void removeClick()
        {

//            notifyCartItem.notifyRemove(dataset.get(getLayoutPosition()));



            progressBarRemove.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.INVISIBLE);

            final CartItem cartItem = dataset.get(getLayoutPosition());

            Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code() == 200)
                    {
                        showToastMessage("Item Deleted");

                        // refresh the list
//                        makeNetworkCall();
                        notifyCartItem.notifyRemove(dataset.get(getLayoutPosition()));
                    }


                    notifyItemRemoved(getLayoutPosition());
                    dataset.remove(cartItem);

                    progressBarRemove.setVisibility(View.INVISIBLE);
                    removeButton.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Remove failed. Try again !");

                    progressBarRemove.setVisibility(View.INVISIBLE);
                    removeButton.setVisibility(View.VISIBLE);
                }
            });
        }




        public void updateClick()
        {

//            notifyCartItem.notifyUpdate(dataset.get(getLayoutPosition()));

            progressBarUpdate.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.INVISIBLE);

            final CartItem cartItem = dataset.get(getLayoutPosition());

            Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code() == 200)
                    {
                        notifyCartItem.notifyUpdate(cartItem);
                    }


                    progressBarUpdate.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Update failed. Try again !");


                    progressBarUpdate.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                }
            });

        }



        public void reduceQuantityClick()
        {
            setFilter();

            CartItem cartItem = dataset.get(getLayoutPosition());

            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){

                try{

                    if(Integer.parseInt(itemQuantity.getText().toString())<=0) {

                        return;
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) - 1));

                    total = cartItem.getRt_itemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }

                itemTotal.setText("Total : " + String.format( "%.2f", total));

            }else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + String.format( "%.2f", total));
            }

        }



        public void increaseQuantityClick()
        {
            setFilter();

            CartItem cartItem = dataset.get(getLayoutPosition());

            int availableItems = cartItem.getRt_availableItemQuantity();

            double total = 0;

            if (!itemQuantity.getText().toString().equals("")) {

                try {


                    if (Integer.parseInt(itemQuantity.getText().toString()) >= availableItems) {
                        return;
                    }

                    itemQuantity.setText(String.valueOf(Integer.parseInt(itemQuantity.getText().toString()) + 1));

                    total = cartItem.getRt_itemPrice() * Integer.parseInt(itemQuantity.getText().toString());


                }catch (Exception ex)
                {

                }

                itemTotal.setText("Total : " + String.format("%.2f", total));

            }else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + String.format( "%.2f", total));
            }

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            setFilter();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CartItem cartItem;

            cartItem = dataset.get(getLayoutPosition());

            double previousTotal = cartItem.getRt_itemPrice()*cartItem.getItemQuantity();

            double total = 0;

            if (!itemQuantity.getText().toString().equals(""))
            {

                try{


                    total = cartItem.getRt_itemPrice() * Integer.parseInt(itemQuantity.getText().toString());

                    cartItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));


                    if(Integer.parseInt(itemQuantity.getText().toString())==0)
                    {


                        //itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");

                    }else
                    {

                        //itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                    }

                }
                catch (Exception ex)
                {
                    //ex.printStackTrace();
                }

                cartTotal = cartTotal - previousTotal + total;


            }else
            {
                //itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
            }


            //itemTotal.setText("Total : " + String.format( "%.2f", total));

            //itemTotal.setText(" x " + cartItem.getRt_itemPrice() + " (Unit Price) = "
            //        + "Rs:" + String.format( "%.2f", total));


            itemTotal.setText("Total " + String.format( "%.2f", total));




            notifyCartItem.notifyTotal(cartTotal);

                    //cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


        void setFilter() {

            CartItem cartItem = null;

            if (getLayoutPosition() != -1) {

                cartItem = dataset.get(getLayoutPosition());
            }

            if (cartItem != null) {
                int availableItems = cartItem.getRt_availableItemQuantity();

                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
            }

        }

    }



    public interface NotifyCartItem{

        void notifyUpdate(CartItem cartItem);

        void notifyRemove(CartItem cartItem);

        void notifyTotal(double total);



    }





    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}
