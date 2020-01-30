package org.nearbyshops.enduserappnew.Lists.DeliveryAddress;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderDeliveryAdddress;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.List;

/**
 * Created by sumeet on 6/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset = null;
    private Context context;
    private ViewHolderDeliveryAdddress.NotifyDeliveryAddress notifyDeliveryAddress;



    private final int VIEW_TYPE_EMPTY_SCREEN = 1;
    private final int VIEW_TYPE_DELIVERY_ADDRESS = 2;



    public Adapter(List<Object> dataset, Context context, ViewHolderDeliveryAdddress.NotifyDeliveryAddress notifyDeliveryAddress) {

        this.dataset = dataset;
        this.context = context;
        this.notifyDeliveryAddress = notifyDeliveryAddress;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType==VIEW_TYPE_DELIVERY_ADDRESS)
        {
            return ViewHolderDeliveryAdddress.create(parent,context,notifyDeliveryAddress);
        }
        else if(viewType == VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context);
        }

        return ViewHolderEmptyScreenFullScreen.create(parent,context);
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderDeliveryAdddress)
        {
            ((ViewHolderDeliveryAdddress) holder).setItem((DeliveryAddress) dataset.get(position));
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }


    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }





    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        int viewType = 0;

        if(dataset.get(position) instanceof EmptyScreenDataListItem)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof DeliveryAddress)
        {
            return VIEW_TYPE_DELIVERY_ADDRESS;
        }


        return viewType;
    }



}
