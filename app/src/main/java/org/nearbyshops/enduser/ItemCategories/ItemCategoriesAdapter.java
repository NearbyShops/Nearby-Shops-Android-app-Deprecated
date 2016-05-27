package org.nearbyshops.enduser.ItemCategories;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopNItemsByCat.ShopItemSwipeView;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesAdapter extends RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder> implements View.OnCreateContextMenuListener{


    List<ItemCategory> dataset;

    Context context;
    ItemCategories itemCategories;



    public ItemCategoriesAdapter(List<ItemCategory> dataset, Context context, ItemCategories itemCategories) {

        this.dataset = dataset;
        this.context = context;
        this.itemCategories = itemCategories;

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


        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + dataset.get(position).getImagePath();

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(holder.categoryImage);

        Log.d("applog",imagePath);

    }



    @Override
    public int getItemCount() {

        return dataset.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,MenuBuilder.Callback,MenuPresenter.Callback {

        private Button editButton;

        Button detachButton;

        Button deleteButton;

        private TextView categoryName,categoryDescription;
        private LinearLayout itemCategoryListItem;
        //@Bind(R.id.categoryImage) ImageView categoryImage;
        //@Bind(R.id.textviewEdit) TextView textViewEdit;

        @Bind(R.id.categoryImage) ImageView categoryImage;
        @Bind(R.id.moreOptions) ImageView optionsOverflow;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);



            DisplayMetrics metrics = new DisplayMetrics();
            itemCategories.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // adjust responsive height
            //categoryImage.setLayoutParams
              //      (new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(metrics.heightPixels/3)+10));


            itemView.setOnClickListener(this);


        }



        @OnClick(R.id.moreOptions)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public void onClick(View v) {


            if(dataset== null || dataset.size()== 0)
            {
                return;
            }

            // Following code is error prone : Index Out of bounds exception:
            if (dataset.get(getLayoutPosition()).getIsLeafNode()) {

                Intent intent = new Intent(context, ShopItemSwipeView.class);

               intent.putExtra(ShopItemSwipeView.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));

               context.startActivity(intent);

            }
            else
            {

                itemCategories.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {


            switch (item.getItemId())
            {


                case R.id.action_add_to_list:

                    Toast.makeText(context, "Option One", Toast.LENGTH_SHORT).show();

                    item.setChecked(true);

                    return true;

                default:

                    return false;
            }



        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menu) {

        }

        @Override
        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {

        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            return false;
        }
    }


    public void notifyDelete()
    {
        itemCategories.notifyDelete();

    }


    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);
    }



}