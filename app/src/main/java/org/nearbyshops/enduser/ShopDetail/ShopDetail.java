package org.nearbyshops.enduser.ShopDetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Login.NotifyAboutLogin;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduser.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.enduser.ModelReviewShop.FavouriteShop;
import org.nearbyshops.enduser.ModelReviewShop.ShopReview;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.FavouriteShopService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewService;
import org.nearbyshops.enduser.ShopReview.ShopReviews;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopDetail extends AppCompatActivity implements NotifyAboutLogin,Target, RatingBar.OnRatingBarChangeListener ,NotifyReviewUpdate, OnMapReadyCallback {

    public final static String SHOP_DETAIL_INTENT_KEY = "intent_key_shop_detail";

    @Inject
    ShopReviewService shopReviewService;

    @Inject
    FavouriteShopService favouriteShopService;

    private GoogleMap mMap;
    Marker currentMarker;

    Shop shop;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.book_title)
    TextView bookTitle;

    @Bind(R.id.author_name)
    TextView authorName;

    @Bind(R.id.date_of_publish)
    TextView publishDate;


//    @Bind(R.id.publisher_name)
//    TextView publisherName;

    @Bind(R.id.book_description)
    TextView bookDescription;

    @Bind(R.id.book_cover)
    ImageView bookCover;

    @Bind(R.id.rating_text)
    TextView ratingText;

    @Bind(R.id.ratings_count)
    TextView ratingsCount;

    @Bind(R.id.ratingBar)
    RatingBar ratingsBar;

    @Bind(R.id.user_rating_review)
    LinearLayout user_review_ratings_block;

    @Bind(R.id.edit_review_text)
    TextView edit_review_text;

    @Bind(R.id.ratingBar_rate)
    RatingBar ratingBar_rate;

    @Bind(R.id.read_all_reviews_button)
    TextView read_all_reviews_button;

    @Bind(R.id.member_profile_image)
    ImageView member_profile_image;

    @Bind(R.id.member_name)
    TextView member_name;

    @Bind(R.id.member_rating)
    RatingBar member_rating_indicator;


    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;


//    Unbinder unbinder;


    public ShopDetail() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        ButterKnife.bind(this);

        ratingBar_rate.setOnRatingBarChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        shop = getIntent().getParcelableExtra(SHOP_DETAIL_INTENT_KEY);
        bindViews(shop);

        if (shop != null) {
            getSupportActionBar().setTitle(shop.getShopName());
            getSupportActionBar().setSubtitle(shop.getShortDescription());
        }


        if (shop != null) {
            checkUserReview();
        }



//        Log.d("ShopLog",String.valueOf(shop.getRt_rating_avg()) + ":" + String.valueOf(shop.getRt_rating_count()));

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


        checkFavourite();
        setupMap();
    }


    void setupMap()
    {
        SupportMapFragment mapFragment1 = new SupportMapFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map,mapFragment1).commit();

        mapFragment1.getMapAsync(this);
    }





    void bindViews(Shop shop) {

        if (shop != null) {


            if (shop.getShopName()==null) {
                bookTitle.setText("Shop Title");
            } else {
                bookTitle.setText(shop.getShopName());
            }

            authorName.setText(shop.getShopAddress() + "\n" + shop.getCity());

//            publisherName.setText("Published By : " + shop.getShopAddress());

            /*if(shop.getDateOfPublish()!=null)
            {
                Log.d("date","Date of Publish binding " + shop.getDateOfPublish().toString());

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM ''yyyy");

                //"EEE, MMM d, ''yy"
                //"yyyy-MM-dd"

                publishDate.setText(dateFormat.format(shop.getDateOfPublish()));
            }*/

            if(shop.getDateTimeStarted().getTime()==0)
            {
                publishDate.setText("Date Started not available !");

            }else
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(shop.getDateTimeStarted().getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format_simple));

                //"EEEEEEE, MMMMMMMMM dd yyyy"
                //getString(R.string.date_format_simple)

                //"MMMM d ''yyyy"
                publishDate.setText("Started : " + dateFormat.format(calendar.getTime()));
            }


            // set Book Cover Image


