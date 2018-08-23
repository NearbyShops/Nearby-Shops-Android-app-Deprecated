package org.nearbyshops.enduserappnew.OrderDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.ModelCartOrder.OrderItem;
import org.nearbyshops.enduserappnew.ModelCartOrder.OrderStats;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Utility.UtilityOrderStatus;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopDetail.ShopDetail;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterOrderDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;
//    private NotifyConfirmOrder notifyConfirmOrder;

    public static final int TAG_VIEW_HOLDER_ORDER = 1;
    public static final int TAG_VIEW_HOLDER_ORDER_ITEM = 2;

    private Context context;
    NotifyItemClick notifyItemClick;


    AdapterOrderDetail(List<Object> dataset, Context context,NotifyItemClick notifyItemClick) {
        this.dataset = dataset;
        this.context = context;
        this.notifyItemClick = notifyItemClick;
//        this.notifyConfirmOrder = notifyConfirmOrder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==TAG_VIEW_HOLDER_ORDER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_order_order_detail,parent,false);

            return new ViewHolderOrder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_order_item_order_detail,parent,false);

            return new ViewHolderOrderItem(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderOrder)
        {
            bindOrder((ViewHolderOrder) holder,position);
        }
        else if(holder instanceof ViewHolderOrderItem)
        {
            bindOrderItem((ViewHolderOrderItem)holder,position);
        }
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(dataset.get(position) instanceof Order)
        {
            return TAG_VIEW_HOLDER_ORDER;
        }
        else if(dataset.get(position) instanceof OrderItem)
        {
            return TAG_VIEW_HOLDER_ORDER_ITEM;
        }

        return -1;
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolderOrder extends RecyclerView.ViewHolder{

        @BindView(R.id.order_id) TextView orderID;
        @BindView(R.id.dateTimePlaced) TextView dateTimePlaced;
        @BindView(R.id.deliveryAddressName) TextView deliveryAddressName;
        @BindView(R.id.deliveryAddress) TextView deliveryAddress;
        @BindView(R.id.deliveryAddressPhone) TextView deliveryAddressPhone;
        @BindView(R.id.numberOfItems) TextView numberOfItems;
        @BindView(R.id.orderTotal) TextView orderTotal;
        @BindView(R.id.currentStatus) TextView currentStatus;


        // order Summary Views

//        @BindView(R.id.item_total) TextView itemTotal;
//        @BindView(R.id.delivery_charges) TextView deliveryCharges;
//        @BindView(R.id.order_total_summary) TextView orderTotalSummary;


//        @Bind(R.id.confirmOrderButton)
//        TextView confirmOrderButton;


        @BindView(R.id.shop_name) TextView shopName;
        @BindView(R.id.shop_address) TextView shopAddress;
        @BindView(R.id.shop_logo) ImageView shopLogo;
        @BindView(R.id.delivery) TextView delivery;
        @BindView(R.id.distance) TextView distance;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.rating_count) TextView rating_count;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.shop_info_card)
        ConstraintLayout list_item;



        public ViewHolderOrder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);


        }


        @OnClick(R.id.shop_info_card)
        void shopDetailsClick()
        {
            if(dataset.get(getLayoutPosition()) instanceof Order)
            {
                Order order = (Order)dataset.get(getLayoutPosition());
                Shop shop = order.getShop();

//                Intent shopHomeIntent = new Intent(context, ShopHome.class);
//                UtilityShopHome.saveShop(shop,context);
//                context.startActivity(shopHomeIntent);


                Intent intent = new Intent(context, ShopDetail.class);
                intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,shop);
                context.startActivity(intent);
            }


        }

/*
        @OnClick(R.id.confirmOrderButton)
        void onClickConfirmButton(View view)
        {
            notifyConfirmOrder.notifyConfirmOrder(dataset.get(getLayoutPosition()));
        }
*/


