package org.nearbyshops.enduser.ShopReview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.ModelReview.ShopReview;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 19/12/15.
 */


public class ShopReviewAdapter extends RecyclerView.Adapter<ShopReviewAdapter.ViewHolder>{



    private List<ShopReview> dataset;
    private Context context;

    public ShopReviewAdapter(List<ShopReview> dataset, Context context) {

        this.dataset = dataset;
        this.context = context;

    }

    @Override
    public ShopReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book_review,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ShopReviewAdapter.ViewHolder holder, final int position) {

        EndUser endUser = dataset.get(position).getRt_end_user_profile();

        holder.member_name.setText(endUser.getName());
        holder.rating.setRating(dataset.get(position).getRating());


        holder.review_date.setText(dataset.get(position).getReviewDate().toLocaleString());


        holder.review_title.setText(dataset.get(position).getReviewTitle());
        holder.review_text.setText(dataset.get(position).getReviewText());

        String imagePath = UtilityGeneral.getImageEndpointURL(context)
                + dataset.get(position).getRt_end_user_profile().getProfileImageURL();



        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.profile_image);

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.profile_image)
        ImageView profile_image;

        @Bind(R.id.member_name)
        TextView member_name;

        @Bind(R.id.rating)
        RatingBar rating;

        @Bind(R.id.review_date)
        TextView review_date;

        @Bind(R.id.review_title)
        TextView review_title;

        @Bind(R.id.review_text)
        TextView review_text;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }


    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}