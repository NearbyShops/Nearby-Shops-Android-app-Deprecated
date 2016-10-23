package org.nearbyshops.enduser.ShopReview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.ModelReview.ShopReview;
import org.nearbyshops.enduser.ModelReview.ShopReviewThanks;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewThanksService;
import org.nearbyshops.enduser.ShopReview.Interfaces.NotifyLoginByAdapter;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class ShopReviewAdapter extends RecyclerView.Adapter<ShopReviewAdapter.ViewHolder>{


    private List<ShopReview> dataset;
    private AppCompatActivity context;
    private Map<Integer,ShopReviewThanks> thanksMap;

    @Inject
    ShopReviewThanksService thanksService;



    public ShopReviewAdapter(List<ShopReview> dataset, Map<Integer,ShopReviewThanks> thanksMap, AppCompatActivity context) {

        this.dataset = dataset;
        this.context = context;
        this.thanksMap = thanksMap;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    public ShopReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_review,parent,false);

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

        holder.thanksCount.setText("(" + String.valueOf(dataset.get(position).getRt_thanks_count()) + ")");

        if(thanksMap!=null)
        {
            if(thanksMap.containsKey(dataset.get(position).getShopReviewID()))
            {
                holder.thanksButton.setText("Thanked !");
                holder.thanksButton.setTextColor(ContextCompat.getColor(context,R.color.phonographyBlue));
            }
            else
            {
                holder.thanksButton.setText("Thanks");
                holder.thanksButton.setTextColor(ContextCompat.getColor(context,R.color.grey800));
            }
        }


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

        @Bind(R.id.thanks_button)
        TextView thanksButton;

        @Bind(R.id.thanks_count)
        TextView thanksCount;

        @Bind(R.id.list_item_shop_review)
        RelativeLayout listItem;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }



        @OnClick({R.id.thanks_button,R.id.thanks_count})
        void listItemClick()
        {
            final ShopReview shopReview = dataset.get(getAdapterPosition());
            EndUser endUser = UtilityLogin.getEndUser(context);

            if(endUser==null)
            {
                showToastMessage("Please Login to use this feature !");

                if(context instanceof NotifyLoginByAdapter)
                {
                    ((NotifyLoginByAdapter)context).NotifyLoginAdapter();
                }

                return;
            }



            if(thanksMap.containsKey(shopReview.getShopReviewID()))
            {
                Call<ResponseBody> deleteCall = thanksService.deleteThanks(shopReview.getShopReviewID(),endUser.getEndUserID());

                deleteCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.code() ==200)
                        {
                            // delete successful
                            shopReview.setRt_thanks_count(shopReview.getRt_thanks_count()-1);
                            thanksMap.remove(shopReview.getShopReviewID());
                            notifyItemChanged(getLayoutPosition());
//                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


            }
            else
            {
                ShopReviewThanks shopReviewThanks = new ShopReviewThanks();
                shopReviewThanks.setEndUserID(endUser.getEndUserID());
                shopReviewThanks.setShopReviewID(dataset.get(getAdapterPosition()).getShopReviewID());

                Call<ShopReviewThanks> insertCall = thanksService.insertThanks(shopReviewThanks);

                insertCall.enqueue(new Callback<ShopReviewThanks>() {
                    @Override
                    public void onResponse(Call<ShopReviewThanks> call, Response<ShopReviewThanks> response) {


                        if(response.code() ==201)
                        {
                            // insert Successful
                            shopReview.setRt_thanks_count(shopReview.getRt_thanks_count()+1);
                            thanksMap.put(response.body().getShopReviewID(),response.body());

                            notifyItemChanged(getLayoutPosition());
//                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ShopReviewThanks> call, Throwable t) {

                    }
                });
            }

        }



    }// ViewHolder Class declaration ends





    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}