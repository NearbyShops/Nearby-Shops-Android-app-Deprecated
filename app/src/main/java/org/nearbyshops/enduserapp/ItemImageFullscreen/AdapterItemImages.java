package org.nearbyshops.enduserapp.ItemImageFullscreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserapp.Model.ItemImage;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
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


    public AdapterItemImages(List<ItemImage> dataset, Context context) {


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_IMAGE)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_image_fullscreen,parent,false);
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


//            nine_hundred_   + ".jpg"
            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/"
                    + dataset.get(position).getImageFilename();

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



        @Bind(R.id.list_item) RelativeLayout listItem;
        @Bind(R.id.item_image) GestureImageView itemImage;
        @Bind(R.id.indicator) TextView indicator;
//        @Bind(R.id.copyright_info) TextView copyrights;


        public ViewHolderItemImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemImage.getController().getSettings().enableGestures();

            itemImage.getController().getSettings()
                    .setMaxZoom(2f)
                    .setPanEnabled(true)
                    .setZoomEnabled(true)
                    .setDoubleTapEnabled(true)
                    .setRotationEnabled(false)
                    .setRestrictRotation(false)
                    .setOverscrollDistance(0f, 0f)
                    .setOverzoomFactor(2f)
                    .setFillViewport(false)
                    .setFitMethod(Settings.Fit.INSIDE)
                    .setGravity(Gravity.CENTER);


        }



        @OnClick(R.id.list_item)
        public void itemCategoryListItemClick()
        {

        }





    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface notificationsFromAdapter
    {

    }
}