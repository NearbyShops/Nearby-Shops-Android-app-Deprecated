package org.nearbyshops.enduserappnew.EditShop;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ImageListForShop.ShopImageList;
import org.nearbyshops.enduserappnew.LocationPickerWithRadius.PickDeliveryRange;
import org.nearbyshops.enduserappnew.PreferencesDeprecated.PrefShopHome;
import org.nearbyshops.enduserappnew.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditShopFragment extends Fragment {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


//    Validator validator;


//    @Inject
//    DeliveryGuySelfService deliveryService;

    @Inject
    ShopService shopService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage)
    ImageView resultView;


    @BindView(R.id.shop_open) CheckBox shopOpen;
//    @BindView(R.id.shop_id) EditText shopID;

    @BindView(R.id.enter_shop_id) EditText shopIDEnter;
    @BindView(R.id.shopName) EditText shopName;

    @BindView(R.id.shopAddress) EditText shopAddress;
    @BindView(R.id.shopCity) EditText city;
    @BindView(R.id.shopPincode) EditText pincode;
    @BindView(R.id.shopLandmark) EditText landmark;

    @BindView(R.id.customerHelplineNumber) EditText customerHelplineNumber;
    @BindView(R.id.deliveryHelplineNumber) EditText deliveryHelplineNumber;

    @BindView(R.id.shopShortDescription) EditText shopDescriptionShort;
    @BindView(R.id.shopLongDescription) EditText shopDescriptionLong;

    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.pick_location_button) TextView pickLocationButton;
    @BindView(R.id.rangeOfDelivery) EditText rangeOfDelivery;

    @BindView(R.id.deliveryCharges) EditText deliveryCharge;
    @BindView(R.id.billAmountForFreeDelivery) EditText billAmountForFreeDelivery;

    @BindView(R.id.pick_from_shop_available) CheckBox pickFromShopAvailable;
    @BindView(R.id.home_delivery_available) CheckBox homeDeliveryAvailable;

    @BindView(R.id.error_delivery_option) TextView errorDeliveryOption;
    @BindView(R.id.error_delivery_option_top) TextView errorDeliveryOptionTop;


//    @Bind(R.id.item_id) EditText item_id;
//    @Bind(R.id.name) EditText name;
//    @Bind(R.id.username) EditText username;
//    @Bind(R.id.password) EditText password;
//    @Bind(R.id.about) EditText about;

//    @Bind(R.id.phone_number) EditText phone;
//    @Bind(R.id.designation) EditText designation;
//    @Bind(R.id.switch_enable) Switch aSwitch;

    @BindView(R.id.saveButton) TextView buttonUpdateItem;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    public static final String SHOP_INTENT_KEY = "shop_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;

//    DeliveryGuySelf deliveryGuySelf = new DeliveryGuySelf();
//    ShopAdmin shopAdmin = new ShopAdmin();
        Shop shop = new Shop();

    public EditShopFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_shop_fragment, container, false);

        ButterKnife.bind(this,rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(savedInstanceState==null)
        {
//            shopAdmin = getActivity().getIntent().getParcelableExtra(SHOP_ADMIN_INTENT_KEY);

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                shop = PrefShopHome.getShop(getContext());

                if(shop!=null) {
                    bindDataToViews();
                }
            }

//            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        if(shop!=null) {
            loadImage(shop.getLogoImagePath());
            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }


        showLogMessage("Inside On Create View !");

        return rootView;
    }

    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Add Shop");
            shopIDEnter.setVisibility(View.GONE);
        }
        else if(current_mode== MODE_UPDATE)
        {
            shopIDEnter.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
        }
    }


    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }



    void loadImage(String imagePath) {

        String iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/v1/Shop/Image/five_hundred_" + imagePath + ".jpg";

        Picasso.get()
                .load(iamgepath)
                .into(resultView);
    }






    @OnClick(R.id.uploadImage)
    void imageClick()
    {
        Intent intent = new Intent(getActivity(), ShopImageList.class);
        intent.putExtra("shop_id",shop.getShopID());
        startActivity(intent);
    }






    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
