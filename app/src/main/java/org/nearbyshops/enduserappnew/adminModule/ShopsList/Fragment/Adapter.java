package org.nearbyshops.enduserappnew.adminModule.ShopsList.Fragment;

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
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Shop> dataset = null;
    private NotifyByShopAdapter notifyByShopAdapter;
    private Context context;


    public static final int VIEW_TYPE_SHOP_APPROVAL = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;
    private Fragment fragment;

    Adapter(List<Shop> dataset, NotifyByShopAdapter notifyByShopAdapter, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.notifyByShopAdapter = notifyByShopAdapter;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_shop_approvals,parent,false);

        View view = null;

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_shop,parent,false);

        if(viewType== VIEW_TYPE_SHOP_APPROVAL)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop,parent,false);

            return new ViewHolderShopApproval(view);
        }
        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }


//        return new ViewHolder(view);

        return null;
    }






    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_SHOP_APPROVAL;
        }

//        return -1;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {



        if(holderVH instanceof ViewHolderShopApproval) {
            ViewHolderShopApproval holder = (ViewHolderShopApproval) holderVH;

            Shop shop = dataset.get(position);



//        holder.shopID.setText("Shop ID : " + String.valueOf(shop.getShopID()));
//        holder.address.setText(shop.getShopAddress() + "\n" + shop.getCity() + " - " + shop.getPincode());
//        holder.shopName.setText(shop.getShopName());


            if(shop!=null)
            {

                holder.shopName.setText(shop.getShopName());


                if(shop.getPickFromShopAvailable())
                {
                    holder.pickFromShopIndicator.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.pickFromShopIndicator.setVisibility(View.GONE);
                }



                if(shop.getHomeDeliveryAvailable())
                {
                    holder.homeDeliveryIndicator.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.homeDeliveryIndicator.setVisibility(View.GONE);
                }



                if(shop.getShopAddress()!=null)
                {
                    holder.shopAddress.setText(shop.getShopAddress() + ", " + shop.getCity()  + " - " + String.valueOf(shop.getPincode()));
                }

//                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                        + shop.getLogoImagePath();

                String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                        + shop.getLogoImagePath() + ".jpg";

                Drawable placeholder = VectorDrawableCompat
                        .create(context.getResources(),
                                R.drawable.ic_nature_people_white_48px, context.getTheme());

                Picasso.get()
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.shopLogo);


                String currency = "";
                currency = PrefGeneral.getCurrencySymbol(context);

                holder.delivery.setText("Delivery : " + currency + ". " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
                holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


                if(shop.getRt_rating_count()==0)
                {
                    holder.rating.setText("N/A");
                    holder.rating_count.setText("( Not Yet Rated )");

                }
                else
                {
                    holder.rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
                    holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
                }


                if(shop.getShortDescription()!=null)
                {
                    holder.description.setText(shop.getShortDescription());
                }


//                holder.shopName.setText(shop.getShopName());
//
//                if(shop.getShopAddress()!=null)
//                {
//                    holder.shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
//                }
//
////                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
////                        + shop.getLogoImagePath();
//
//                String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
//                        + shop.getLogoImagePath() + ".jpg";
//
//                Drawable placeholder = VectorDrawableCompat
//                        .create(context.getResources(),
//                                R.drawable.ic_nature_people_white_48px, context.getTheme());
//
//                Picasso.with(context)
//                        .load(imagePath)
//                        .placeholder(placeholder)
//                        .into(holder.shopLogo);
//
//                holder.delivery.setText("Delivery : Rs " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
//                holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");
//
//
//                if(shop.getRt_rating_count()==0)
//                {
//                    holder.rating.setText("N/A");
//                    holder.rating_count.setText("( Not Yet Rated )");
//
//                }
//                else
//                {
//                    holder.rating.setText(String.valueOf(shop.getRt_rating_avg()));
//                    holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
//                }
//
//
//                if(shop.getShortDescription()!=null)
//                {
//                    holder.description.setText(shop.getShortDescription());
//                }

            }

            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/" + "three_hundred_"+ shop.getLogoImagePath() + ".jpg";

            System.out.println(imagePath);


            Picasso.get()
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.shopLogo);
        }
        else if(holderVH instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof FragmentShopList)
            {
                int items_count = ((FragmentShopList) fragment).item_count;

                if(dataset.size() == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }



    }





    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



    public class ViewHolderShopApproval extends RecyclerView.ViewHolder{




        @BindView(R.id.shop_name) TextView shopName;
        @BindView(R.id.shop_address) TextView shopAddress;
        @BindView(R.id.shop_logo) ImageView shopLogo;
        @BindView(R.id.delivery) TextView delivery;
        @BindView(R.id.distance) TextView distance;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.rating_count) TextView rating_count;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.list_item_shop) ConstraintLayout list_item;

        @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
        @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;



        public ViewHolderShopApproval(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



        @OnClick({R.id.list_item_shop})
        void editClick()
        {
            notifyByShopAdapter.notifyEditClick(dataset.get(getLayoutPosition()));
        }

    }




    interface NotifyByShopAdapter {
        void notifyEditClick(Shop shop);
        void notifyListItemClick(Shop shop);
    }

}
