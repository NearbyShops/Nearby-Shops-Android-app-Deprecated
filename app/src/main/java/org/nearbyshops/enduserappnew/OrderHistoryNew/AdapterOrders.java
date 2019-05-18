package org.nearbyshops.enduserappnew.OrderHistoryNew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.enduserappnew.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Order> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;


    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;


    private Context context;
    private Fragment fragment;

    AdapterOrders(List<Order> dataset, NotifyConfirmOrder notifyConfirmOrder, Fragment fragment, Context context) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==VIEW_TYPE_ORDER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_order,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }

        return null;
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_ORDER;
        }

//        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolder)
        {
            if(dataset!=null)
            {
                ViewHolder holder = (ViewHolder) holderVH;

                Order order = dataset.get(position);
                DeliveryAddress deliveryAddress = order.getDeliveryAddress();
//                OrderStats orderStats = order.getOrderStats();

                holder.orderID.setText("Order ID : " + order.getOrderID());
                holder.dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());


                holder.deliveryAddressName.setText(deliveryAddress.getName());

                holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                        + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

                holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

//                holder.numberOfItems.setText(orderStats.getItemCount() + " Items");

                holder.numberOfItems.setText(order.getItemCount() + " Items");
//                holder.orderTotal.setText("| Total : " +String.valueOf(PrefGeneral.getCurrencySymbol(context)) + " " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
                holder.orderTotal.setText("| Total : " + PrefGeneral.getCurrencySymbol(context) + String.format( " %.2f" ,order.getNetPayable()));
                //holder.currentStatus.setText();


//                String status = UtilityOrderStatus.getStatus(order.getStatusHomeDelivery(),order.getDeliveryReceived(),order.getPaymentReceived());
                String status = "";



//                showLog("Order PickfromShop : " + String.valueOf(order.getPickFromShop()));
//                showLog("Order Status Home Delivery : "  + String.valueOf(order.getStatusHomeDelivery()));
//                showLog("Order Status Pick from Shop : " + String.valueOf(order.getStatusPickFromShop()));



                if(order.isPickFromShop())
                {

                    holder.isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
                    holder.isPickFromShop.setText("Pick from Shop");


                    status = OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop());

//                    showLog("Status : " + OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop()));

                    int statusCode = order.getStatusPickFromShop();

                    if (statusCode == OrderStatusPickFromShop.ORDER_PLACED ||
                            statusCode == OrderStatusPickFromShop.ORDER_CONFIRMED ||
                            statusCode == OrderStatusPickFromShop.ORDER_PACKED)
                    {
                        holder.closeButton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.closeButton.setVisibility(View.GONE);
                    }


                    if(statusCode==OrderStatusPickFromShop.CANCELLED)
                    {
                        holder.cancelledImage.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.cancelledImage.setVisibility(View.GONE);
                    }


                }
                else
                {
                    holder.isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
                    holder.isPickFromShop.setText("Home Delivery");

                    status = OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery());

//                    showLog("Status : " + OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery()));

                    int statusCode = order.getStatusHomeDelivery();

                    if (statusCode == OrderStatusHomeDelivery.ORDER_PLACED ||
                            statusCode == OrderStatusHomeDelivery.ORDER_CONFIRMED ||
                            statusCode == OrderStatusHomeDelivery.ORDER_PACKED)
                    {
                        holder.closeButton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.closeButton.setVisibility(View.GONE);
                    }



                    if(statusCode==OrderStatusHomeDelivery.CANCELLED_WITH_DELIVERY_GUY ||
                            statusCode==OrderStatusHomeDelivery.CANCELLED)
                    {
                        holder.cancelledImage.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.cancelledImage.setVisibility(View.GONE);
                    }

                }




                holder.currentStatus.setText("Current Status : " + status);
            }
        }
        else if(holderVH instanceof LoadingViewHolder)
        {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof OrdersFragmentNew)
            {
                int items_count = ((OrdersFragmentNew) fragment).item_count;

                if(dataset.size() == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }
    }



    void showLog(String message)
    {
        Log.d("order_status",message);
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





    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.order_id)
        TextView orderID;

        @BindView(R.id.dateTimePlaced)
        TextView dateTimePlaced;

        @BindView(R.id.is_pick_from_shop)
        TextView isPickFromShop;

        @BindView(R.id.deliveryAddressName)
        TextView deliveryAddressName;

        @BindView(R.id.deliveryAddress)
        TextView deliveryAddress;

        @BindView(R.id.deliveryAddressPhone)
        TextView deliveryAddressPhone;


        @BindView(R.id.numberOfItems)
        TextView numberOfItems;

        @BindView(R.id.orderTotal)
        TextView orderTotal;

        @BindView(R.id.currentStatus)
        TextView currentStatus;

//        @Bind(R.id.confirmOrderButton)
//        TextView confirmOrderButton;

        @BindView(R.id.close_button)
        ImageView closeButton;

        @BindView(R.id.cancelled_image)
        ImageView cancelledImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


//        @OnClick(R.id.confirmOrderButton)
        void onClickConfirmButton(View view)
        {
//            notifyConfirmOrder.notifyConfirmOrder(dataset.get(getLayoutPosition()));
        }


        @OnClick(R.id.close_button)
        void closeButton(View view)
        {
            notifyConfirmOrder.notifyCancelOrder(dataset.get(getLayoutPosition()));
        }

        @Override
        public void onClick(View v) {

            notifyConfirmOrder.notifyOrderSelected(dataset.get(getLayoutPosition()));
        }
    }




    interface NotifyConfirmOrder{
//        void notifyConfirmOrder(Order order);
        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order);
    }

}
