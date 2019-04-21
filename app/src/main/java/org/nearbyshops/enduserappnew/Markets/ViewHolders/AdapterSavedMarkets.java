package org.nearbyshops.enduserappnew.Markets.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.Markets.Interfaces.listItemMarketNotifications;

import java.util.List;

public class AdapterSavedMarkets extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<ServiceConfigurationGlobal> dataset;
    private Context context;
    private listItemMarketNotifications subscriber;




    public AdapterSavedMarkets(List<ServiceConfigurationGlobal> dataset, Context context, listItemMarketNotifications subscriber) {
        this.dataset = dataset;
        this.context = context;
        this.subscriber = subscriber;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return ViewHolderSavedMarket.create(viewGroup,context,subscriber);
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if(viewHolder instanceof ViewHolderSavedMarket)
        {
            ((ViewHolderSavedMarket) viewHolder).setItem(dataset.get(i));
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }




}
