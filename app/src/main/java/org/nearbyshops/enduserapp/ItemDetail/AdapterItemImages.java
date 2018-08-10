package org.nearbyshops.enduserapp.ItemDetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserapp.Model.ItemImage;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemImages extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<ItemImage> dataset;
    private Context context;
    private AppCompatActivity activity;
    private notificationsFromAdapter notificationReceiver;


//    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public AdapterItemImages(List<ItemImage> dataset, Context context,notificationsFromAdapter notificationReceiver) {


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_IMAGE)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_image,parent,false);
            return new ViewHolderItemImage(v);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, final int position) {

        if(holderGiven instanceof ViewHolderItemImage)
        {
            ViewHolderItemImage holder = (ViewHolderItemImage) holderGiven;

            holder.indicator.setText(String.valueOf(position+1) + "/" + String.valueOf(getItemCount()));

//            holder.copyrights.setText(dataset.get(position).getImageCopyrights());


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/five_hundred_"
                    + dataset.get(position).getImageFilename() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.itemImage);
        }


    }



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



        @BindView(R.id.list_item) RelativeLayout listItem;
        @BindView(R.id.item_image) ImageView itemImage;
        @BindView(R.id.indicator) TextView indicator;
//        @Bind(R.id.copyright_info) TextView copyrights;


        public ViewHolderItemImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



        @OnClick(R.id.list_item)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.listItemClick();
        }

    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface notificationsFromAdapter
    {
        void listItemClick();
    }
}