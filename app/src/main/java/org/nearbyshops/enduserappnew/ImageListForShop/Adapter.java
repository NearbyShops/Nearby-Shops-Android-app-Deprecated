package org.nearbyshops.enduserappnew.ImageListForShop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import org.nearbyshops.enduserappnew.ImageListForShop.ViewHolders.ViewHolderShopImage;
import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;

    public static final int VIEW_TYPE_ITEM_IMAGE = 1;


    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 7;



    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_ITEM_IMAGE) {

            return ViewHolderShopImage.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            return ViewHolderHeader.create(parent,context);

        }
        else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context);
        }


        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderShopImage) {


            ((ViewHolderShopImage) holder).setItem((ShopImage) dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {

                HeaderTitle header = (HeaderTitle) dataset.get(position);
                ((ViewHolderHeader) holder).setItem(header);
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {

            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if (dataset.get(position) instanceof HeaderTitle) {

            return VIEW_TYPE_HEADER;
        }
        else if (dataset.get(position) instanceof ShopImage) {

            return VIEW_TYPE_ITEM_IMAGE;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
    }




    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }



    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }


}