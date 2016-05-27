package org.nearbyshops.enduser;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.ItemCategories.ItemCategories;
import org.nearbyshops.enduser.Model.ItemCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesAdapter extends RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder>{


    List<ItemCategory> dataset;

    Context context;
    ItemCategories itemCategories;

    final String IMAGE_ENDPOINT_URL = "/api/Images";

    public ItemCategoriesAdapter(List<ItemCategory> dataset, Context context) {

        this.dataset = dataset;
        this.context = context;


        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }

    @Override
    public ItemCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_addstock_item_category,parent,false);

        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ItemCategoriesAdapter.ViewHolder holder, int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());


        String imagePath = getServiceURL() + IMAGE_ENDPOINT_URL + dataset.get(position).getImagePath();

        Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.categoryImage);

        Log.d("applog",imagePath);


        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        //}

        //final int positionInner = position;


//        holder.itemCategoryListItem.setOnClickListener(new View.OnClickListener() {
  //          @Override
    //        public void onClick(View v) {



      //      }
        //});

    }



    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button editButton;

        Button detachButton;

        Button deleteButton;

        private TextView categoryName,categoryDescription;
        private LinearLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;
        //@Bind(R.id.textviewEdit) TextView textViewEdit;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


            if(dataset== null || dataset.size()== 0)
            {
                return;
            }

            // Following code is error prone : Index Out of bounds exception:
            if (dataset.get(getLayoutPosition()).getIsLeafNode()) {

             //   Intent intent = new Intent(context, Items.class);

               // intent.putExtra(Items.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));

               // context.startActivity(intent);

            }
            else
            {

                itemCategories.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
            }
        }
    }


    public void notifyDelete()
    {
//        itemCategories.notifyDelete();

    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }

    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);
    }



}