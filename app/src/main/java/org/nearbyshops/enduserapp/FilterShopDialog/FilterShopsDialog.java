package org.nearbyshops.enduserapp.FilterShopDialog;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.ItemDetail.NotifyReviewUpdate;
import org.nearbyshops.enduserapp.ModelReviewItem.ItemReview;
import org.nearbyshops.enduserapp.ModelRoles.User;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemReviewService;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;
import org.nearbyshops.enduserapp.Utility.PrefLogin;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 12/8/16.
 */

public class FilterShopsDialog extends DialogFragment {


    @BindView(R.id.dialog_dismiss_icon)
    ImageView dismiss_dialog_button;

    @BindView(R.id.submit_button)
    TextView submit_button;

    @BindView(R.id.cancel_button)
    TextView cancel_button;

    @BindView(R.id.review_text)
    EditText review_text;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.review_title)
    TextView review_title;

    @BindView(R.id.member_name)
    TextView member_name;

    @BindView(R.id.member_profile_image)
    ImageView member_profile_image;

    int book_id;


    ItemReview review_for_edit;
    boolean isModeEdit = false;


    @Inject
    ItemReviewService itemReviewService;


    @Inject
    EndUserService endUserService;


    public FilterShopsDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



//    Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_rate_review_item, container);

        ButterKnife.bind(this,view);

        if(isModeEdit && review_for_edit!=null)
        {
            submit_button.setText("Update");
            cancel_button.setText("Delete");

            review_title.setText(review_for_edit.getReviewTitle());
            review_text.setText(review_for_edit.getReviewText());

            member_name.setText(" by "  + review_for_edit.getRt_end_user_profile().getName());

            ratingBar.setRating(review_for_edit.getRating());

            String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
                    + review_for_edit.getRt_end_user_profile().getProfileImageURL();


            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

            Picasso.with(getActivity()).load(imagePath)
                    .placeholder(placeholder)
                    .into(member_profile_image);


        }



        if(!isModeEdit)
        {
            setMember();
        }

//        setMember();


//        dismiss_dialog_button = (ImageView) view.findViewById(R.id.dialog_dismiss_icon);

        return view;
    }




    @OnClick(R.id.dialog_dismiss_icon)
    void dismiss_dialog()
    {
        dismiss();
        showToastMessage("Dismissed !");
    }



    void cancel_button()
    {
        dismiss();
        showToastMessage("Cancelled !");
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*if(unbinder!=null)
        {
            unbinder.unbind();
        }*/
    }


    public void setMode(ItemReview reviewForUpdate,boolean isModeEdit, int book_id)
    {

        this.book_id = book_id;
        review_for_edit = reviewForUpdate;
        this.isModeEdit = isModeEdit;
    }



    void setMember()
    {

        User endUser = PrefLogin.getUser(getActivity());


        if(endUser!=null)
        {
            member_name.setText(" by " + endUser.getName());


            String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
                    + endUser.getProfileImagePath();


            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

            Picasso.with(getActivity()).load(imagePath)
                    .placeholder(placeholder)
                    .into(member_profile_image);
        }





    }


    @OnClick(R.id.submit_button)
    void update_submit_click()
    {

        if(isModeEdit)
        {
            updateReview();
        }else
        {
            submitReview();
        }
    }


    @OnClick(R.id.cancel_button)
    void cancel_delete_click()
    {
        if(isModeEdit)
        {
            // delete the review

            Call<ResponseBody> call = itemReviewService.deleteItemReview(review_for_edit.getItemReviewID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        showToastMessage("Deleted !");


                        if(getActivity() instanceof NotifyReviewUpdate)
                        {
                            ((NotifyReviewUpdate)getActivity()).notifyReviewDeleted();
                        }

                        dismiss();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        else
        {
            cancel_button();
        }

    }


    void submitReview()
    {
        ItemReview itemReview = new ItemReview();
//        bookReview.setReviewDate(new java.sql.Date(System.currentTimeMillis()));
        itemReview.setRating((int) ratingBar.getRating());
        itemReview.setReviewTitle(review_title.getText().toString());
        itemReview.setReviewText(review_text.getText().toString());
        itemReview.setItemID(book_id);
        itemReview.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

        Call<ItemReview> call = itemReviewService.insertItemReview(itemReview);

        call.enqueue(new Callback<ItemReview>() {
            @Override
            public void onResponse(Call<ItemReview> call, Response<ItemReview> response) {

                if(response.code()==201)
                {
                    showToastMessage("Submitted !");

                    if(getActivity() instanceof NotifyReviewUpdate)
                    {
                        ((NotifyReviewUpdate)getActivity()).notifyReviewSubmitted();
                    }

                    dismiss();

                }
            }

            @Override
            public void onFailure(Call<ItemReview> call, Throwable t) {


                showToastMessage("Failed !");

            }
        });
    }




    void updateReview()
    {
        if(review_for_edit!=null)
        {

            review_for_edit.setRating((int)ratingBar.getRating());
            review_for_edit.setReviewTitle(review_title.getText().toString());
            review_for_edit.setReviewText(review_text.getText().toString());


            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date);

//            review_for_edit.setReviewDate(new java.sql.Date(date));

            Call<ResponseBody> call = itemReviewService.updateItemReview(review_for_edit,review_for_edit.getItemReviewID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code()==200)
                    {
                        showToastMessage(getString(R.string.udate_successful_api_response));

                        if(getActivity() instanceof NotifyReviewUpdate)
                        {
                            ((NotifyReviewUpdate)getActivity()).notifyReviewUpdated();
                        }

                        dismiss();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage(getString(R.string.api_response_no_item_updated));
                }
            });

        }
    }



}
