package org.nearbyshops.enduser.Services.ServiceFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduser.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ServiceConfigurationGlobal> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;


    public static final int VIEW_TYPE_SERVICE = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;


    private Fragment fragment;



    Adapter(List<ServiceConfigurationGlobal> dataset, NotifyConfirmOrder notifyConfirmOrder, Fragment fragment) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType== VIEW_TYPE_SERVICE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_service,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }

        return null;


    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_SERVICE;
        }

//        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {

        if(holderVH instanceof ViewHolder)
        {
            ViewHolder holder = (ViewHolder) holderVH;

            if(dataset!=null)
            {
                ServiceConfigurationGlobal service = dataset.get(position);

                holder.serviceName.setText(service.getServiceName());
                holder.serviceURL.setText(service.getServiceURL());
                holder.serviceAddress.setText(service.getAddress());
                holder.city.setText(service.getCity() + ", " + service.getCountry() + " - " + service.getPincode());
                holder.helplineNumber.setText("Helpline Number : " + service.getHelplineNumber());
                holder.serviceRange.setText("Service Range : " + String.valueOf(service.getServiceRange()) + " Kms");

                holder.serviceType.setText("Service Type : " + getServiceType(service.getServiceType()));

                if(service.getOfficial())
                {
                    holder.isOfficial.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.isOfficial.setVisibility(View.GONE);
                }

                if(service.getVerified())
                {
                    holder.isVerified.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.isVerified.setVisibility(View.GONE);
                    if(!service.getOfficial())
                    {
                        holder.isOfficial.setVisibility(View.GONE);
                    }
                }

                if(service.getServiceType()==1)
                {
                    holder.tagServiceType.setText(" Nonprofit ");
                    holder.tagServiceType.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(),R.color.gplus_color_1));
                }
                else if(service.getServiceType()==2)
                {
                    holder.tagServiceType.setText(" Government ");
                    holder.tagServiceType.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(),R.color.buttonColorDark));
                }
                else if(service.getServiceType()==3)
                {
                    holder.tagServiceType.setText(" Commercial ");
                    holder.tagServiceType.setBackgroundColor(ContextCompat.getColor(fragment.getActivity(),R.color.orange));
                }
                else
                {
                    holder.tagServiceType.setVisibility(View.GONE);
                }



                String imagePath = UtilityGeneral.getServiceURL_SDS(fragment.getActivity())
                        + "/api/v1/ServiceConfiguration/Image/three_hundred_" + service.getLogoImagePath() + ".jpg";

//                System.out.println("Service LOGO : " + imagePath);

                Drawable placeholder = VectorDrawableCompat
                        .create(fragment.getActivity().getResources(),
                                R.drawable.ic_nature_people_white_48px, fragment.getActivity().getTheme());



                Picasso.with(fragment.getActivity())
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.serviceLogo);


            }
        }

        else if(holderVH instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof ServicesFragment)
            {
                int items_count = ((ServicesFragment) fragment).item_count;

                if(dataset.size() == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Bind(R.id.service_name) TextView serviceName;
        @Bind(R.id.service_url) TextView serviceURL;
        @Bind(R.id.service_address) TextView serviceAddress;
        @Bind(R.id.city) TextView city;
        @Bind(R.id.helpline_number) TextView helplineNumber;
        @Bind(R.id.service_type) TextView serviceType;
        @Bind(R.id.service_range) TextView serviceRange;
        @Bind(R.id.rating) TextView rating;
        @Bind(R.id.rating_count) TextView ratingCount;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.service_logo) ImageView serviceLogo;

        @Bind(R.id.is_official) TextView isOfficial;
        @Bind(R.id.is_verified) TextView isVerified;

        @Bind(R.id.tag_service_type) TextView tagServiceType;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @OnClick(R.id.description)
        void copyURLClick()
        {
            ClipboardManager clipboard = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("URL", serviceURL.getText().toString());
            clipboard.setPrimaryClip(clip);

            showToastMessage("Copied !");
        }

        @Override
        public void onClick(View v) {
            notifyConfirmOrder.notifyServiceClick(dataset.get(getLayoutPosition()));
        }
    }



    void showToastMessage(String message)
    {
        if(fragment.getActivity()!=null)
        {
            Toast.makeText(fragment.getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    interface NotifyConfirmOrder{
        void notifyServiceClick(ServiceConfigurationGlobal serviceConfigurationGlobal);
//        void notifyConfirmOrder(Order order);
//        void notifyCancelOrder(Order order);
    }



    String getServiceType(int serviceType)
    {
        if(serviceType==1)
        {
            return "Nonprofit";
        }
        else if(serviceType==2)
        {
            return "Government";
        }
        else if(serviceType==3)
        {
            return "Commercial";
        }

        return "";
    }

}
