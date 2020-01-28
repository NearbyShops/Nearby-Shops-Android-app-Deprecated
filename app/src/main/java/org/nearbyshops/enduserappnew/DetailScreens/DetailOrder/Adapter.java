package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.ViewHolders.ViewHolderOrderItem;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.ViewHolders.ViewHolderOrderWithBill;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.OrderItem;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int TAG_VIEW_HOLDER_ORDER = 1;
    public static final int TAG_VIEW_HOLDER_ORDER_ITEM = 2;



    Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==TAG_VIEW_HOLDER_ORDER)
        {

            return ViewHolderOrderWithBill.create(parent,context,fragment);
        }
        else if(viewType==TAG_VIEW_HOLDER_ORDER_ITEM)
        {

            return ViewHolderOrderItem.create(parent,context,fragment);
        }



        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderOrderWithBill)
        {
            ((ViewHolderOrderWithBill) holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderItem)
        {
            ((ViewHolderOrderItem) holder).setItem((OrderItem) dataset.get(position));
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






}
