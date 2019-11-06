package org.nearbyshops.enduserappnew.ImagesShop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int itemCount;



    // keeping track of selections
//    private Map<Integer, ShopImage> selectedItems = new HashMap<>();
//    private ShopImage selectedItemSingle;


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;
    private Fragment fragment;

    public static final int VIEW_TYPE_ITEM_IMAGE = 1;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
//    private final static int VIEW_TYPE_FILTER = 7;
//    private final static int VIEW_TYPE_FILTER_SUBMISSIONS = 8;


    public Adapter(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_ITEM_IMAGE) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_image_new, parent, false);


            return new ViewHolderTripRequest(view);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_item_image, parent, false);

            return new ViewHolderHeader(view);

        } else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);

        }

        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderTripRequest) {

            bindTripRequest((ViewHolderTripRequest) holder, position);
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {
                HeaderTitle header = (HeaderTitle) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;




//            if (fragment instanceof ShopImageListFragment) {
//                itemCount = (((ShopImageListFragment) fragment).item_count + 1 );
//            }


//            itemCount = dataset.size();

            if (dataset.size() >= itemCount) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);
            }

        }
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {
            return VIEW_TYPE_PROGRESS_BAR;
        } else if (dataset.get(position) instanceof HeaderTitle) {
            return VIEW_TYPE_HEADER;
        } else if (dataset.get(position) instanceof ShopImage) {
            return VIEW_TYPE_ITEM_IMAGE;
        }

        return -1;
    }




    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }


    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }




    void bindTripRequest(ViewHolderTripRequest holder, int position)
    {

        if(dataset.get(position) instanceof ShopImage)
        {
            ShopImage taxiImage = (ShopImage) dataset.get(position);

            holder.imageTitle.setText(taxiImage.getCaptionTitle());
            holder.imageDescription.setText(taxiImage.getCaption());
            holder.copyrights.setText(taxiImage.getCopyrights());


            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);

//            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/TaxiImages/Image/" + "_hundred_"+ taxiImage.getImageFilename() + ".jpg";
//            String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/TaxiImages/Image/" + taxiImage.getImageFilename();

            String imagePathSmall = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/five_hundred_"
                    + taxiImage.getImageFilename() + ".jpg";


            String imagePathMedium = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/nine_hundred_"
                    + taxiImage.getImageFilename() + ".jpg";


            String imagePathFullSize = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/"
                    + taxiImage.getImageFilename();



            Picasso.get()
                    .load(imagePathMedium)
                    .placeholder(drawable)
                    .into(holder.taxiImage);




//            if(selectedItems.containsKey(taxiImage.getShopImageID()))
//            {
////                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
////                holder.listItem.animate().scaleXBy(-3);
////                holder.listItem.animate().scaleYBy(-3);
////                holder.listItem.animate().scaleY(-2);
//
//                holder.checkIcon.setVisibility(View.VISIBLE);
//
//            }
//            else
//            {
////                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
//
//                holder.checkIcon.setVisibility(View.INVISIBLE);
//            }

        }
    }








    class ViewHolderTripRequest extends RecyclerView.ViewHolder{


        @BindView(R.id.title)
        TextView imageTitle;
        @BindView(R.id.description)
        TextView imageDescription;
        @BindView(R.id.copyright_info)
        TextView copyrights;
        @BindView(R.id.taxi_image)
        ImageView taxiImage;
        @BindView(R.id.list_item)
        ConstraintLayout listItem;
        @BindView(R.id.check_icon)
        ImageView checkIcon;





        public ViewHolderTripRequest(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//
//
//
//                }
//            });
        }



//        @OnLongClick(R.id.list_item)
//        boolean listItemLongClick(View view)
//        {
//            ShopImage shopImage = (ShopImage) dataset.get(getLayoutPosition());
//
//
//
//            if(selectedItems.containsKey(
//                    shopImage.getShopImageID()
//            ))
//            {
//                selectedItems.remove(shopImage.getShopImageID());
//                checkIcon.setVisibility(View.INVISIBLE);
//
//            }else
//            {
//                selectedItems.put(shopImage.getShopImageID(),shopImage);
//                checkIcon.setVisibility(View.VISIBLE);
//                selectedItemSingle = shopImage;
//            }
//
//
//
//            notificationReceiver.notifyListItemSelected();
////                    notifyItemChanged(getLayoutPosition());
//
//
//
////                    if(selectedItems.containsKey(taxiImage.getImageID()))
////                    {
////
////
////                    }
////                    else
////                    {
////
////                        checkIcon.setVisibility(View.INVISIBLE);
////                    }
//
//
//            return notificationReceiver.listItemLongClick(view,
//                    (ShopImage) dataset.get(getLayoutPosition()),
//                    getLayoutPosition()
//            );
//        }
//


        @OnClick(R.id.list_item)
        void listItemClick()
        {

            notificationReceiver.listItemClick(
                    (ShopImage) dataset.get(getLayoutPosition()),
                    getLayoutPosition()
            );

        }


    }// ViewHolder Class declaration ends






    class ViewHolderHeader extends RecyclerView.ViewHolder{

        @BindView(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends





    interface NotificationsFromAdapter
    {
        void notifyListItemSelected();
        void listItemClick(ShopImage shopImage, int position);
        boolean listItemLongClick(View view, ShopImage shopImage, int position);
    }



    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}