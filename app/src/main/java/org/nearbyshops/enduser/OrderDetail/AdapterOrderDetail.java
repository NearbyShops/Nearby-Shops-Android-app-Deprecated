package org.nearbyshops.enduser.OrderDetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.ModelCartOrder.Order;
import org.nearbyshops.enduser.ModelCartOrder.OrderItem;
import org.nearbyshops.enduser.ModelCartOrder.OrderStats;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.Utility.UtilityOrderStatus;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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


        @Bind(R.id.order_id)
        TextView orderID;

        @Bind(R.id.dateTimePlaced)
        TextView dateTimePlaced;

        @Bind(R.id.deliveryAddressName)
        TextView deliveryAddressName;

        @Bind(R.id.deliveryAddress)
        TextView deliveryAddress;

        @Bind(R.id.deliveryAddressPhone)
        TextView deliveryAddressPhone;


        @Bind(R.id.numberOfItems)
        TextView numberOfItems;

        @Bind(R.id.orderTotal)
        TextView orderTotal;

        @Bind(R.id.currentStatus)
        TextView currentStatus;

//        @Bind(R.id.confirmOrderButton)
//        TextView confirmOrderButton;


        public ViewHolderOrder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);


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
            if(dataset.size() <= position)
            {
                return;
            }

            Order order = (Order)dataset.get(position);
            DeliveryAddress deliveryAddress = order.getDeliveryAddress();
            OrderStats orderStats = order.getOrderStats();

            holder.orderID.setText("Order ID : " + order.getOrderID());
            holder.dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                    + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
            //holder.currentStatus.setText();


            String status = UtilityOrderStatus.getStatus(order.getStatusHomeDelivery(),order.getDeliveryReceived(),order.getPaymentReceived());
            holder.currentStatus.setText("Current Status : " + status);

        }
    }



    class ViewHolderOrderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.itemImage)
        ImageView itemImage;

        @Bind(R.id.itemName)
        TextView itemName;

        @Bind(R.id.quantity)
        TextView quantity;

        @Bind(R.id.pincode)
        TextView itemPrice;

        @Bind(R.id.item_total)
        TextView itemTotal;

//        @Bind(R.id.item_rating)
//        TextView itemRating;

//        @Bind(R.id.rating_count)
//        TextView ratingCount;

        @Bind(R.id.item_id)
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

        holder.itemName.setText(item.getItemName());
        holder.quantity.setText("Item Quantity : " + String.valueOf(orderItem.getItemQuantity()) + " "  + item.getQuantityUnit());
        holder.itemPrice.setText("Item Price : " + String.valueOf(orderItem.getItemPriceAtOrder()));
        holder.itemTotal.setText("Item Total : " + String.valueOf(orderItem.getItemPriceAtOrder()*orderItem.getItemQuantity()));


        holder.itemID.setText("Item ID : " + String.valueOf(orderItem.getItemID()));


        // bind Item Image

//        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext()) + item.getItemImageURL();


        String imagePath = UtilityGeneral.getServiceURL(context)
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