//            String imagePath = UtilityGeneral.getImageEndpointURL(this)
//                    + shop.getLogoImagePath();


            String imagePath = UtilityGeneral.getServiceURL(this) + "/api/v1/Shop/Image/"
                    + shop.getLogoImagePath();

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

            if (shop.getRt_rating_count() == 0) {

                ratingText.setText(R.string.rating_not_available);
                ratingsCount.setText((getString(R.string.not_yet_rated)));
                ratingsBar.setVisibility(View.GONE);

            } else {
                ratingText.setText("Rating : " + String.format("%.1f", shop.getRt_rating_avg()));
                ratingsCount.setText((int) shop.getRt_rating_count() + " Ratings");
                ratingsBar.setRating(shop.getRt_rating_avg());
            }


            bookDescription.setText(shop.getLongDescription());

            /*if (shop.getLongDescription()!=null && !shop.getLongDescription().equals("null") && !shop.getDe.equals("")) {

            }*/
//                bookDescription.setText("Book description Not Available.");
        }
    }



    @Bind(R.id.edit_review_block)
    RelativeLayout edit_review_block;

    @Bind(R.id.review_title)
    TextView review_title;

    @Bind(R.id.review_description)
    TextView review_description;

    @Bind(R.id.review_date)
    TextView review_date;

    ShopReview reviewForUpdate;


    // method to check whether the user has written the review or not if the user is currently logged in.
    void checkUserReview() {

        if (UtilityLogin.getEndUser(this) == null) {

            user_review_ratings_block.setVisibility(View.GONE);

        } else {

            // Unhide review dialog


            if (shop.getRt_rating_count() == 0) {

                user_review_ratings_block.setVisibility(View.VISIBLE);
                edit_review_block.setVisibility(View.GONE);

                edit_review_text.setText(R.string.shop_review_be_the_first_to_review);
            } else if (shop.getRt_rating_count() > 0) {


                Call<ShopReviewEndPoint> call = shopReviewService.getReviews(shop.getShopID(),
                        UtilityLogin.getEndUser(this).getEndUserID(), true, "REVIEW_DATE", null, null, null);

//                Log.d("review_check",String.valueOf(UtilityGeneral.getUserID(this)) + " : " + String.valueOf(shop.getBookID()));

                call.enqueue(new Callback<ShopReviewEndPoint>() {
                    @Override
                    public void onResponse(Call<ShopReviewEndPoint> call, Response<ShopReviewEndPoint> response) {


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

                                String imagePath = UtilityGeneral.getImageEndpointURL(ShopDetail.this)
                                        + member.getProfileImageURL();



                                Drawable placeholder = VectorDrawableCompat
                                        .create(getResources(),
                                                R.drawable.ic_nature_people_white_48px, getTheme());

                                Picasso.with(ShopDetail.this).load(imagePath)
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
                    public void onFailure(Call<ShopReviewEndPoint> call, Throwable t) {


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


        ButterKnife.unbind(this);

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
            RateReviewDialog dialog = new RateReviewDialog();
            dialog.show(fm, "rate");
            dialog.setMode(reviewForUpdate, true, reviewForUpdate.getShopID());
        }

    }


    @OnClick({R.id.edit_review_text,R.id.ratingBar_rate})
    void write_review_click() {

        FragmentManager fm = getSupportFragmentManager();
        RateReviewDialog dialog = new RateReviewDialog();
        dialog.show(fm, "rate");

        if (shop != null) {
            dialog.setMode(null, false, shop.getShopID());
        }
    }


    @Override
    public void notifyReviewUpdated() {

        checkUserReview();
    }

    @Override
    public void notifyReviewDeleted() {

        shop.setRt_rating_count(shop.getRt_rating_count() - 1);
        checkUserReview();
    }

    @Override
    public void notifyReviewSubmitted() {

        shop.setRt_rating_count(shop.getRt_rating_count() + 1);
        checkUserReview();
    }


    @OnClick(R.id.read_all_reviews_button)
    void readAllReviewsButton() {

        Intent intent = new Intent(this, ShopReviews.class);
        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, shop);
        startActivity(intent);

    }



    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }



    @OnClick(R.id.fab)
    void fabClick()
    {

        if(UtilityLogin.getEndUser(this)==null)
        {
            // User Not logged In.
            showMessageSnackBar("Please Login to add shop to Favourites !");

            showLoginDialog();


        }else
        {
            toggleFavourite();
        }
    }




    private void showLoginDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }



    @Override
    public void NotifyLogin() {

//        fabClick();
    }






    void toggleFavourite(){

        if(shop !=null && UtilityLogin.getEndUser(this)!=null)
        {

            Call<FavouriteShopEndpoint> call = favouriteShopService.getFavouriteBooks(shop.getShopID(),UtilityLogin.getEndUser(this).getEndUserID()
                    ,null,null,null,null);


            call.enqueue(new Callback<FavouriteShopEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteShopEndpoint> call, Response<FavouriteShopEndpoint> response) {


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
                public void onFailure(Call<FavouriteShopEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });
        }
    }


    void insertFavourite()
    {


        if(shop !=null && UtilityLogin.getEndUser(this)!=null)
        {

            FavouriteShop favouriteBook = new FavouriteShop();
            favouriteBook.setShopID(shop.getShopID());
            favouriteBook.setEndUserID(UtilityLogin.getEndUser(this).getEndUserID());

            Call<FavouriteShop> call = favouriteShopService.insertFavouriteBook(favouriteBook);

            call.enqueue(new Callback<FavouriteShop>() {
                @Override
                public void onResponse(Call<FavouriteShop> call, Response<FavouriteShop> response) {

                    if(response.code() == 201)
                    {
                        // created successfully

                        setFavouriteIcon(true);
                    }
                }

                @Override
                public void onFailure(Call<FavouriteShop> call, Throwable t) {

                    showToastMessage("Network Request failed !");

                }
            });
        }


    }

    void deleteFavourite()
    {

        if(shop !=null && UtilityLogin.getEndUser(this)!=null)
        {
            Call<ResponseBody> call = favouriteShopService.deleteFavouriteBook(shop.getShopID(),
                    UtilityLogin.getEndUser(this).getEndUserID());


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

        if(shop !=null && UtilityLogin.getEndUser(this)!=null)
        {

            Call<FavouriteShopEndpoint> call = favouriteShopService.getFavouriteBooks(shop.getShopID(),UtilityLogin.getEndUser(this).getEndUserID()
                    ,null,null,null,null);


            call.enqueue(new Callback<FavouriteShopEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteShopEndpoint> call, Response<FavouriteShopEndpoint> response) {



                    if(response.body()!=null)
                    {
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
                public void onFailure(Call<FavouriteShopEndpoint> call, Throwable t) {

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

        String url = UtilityGeneral.getServiceURL(this)+ "/api/Images" + String.valueOf(shop.getLogoImagePath());
//        intent.putExtra(Intent.EXTRA_TEXT,url);
        intent.putExtra(Intent.EXTRA_TEXT,url);
//        intent.putExtra(Intent.EXTRA_TITLE,shop.getBookName());
        startActivity(Intent.createChooser(intent,"Share Link"));
    }


    @Bind(R.id.read_full_button)
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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(,14));

//        Location currentLocation = UtilityLocationOld.getCurrentLocation(this);

        if(shop!=null)
        {
            LatLng latLng = new LatLng(shop.getLatCenter(),shop.getLonCenter());

            Circle circle = mMap
                    .addCircle(
                            new CircleOptions()
                                    .center(latLng)
                                    .radius(shop.getDeliveryRange()*1000)
                                    .fillColor(0x11000000)
                                    .strokeWidth(1)
                                    .strokeColor(R.color.buttonColorDark)
                    );

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,getZoomLevel(circle)));
            //

            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(shop.getShopName()));
//                mMap.moveCamera();
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            currentMarker.showInfoWindow();

        }
    }



    public int getZoomLevel(Circle circle)
    {
        int zoomLevel = 11;
        if (circle != null)
        {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));
        }
        return zoomLevel ;
    }



    @OnClick(R.id.get_directions_button)
    void getDirections()
    {
        String str_latitude = String.valueOf(shop.getLatCenter());
        String str_longitude = String.valueOf(shop.getLonCenter());

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_latitude +  "," + str_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


}
