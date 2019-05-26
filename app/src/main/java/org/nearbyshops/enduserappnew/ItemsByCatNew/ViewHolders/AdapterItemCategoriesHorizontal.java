package org.nearbyshops.enduserappnew.ItemsByCatNew.ViewHolders;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.enduserappnew.Markets.Interfaces.listItemMarketNotifications;
import org.nearbyshops.enduserappnew.Markets.ViewHolders.ViewHolderSavedMarket;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;

import java.util.List;





public class AdapterItemCategoriesHorizontal extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<ItemCategory> dataset;
    private Context context;
    private Fragment fragment;



    public AdapterItemCategoriesHorizontal(List<ItemCategory> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return ViewHolderItemCategoryNew.create(viewGroup,context,fragment,this);
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if(viewHolder instanceof ViewHolderItemCategoryNew)
        {
            ((ViewHolderItemCategoryNew) viewHolder).bindItemCategory(dataset.get(i));
        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
