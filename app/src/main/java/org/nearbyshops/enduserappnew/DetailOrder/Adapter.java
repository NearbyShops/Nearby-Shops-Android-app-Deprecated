package org.nearbyshops.enduserappnew.DetailOrder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DetailOrder.ViewHolders.ViewHolderOrderItem;
import org.nearbyshops.enduserappnew.DetailOrder.ViewHolders.ViewHolderOrderWithBill;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.OrderItem;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.DetailShop.ShopDetail;
import org.nearbyshops.enduserappnew.DetailShop.ShopDetailFragment;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int TAG_VIEW_HOLDER_ORDER = 1;
    public static final int TAG_VIEW_HOLDER_ORDER_ITEM = 2;



    Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==TAG_VIEW_HOLDER_ORDER)
        {

            return ViewHolderOrderWithBill.create(parent,context,fragment);
        }
        else if(viewType==TAG_VIEW_HOLDER_ORDER_ITEM)
        {

            return ViewHolderOrderItem.create(parent,context,fragment);
        }



        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderOrderWithBill)
        {
            ((ViewHolderOrderWithBill) holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderItem)
        {
            ((ViewHolderOrderItem) holder).setItem((OrderItem) dataset.get(position));
        }
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(dataset.get(position) instanceof Order)
        {
            return TAG_VIEW_HOLDER_ORDER;
        }
        else if(dataset.get(position) instanceof OrderItem)
        {
            return TAG_VIEW_HOLDER_ORDER_ITEM;
        }

        return -1;
    }





    @Override
    public int getItemCount() {
        return dataset.size();
    }






}