//            showToastMessage("Please correct form data before save !");
            return;
        }

        if(current_mode == MODE_ADD)
        {
            shop = new Shop();
            addAccount();
        }
        else if(current_mode == MODE_UPDATE)
        {
            update();
        }
    }


    boolean validateData()
    {
        boolean isValid = true;

        if(shopName.getText().toString().length()==0)
        {
            shopName.setError("Please enter Shop Name");
            shopName.requestFocus();
            isValid= false;
        }


        if(!homeDeliveryAvailable.isChecked() && !pickFromShopAvailable.isChecked())
        {
            homeDeliveryAvailable.setError("You must pick at least one delivery Option");
            pickFromShopAvailable.setError("You must pick at least one delivery Option");

            errorDeliveryOption.setVisibility(View.VISIBLE);
            errorDeliveryOptionTop.setVisibility(View.VISIBLE);

            homeDeliveryAvailable.requestFocus();
            pickFromShopAvailable.requestFocus();

            isValid = false;
        }
        else
        {

            errorDeliveryOption.setVisibility(View.GONE);
            errorDeliveryOptionTop.setVisibility(View.GONE);
        }




        if(latitude.getText().toString().length()==0)
        {
            latitude.setError("Latitude cant be empty !");
            latitude.requestFocus();
            isValid = false;
        }
        else
        {
            double lat = Double.parseDouble(latitude.getText().toString());

            if(lat >90 || lat <- 90)
            {
                latitude.setError("Invalid Latitude !");
                isValid  = false;
            }
        }


        if(longitude.getText().toString().length()==0)
        {
            longitude.setError("Longitude cant be empty !");
            longitude.requestFocus();
            isValid= false;
        }
        else
        {
            double lon = Double.parseDouble(longitude.getText().toString());

            if(lon >180 || lon < -180)
            {
                longitude.setError("Invalid Longitude !");
                isValid = false;
            }

        }

        if(rangeOfDelivery.getText().toString().length()==0)
        {
            rangeOfDelivery.setError("Range of Delivery cant be empty !");
            rangeOfDelivery.requestFocus();
            isValid = false;
        }

        if(shopDescriptionShort.getText().toString().length()>100)
        {
            shopDescriptionShort.setError("Should not be more than 100 characters !");
            shopDescriptionShort.requestFocus();
            isValid = false;
        }



        return isValid;
    }




    void addAccount()
    {
        if(isImageChanged)
        {
            if(!isImageRemoved)
            {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        }
        else
        {
            // post request
            retrofitPOSTRequest();
        }

    }


    void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(shop.getLogoImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                shop.setLogoImagePath(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindDataToViews()
    {
        if(shop!=null) {

            shopOpen.setChecked(shop.isOpen());
            shopIDEnter.setText(String.valueOf(shop.getShopID()));
            shopName.setText(shop.getShopName());
            shopAddress.setText(shop.getShopAddress());

            city.setText(shop.getCity());
            pincode.setText(String.valueOf(shop.getPincode()));
            landmark.setText(shop.getLandmark());
            customerHelplineNumber.setText(shop.getCustomerHelplineNumber());

            deliveryHelplineNumber.setText(shop.getDeliveryHelplineNumber());
            shopDescriptionShort.setText(shop.getShortDescription());
            shopDescriptionLong.setText(shop.getLongDescription());
            latitude.setText(String.valueOf(shop.getLatCenter()));

            longitude.setText(String.valueOf(shop.getLonCenter()));
            rangeOfDelivery.setText(String.valueOf(shop.getDeliveryRange()));
            deliveryCharge.setText(String.valueOf(shop.getDeliveryCharges()));
            billAmountForFreeDelivery.setText(String.valueOf(shop.getBillAmountForFreeDelivery()));

            pickFromShopAvailable.setChecked(shop.getPickFromShopAvailable());
            homeDeliveryAvailable.setChecked(shop.getHomeDeliveryAvailable());

        }
    }





    void getDataFromViews()
    {
        if(shop==null)
        {
            if(current_mode == MODE_ADD)
            {
                shop = new Shop();
            }
            else
            {
                return;
            }
        }

//        if(current_mode == MODE_ADD)
//        {
//            deliveryGuySelf.setShopID(UtilityShopHome.getShop(getContext()).getShopID());
//        }

        shop.setOpen(shopOpen.isChecked());
        shop.setShopName(shopName.getText().toString());
        shop.setShopAddress(shopAddress.getText().toString());

        shop.setCity(city.getText().toString());
        shop.setPincode(Long.parseLong(pincode.getText().toString()));
        shop.setLandmark(landmark.getText().toString());
        shop.setCustomerHelplineNumber(customerHelplineNumber.getText().toString());

        shop.setDeliveryHelplineNumber(deliveryHelplineNumber.getText().toString());
        shop.setShortDescription(shopDescriptionShort.getText().toString());
        shop.setLongDescription(shopDescriptionLong.getText().toString());


        if(!latitude.getText().toString().equals("") && !longitude.getText().toString().equals("") && !rangeOfDelivery.getText().toString().equals(""))
        {
            shop.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            shop.setLonCenter(Double.parseDouble(longitude.getText().toString()));
            shop.setDeliveryRange(Double.parseDouble(rangeOfDelivery.getText().toString()));
        }


        if(!deliveryCharge.getText().toString().equals(""))
        {
            shop.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        }

        if(!billAmountForFreeDelivery.getText().toString().equals(""))
        {
            shop.setBillAmountForFreeDelivery(Integer.parseInt(billAmountForFreeDelivery.getText().toString()));
        }


        shop.setPickFromShopAvailable(pickFromShopAvailable.isChecked());
        shop.setHomeDeliveryAvailable(homeDeliveryAvailable.isChecked());

    }



    public void retrofitPUTRequest()
    {

        getDataFromViews();


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = shopService.updateBySelf(
                PrefLogin.getAuthorizationHeaders(getContext()),
                shop
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    PrefShopHome.saveShop(shop,getContext());
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    void retrofitPOSTRequest()
    {
        getDataFromViews();

        Call<Shop> call = shopService.postShop(PrefLogin.getAuthorizationHeaders(getContext()),shop);



        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    shop = response.body();
                    bindDataToViews();

                    PrefShopHome.saveShop(shop,getContext());

                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                showToastMessage("Add failed !");



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }







    /*
        Utility Methods
     */




    void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }




    @BindView(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }



    public static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }



        clearCache(getContext());

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);




        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        }
        else if (resultCode == UCrop.RESULT_ERROR) {



            final Throwable cropError = UCrop.getError(result);


        }
        else if(requestCode==3 && resultCode==3)
        {
            latitude.setText(String.valueOf(result.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("lon_dest",0.0)));
            rangeOfDelivery.setText(String.valueOf(result.getDoubleExtra("radius",0.0)));
        }

    }





    // upload image after being picked up
    private void startCropActivity(Uri sourceUri, Context context) {


        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.blueGrey800));
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .withMaxResultSize(1500,1500)
                .start(context,this);


        //.withMaxResultSize(400,300)
        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }





    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Read External Storage . ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }





    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }


        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);

        } catch (Exception e) {
            e.printStackTrace();
        }



        Call<Image> imageCall = shopService.uploadImage(PrefLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);



        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    shop.setLogoImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    shop.setLogoImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    shop.setLogoImagePath(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

                showToastMessage("Image Upload failed !");
                shop.setLogoImagePath(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }






    void deleteImage(String filename)
    {

        Call<ResponseBody> call = shopService.deleteImage(
                PrefLogin.getAuthorizationHeaders(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");


            }
        });
    }








    // code for picking up location


    @OnClick(R.id.pick_location_button)
    void pickLocation()
    {
        Intent intent = new Intent(getActivity(), PickDeliveryRange.class);
        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        intent.putExtra("radius",Double.parseDouble(rangeOfDelivery.getText().toString()));
        startActivityForResult(intent,3);
    }


}
