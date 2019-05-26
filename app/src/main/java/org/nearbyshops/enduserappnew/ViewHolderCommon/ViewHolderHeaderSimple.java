package org.nearbyshops.enduserappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolderCommon.Models.HeaderTitle;


public class ViewHolderHeaderSimple extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;


    private Context context;
    private HeaderTitle item;




    public static ViewHolderHeaderSimple create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_type_simple, parent, false);


        return new ViewHolderHeaderSimple(view,context);
    }





    public ViewHolderHeaderSimple(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }


    public void setItem(HeaderTitle data)
    {
        header.setText(data.getHeading());
    }


}

