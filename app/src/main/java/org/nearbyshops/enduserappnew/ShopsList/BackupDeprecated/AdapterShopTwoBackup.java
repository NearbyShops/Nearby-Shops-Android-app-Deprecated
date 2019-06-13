package org.nearbyshops.enduserappnew.ShopsList.BackupDeprecated;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopHome.ShopHome;
import org.nearbyshops.enduserappnew.ShopsList.FragmentShopsList;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterShopTwoBackup extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Shop> dataset = null;
    private Context context;
    private Fragment fragment;


    public AdapterShopTwoBackup(List<Shop> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType==0)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop,parent,false);

            return new ViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_given, int position) {


        if(holder_given instanceof AdapterShopTwoBackup.ViewHolder)
        {
            AdapterShopTwoBackup.ViewHolder holder = (AdapterShopTwoBackup.ViewHolder)holder_given;

            Shop shop = dataset.get(position);


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

                holder.delivery.setText("Delivery : " + currency + " " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
                holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


                if(shop.getRt_rating_count()==0)
                {
//                    holder.rating.setText("N/A");
                    holder.rating.setText(" New ");
                    holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
                    holder.rating_count.setText("( Not Yet Rated )");
                    holder.rating_count.setVisibility(View.GONE);

                }
                else
                {
                    holder.rating_count.setVisibility(View.VISIBLE);
                    holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
                    holder.rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
                    holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
                }


                if(shop.getShortDescription()!=null)
                {
                    holder.description.setText(shop.getShortDescription());
                }

            }


        }
        else if(holder_given instanceof LoadingViewHolder)
        {
            AdapterShopTwoBackup.LoadingViewHolder viewHolder = (LoadingViewHolder) holder_given;

            int itemCount = 0;

            if(fragment instanceof FragmentShopsList)
            {
                itemCount = ((FragmentShopsList) fragment).getItemCount();
            }


            if(position == 0 || position == itemCount)
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

    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return (dataset.size()+1);
    }


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position==dataset.size())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

//        TextView shopName;
//        ImageView shopImage;
//        TextView distance;
//        TextView rating;
//        RelativeLayout listItem;



        @BindView(R.id.shop_name) TextView shopName;
        @BindView(R.id.shop_address) TextView shopAddress;
        @BindView(R.id.shop_logo) ImageView shopLogo;
        @BindView(R.id.delivery) TextView delivery;
        @BindView(R.id.distance) TextView distance;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.rating_count) TextView rating_count;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
        @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }





        @OnClick(R.id.list_item)
        void listItemClick()
        {

            Intent shopHomeIntent = new Intent(context, ShopHome.class);
            PrefShopHome.saveShop(dataset.get(getLayoutPosition()),context);
            context.startActivity(shopHomeIntent);
        }


//        @OnClick(R.id.shop_logo)
        void shopLogoClick()
        {
//            Intent intent = new Intent(context, MarketDetail.class);
//            intent.putExtra(MarketDetail.SHOP_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
//            context.startActivity(intent);
        }


    }
}
