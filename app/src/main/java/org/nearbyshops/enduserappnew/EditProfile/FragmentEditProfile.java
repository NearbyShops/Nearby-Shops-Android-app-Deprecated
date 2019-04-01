package org.nearbyshops.enduserappnew.EditProfile;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditProfile.ChangeEmail.ChangeEmail;
import org.nearbyshops.enduserappnew.EditProfile.ChangeEmail.PrefChangeEmail;
import org.nearbyshops.enduserappnew.EditProfile.ChangePhone.ChangePhone;
import org.nearbyshops.enduserappnew.EditProfile.ChangePhone.PrefChangePhone;
import org.nearbyshops.enduserappnew.EditProfile.Interfaces.NotifyChangePassword;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class FragmentEditProfile extends Fragment {





    /* Token renewal variables : BEGIN */

    boolean isDestroyed = false;

    // constants - request codes for token renewal
    public static final int REQUEST_CODE_DELETE_IMAGE = 1;
    public static final int REQUEST_CODE_UPLOAD_PICKED_IMAGE = 2;
    private static final int REQUEST_CODE_UPDATE_USER = 3;

    // housekeeping for token renewal
    int token_renewal_attempts = 0;  // variable to keep record of renewal attempts
    int token_renewal_request_code = -1; // variable to store the request code;

    /* Token renewal variables : END */



    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


//    Validator validator;


//    @Inject
//    DeliveryGuySelfService deliveryService;

    @Inject
    UserService userService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage)
    ImageView resultView;

    @BindView(R.id.choice_male)
    RadioButton choiceMale;
    @BindView(R.id.choice_female)
    RadioButton choiceFemale;


    @BindView(R.id.item_id)
    EditText item_id;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.about)
    EditText about;
    @BindView(R.id.saveButton)
    TextView saveButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

//    @BindView(R.id.label_verify_email) TextView messageEmailVerified;
    @BindView(R.id.label_change_password)
TextView messageChangePassword;
    @BindView(R.id.label_add_or_change_email)
    TextView messageChangeEmail;
    @BindView(R.id.label_add_or_change_phone)
    TextView messageChangePhone;

    @BindViews({R.id.label_change_password,R.id.label_add_or_change_email})
    List<TextView> label_instructions;

    static final ButterKnife.Action<View> GONE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    static final ButterKnife.Action<View> VISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;

//    DeliveryGuySelf deliveryGuySelf = new DeliveryGuySelf();
    User driver = new User();



    public FragmentEditProfile() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_profile, container, false);

        ButterKnife.bind(this,rootView);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ContextCompat.getColor(getActivity(),R.color.mapbox_blue);



        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                driver = PrefLogin.getUser(getContext());

                if(driver !=null) {
                    bindDataToViews();
                }
            }

//            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }


        updateIDFieldVisibility();


        if(driver !=null) {
            loadImage(driver.getProfileImagePath());
            System.out.println("Loading Image !");
//            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }



        showLogMessage("Inside On Create View !");



//        if(current_mode==MODE_UPDATE)
//        {
//            checkTokenExpired(false);
//        }


        setActionBarTitle();

        return rootView;
    }






    @OnClick(R.id.label_change_password)
    void changePasswordClick()
    {
        if(getActivity() instanceof NotifyChangePassword)
        {
            ((NotifyChangePassword)getActivity()).changePasswordClick();
        }
    }



    @OnClick(R.id.label_add_or_change_email)
    void changeEmailClick()
    {

        PrefChangeEmail.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(),ChangeEmail.class);
        startActivityForResult(intent,10);
    }




    @OnClick(R.id.label_add_or_change_phone)
    void changePhoneClick()
    {
        PrefChangePhone.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(),ChangePhone.class);
        startActivityForResult(intent,10);
    }






    void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar!=null)
            {
                if(current_mode == MODE_ADD)
                {
                    actionBar.setTitle("Sign Up");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Profile");
                }

            }
        }
    }




    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            saveButton.setText("Sign Up");
            item_id.setVisibility(View.GONE);


//            messageEmailVerified.setVisibility(View.GONE);
            ButterKnife.apply(label_instructions,GONE);

            password.setEnabled(true);
            password.setText("");
            email.setEnabled(true);


        }
        else if(current_mode== MODE_UPDATE)
        {
            item_id.setVisibility(View.VISIBLE);
            saveButton.setText("Save");

            ButterKnife.apply(label_instructions,VISIBLE);


//            if(!driver.isEmailVerified())
//            {
//                messageEmailVerified.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                messageEmailVerified.setVisibility(View.GONE);
//            }

            password.setEnabled(false);
            password.setText("Password");
            email.setEnabled(false);
        }


        setActionBarTitle();
    }






    public static final String TAG_LOG = "edit_profile";

    void showLogMessage(String message)
    {
        Log.d(TAG_LOG,message);
        System.out.println(message);
    }





    void loadImage(String imagePath) {

        String iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/v1/User/Image/five_hundred_" + imagePath + ".jpg";

        System.out.println(iamgepath);

        Picasso.with(getContext())
                .load(iamgepath)
                .into(resultView);
    }





    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
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

//        if(phone.getText().toString().length()==0)
//        {
//            phone.setError("Please enter Phone Number");
//            phone.requestFocus();
//            isValid= false;
//        }


