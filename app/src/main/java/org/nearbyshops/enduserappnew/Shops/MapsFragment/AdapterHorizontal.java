package org.nearbyshops.enduserappnew.Shops.MapsFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Shops.Interfaces.NotifyListItemClick;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class AdapterHorizontal extends RecyclerView.Adapter<AdapterHorizontal.ViewHolder> {


    private List<Shop> dataset = null;
    private Context context;
    private Fragment fragment;


    public AdapterHorizontal(List<Shop> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public AdapterHorizontal.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shops_map,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterHorizontal.ViewHolder holder, int position) {


        holder.shopName.setText(String.valueOf(position + 1) + ". " + dataset.get(position).getShopName());

        holder.rating.setText(String.format( "%.1f", dataset.get(position).getRt_rating_avg()));
        holder.distance.setText(String.format( "%.2f", dataset.get(position).getRt_distance())+ " Km");
        holder.address.setText(dataset.get(position).getShopAddress());

//        Log.d("applog","on BInd()");

//        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                + dataset.get(position).getLogoImagePath();

        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                + dataset.get(position).getLogoImagePath() + ".jpg";


//        Drawable placeholder = ContextCompat.getDrawable(context,R.drawable.ic_domain_black_24px);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_domain_black_24px, context.getTheme());

        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.shopImage);


    }

    @Override
    public int getItemCount() {

//        Log.d("applog",String.valueOf(dataset.size()));

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shopName;
        ImageView shopImage;
        TextView distance;
        TextView rating;
        TextView address;
        RelativeLayout listItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            address = (TextView) itemView.findViewById(R.id.address);
            listItem = (RelativeLayout) itemView.findViewById(R.id.list_item_shop);
            rating = (TextView) itemView.findViewById(R.id.rating);
            distance = (TextView) itemView.findViewById(R.id.distance);
            shopName = (TextView) itemView.findViewById(R.id.shopName);
            shopImage = (ImageView) itemView.findViewById(R.id.shopImage);

            shopImage.setOnClickListener(this);
            listItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //Toast.makeText(context,"Item Click : " + String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

            switch (v.getId())
            {
                case R.id.shopImage:

                    /*Intent intent = new Intent(context, ShopDetail.class);
                    intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
                    context.startActivity(intent);
*/
                    break;

                case R.id.list_item_shop:

                    if(fragment instanceof NotifyListItemClick)
                    {
                        ((NotifyListItemClick)fragment).notifyListItemClick(getLayoutPosition());
                    }

                    break;

                default:
                    break;
            }
        }
    }
}