/*
        @OnClick(R.id.close_button)
        void closeButton(View view)
        {
            notifyConfirmOrder.notifyCancelOrder(dataset.get(getLayoutPosition()));
        }
*/
    }


    private void bindOrder(ViewHolderOrder holder, int position)
    {
        if(dataset!=null)
        {
//            if(dataset.size() <= position)
//            {
//                return;
//            }


            Order order = (Order)dataset.get(position);
            DeliveryAddress deliveryAddress = order.getDeliveryAddress();
            OrderStats orderStats = order.getOrderStats();
            Shop shop = order.getShop();

            holder.orderID.setText("Order ID : " + order.getOrderID());
            holder.dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                    + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + String.valueOf(PrefGeneral.getCurrencySymbol(context)) + " " + String.valueOf(orderStats.getItemTotal() + order.getDeliveryCharges()));
            //holder.currentStatus.setText();


            String status = UtilityOrderStatus.getStatus(order.getStatusHomeDelivery(),order.getDeliveryReceived(),order.getPaymentReceived());
            holder.currentStatus.setText("Current Status : " + status);



            // bind shop Summary Views

//            holder.itemTotal.setText("Item Total : " + String.valueOf(orderStats.getItemTotal()));
//            holder.deliveryCharges.setText("Delivery Charges : " + String.valueOf(order.getDeliveryCharges()));
//            holder.orderTotalSummary.setText("Total : " + String.valueOf(orderStats.getItemTotal() + order.getDeliveryCharges()));
//

            if(shop!=null)
            {
                holder.shopName.setText(shop.getShopName());

                if(shop.getShopAddress()!=null)
                {
                    holder.shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
                }

//                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                        + shop.getLogoImagePath();

                String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                        + shop.getLogoImagePath() + ".jpg";

                Drawable placeholder = VectorDrawableCompat
                        .create(context.getResources(),
                                R.drawable.ic_nature_people_white_48px, context.getTheme());

                Picasso.with(context)
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.shopLogo);

                holder.delivery.setText("Delivery : Rs " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
                holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


                if(shop.getRt_rating_count()==0)
                {
                    holder.rating.setText("N/A");
                    holder.rating_count.setText(" - ");

                }
                else
                {
                    holder.rating.setText(String.valueOf(shop.getRt_rating_avg()));
                    holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
                }


                if(shop.getShortDescription()!=null)
                {
                    holder.description.setText(shop.getShortDescription());
                }

            }
        }
    }





    class ViewHolderOrderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.itemImage)
        ImageView itemImage;

        @BindView(R.id.itemName)
        TextView itemName;

        @BindView(R.id.quantity)
        TextView quantity;

        @BindView(R.id.pincode)
        TextView itemPrice;

        @BindView(R.id.item_total)
        TextView itemTotal;

//        @Bind(R.id.item_rating)
//        TextView itemRating;

//        @Bind(R.id.rating_count)
//        TextView ratingCount;

        @BindView(R.id.item_id)
        TextView itemID;



        ViewHolderOrderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(dataset.get(getLayoutPosition()) instanceof OrderItem)
            {
                OrderItem orderItem = (OrderItem) dataset.get(getLayoutPosition());
                Item item = orderItem.getItem();
                notifyItemClick.notifyItemClicked(item);
            }
        }
    }



    private void bindOrderItem(ViewHolderOrderItem holder, int position)
    {
        if(!(dataset.get(position) instanceof OrderItem))
        {
//            return;
        }


        OrderItem orderItem = (OrderItem) dataset.get(position);
        Item item = orderItem.getItem();

        holder.itemID.setText("Item ID : " + String.valueOf(orderItem.getItemID()));

        holder.itemName.setText(item.getItemName());
        holder.quantity.setText("Item Quantity : " + String.valueOf(orderItem.getItemQuantity()) + " "  + item.getQuantityUnit());
        holder.itemPrice.setText("Item Price : " + String.valueOf(PrefGeneral.getCurrencySymbol(context)) + " " + String.valueOf(orderItem.getItemPriceAtOrder())+ " per "  + item.getQuantityUnit());

        holder.itemTotal.setText("Item Total : " + String.valueOf(PrefGeneral.getCurrencySymbol(context)) + " " + String.valueOf(orderItem.getItemPriceAtOrder()*orderItem.getItemQuantity()));



        // bind Item Image

//        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext()) + item.getItemImageURL();


        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.nature_people, context.getTheme());

        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.itemImage);

    }





    interface NotifyItemClick{
        void notifyItemClicked(Item item);
    }

}
