package org.nearbyshops.enduser.OrdersCancelledPFS.CancelledByShop;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.nearbyshops.enduser.ModelPickFromShop.OrderPFS;
import org.nearbyshops.enduser.ModelPickFromShop.OrderStatsPFS;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.OrderHistoryPFS.Utility.UtilityOrderStatusPFS;
import org.nearbyshops.enduser.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterCancelledByShopPFS extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<OrderPFS> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;


    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;


    private Fragment fragment;

    AdapterCancelledByShopPFS(List<OrderPFS> dataset, NotifyConfirmOrder notifyConfirmOrder, Fragment fragment) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==VIEW_TYPE_ORDER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_cancelled_by_shop_pfs,parent,false);

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

                OrderPFS order = dataset.get(position);
                DeliveryAddress deliveryAddress = order.getDeliveryAddress();
                OrderStatsPFS orderStats = order.getOrderStats();

                holder.orderID.setText("Order ID : " + order.getOrderID());
                holder.dateTimePlaced.setText("" + order.getTimestampPlaced().toLocaleString());


                holder.deliveryAddressName.setText(deliveryAddress.getName());

                holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                        + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

                holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

                holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
                holder.orderTotal.setText("| Total : " + orderStats.getItemTotal());
                //holder.currentStatus.setText();


                String status = UtilityOrderStatusPFS.getStatusPFS(order.getStatusPickFromShop(),order.getDeliveryReceived(),order.getPaymentReceived());
                holder.currentStatus.setText("Current Status : " + status);
            }
        }
        else if(holderVH instanceof LoadingViewHolder)
        {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof CancelledByShopFragmentPFS)
            {
                int items_count = ((CancelledByShopFragmentPFS) fragment).item_count;

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




    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }





    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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
        void notifyOrderSelected(OrderPFS order);
        void notifyCancelOrder(OrderPFS order);
    }

}
