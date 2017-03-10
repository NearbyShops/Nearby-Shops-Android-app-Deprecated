package org.nearbyshops.enduserapp.ItemDetail;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationValue;
import org.nearbyshops.enduserapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemSpecifications extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<ItemSpecificationName> dataset;
    private Context context;
    private AppCompatActivity activity;
    private NotifyItemSpecs notificationReceiver;


//    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public AdapterItemSpecifications(List<ItemSpecificationName> dataset, Context context) {


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.activity = activity;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_IMAGE)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_specs,parent,false);
            return new ViewHolderItemImage(v);
        }

        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, final int position) {

        if(holderGiven instanceof ViewHolderItemImage)
        {
            ViewHolderItemImage holder = (ViewHolderItemImage) holderGiven;

            holder.name.setText(dataset.get(position).getTitle());

            String values = "";

            for(ItemSpecificationValue value : dataset.get(position).getRt_itemSpecificationValue())
            {
                if(values.equals(""))
                {
                    values = value.getTitle();
                }
                else
                {
                    values = values + ", " + value.getTitle();
                }


            }

            holder.values.setText(values);
        }


    }

//    public static final int VIEW_TYPE_ADD_IMAGE = 2;
    public static final int VIEW_TYPE_IMAGE = 1;

    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPE_IMAGE;
    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }





    public class ViewHolderItemImage extends RecyclerView.ViewHolder{



//        @Bind(R.id.list_item) RelativeLayout listItem;
        @Bind(R.id.title_name) TextView name;
        @Bind(R.id.values) TextView values;


        public ViewHolderItemImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


//
//        @OnClick(R.id.list_item)
//        public void itemCategoryListItemClick()
//        {
//
//        }


    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface NotifyItemSpecs
    {
        // method for notifying the list object to request sub category
//        void addItemSpec();
//        void editItemImage(ItemImage itemImage, int position);
//        void removeItemImage(ItemImage itemImage, int position);

    }
}