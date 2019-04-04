package org.nearbyshops.enduserappnew.Shops.ListFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopHome.ShopHome;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterShopTwo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Shop> dataset = null;
    private Context context;
    private Fragment fragment;


    public AdapterShopTwo(List<Shop> dataset, Context context, Fragment fragment) {
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


        if(holder_given instanceof AdapterShopTwo.ViewHolder)
        {
            AdapterShopTwo.ViewHolder holder = (AdapterShopTwo.ViewHolder)holder_given;

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

                Picasso.with(context)
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.shopLogo);


                String currency = "";
                currency = PrefGeneral.getCurrencySymbol(context);

                holder.delivery.setText("Delivery : " + currency + " " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
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

            }


        }
        else if(holder_given instanceof LoadingViewHolder)
        {
            AdapterShopTwo.LoadingViewHolder viewHolder = (LoadingViewHolder) holder_given;

            int itemCount = 0;

            if(fragment instanceof FragmentShopNew)
            {
                itemCount = ((FragmentShopNew) fragment).getItemCount();
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        @BindView(R.id.shop_info_card) ConstraintLayout list_item;
        @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
        @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);


//            listItem = (RelativeLayout) itemView.findViewById(R.id.list_item_shop);
//            rating = (TextView) itemView.findViewById(R.id.rating);
//            distance = (TextView) itemView.findViewById(R.id.distance);
//            shopName = (TextView) itemView.findViewById(R.id.shopName);
//            shopImage = (ImageView) itemView.findViewById(R.id.shopImage);

//            shopImage.setOnClickListener(this);
//            listItem.setOnClickListener(this);
        }



        @OnClick(R.id.shop_info_card)
        void listItemClick()
        {

            Intent shopHomeIntent = new Intent(context, ShopHome.class);
            PrefShopHome.saveShop(dataset.get(getLayoutPosition()),context);
            context.startActivity(shopHomeIntent);
        }


//        @OnClick(R.id.shop_logo)
        void shopLogoClick()
        {
//            Intent intent = new Intent(context, ShopDetail.class);
//            intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
//            context.startActivity(intent);
        }


        @Override
        public void onClick(View v) {

            //Toast.makeText(context,"Item Click : " + String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

            switch (v.getId())
            {
                case R.id.shopImage:


                    break;

                case R.id.list_item_shop:

                    Intent shopHomeIntent = new Intent(context, ShopHome.class);
                    PrefShopHome.saveShop(dataset.get(getLayoutPosition()),context);
                    context.startActivity(shopHomeIntent);

                    break;

                default:
                    break;
            }
        }
    }
}
