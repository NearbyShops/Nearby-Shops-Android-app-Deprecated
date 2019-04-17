package org.nearbyshops.enduserappnew.SelectMarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderUserProfile extends RecyclerView.ViewHolder {


    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.user_id) TextView userID;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.phone) TextView phone;



    private Context context;




    public static ViewHolderUserProfile create(ViewGroup parent, Context context)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user_profile,parent,false);

        return new ViewHolderUserProfile(view,context);
    }




    public ViewHolderUserProfile(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);

    }



    void setItem(User user)
    {

        userID.setText("User ID : " + String.valueOf(user.getUserID()));


        Drawable placeholder = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
        String imagePath = PrefServiceConfig.getServiceURL_SDS(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";


        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(profileImage);


        phone.setText(user.getPhone());
        userName.setText(user.getName());
    }


}
