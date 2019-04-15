package org.nearbyshops.enduserappnew.SelectMarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderCurrentMarket extends RecyclerView.ViewHolder {


    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.address) TextView marketAddress;
//    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.description) TextView marketDescription;
    @BindView(R.id.market_image) ImageView marketPhoto;


    private ServiceConfigurationLocal configurationLocal;
    private Context context;



    public ViewHolderCurrentMarket(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
    }




    void setItem(ServiceConfigurationLocal item)
    {

        this.configurationLocal = item;

        marketName.setText(configurationLocal.getServiceName());
        marketAddress.setText(configurationLocal.getAddress() + ", " + configurationLocal.getCity());
//        distance.setText("Distance : " + String.format("%.2f",configurationLocal.getRt_distance()));
        marketDescription.setText(configurationLocal.getDescriptionShort());



        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/serviceconfiguration/Image/three_hundred_" + configurationLocal.getLogoImagePath() + ".jpg";


//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(marketPhoto);

    }




}
