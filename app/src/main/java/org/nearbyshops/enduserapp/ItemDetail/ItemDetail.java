package org.nearbyshops.enduserapp.ItemDetail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.ItemImageSlider.ItemImageFullscreenFragment;
import org.nearbyshops.enduserapp.ItemImageSlider.ItemImagesFullscreen;
import org.nearbyshops.enduserapp.LoginNew.Login;
import org.nearbyshops.enduserapp.Model.Endpoints.ItemImageEndPoint;
import org.nearbyshops.enduserapp.Model.Item;
import org.nearbyshops.enduserapp.Model.ItemImage;
import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.enduserapp.ModelReviewItem.FavouriteItem;
import org.nearbyshops.enduserapp.ModelReviewItem.FavouriteItemEndpoint;
import org.nearbyshops.enduserapp.ModelReviewItem.ItemReview;
import org.nearbyshops.enduserapp.ModelReviewItem.ItemReviewEndPoint;
import org.nearbyshops.enduserapp.ModelRoles.EndUser;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.FavouriteItemService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemImageService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemReviewService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemSpecNameService;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;
import org.nearbyshops.enduserapp.Utility.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ItemDetail extends AppCompatActivity implements
        Target, RatingBar.OnRatingBarChangeListener, NotifyReviewUpdate, AdapterItemImages.notificationsFromAdapter {



    public final static String ITEM_DETAIL_INTENT_KEY = "intent_key_item_detail";

    @Inject
    ItemReviewService itemReviewService;

    @Inject
    FavouriteItemService favouriteItemService;

    @Inject
    ItemImageService itemImageService;

    @Inject
    ItemSpecNameService itemSpecNameService;

    private GoogleMap mMap;
    Marker currentMarker;

    Item item;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.book_title)
    TextView bookTitle;

    @BindView(R.id.author_name)
    TextView authorName;

//    @Bind(R.id.date_of_publish)
//    TextView publishDate;
//
//
//    @Bind(R.id.publisher_name)
//    TextView publisherName;

    @BindView(R.id.book_description)
    TextView bookDescription;

    @BindView(R.id.book_cover)
    ImageView bookCover;

    @BindView(R.id.rating_text)
    TextView ratingText;

    @BindView(R.id.ratings_count)
    TextView ratingsCount;

    @BindView(R.id.ratingBar)
    RatingBar ratingsBar;

    @BindView(R.id.user_rating_review)
    LinearLayout user_review_ratings_block;

    @BindView(R.id.edit_review_text)
    TextView edit_review_text;

    @BindView(R.id.ratingBar_rate)
    RatingBar ratingBar_rate;

    @BindView(R.id.read_all_reviews_button)
    TextView read_all_reviews_button;

    @BindView(R.id.member_profile_image)
    ImageView member_profile_image;

    @BindView(R.id.member_name)
    TextView member_name;

    @BindView(R.id.member_rating)
    RatingBar member_rating_indicator;


    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;


//    Unbinder unbinder;


    public ItemDetail() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ButterKnife.bind(this);

        ratingBar_rate.setOnRatingBarChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        item = getIntent().getParcelableExtra(ITEM_DETAIL_INTENT_KEY);
        bindViews(item);