//
//        if(email.getText().toString().length()==0)
//        {
//            email.requestFocus();
//            email.setError("E-mail cannot be empty !");
//            isValid = false;
//        }


        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Password cannot be empty");
            isValid = false;
        }



//        if(username.getText().toString().length()==0)
//        {
//            username.requestFocus();
//            username.setError("Username cannot be empty");
//            isValid= false;
//        }


        if(name.getText().toString().length()==0)
        {

//            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_close_black_24dp);
            name.requestFocus();
            name.setError("Name cannot be empty");
            isValid = false;
        }


        return isValid;
    }





    @OnTextChanged(R.id.username)
    void usernameCheck()
    {


        if(driver !=null && driver.getUsername()!=null
                &&
                username.getText().toString().equals(driver.getUsername()))
        {
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
            return;
        }


        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = userService.checkUsernameExists(username.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    //username already exists
                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_4));
                    username.setError("Username already exist !");
                }
                else if(response.code() == 204)
                {
                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
                }


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
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
            deleteImage(driver.getProfileImagePath());


            if(isImageRemoved)
            {

                driver.setProfileImagePath(null);
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
        if(driver !=null) {

            item_id.setText(String.valueOf(driver.getUserID()));
            name.setText(driver.getName());
            username.setText(driver.getUsername());
//            password.setText(driver.getPassword());
            email.setText(driver.getEmail());
            about.setText(driver.getAbout());
            phone.setText(driver.getPhone());


            if(driver.getGender()!=null)
            {
                if(driver.getGender())
                {
                    choiceMale.setChecked(true);
                }
                else
                {
                    choiceFemale.setChecked(true);
                }
            }


        }
    }


    void getDataFromViews()
    {

        driver.setName(name.getText().toString());
        driver.setUsername(username.getText().toString());
        driver.setAbout(about.getText().toString());
        driver.setGender(choiceMale.isChecked());




        if(username.getText().toString().length()==0)
        {
            driver.setUsername(null);
        }
        else
        {
            driver.setUsername(username.getText().toString());
        }



        if(email.getText().toString().length()==0)
        {
            driver.setEmail(null);
        }
        else
        {
            driver.setEmail(email.getText().toString());
        }

        if(phone.getText().toString().length()==0)
        {
            driver.setPhone(null);
        }
        else
        {
            driver.setPhone(phone.getText().toString());
        }



    }






    public void retrofitPUTRequest()
    {

        getDataFromViews();


        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        // update Item Call
        Call<ResponseBody> call = userService.updateProfileEndUser(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                driver
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");

                    if(getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_UPDATE)==MODE_UPDATE)
                    {
                        PrefLogin.saveUserProfile(driver,getContext());

                    }


                }
                else
                {
                    showToastMessage("Update failed code : " + String.valueOf(response.code()));
                }


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Update Failed !");


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }





    void retrofitPOSTRequest()
    {
        getDataFromViews();



//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//
//
//        Call<User> call = userService.insertUser(driver);
//
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                if(response.code()==201)
//                {
//                    showToastMessage("Add successful !");
//
//                    current_mode = MODE_UPDATE;
//                    updateIDFieldVisibility();
//                    driver = response.body();
//                    bindDataToViews();
//
//                    PrefLogin.saveCredentials(
//                            getContext(), driver.getUsername(),
//                            driver.getPassword());
//
//                }
//                else
//                {
//                    showToastMessage("Add failed !");
//                }
//
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//                showToastMessage("Sign up not completed due to network failure !");
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        });

    }








    /*
        Utility Methods
     */




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
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

        showLogMessage("FragmentEditProfile : onActivityResult()"
        + "\nRequest Code : " + String.valueOf(requestCode)
        + "\nResponse Code : " + String.valueOf(resultCode)
        );

        if(requestCode==10 && resultCode == 10)
        {

            // result obtained after email change
            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                driver = PrefLogin.getUser(getContext());

                if(driver !=null) {
                    bindDataToViews();
                }
            }


            showLogMessage("FragmentEditProfile : onActivityResult()");

        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }
    }



    // upload image after being picked up
    void startCropActivity(Uri sourceUri, Context context) {



        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(getContext(),R.color.blueGrey800));
        options.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.backgroundTinted));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
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




        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<Image> imageCall = userService.uploadImage(PrefLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");


                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());



                    driver.setProfileImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    driver.setProfileImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed Code : " + String.valueOf(response.code()));
                    driver.setProfileImagePath(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }



            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Image Upload failed !");
                driver.setProfileImagePath(null);

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }



    void deleteImage(String filename)
    {


//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = userService.deleteImage(PrefLogin.getAuthorizationHeaders(getContext()),filename);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }



//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");


                if(isDestroyed)
                {
                    return;
                }



//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }







    // Image Methods Ends
//
//
//    boolean checkTokenExpired(boolean showMessage)
//    {
//        if(PrefLogin.getUsername(getActivity())==null)
//        {
//            // not logged in
//            return false;
//        }
//
//
//        if(PrefLogin.getExpires(getActivity())
//                .before(new Timestamp(System.currentTimeMillis())))
//        {
//            // Token expired renew the token
//
//            if(showMessage)
//            {
//                showToastMessage("Please try again !");
//            }
//            getActivity().startService(new Intent(getActivity(),RenewToken.class));
//
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }


    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }



}
