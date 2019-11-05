package org.nearbyshops.enduserappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.EmptyScreenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderEmptyScreenListItem extends RecyclerView.ViewHolder{



    private Context context;
    private Fragment fragment;

    @BindView(R.id.message) TextView message;
    @BindView(R.id.button) TextView button;
    @BindView(R.id.image) ImageView graphicImage;


    private EmptyScreenData data;

//    Create your own Market and help local Economy ... Its free !



    public static ViewHolderEmptyScreenListItem create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen, parent, false);

        return new ViewHolderEmptyScreenListItem(view,parent,context, fragment);
    }





    public ViewHolderEmptyScreenListItem(View itemView, ViewGroup parent, Context context, Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }






    @OnClick(R.id.button)
    void selectMarket()
    {
        if(fragment instanceof VHEmptyScreen)
        {
            ((VHEmptyScreen) fragment).buttonClick(data.getUrlForButtonClick());
        }
    }








    public void setItem(EmptyScreenData data)
    {
        this.data = data;

        message.setText(data.getMessage());
        button.setText(data.getButtonText());

        if(data.getImageResource()==0)
        {
            graphicImage.setVisibility(View.GONE);
        }
        else
        {
            graphicImage.setVisibility(View.VISIBLE);
            graphicImage.setImageResource(data.getImageResource());
        }

    }




    public interface VHEmptyScreen
    {
        void buttonClick(String url);
    }

}



