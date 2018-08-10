package org.nearbyshops.enduserapp.ItemImageSlider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserapp.Model.ItemImage;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;

import java.util.List;

/**
 * Created by sumeet on 23/3/17.
 */

//public class ImagePagerAdapter extends RecyclePagerAdapter<> {
//
//    public ImagePagerAdapter() {
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//
//        super.instantiateItem(container, position);
//
//        LayoutInflater inflater = LayoutInflater.from(container.getContext());
//        View rootView = inflater.inflate(R.layout.list_item_item_image_fullscreen, container, false);
//
//        GestureImageView imageView = (GestureImageView) rootView.findViewById(R.id.item_image);
//
//
//
//        return rootView;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return false;
//    }
//}


public class ImagePagerAdapter extends RecyclePagerAdapter<RecyclePagerAdapter.ViewHolder> {

    List<ItemImage> dataset;
    private final ViewPager viewPager;
    Context context;


//    private final GestureSettingsSetupListener setupListener;

    public ImagePagerAdapter(ViewPager pager, List<ItemImage> itemImages,Context context) {
        this.viewPager = pager;
        this.dataset = itemImages;
        this.context = context;
//        this.paintings = paintings;
//        this.setupListener = listener;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {


        ViewHolderPaintings holder = new ViewHolderPaintings(container);
        holder.image.getController().enableScrollInViewPager(viewPager);

        holder.image.getController().getSettings()
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

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclePagerAdapter.ViewHolder holder, int position) {

    ViewHolderPaintings paintingsHolder = (ViewHolderPaintings) holder;

//        nine_hundred_+ ".jpg"


        paintingsHolder.image.setBackgroundColor(ContextCompat.getColor(context,R.color.backgroundClean));

        String imagePath = "";


        if(position==0)
        {
            imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Item/Image/"
                    + dataset.get(position).getImageFilename();
        }
        else
        {
            imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/"
                    + dataset.get(position).getImageFilename();
        }

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        ((ViewHolderPaintings) holder).progressBar.setVisibility(View.VISIBLE);

        Picasso.with(context).load(imagePath)
                .placeholder(placeholder)
                .into(paintingsHolder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                        ((ViewHolderPaintings) holder).progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

    }



    static class ViewHolderPaintings extends RecyclePagerAdapter.ViewHolder {

        final GestureImageView image;
        ProgressBar progressBar;

        ViewHolderPaintings(ViewGroup container) {

            super(LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_item_image_fullscreen,container,false));
//            super(new GestureImageView(container.getContext()));



//            LayoutInflater inflater = LayoutInflater.from(container.getContext());
//            View rootView = inflater.inflate(R.layout.list_item_item_image_fullscreen, container, false);
////



            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            image = (GestureImageView) itemView.findViewById(R.id.item_image);

//            image = (GestureImageView) itemView;
        }
    }

}

