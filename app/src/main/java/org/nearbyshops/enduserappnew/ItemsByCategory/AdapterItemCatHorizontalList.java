package org.nearbyshops.enduserappnew.ItemsByCategory;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.enduserappnew.ItemsByCategory.ViewHolders.ViewHolderItemCategoryHorizontal;
import org.nearbyshops.enduserappnew.Model.ItemCategory;

import java.util.List;





public class AdapterItemCatHorizontalList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<ItemCategory> dataset;
    private Context context;
    private Fragment fragment;



    public AdapterItemCatHorizontalList(List<ItemCategory> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return ViewHolderItemCategoryHorizontal.create(viewGroup,context,fragment,this);
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if(viewHolder instanceof ViewHolderItemCategoryHorizontal)
        {
            ((ViewHolderItemCategoryHorizontal) viewHolder).bindItemCategory(dataset.get(i));
        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