//        if (item != null) {
//            getSupportActionBar().setTitle(item.getItemName());
//            getSupportActionBar().setSubtitle(item.getItemDescription());
//        }


        if (item != null) {
            checkUserReview();
        }


        Log.d("ShopLog",String.valueOf(item.getRt_rating_avg()) + ":" + String.valueOf(item.getRt_rating_count()));

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupRecyclerView();
        setupRecyclerViewSpecs();

        checkFavourite();
    }




    ArrayList<ItemImage> dataset = new ArrayList<>();

    @BindView(R.id.recyclerview_item_images)

    RecyclerView itemImagesList;
    AdapterItemImages adapterItemImages;
    GridLayoutManager layoutManager;



    void setupRecyclerView() {

        adapterItemImages = new AdapterItemImages(dataset,this,this);
        itemImagesList.setAdapter(adapterItemImages);
        layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false);
        itemImagesList.setLayoutManager(layoutManager);

        makeNetworkCallItemImages(true);
    }



    void makeNetworkCallItemImages(final boolean clearDataset)
    {

            Call<ItemImageEndPoint> call = itemImageService.getItemImages(
                    item.getItemID(),ItemImage.IMAGE_ORDER,null,null,null
            );


            call.enqueue(new Callback<ItemImageEndPoint>() {
                @Override
                public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!=null)
                    {
                        if(response.body().getResults()!=null)
                        {
                            if(clearDataset)
                            {
                                dataset.clear();
                            }

                            dataset.addAll(response.body().getResults());
                            adapterItemImages.notifyDataSetChanged();


//                            showToastMessage("Dataset Changed : Item ID : " + String.valueOf(item.getItemID()) +
//                            "\nDataset Count" + String.valueOf(response.body().getResults().size())
//                            );

                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }


                    showToastMessage("Loading Images Failed !");
                }
            });
    }




    ArrayList<ItemSpecificationName> datasetSpecs = new ArrayList<>();

    @BindView(R.id.recyclerview_item_specifications)
    RecyclerView itemSpecsList;
    AdapterItemSpecifications adapterItemSpecs;
    GridLayoutManager layoutManagerItemSpecs;



    void setupRecyclerViewSpecs()
    {
        adapterItemSpecs = new AdapterItemSpecifications(datasetSpecs,this);
        itemSpecsList.setAdapter(adapterItemSpecs);
        layoutManagerItemSpecs = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        itemSpecsList.setLayoutManager(layoutManagerItemSpecs);

        makeNetworkCallSpecs(true);
    }


    void makeNetworkCallSpecs(final boolean clearDataset)
    {
        Call<List<ItemSpecificationName>> call = itemSpecNameService.getItemSpecName(
                item.getItemID(),null
        );


        call.enqueue(new Callback<List<ItemSpecificationName>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationName>> call, Response<List<ItemSpecificationName>> response) {

                if(response.code()==200)
                {
                    if(clearDataset)
                    {
                        datasetSpecs.clear();
                    }

                    datasetSpecs.addAll(response.body());

                    adapterItemSpecs.notifyDataSetChanged();
                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not Permitted");
                }
                else
                {
                    showToastMessage("Failed to load specs : code " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationName>> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }











    boolean isDestroyed = false;

    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }









    void bindViews(Item item) {

        if (item != null) {


            if (item.getItemName()==null) {
                bookTitle.setText("Shop Title");
            } else {
                bookTitle.setText(item.getItemName());
            }

            authorName.setText(item.getItemDescription());



            // set Book Cover Image


//            String imagePath = UtilityGeneral.getImageEndpointURL(this)
//                    + item.getItemImageURL();


            String imagePath = PrefGeneral.getServiceURL(this)
                    + "/api/v1/Item/Image/seven_hundred_" + item.getItemImageURL() + ".jpg";

            //five_hundred_  + ".jpg"

//            if (!shop.getBookCoverImageURL().equals("")) {

            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getTheme());

            Picasso.with(this).load(imagePath)
                    .placeholder(placeholder)
                    .into(bookCover);

            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(this);

//            }

            if (item.getRt_rating_count() == 0) {

                ratingText.setText(R.string.rating_not_available);
                ratingsCount.setText((getString(R.string.not_yet_rated)));
                ratingsBar.setVisibility(View.GONE);

            } else {
                ratingText.setText("Rating : " + String.format("%.1f", item.getRt_rating_avg()));
                ratingsCount.setText((int) item.getRt_rating_count() + " Ratings");
                ratingsBar.setRating(item.getRt_rating_avg());
            }


            bookDescription.setText(item.getItemDescriptionLong());

            /*if (shop.getLongDescription()!=null && !shop.getLongDescription().equals("null") && !shop.getDe.equals("")) {

            }*/
//                bookDescription.setText("Book description Not Available.");


        }

    }


    @BindView(R.id.edit_review_block)
    RelativeLayout edit_review_block;

    @BindView(R.id.review_title)
    TextView review_title;

    @BindView(R.id.review_description)
    TextView review_description;

    @BindView(R.id.review_date)
    TextView review_date;

    ItemReview reviewForUpdate;


    // method to check whether the user has written the review or not if the user is currently logged in.
    void checkUserReview() {

        if (PrefLogin.getUser(this) == null) {

            user_review_ratings_block.setVisibility(View.GONE);

        } else {

            // Unhide review dialog


            if (item.getRt_rating_count() == 0) {

                user_review_ratings_block.setVisibility(View.VISIBLE);
                edit_review_block.setVisibility(View.GONE);

                edit_review_text.setText(R.string.item_review_be_the_first_to_review);

            } else if (item.getRt_rating_count() > 0) {


                Call<ItemReviewEndPoint> call = itemReviewService.getReviews(item.getItemID(),
                        PrefLogin.getUser(this).getUserID(), true, "REVIEW_DATE", null, null, null);

//                Log.d("review_check",String.valueOf(UtilityGeneral.getUserID(this)) + " : " + String.valueOf(shop.getBookID()));

                call.enqueue(new Callback<ItemReviewEndPoint>() {
                    @Override
                    public void onResponse(Call<ItemReviewEndPoint> call, Response<ItemReviewEndPoint> response) {


                        if (response.body() != null) {
                            if (response.body().getItemCount() > 0) {

//                                edit_review_text.setText("Edit your review and Rating !");


                                if(edit_review_block==null)
                                {
                                    // If the views are not bound then return. This can happen in delayed response. When this call is executed
                                    // after the activity have gone out of scope.
                                    return;
                                }

                                edit_review_block.setVisibility(View.VISIBLE);
                                user_review_ratings_block.setVisibility(View.GONE);

                                reviewForUpdate = response.body().getResults().get(0);

                                review_title.setText(response.body().getResults().get(0).getReviewTitle());
                                review_description.setText(response.body().getResults().get(0).getReviewText());

                                review_date.setText(response.body().getResults().get(0).getReviewDate().toLocaleString());

                                member_rating_indicator.setRating(response.body().getResults().get(0).getRating());


//                                user_review.setText(response.body().getResults().get(0).getReviewText());
//                                ratingBar_rate.setRating(response.body().getResults().get(0).getRating());

                                EndUser member = response.body().getResults().get(0).getRt_end_user_profile();
                                member_name.setText(member.getName());

                                String imagePath = PrefGeneral.getImageEndpointURL(ItemDetail.this)
                                        + member.getProfileImageURL();



                                Drawable placeholder = VectorDrawableCompat
                                        .create(getResources(),
                                                R.drawable.ic_nature_people_white_48px, getTheme());

                                Picasso.with(ItemDetail.this).load(imagePath)
                                        .placeholder(placeholder)
                                        .into(member_profile_image);


                            } else if (response.body().getItemCount() == 0) {
                                edit_review_text.setText("Rate this shop !");
                                edit_review_block.setVisibility(View.GONE);
                                user_review_ratings_block.setVisibility(View.VISIBLE);

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ItemReviewEndPoint> call, Throwable t) {


//                        showToastMessage("Network Request Failed. Check your internet connection !");

                    }
                });


            }

            // check shop ratings count
            // If ratings count is 0 then set message : Be the first to review


            // If ratings count is >0 then
            // check if user has written the review or not
            // if Yes
            // Write messsage : Edit your review and rating
            // If NO
            // Write message : Rate and Review this shop

        }

    }


    void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        isDestroyed = true;
//        ButterKnife.unbind(this);

        /*if (unbinder != null) {
            unbinder.unbind();
        }*/
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


        Palette palette = Palette.from(bitmap).generate();

        int color = getResources().getColor(R.color.colorPrimaryDark);
        int colorLight = getResources().getColor(R.color.colorPrimary);
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(colorLight);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            this.getWindow().setStatusBarColor(vibrantDark);

        }

        bookTitle.setBackgroundColor(vibrant);
        authorName.setBackgroundColor(vibrant);


        if (fab != null && vibrantDark != 0) {

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);

        //originalTitle.setBackgroundColor(vibrantDark);


        if (collapsingToolbarLayout != null) {

            collapsingToolbarLayout.setContentScrimColor(vibrant);

            //ContextCompat.getColor(this,R.color.transparent)
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

        write_review_click();
    }


    @OnClick({R.id.edit_icon, R.id.edit_review_label})
    void edit_review_Click() {

        if (reviewForUpdate != null) {
            FragmentManager fm = getSupportFragmentManager();
            RateReviewItemDialog dialog = new RateReviewItemDialog();
            dialog.show(fm, "rate");
            dialog.setMode(reviewForUpdate, true, reviewForUpdate.getItemID());
        }

    }


    @OnClick({R.id.edit_review_text,R.id.ratingBar_rate})
    void write_review_click() {

        FragmentManager fm = getSupportFragmentManager();
        RateReviewItemDialog dialog = new RateReviewItemDialog();
        dialog.show(fm, "rate");

        if (item != null) {
            dialog.setMode(null, false, item.getItemID());
        }
    }


    @Override
    public void notifyReviewUpdated() {

        checkUserReview();
    }

    @Override
    public void notifyReviewDeleted() {

        item.setRt_rating_count(item.getRt_rating_count() - 1);
        checkUserReview();
    }

    @Override
    public void notifyReviewSubmitted() {

        item.setRt_rating_count(item.getRt_rating_count() + 1);
        checkUserReview();
    }


    @OnClick(R.id.read_all_reviews_button)
    void readAllReviewsButton() {

//        Intent intent = new Intent(this, ShopReviews.class);
//        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, item);
//        startActivity(intent);

    }



    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

//
//    void showToastMessage(String message)
//    {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
//    }


    @OnClick(R.id.fab)
    void fabClick()
    {

        if(PrefLogin.getUser(this)==null)
        {
            // User Not logged In.
//            showMessageSnackBar("Please Login to use this Feature !");
            showToastMessage("Please Login to use this Feature !");
            showLoginDialog();
        }else
        {
            toggleFavourite();
        }
    }




    private void showLoginDialog()
    {
//        FragmentManager fm = getSupportFragmentManager();
//        LoginDialog loginDialog = new LoginDialog();
//        loginDialog.show(fm,"serviceUrl");


        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }



//    @Override
//    public void NotifyLogin() {
//
////        fabClick();
//    }






    void toggleFavourite()
    {


        if(item !=null && PrefLogin.getUser(this)!=null)
        {

            Call<FavouriteItemEndpoint> call = favouriteItemService
                    .getFavouriteBooks(item.getItemID(), PrefLogin.getUser(this).getUserID()
                    ,null,null,null,null);


            call.enqueue(new Callback<FavouriteItemEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteItemEndpoint> call, Response<FavouriteItemEndpoint> response) {


                    if(response.body()!=null)
                    {
                        if(response.body().getItemCount()>=1)
                        {
                            deleteFavourite();

                        }
                        else if(response.body().getItemCount()==0)
                        {
                            insertFavourite();
                        }
                    }

                }

                @Override
                public void onFailure(Call<FavouriteItemEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });
        }


    }



    void insertFavourite()
    {


        if(item !=null && PrefLogin.getUser(this)!=null)
        {

            FavouriteItem favouriteItem = new FavouriteItem();
            favouriteItem.setItemID(item.getItemID());
            favouriteItem.setEndUserID(PrefLogin.getUser(this).getUserID());

            Call<FavouriteItem> call = favouriteItemService.insertFavouriteItem(favouriteItem);

            call.enqueue(new Callback<FavouriteItem>() {
                @Override
                public void onResponse(Call<FavouriteItem> call, Response<FavouriteItem> response) {

                    if(response.code() == 201)
                    {
                        // created successfully

                        setFavouriteIcon(true);
                    }
                }

                @Override
                public void onFailure(Call<FavouriteItem> call, Throwable t) {

                    showToastMessage("Network Request failed !");

                }
            });
        }


    }

    void deleteFavourite()
    {

        if(item !=null && PrefLogin.getUser(this)!=null)
        {
            Call<ResponseBody> call = favouriteItemService.deleteFavouriteItem(item.getItemID(),
                    PrefLogin.getUser(this).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        setFavouriteIcon(false);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request Failed !");
                }
            });
        }

    }




    void setFavouriteIcon(boolean isFavourite)
    {

        if(fab==null)
        {
            return;
        }

        if(isFavourite)
        {
            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_white_24px, getTheme());
            fab.setImageDrawable(drawable);
        }
        else
        {
            Drawable drawable2 = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_border_white_24px, getTheme());
            fab.setImageDrawable(drawable2);
        }
    }





    void checkFavourite()
    {

        // make a network call to check the favourite

//        Log.d("Before Check Favourite", "Item ID : EndUser ID" + String.valueOf(item.getItemID()) + " : " + String.valueOf(UtilityLogin.getEndUser(this).getEndUserID()));

        if(PrefLogin.getUser(this)==null)
        {
            return;
        }


        if(item != null && PrefLogin.getUser(this) != null)
        {


            Log.d("After Favourite", "Item ID : EndUser ID" + String.valueOf(item.getItemID()) + " : " + String.valueOf(PrefLogin.getUser(this).getUserID()));

            Call<FavouriteItemEndpoint> call = favouriteItemService.getFavouriteBooks(item.getItemID(),
                    PrefLogin.getUser(this).getUserID()
                    ,null,null,null,null);


            call.enqueue(new Callback<FavouriteItemEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteItemEndpoint> call, Response<FavouriteItemEndpoint> response) {


                    if(response.body()!=null)
                    {

                        Log.d("After Favourite", "Item Count : " + String.valueOf(response.body().getItemCount()));


                        if(response.body().getItemCount()>=1)
                        {
                            setFavouriteIcon(true);

                        }
                        else if(response.body().getItemCount()==0)
                        {
                            setFavouriteIcon(false);
                        }
                    }

                }

                @Override
                public void onFailure(Call<FavouriteItemEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });

        }
    }





    @OnClick(R.id.share_buttons)
    void shareButtonClick()
    {

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("image/jpg")
                .getIntent();

        String url = PrefGeneral.getServiceURL(this)+ "/api/Images" + String.valueOf(item.getItemImageURL());
//        intent.putExtra(Intent.EXTRA_TEXT,url);
        intent.putExtra(Intent.EXTRA_TEXT,url);
//        intent.putExtra(Intent.EXTRA_TITLE,shop.getBookName());
        startActivity(Intent.createChooser(intent,"Share Link"));
    }





    @BindView(R.id.read_full_button)
    TextView readFullDescription;

    @OnClick(R.id.read_full_button)
    void readFullButtonClick()
    {
/*
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.author_name);
        layoutParams.setMargins(0,10,0,0);
        bookDescription.setLayoutParams(layoutParams);
*/


        bookDescription.setMaxLines(Integer.MAX_VALUE);
        readFullDescription.setVisibility(View.GONE);
    }





    @OnClick(R.id.book_cover)
    void profileImageClick()
    {
        listItemClick();
    }



    @Override
    public void listItemClick() {


        ItemImage itemImage = new ItemImage();
        itemImage.setImageFilename(item.getItemImageURL());

        List<ItemImage> list = new ArrayList<>();

        list.addAll(dataset);
        list.add(0,itemImage);

        Gson gson = new Gson();
        String json = gson.toJson(list);

        Intent intent = new Intent(this, ItemImagesFullscreen.class);
        intent.putExtra(ItemImageFullscreenFragment.ITEM_IMAGES_INTENT_KEY,json);
        startActivity(intent);
    }
}
