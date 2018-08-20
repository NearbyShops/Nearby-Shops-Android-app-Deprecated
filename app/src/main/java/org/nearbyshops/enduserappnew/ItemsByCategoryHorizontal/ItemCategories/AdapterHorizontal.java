package org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal.ItemCategories;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.PrefGeneral;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterHorizontal extends RecyclerView.Adapter<AdapterHorizontal.ViewHolder>{


    Map<Integer,ItemCategory> selectedItems = new HashMap<>();


    private List<ItemCategory> dataset;
    private Context context;
    private ItemCategoriesHorizontal activity;
    private ItemCategory requestedChangeParent = null;
    private ReceiveNotificationsFromAdapter notificationReceiver;


//    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public AdapterHorizontal(List<ItemCategory> dataset, Context context, ItemCategoriesHorizontal activity,
                             ReceiveNotificationsFromAdapter notificationReceiver) {


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.activity = activity;

//        if(this.dataset == null)
//        {
//            this.dataset = new ArrayList<ItemCategory>();
//        }


    }

    @Override
    public AdapterHorizontal.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_horizontal,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AdapterHorizontal.ViewHolder holder, final int position) {


        holder.categoryName.setText(String.valueOf(dataset.get(position).getCategoryName()));
//        holder.categoryDescription.setText(dataset.get(position).getDescriptionShort());


        if(selectedItems.containsKey(dataset.get(position).getItemCategoryID()))
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));
        }
        else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }



//        String imagePath = UtilityGeneral.getImageEndpointURL(context)
//                + dataset.get(position).getImagePath();

        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + dataset.get(position).getImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(placeholder)
                .into(holder.categoryImage);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        TextView categoryName;

        @BindView(R.id.itemCategoryListItem)
        RelativeLayout itemCategoryListItem;

        @BindView(R.id.categoryImage) ImageView categoryImage;

        @BindView(R.id.cardview)
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
//            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

//            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }






        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }

            notificationReceiver.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
            selectedItems.clear();
        }





        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {


            }

            return false;
        }



    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface ReceiveNotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);
        public void notifyItemCategorySelected();
    }
}